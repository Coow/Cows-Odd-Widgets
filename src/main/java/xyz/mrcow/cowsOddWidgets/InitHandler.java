package xyz.mrcow.cowsOddWidgets;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import xyz.mrcow.cowsOddWidgets.config.Configs;
import xyz.mrcow.cowsOddWidgets.event.InputHandler;
import xyz.mrcow.cowsOddWidgets.event.KeybindCallbacks;

public class InitHandler implements IInitializationHandler {
    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance().registerConfigHandler(Reference.MOD_ID, new Configs());

        InputHandler handler = new InputHandler();
        InputEventHandler.getKeybindManager().registerKeybindProvider(handler);
        InputEventHandler.getInputManager().registerKeyboardInputHandler(handler);

        KeybindCallbacks.INSTANCE.setCallbacks();
    }
}
