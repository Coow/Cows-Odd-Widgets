package xyz.mrcow.cowsOddWidgets.event;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import net.minecraft.client.MinecraftClient;
import xyz.mrcow.cowsOddWidgets.config.Hotkeys;
import xyz.mrcow.cowsOddWidgets.gui.GuiConfigs;
import xyz.mrcow.cowsOddWidgets.util.AntiGhost;

public class KeybindCallbacks implements IHotkeyCallback {

    public static final KeybindCallbacks INSTANCE = new KeybindCallbacks();

    public static KeybindCallbacks getInstance()
    {
        return INSTANCE;
    }

    public void setCallbacks()
    {
        for (ConfigHotkey hotkey : Hotkeys.HOTKEY_LIST)
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

        if (key == Hotkeys.OPEN_GUI_SETTINGS.getKeybind())
        {
            GuiBase.openGui(new GuiConfigs());
            return true;
        } else if(key == Hotkeys.ANTIGHOST.getKeybind()){
            AntiGhost.requestBlocks(8);
            return true;
        }

        return false;
    }
}
