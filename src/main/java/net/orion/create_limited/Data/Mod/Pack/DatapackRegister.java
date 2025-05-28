package net.orion.create_limited.Data.Mod.Pack;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.orion.create_limited.Data.Constants.CommonConstants;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

@EventBusSubscriber(modid = CommonConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DatapackRegister {
    public static final ResourceKey<Registry<DecayType>> DECAY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(CommonConstants.MOD_ID, "decay_data"));

    @SubscribeEvent
    public static void onDataPackRegistry(DataPackRegistryEvent.NewRegistry newRegistry) {
        newRegistry.dataPackRegistry(DECAY_KEY, DecayType.CODEC);
    }

    public static boolean isInvalidRepairItem(ServerLevel server, BlockPos blockPos, String itemId) {
        RegistryAccess registryAccess = server.registryAccess();
        Optional<Registry<DecayType>> registryOptional = registryAccess.registry(DatapackRegister.DECAY_KEY);
        if (registryOptional.isEmpty()) return true;

        BlockState blockState = server.getBlockState(blockPos);
        String blockKey = BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).toString();

        for (DecayType decayType : registryOptional.get()) {
            Map<String, DecayType.DecayEntry> map = decayType.blockDecayValueMap();
            if (map.containsKey(blockKey)) {
                if (map.get(blockKey).repairItems().items().contains(itemId)) return false;
            }
        }
        return true;
    }

    @Nullable
    public static Long getDecayValue(ServerLevel server, BlockPos blockPos) {
        RegistryAccess registryAccess = server.registryAccess();
        Optional<Registry<DecayType>> registryOptional = registryAccess.registry(DatapackRegister.DECAY_KEY);
        if (registryOptional.isEmpty()) return null;

        BlockState blockState = server.getBlockState(blockPos);
        String blockKey = BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).toString();

        for (DecayType decayType : registryOptional.get()) {
            Map<String, DecayType.DecayEntry> map = decayType.blockDecayValueMap();
            if (map.containsKey(blockKey)) return map.get(blockKey).decayTime();
        }
        return null;
    }
}
