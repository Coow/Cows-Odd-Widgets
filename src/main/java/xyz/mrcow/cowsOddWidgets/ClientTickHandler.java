package xyz.mrcow.cowsOddWidgets;

import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import net.minecraft.client.MinecraftClient;
import xyz.mrcow.cowsOddWidgets.config.Configs;
import xyz.mrcow.cowsOddWidgets.config.FeatureToggle;
import xyz.mrcow.cowsOddWidgets.features.AgreeMode;
import xyz.mrcow.cowsOddWidgets.features.AutoTotem;
import xyz.mrcow.cowsOddWidgets.features.DerpMode;

public class ClientTickHandler implements IClientTickHandler {


    @Override
    public void onClientTick(MinecraftClient mc){
        if (mc.world != null && mc.player != null) {
            if (FeatureToggle.DERP_MODE.getBooleanValue()) {
                DerpMode.doDerp(mc);
            } else if (FeatureToggle.AGREE_MODE.getBooleanValue()) {
                AgreeMode.Agree(mc);
            }

            if (FeatureToggle.AUTO_TOTEM.getBooleanValue()) {
                AutoTotem.update(mc);
            }



        }
    }
}
