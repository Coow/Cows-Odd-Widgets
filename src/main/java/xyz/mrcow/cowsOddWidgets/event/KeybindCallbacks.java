package xyz.mrcow.cowsOddWidgets.event;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.client.MinecraftClient;
import xyz.mrcow.cowsOddWidgets.config.Configs;
import xyz.mrcow.cowsOddWidgets.features.IKeybindable;
import xyz.mrcow.cowsOddWidgets.features.OpenGui;
import xyz.mrcow.cowsOddWidgets.features.StopElytra;
import xyz.mrcow.cowsOddWidgets.features.AntiGhost;

import java.util.HashMap;

public class KeybindCallbacks implements IHotkeyCallback {

    public static final KeybindCallbacks INSTANCE = new KeybindCallbacks();

    public HashMap<IKeybind, IKeybindable> keyList = new HashMap<>();

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

        keyList.put(Configs.Settings.ANTIGHOST.getKeybind(), new AntiGhost());
        keyList.put(Configs.Settings.STOP_ELYTRA.getKeybind(), new StopElytra());
        keyList.put(Configs.Settings.OPEN_GUI_SETTINGS.getKeybind(), new OpenGui());
    }

    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key)
    {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player == null || mc.world == null)
        {
            return false;
        }

        if (keyList.containsKey(key))
        {
            return keyList.get(key).invokeHotkey();
        }

        return false;
    }
}
