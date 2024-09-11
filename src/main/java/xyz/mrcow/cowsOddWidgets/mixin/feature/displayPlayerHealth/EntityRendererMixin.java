package xyz.mrcow.cowsOddWidgets.mixin.feature.displayPlayerHealth;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.mrcow.cowsOddWidgets.config.Configs;
import xyz.mrcow.cowsOddWidgets.config.FeatureToggle;
import xyz.mrcow.cowsOddWidgets.features.DisplayPlayerHealth;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @ModifyVariable(method = "renderLabelIfPresent", argsOnly = true, at = @At(value = "LOAD", ordinal = 0))
    private Text renderLabelIfPresent(Text text, @Local(argsOnly = true) Entity entity) {
        if(entity instanceof PlayerEntity && FeatureToggle.DISPLAY_PLAYER_HEALTH.getBooleanValue()){
            return  DisplayPlayerHealth.addHealthText((LivingEntity) entity, text);
        } else {
            return text;
        }
    }



}
