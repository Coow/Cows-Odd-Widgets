package xyz.mrcow.cowsOddWidgets.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.mrcow.cowsOddWidgets.CowsOddWidgets;
import xyz.mrcow.cowsOddWidgets.config.Configs;
import xyz.mrcow.cowsOddWidgets.config.FeatureToggle;
import xyz.mrcow.cowsOddWidgets.features.DisplayMobHealth;
import xyz.mrcow.cowsOddWidgets.features.DisplayPetOwner;
import xyz.mrcow.cowsOddWidgets.features.DisplayPlayerHealth;
import xyz.mrcow.cowsOddWidgets.gui.EntityExtraInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    @Final
    @Shadow
    protected EntityRenderDispatcher dispatcher;


    @Shadow public abstract TextRenderer getTextRenderer();

    //https://github.com/PotatoPresident/PetOwner/blob/master/src/main/java/us/potatoboy/petowner/mixin/OwnerNameTagRendering.java
    @Inject(method = "render", at = @At("HEAD"))
    private void renderEntityOwner(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){

        //If HUD is hidden
        if (MinecraftClient.getInstance().options.hudHidden ||
                //If the entity is not targeted
                dispatcher.targetedEntity != entity ||
                //If the player is riding the entity
                entity.hasPassenger(MinecraftClient.getInstance().player)
        ) return;


        EntityExtraInfo extraInfo = new EntityExtraInfo();

        if (FeatureToggle.DISPLAY_PET_OWNER.getBooleanValue()) {
            List<UUID> ownerIds = DisplayPetOwner.getOwnerIds(entity);

            for (UUID ownerId : ownerIds) {
                if (ownerId == null) return;

                Optional<String> usernameString = DisplayPetOwner.getNameFromId(ownerId);

                if (usernameString.isPresent()) {
                    extraInfo.names.add(Text.literal(usernameString.map(s -> "§e" + s).orElse("§4Error!")).formatted(Formatting.YELLOW));
                }
            }
        }

        if (FeatureToggle.DISPLAY_MOB_HEALTH.getBooleanValue() && entity instanceof MobEntity) {
            extraInfo.health = DisplayPlayerHealth.addHealthText((MobEntity)entity, Text.literal("").formatted(Formatting.RED));
        }

        if (!extraInfo.isEmpty()) {
            renderExtras(entity, extraInfo, matrices, vertexConsumers, light);
        }
    }

    @Unique
    private void renderExtras(T entity, EntityExtraInfo extras, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
    {
        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        @SuppressWarnings("rawtypes") EntityRenderer entityRenderer = (EntityRenderer) (Object) this;

        float maxwidth = 0;
        if (d <= 4096.0D) {

            if (!extras.namesEmpty())
            {
                for (int i = 0; i < extras.names.size(); i++)
                {
                    float height = entity.getHeight() + 0.75F;
                    TextRenderer textRenderer = entityRenderer.getTextRenderer();
                    float textwidth = textRenderer.getWidth(extras.names.get(i));
                    if (textwidth > maxwidth)
                    {
                        maxwidth = textwidth;
                    }

                    renderExtraLabel(entityRenderer, extras.names.get(i), height, matrices, vertexConsumers, light);
                }
            }

            if (!extras.healthEmpty())
            {
                float height = entity.getHeight() + 0.5F;

                renderExtraLabel(entityRenderer, extras.health, height, matrices, vertexConsumers, light);
            }
        }
    }

    @Unique
    private void renderExtraLabel(EntityRenderer entityRenderer, Text text, float height, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)  {

        matrices.push();
        matrices.translate(0.0, height, 0.0);
        matrices.multiply(this.dispatcher.getRotation());
        matrices.scale(0.025F, -0.025F, 0.025F);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float f = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
        int j = (int) (f * 255.0F) << 24;
        TextRenderer textRenderer = entityRenderer.getTextRenderer();
        float g = (float) (-textRenderer.getWidth(text) / 2);
        textRenderer.draw(text, g, 0, 553648127, false, matrix, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, j, light);
        textRenderer.draw(text, g, 0, -1, false, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
        matrices.pop();
    }


}
