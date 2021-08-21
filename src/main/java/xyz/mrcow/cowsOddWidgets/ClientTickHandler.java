package xyz.mrcow.cowsOddWidgets;

import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import xyz.mrcow.cowsOddWidgets.config.Configs;
import xyz.mrcow.cowsOddWidgets.features.AutoTotem;
import xyz.mrcow.cowsOddWidgets.features.DerpMode;


public class ClientTickHandler implements IClientTickHandler {



    @Override
    public void onClientTick(MinecraftClient mc){
        if (mc.world != null && mc.player != null){
            if(Configs.Settings.DERP_MODE.getBooleanValue() == true){
                DerpMode.doDerp(mc);
            }
            if(Configs.Settings.AUTO_TOTEM.getBooleanValue() == true){
                AutoTotem.update(mc);
            }
        }
    }
}
