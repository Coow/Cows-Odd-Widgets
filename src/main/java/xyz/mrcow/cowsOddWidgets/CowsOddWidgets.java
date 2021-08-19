package xyz.mrcow.cowsOddWidgets;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ModInitializer;

public class CowsOddWidgets implements ModInitializer {
    @Override
    public void onInitialize() {
        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
        System.out.println("Moo!");
    }
}
