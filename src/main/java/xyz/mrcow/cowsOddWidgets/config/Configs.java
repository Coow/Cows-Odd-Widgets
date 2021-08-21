package xyz.mrcow.cowsOddWidgets.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import xyz.mrcow.cowsOddWidgets.Reference;

import java.io.File;
import java.util.List;

public class Configs implements IConfigHandler {

    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static class Settings {
        public static final ConfigInteger ANTIGHOST_RANGE = new ConfigInteger("antiGhostRange", 6, 2, 10, true, "Range of AntiGhost features area\nValues over 6 might cause issues due to the AntiCheat detecting out of reach blocks");
        public static final ConfigBoolean DERP_MODE = new ConfigBoolean("derpMode", false,"Spins the players head server side\nMight mess with Tweakeroo features, such as flexibleBlockRotation");
        public static final ConfigBoolean AUTO_TOTEM = new ConfigBoolean("autoTotem", false,"Automatically put a Totem of Undying in the players offhand if there is one avaliable elsewhere in the inventory");

        public static final ConfigBoolean AGREE_MODE = new ConfigBoolean("agreeMode", false,"Makes you agree to everything!");

        public static final ConfigDouble AGREEMENT = new ConfigDouble("agreement", 10, 1, 15, true, "How much you agree");
        public static final ConfigDouble AGREEMENT_SPEED = new ConfigDouble("agreementSpeed", 50, 10, 180, true, "How fast you agree");

        public static final ConfigHotkey ANTIGHOST = new ConfigHotkey("antiGhost", "LEFT_CONTROL,G", "Refreshes blocks near you");
        public static final ConfigHotkey OPEN_GUI_SETTINGS = new ConfigHotkey("openGuiSettings","C,O,W",  "Open the Config GUI");
        public static final ConfigHotkey TOGGLE_DERP_MODE = new ConfigHotkey("toggleDerpMode","",  "Toggle Derp Mode");
        //public static final ConfigHotkey    TOGGLE_AUTO_TOTEM = new ConfigHotkey("toggleAutoTotem","",  "Toggle Auto Totem");
        public static final ConfigBooleanHotkeyed    TOGGLE_AUTO_TOTEM = new ConfigBooleanHotkeyed("toggleAutoTotem", false, "",  "Toggle Auto Totem");

        public static final ConfigBoolean STEP_MODE = new ConfigBoolean("stepMode", false,"Makes you step up blocks instead of having to jump!\nHard coded to be avoid cheaty behaviour");
        public static final ConfigBoolean DISPLAY_PLAYER_HEALTH = new ConfigBoolean("displayPlayerHealth", false,"Displays players health in the Name Text");


        public static final ImmutableList<IConfigValue> OPTIONS = ImmutableList.of(
                //TOGGLE_AUTO_TOTEM,
                //TOGGLE_DERP_MODE,
                AGREEMENT,
                AGREEMENT_SPEED,
                AGREE_MODE,
                ANTIGHOST,
                ANTIGHOST_RANGE,
                AUTO_TOTEM,
                DERP_MODE,
                DISPLAY_PLAYER_HEALTH,
                OPEN_GUI_SETTINGS,
                STEP_MODE
        );

        public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
                ANTIGHOST,
                OPEN_GUI_SETTINGS
        );
    }

    public static void loadFromFile()
    {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead())
        {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Generic", Settings.OPTIONS);
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
