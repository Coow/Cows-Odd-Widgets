package xyz.mrcow.cowsOddWidgets.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.mrcow.cowsOddWidgets.config.Configs;
import xyz.mrcow.cowsOddWidgets.features.DisplayMobHealth;
import xyz.mrcow.cowsOddWidgets.features.DisplayPetOwner;
import xyz.mrcow.cowsOddWidgets.features.DisplayPlayerHealth;
import xyz.mrcow.cowsOddWidgets.gui.EntityExtraInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @Inject(at = {@At("HEAD")},
    method = {"renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
            cancellable = true)
    private void renderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if(entity instanceof PlayerEntity && Configs.Settings.DISPLAY_PLAYER_HEALTH.getBooleanValue()){
            text = DisplayPlayerHealth.addHealthText((LivingEntity) entity, text);
        }
    }

    @Final
    @Shadow
    protected EntityRenderDispatcher dispatcher;


    //https://github.com/PotatoPresident/PetOwner/blob/master/src/main/java/us/potatoboy/petowner/mixin/OwnerNameTagRendering.java
    @Inject(at = {@At("HEAD")},
    method = {"render"})
    private void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        //If HUD is hidden
        if (MinecraftClient.getInstance().options.hudHidden) return;
        //If the entity is not targeted
        if (dispatcher.targetedEntity != entity) return;
        //If the player is riding the entity
        if (entity.hasPassenger(MinecraftClient.getInstance().player)) return;

        EntityExtraInfo extraInfo = new EntityExtraInfo();

        //If the feature is enabled
        if (Configs.Settings.DISPLAY_PET_OWNER.getBooleanValue())
        {
            List<UUID> ownerIds = DisplayPetOwner.getOwnerIds(entity);

            for (int i = 0; i < ownerIds.size(); i++) {
                UUID ownerId = ownerIds.get(i);
                if (ownerId == null) return;

                Optional<String> usernameString = DisplayPetOwner.getNameFromId(ownerId);

                if (!usernameString.isEmpty())
                {
                    extraInfo.names.add(Text.literal(usernameString.isPresent() ?
                            "§e" + usernameString.get() : "§4Error!").formatted(Formatting.YELLOW));
                }
            }
        }

        if (Configs.Settings.DISPLAY_MOB_HEALTH.getBooleanValue() && entity instanceof MobEntity)
        {
            extraInfo.health = DisplayPlayerHealth.addHealthText((MobEntity)entity, Text.literal("").formatted(Formatting.RED));
        }

        if (extraInfo.isEmpty()) return;
        else
        {
            renderExtras(entity, extraInfo, matrices, vertexConsumers, light);
        }
    }

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
                    float height = entity.getHeight() + 0.5F;
                    int y = 10 + (10 * i);
                    TextRenderer textRenderer = entityRenderer.getTextRenderer();
                    float textwidth = textRenderer.getWidth(extras.names.get(i));
                    if (textwidth > maxwidth)
                    {
                        maxwidth = textwidth;
                    }
                    float x = (float) (-textwidth / 2);

                    renderExtraLabel(entityRenderer, extras.names.get(i), y, x, height, matrices, vertexConsumers, light);
                }
            }

            if (!extras.healthEmpty())
            {
                TextRenderer textRenderer = entityRenderer.getTextRenderer();
                float height = entity.getHeight() + 0.5F;
                int y = 10;
                float textwidth = textRenderer.getWidth(extras.health);
                float x = -(textwidth/2);

                if (!extras.namesEmpty())
                {
                    y = y + (10 * extras.names.size()/2);
                    x = maxwidth/2 + DisplayMobHealth.healthLabelOffset;
                }
                renderExtraLabel(entityRenderer, extras.health, y, x, height, matrices, vertexConsumers, light);
            }
        }
    }

    private void renderExtraLabel(EntityRenderer entityRenderer, Text text, float y, float x, float height, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
    {
        matrices.push();
        matrices.translate(0.0D, height, 0.0D);
        matrices.multiply(this.dispatcher.getRotation());
        matrices.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        TextRenderer textRenderer = entityRenderer.getTextRenderer();
        float backgroundOpacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
        int backgroundColor = (int) (backgroundOpacity * 255.0F) << 24;

        textRenderer.draw(text, x, (float) y, text.getStyle().getColor().hashCode(), false, matrix4f, vertexConsumers, true, backgroundColor, light);
        textRenderer.draw(text, x, (float) y, -1, false, matrix4f, vertexConsumers, false, 0, light);

        matrices.pop();
    }


}
