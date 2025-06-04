package net.orion.create_limited.Data.Mod.Pack;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.orion.create_limited.Data.Constants.CommonConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@EventBusSubscriber(modid = CommonConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DatapackRegister {
    public static final ResourceKey<Registry<DecayType>> DECAY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(CommonConstants.MOD_ID, "decay_data"));

    @SubscribeEvent
    public static void onDataPackRegistry(DataPackRegistryEvent.NewRegistry newRegistry) {
        newRegistry.dataPackRegistry(DECAY_KEY, DecayType.CODEC);
    }

    public static DecayType.DecayEntry getDecayEntry(ServerLevel server, BlockPos blockPos) {
        RegistryAccess registryAccess = server.registryAccess();
        Optional<Registry<DecayType>> registryOptional = registryAccess.registry(DatapackRegister.DECAY_KEY);
        if (registryOptional.isEmpty()) return null;

        String blockKey = BuiltInRegistries.BLOCK.getKey(getBlock(server, blockPos)).toString();

        for (DecayType decayType : registryOptional.get()) {
            Map<String, DecayType.DecayEntry> map = decayType.blockDecayValueMap();
            if (map.containsKey(blockKey)) return map.get(blockKey);
        }
        return null;
    }

    public static BlockState getToReplaceWith(ServerLevel server, BlockPos pos) {
        if (getDecayEntry(server, pos) instanceof DecayType.DecayEntry decayEntry) {
            CommonConstants.debug(decayEntry.replaceWith());
            CommonConstants.debug(BuiltInRegistries.BLOCK.get(ResourceLocation.parse(decayEntry.replaceWith())).toString());
            return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(decayEntry.replaceWith())).defaultBlockState();
        } else return Blocks.AIR.defaultBlockState();
    }

    public static int getRepairAmount(ServerLevel server, BlockPos pos) {
        if (getDecayEntry(server, pos) instanceof DecayType.DecayEntry decayEntry) return decayEntry.amountConsumed();
        else return 1;
    }

    public static boolean isValidRepairItem(ServerLevel server, BlockPos blockPos, ItemStack itemStack) {
        if (getDecayEntry(server, blockPos) instanceof DecayType.DecayEntry decayEntry) {
            String itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();
            List<String> repairItems = decayEntry.repairItems().items();
            CommonConstants.debug(repairItems.toString());
            return repairItems.contains(itemId) || itemStack.getTags().anyMatch(itemTagKey -> repairItems.contains("#" + itemTagKey.location()));
        } else return false;
    }

    @Nullable
    public static Long getDecayValue(ServerLevel server, BlockPos blockPos) {
        RegistryAccess registryAccess = server.registryAccess();
        Optional<Registry<DecayType>> registryOptional = registryAccess.registry(DatapackRegister.DECAY_KEY);
        if (registryOptional.isEmpty()) return null;

        String blockKey = BuiltInRegistries.BLOCK.getKey(getBlock(server, blockPos)).toString();

        for (DecayType decayType : registryOptional.get()) {
            Map<String, DecayType.DecayEntry> map = decayType.blockDecayValueMap();
            if (map.containsKey(blockKey)) return map.get(blockKey).decayTime();
        }
        return null;
    }

    public static Block getBlock(ServerLevel server, BlockPos blockPos) {
        BlockState blockState = server.getBlockState(blockPos);
        return blockState.getBlock();
    }
}
