package xyz.mrcow.cowsOddWidgets.features;

import net.minecraft.client.MinecraftClient;

public class StopElytra implements IKeybindable {

    @Override
    public boolean invokeHotkey() {
        MinecraftClient mc = MinecraftClient.getInstance();
        stop(mc);
        return false;
    }

    public static void stop(MinecraftClient mc) {
        if (mc.player != null)
        {
            mc.player.stopFallFlying();
        }
    }
}
