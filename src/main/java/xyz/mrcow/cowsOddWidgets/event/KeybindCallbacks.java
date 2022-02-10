package xyz.mrcow.cowsOddWidgets.event;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.client.MinecraftClient;
import xyz.mrcow.cowsOddWidgets.config.Configs;
import xyz.mrcow.cowsOddWidgets.gui.GuiConfigs;
import xyz.mrcow.cowsOddWidgets.features.AntiGhost;

public class KeybindCallbacks implements IHotkeyCallback {

    public static final KeybindCallbacks INSTANCE = new KeybindCallbacks();

    public static KeybindCallbacks getInstance()
    {
        return INSTANCE;
    }

    public void setCallbacks()
    {
        for (ConfigHotkey hotkey : Configs.Settings.HOTKEY_LIST)
        {
            hotkey.getKeybind().setCallback(this);
        }
    }

    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key)
    {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player == null || mc.world == null)
        {
            return false;
        }

        if (key == Configs.Settings.OPEN_GUI_SETTINGS.getKeybind())
        {
            GuiBase.openGui(new GuiConfigs());
            return true;
        } else if(key == Configs.Settings.ANTIGHOST.getKeybind()){
            AntiGhost.requestBlocks(Configs.Settings.ANTIGHOST_RANGE.getIntegerValue());
            return true;
        } else if(key == Configs.Settings.STOP_ELYTRA.getKeybind()){
            mc.player.stopFallFlying();
            return false;
        }

        return false;
    }
}
