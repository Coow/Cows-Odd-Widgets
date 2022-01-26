package xyz.mrcow.cowsOddWidgets.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.mrcow.cowsOddWidgets.config.Configs;
import xyz.mrcow.cowsOddWidgets.features.DisplayPetOwner;
import xyz.mrcow.cowsOddWidgets.features.DisplayPlayerHealth;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Inject(at = {@At("HEAD")},
    method = {"Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
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

        List<UUID> ownerIds = DisplayPetOwner.getOwnerIds(entity);

        //If HUD is hidden or the feature is enabled or the entity is not targeted or the player is riding the entity.
        if (MinecraftClient.getInstance().options.hudHidden || !Configs.Settings.DISPLAY_PET_OWNER.getBooleanValue() || dispatcher.targetedEntity != entity || entity.hasPassenger(MinecraftClient.getInstance().player) || ownerIds.isEmpty()) return;

        for (int i = 0; i < ownerIds.size(); i++) {
            UUID ownerId = ownerIds.get(i);
            if (ownerId == null) return;

            Optional<String> usernameString = DisplayPetOwner.getNameFromId(ownerId);

            Text text = new LiteralText(usernameString.map(s -> "§e" + s).orElse("§4Error!"));


            double d = this.dispatcher.getSquaredDistanceToCamera(entity);
            @SuppressWarnings("rawtypes") EntityRenderer entityRenderer = (EntityRenderer) (Object) this;
            if (d <= 4096.0D) {
                float height = entity.getHeight() + 0.5F;
                int y = 10 + (10 * i);
                matrices.push();
                matrices.translate(0.0D, height, 0.0D);
                matrices.multiply(this.dispatcher.getRotation());
                matrices.scale(-0.025F, -0.025F, 0.025F);
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                TextRenderer textRenderer = entityRenderer.getTextRenderer();
                float x = (float) (-textRenderer.getWidth(text) / 2);

                float backgroundOpacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
                int backgroundColor = (int) (backgroundOpacity * 255.0F) << 24;

                textRenderer.draw(text, x, (float) y, 553648127, false, matrix4f, vertexConsumers, true, backgroundColor, light);
                textRenderer.draw(text, x, (float) y, -1, false, matrix4f, vertexConsumers, false, 0, light);

                matrices.pop();
            }
        }

    }

}
