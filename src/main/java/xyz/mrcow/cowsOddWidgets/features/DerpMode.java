package xyz.mrcow.cowsOddWidgets.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Random;

public class DerpMode {

    private static Random random;

    public static void doDerp(MinecraftClient mc){
        //Not sure exactly why all the extra math, but thanks Wurst <3
        float yaw = mc.player.getYaw() + random.nextFloat() * 360F - 180F;
        float pitch = random.nextFloat() * 180F - 90F;

        mc.player.networkHandler
                .sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch,
                        mc.player.isOnGround()));
    }
}
