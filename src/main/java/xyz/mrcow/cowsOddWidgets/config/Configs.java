package xyz.mrcow.cowsOddWidgets.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import xyz.mrcow.cowsOddWidgets.Reference;

import java.io.File;
import java.util.List;

public class Configs implements IConfigHandler {

    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static class Settings {
        public static final ConfigDouble AGREEMENT = new ConfigDouble("agreement", 10, 1, 15, true, "How much you agree");
        public static final ConfigDouble AGREEMENT_SPEED = new ConfigDouble("agreementSpeed", 50, 10, 180, true, "How fast you agree");

        public static final ConfigInteger ANTIGHOST_RANGE = new ConfigInteger("antiGhostRange", 6, 2, 10, true, "Range of AntiGhost features area\nValues over 6 might cause issues due to the AntiCheat detecting out of reach blocks");
        public static final ConfigHotkey ANTIGHOST = new ConfigHotkey("antiGhost", "LEFT_CONTROL,G", "Refreshes blocks near you");

        public static final ConfigHotkey OPEN_GUI_SETTINGS = new ConfigHotkey("openGuiSettings","C,O,W", KeybindSettings.PRESS_NON_ORDER_SENSITIVE, "Open the Config GUI");
        public static final ConfigHotkey STOP_ELYTRA = new ConfigHotkey("stopElytra","LEFT_CONTROL,R","Prevents flying while hotkey pressed");




        public static final ImmutableList<IConfigValue> OPTIONS = ImmutableList.of(
                AGREEMENT,
                AGREEMENT_SPEED,
                ANTIGHOST,
                ANTIGHOST_RANGE,
                OPEN_GUI_SETTINGS,
                STOP_ELYTRA
        );

        public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
                ANTIGHOST,
                OPEN_GUI_SETTINGS,
                STOP_ELYTRA
        );
    }

    public static void loadFromFile()
    {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Generic", Settings.OPTIONS);
                ConfigUtils.readHotkeyToggleOptions(root, "FeatureHotkeys", "FeatureToggles", FeatureToggle.VALUES);

            }
        }
    }

    public static void saveToFile()
    {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
        {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Settings.OPTIONS);

            ConfigUtils.writeHotkeyToggleOptions(root, "FeatureHotkeys", "FeatureToggles", FeatureToggle.VALUES);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }

    @Override
    public void load()
    {
        loadFromFile();
    }

    @Override
    public void save()
    {
        saveToFile();
    }
}
