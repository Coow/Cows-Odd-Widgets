package xyz.mrcow.cowsOddWidgets.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.config.IConfigBoolean;
import fi.dy.masa.malilib.config.IConfigNotifiable;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyCallbackToggleBooleanConfigWithMessage;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.interfaces.IValueChangeCallback;
import fi.dy.masa.malilib.util.StringUtils;
import xyz.mrcow.cowsOddWidgets.CowsOddWidgets;
import xyz.mrcow.cowsOddWidgets.Reference;

public enum FeatureToggle implements IHotkeyTogglable, IConfigNotifiable<IConfigBoolean>
{
    STEP_MODE                   (false, ""),
    AUTO_TOTEM                  (false, ""),
    AGREE_MODE                  (false, ""),
    DERP_MODE                   (false, ""),
    DISPLAY_PET_OWNER           (false, ""),
    DISPLAY_PLAYER_HEALTH       (false, ""),
    DISPLAY_MOB_HEALTH          (false, ""),
    STEP_MODE_BLOCKED_BY_SNEAK  (false, "");


    public static final String CATEGORY_ID = "feature_toggle";
    public static final ImmutableList<FeatureToggle> VALUES = ImmutableList.copyOf(values());

    private final boolean defaultValueBoolean;
    private final IKeybind keybind;
    private final boolean singlePlayer;
    private boolean valueBoolean;
    private IValueChangeCallback<IConfigBoolean> callback;


    FeatureToggle(boolean defaultValue, String defaultHotkey) {
        this(defaultValue, defaultHotkey, KeybindSettings.DEFAULT);
    }

    FeatureToggle(boolean defaultValue, String defaultHotkey, KeybindSettings settings) {
        this(defaultValue, defaultHotkey, settings, false);

    }

    FeatureToggle(boolean defaultValue, String defaultHotkey, KeybindSettings settings, boolean singlePlayer)
    {
        this.valueBoolean = defaultValue;
        this.defaultValueBoolean = defaultValue;
        this.singlePlayer = singlePlayer;
        this.keybind = KeybindMulti.fromStorageString(defaultHotkey, settings);
        this.keybind.setCallback(new KeyCallbackToggleBooleanConfigWithMessage(this));
    }

    @Override
    public ConfigType getType() {
        return ConfigType.HOTKEY;
    }

    @Override
    public String getName() {
        if (this.singlePlayer)
        {
            return GuiBase.TXT_GOLD + snakeToCamelCase(this.name()) + GuiBase.TXT_RST;
        }

        return snakeToCamelCase(this.name());
    }

    @Override
    public String getConfigGuiDisplayName()
    {
        // This doesn't get called ?
        String name = StringUtils.getTranslatedOrFallback(this.getTranslatedName(), this.getName());

        if (this.singlePlayer)
        {
            return GuiBase.TXT_GOLD + name + GuiBase.TXT_RST;
        }

        return name;
    }

    @Override
    public String getPrettyName() {
        return StringUtils.getTranslatedOrFallback(
                Reference.MOD_ID + ".config."+ CATEGORY_ID +".prettyName." + this.getName(), this.getName());
    }

    @Override
    public String getComment() {
        String comment =  StringUtils.getTranslatedOrFallback(
                Reference.MOD_ID + ".config."+ CATEGORY_ID +".comment." + this.getName(), "");

        if (comment != null && this.singlePlayer) {
            return comment + "\n" + StringUtils.translate(Reference.MOD_ID + ".label.config_comment.single_player_only");
        }

        return comment;
    }

    @Override
    public String getTranslatedName() {
        return StringUtils.getTranslatedOrFallback(
                Reference.MOD_ID + ".config."+ CATEGORY_ID +".name." + this.getName(), this.getName());
    }

    @Override
    public String getStringValue()
    {
        return String.valueOf(this.valueBoolean);
    }

    @Override
    public String getDefaultStringValue()
    {
        return String.valueOf(this.defaultValueBoolean);
    }

    @Override
    public void setValueFromString(String value)
    {
    }

    @Override
    public void onValueChanged() {
        if (this.callback != null) {
            this.callback.onValueChanged(this);
        }
    }

    @Override
    public void setValueChangeCallback(IValueChangeCallback<IConfigBoolean> callback)
    {
        this.callback = callback;
    }



    @Override
    public IKeybind getKeybind()
    {
        return this.keybind;
    }

    @Override
    public boolean getBooleanValue()
    {
        return this.valueBoolean;
    }

    @Override
    public boolean getDefaultBooleanValue()
    {
        return this.defaultValueBoolean;
    }

    @Override
    public void setBooleanValue(boolean value)
    {
        boolean oldValue = this.valueBoolean;
        this.valueBoolean = value;

        if (oldValue != this.valueBoolean)
        {
            this.onValueChanged();
        }
    }

    @Override
    public boolean isModified()
    {
        return this.valueBoolean != this.defaultValueBoolean;
    }

    @Override
    public boolean isModified(String newValue)
    {
        return Boolean.parseBoolean(newValue) != this.defaultValueBoolean;
    }

    @Override
    public void resetToDefault()
    {
        this.valueBoolean = this.defaultValueBoolean;
    }

    @Override
    public JsonElement getAsJsonElement()
    {
        return new JsonPrimitive(this.valueBoolean);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element)
    {
        try
        {
            if (element.isJsonPrimitive())
            {
                this.valueBoolean = element.getAsBoolean();
            }
            else
            {
                CowsOddWidgets.LOGGER.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element);
            }
        }
        catch (Exception e)
        {
            CowsOddWidgets.LOGGER.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element, e);
        }
    }

    public static String snakeToCamelCase(String in) {
        String phrase = in.toLowerCase();
        while(phrase.contains("_")) {
            phrase = phrase.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(phrase.charAt(phrase.indexOf("_") + 1))));
        }
        return phrase;
    }
}
