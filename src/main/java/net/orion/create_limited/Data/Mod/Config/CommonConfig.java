package net.orion.create_limited.Data.Mod.Config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CommonConfig {

    public static final CommonConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    private final ModConfigSpec.Builder builder;

    static {
        Pair<CommonConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(CommonConfig::new);

        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    // Debug
    public final ModConfigSpec.BooleanValue log;

    private CommonConfig(final ModConfigSpec.Builder builder) {
        this.builder = builder;

        this.builder.push("Create: Limited config.");
        this.builder.pop();

        this.builder.push("Debug:");

        this.log = this.builder
                .comment("Enable logging.")
                .define("log", false);

        this.builder.pop();
    }
}
