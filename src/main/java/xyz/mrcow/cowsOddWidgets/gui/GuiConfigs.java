package xyz.mrcow.cowsOddWidgets.gui;

import java.util.Collections;
import java.util.List;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.BooleanHotkeyGuiWrapper;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import xyz.mrcow.cowsOddWidgets.Reference;
import xyz.mrcow.cowsOddWidgets.config.Configs;
import xyz.mrcow.cowsOddWidgets.config.FeatureToggle;

public class GuiConfigs extends GuiConfigsBase {

    private static ConfigGuiTab tab = ConfigGuiTab.GENERIC;

    public GuiConfigs()
    {
        super(10, 50, Reference.MOD_ID, null, "Cow's Odd Widgets");
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;

        for (ConfigGuiTab tab : ConfigGuiTab.values())
        {
            x += this.createButton(x, y, -1, tab);
        }
    }

    private int createButton(int x, int y, int width, ConfigGuiTab tab)
    {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
        button.setEnabled(GuiConfigs.tab != tab);
        this.addButton(button, new ButtonListener(tab, this));

        return button.getWidth() + 2;
    }

    @Override
    protected int getConfigWidth()
    {
        ConfigGuiTab tab = GuiConfigs.tab;

        if (tab == ConfigGuiTab.GENERIC)
        {
            return 150;
        }

        return super.getConfigWidth();
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs()
    {
        ConfigGuiTab tab = GuiConfigs.tab;

        if (tab == ConfigGuiTab.GENERIC) {
            return ConfigOptionWrapper.createFor(Configs.Settings.OPTIONS);
        }
        if (tab == ConfigGuiTab.FEATURE_TOGGLE) {
            return ConfigOptionWrapper.createFor(FeatureToggle.VALUES.stream().map(this::wrapConfig).toList());
        }

        return Collections.emptyList();

    }

    protected BooleanHotkeyGuiWrapper wrapConfig(FeatureToggle config)
    {
        return new BooleanHotkeyGuiWrapper(config.getName(), config, config.getKeybind());
    }


    private static class ButtonListener implements IButtonActionListener
    {
        private final GuiConfigs parent;
        private final ConfigGuiTab tab;

        public ButtonListener(ConfigGuiTab tab, GuiConfigs parent)
        {
            this.tab = tab;
            this.parent = parent;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton)
        {
            GuiConfigs.tab = this.tab;

            this.parent.reCreateListWidget(); // apply the new config width
            this.parent.getListWidget().resetScrollbarPosition();
            this.parent.initGui();
        }
    }

    public enum ConfigGuiTab
    {
        GENERIC ("Generic"),
        FEATURE_TOGGLE ("FeatureToggle");

        private final String translationKey;

        private ConfigGuiTab(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public String getDisplayName()
        {
            return StringUtils.translate(this.translationKey);
        }
    }
}
