package xyz.mrcow.cowsOddWidgets.event;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler;
import net.minecraft.client.MinecraftClient;
import xyz.mrcow.cowsOddWidgets.Reference;
import xyz.mrcow.cowsOddWidgets.config.Configs;
import xyz.mrcow.cowsOddWidgets.config.FeatureToggle;

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

        for (FeatureToggle toggle : FeatureToggle.values())
        {
            manager.addKeybindToMap(toggle.getKeybind());
        }

    }

    @Override
    public void addHotkeys(IKeybindManager manager)
    {
        manager.addHotkeysForCategory(Reference.MOD_NAME, "Generic", Configs.Settings.HOTKEY_LIST);
        manager.addHotkeysForCategory(Reference.MOD_NAME, Reference.MOD_ID + ".hotkeys.category.tweak_toggle_hotkeys", FeatureToggle.VALUES);
    }

    @Override
    public boolean onKeyInput(int keyCode, int scanCode, int modifiers, boolean eventKeyState)
    {
        if (eventKeyState)
        {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.options.useKey.matchesKey(keyCode, scanCode))
            {
                return this.handleUseKey(mc);
            }
        }
        return false;
    }

    private boolean handleUseKey(MinecraftClient mc)
    {

        return false;
    }
}
