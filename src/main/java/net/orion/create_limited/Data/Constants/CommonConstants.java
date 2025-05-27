package net.orion.create_limited.Data.Constants;

import net.orion.create_limited.Data.Mod.Config.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonConstants {

    public static final String MOD_ID = "create_limited";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void debug(String message) {
        if (CommonConfig.CONFIG.log.get()) {
            LOGGER.info("[Debug] " + message);
        }
    }

    public static void debug(String format, Object... args) {
        if (CommonConfig.CONFIG.log.get()) {
            LOGGER.info("[Debug] " + format, args);
        }
    }
}
