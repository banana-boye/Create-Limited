package net.orion.create_limited.Data.Constants;

import net.orion.create_limited.Data.Mod.Config.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class CommonConstants {

    public static final String MOD_ID = "create_limited";

    // Logger

    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final Marker DEBUG_MARKER = MarkerFactory.getMarker("Debug");
    private static final Marker INFO_MARKER = MarkerFactory.getMarker("Info");

    public static void mandatoryLog(String message) {
        LOGGER.info(INFO_MARKER, message);
    }

    public static void mandatoryLog(String message, Object... args) {
        LOGGER.info(INFO_MARKER, message, args);
    }

    public static void debug(String message) {
        if (CommonConfig.CONFIG.log.get()) {
            LOGGER.info(DEBUG_MARKER, message);
        }
    }

    public static void debug(String message, Object... args) {
        if (CommonConfig.CONFIG.log.get()) {
            LOGGER.info(DEBUG_MARKER, message, args);
        }
    }
}
