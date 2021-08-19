package xyz.mrcow.cowsOddWidgets.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AntiGhost {
    public static void requestBlocks(double range) {
        MinecraftClient mc = MinecraftClient.getInstance();

        mc.inGameHud.addChatMessage(MessageType.SYSTEM, Text.of("Requesting blocks!"), mc.player.getUuid());

        ClientPlayNetworkHandler conn = mc.getNetworkHandler();
        if (conn==null)
            return;
        BlockPos pos=mc.player.getBlockPos();
        for (double dx=-range; dx<=range; dx++)
            for (double dy=-range; dy<=range; dy++)
                for (double dz=-range; dz<=range; dz++) {
                    PlayerActionC2SPacket packet=new PlayerActionC2SPacket(
                            PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK,
                            //PlayerActionC2SPacket.Action.START_DESTROY_BLOCK,
                            new BlockPos(pos.getX()+dx, pos.getY()+dy, pos.getZ()+dz),
                            Direction.UP       // with ABORT_DESTROY_BLOCK, this value is unused
                    );
                    conn.sendPacket(packet);
                }
    }
}
