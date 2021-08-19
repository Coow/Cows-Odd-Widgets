package xyz.mrcow.cowsOddWidgets.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigHotkey;

import java.util.List;

public class Hotkeys {
    
    public static final ConfigHotkey ANTIGHOST = new ConfigHotkey("antiGhost", "LEFT_CONTROL,G", "Refreshes blocks near you");
    public static final ConfigHotkey OPEN_GUI_SETTINGS = new ConfigHotkey("openGuiSettings","C,O,W",  "Open the Config GUI");

    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            ANTIGHOST,
            OPEN_GUI_SETTINGS
    );
}
