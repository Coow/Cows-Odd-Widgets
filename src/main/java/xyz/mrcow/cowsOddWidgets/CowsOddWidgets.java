package xyz.mrcow.cowsOddWidgets;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CowsOddWidgets implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

    @Override
    public void onInitialize() {
        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
        LOGGER.info("Moo!");
    }


}
