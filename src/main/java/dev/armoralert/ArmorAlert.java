package dev.armoralert;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArmorAlert implements ModInitializer {
    public static final String MOD_ID = "armoralert";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("ArmorAlert initialized for Minecraft 26.3");
    }
}
