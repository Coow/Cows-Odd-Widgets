package xyz.mrcow.cowsOddWidgets.event;

import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler;
import net.minecraft.client.MinecraftClient;
import xyz.mrcow.cowsOddWidgets.Reference;
import xyz.mrcow.cowsOddWidgets.config.Configs;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler {

    private final KeybindCallbacks callbacks;

    public InputHandler()
    {
        this.callbacks = KeybindCallbacks.getInstance();
    }

    @Override
    public void addKeysToMap(IKeybindManager manager)
    {
        for (IHotkey hotkey : Configs.Settings.HOTKEY_LIST)
        {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager)
    {
        manager.addHotkeysForCategory(Reference.MOD_NAME, "Generic", Configs.Settings.HOTKEY_LIST);
    }

    @Override
    public boolean onKeyInput(int keyCode, int scanCode, int modifiers, boolean eventKeyState)
    {
        if (eventKeyState)
        {
            MinecraftClient mc = MinecraftClient.getInstance();

            if (mc.options.keyUse.matchesKey(keyCode, scanCode))
            {
                return this.handleUseKey(mc);
            }
        }
        return false;
    }

    private boolean handleUseKey(MinecraftClient mc)
    {
        boolean cancel = false;

        return false;
    }
}
