package xyz.mrcow.cowsOddWidgets.features;

import fi.dy.masa.malilib.gui.GuiBase;
import xyz.mrcow.cowsOddWidgets.gui.GuiConfigs;

public class OpenGui implements IKeybindable{
    @Override
    public boolean invokeHotkey() {
        GuiBase.openGui(new GuiConfigs());
        return true;
    }
}
