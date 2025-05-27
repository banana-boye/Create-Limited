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
        ModContainer container = ModLoadingContext.get().getActiveContainer();
//        IEventBus modEventBus = container.getEventBus();

        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG_SPEC);

        CommonConstants.debug("Initializing Create: Limited...");

        CommonConstants.debug("Create: Limited initialized successfully.");
    }
}