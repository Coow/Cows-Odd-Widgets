package xyz.mrcow.cowsOddWidgets;

import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import xyz.mrcow.cowsOddWidgets.config.Configs;

import java.util.Random;

public class ClientTickHandler implements IClientTickHandler {

    private final Random random = new Random();

    @Override
    public void onClientTick(MinecraftClient mc){
        if (mc.world != null && mc.player != null){
            if(Configs.Settings.DERP_MODE.getBooleanValue() == true){
                //Not sure exactly why all the extra math, but thanks Wurst <3
                float yaw = mc.player.getYaw() + random.nextFloat() * 360F - 180F;
                float pitch = random.nextFloat() * 180F - 90F;

                mc.player.networkHandler
                        .sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch,
                                mc.player.isOnGround()));
            }
        }
    }
}
