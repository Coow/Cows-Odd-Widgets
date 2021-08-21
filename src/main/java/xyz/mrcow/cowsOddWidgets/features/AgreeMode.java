package xyz.mrcow.cowsOddWidgets.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import xyz.mrcow.cowsOddWidgets.config.Configs;

public class AgreeMode {
    public static void Agree(MinecraftClient mc) {
        float time = (float) (mc.player.age % 20 / Configs.Settings.AGREEMENT.getDoubleValue()); //Lower number, faster nodding speed 1-30 FAST AGREEMENT
        float pitch = (float) Math.sin(time * Math.PI) * (float) Configs.Settings.AGREEMENT_SPEED.getDoubleValue(); //Higher number, bigger angle of nod 10-180 AGREEMENT_SPEED

        mc.player.networkHandler
                .sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(mc.player.getYaw(), pitch, mc.player.isOnGround()));
    }
}
