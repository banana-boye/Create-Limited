package net.orion.create_limited;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.orion.create_limited.Data.Constants.CommonConstants;
import net.orion.create_limited.Data.Mod.Config.CommonConfig;

@Mod(CommonConstants.MOD_ID)
public class CreateLimited {

    public CreateLimited() {
        CommonConstants.mandatoryLog("Initializing Create: Limited...");
        ModContainer container = ModLoadingContext.get().getActiveContainer();

        CommonConstants.mandatoryLog("Loading config..");
        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG_SPEC);
        CommonConstants.mandatoryLog("Config loaded.");

        CommonConstants.mandatoryLog("Create: Limited initialized successfully.");
    }
}