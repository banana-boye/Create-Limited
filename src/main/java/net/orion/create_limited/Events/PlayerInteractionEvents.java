package net.orion.create_limited.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.orion.create_limited.Data.Constants.CommonConstants;
import net.orion.create_limited.Data.Mod.Pack.DatapackRegister;
import net.orion.create_limited.Data.Mod.Pack.DecayType;
import net.orion.create_limited.Interfaces.DecayingBlockEntity;

import java.util.Objects;

@EventBusSubscriber(modid = CommonConstants.MOD_ID)
public class PlayerInteractionEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock rightClickBlock) {
        if (handleUse(rightClickBlock.getItemStack(), rightClickBlock.getPos(), rightClickBlock.getLevel())) rightClickBlock.setCanceled(true);
    }

    public static boolean handleUse(ItemStack itemStack, BlockPos pos, Level level) {
        if (level instanceof ServerLevel server && DatapackRegister.getDecayEntry(server, pos) instanceof DecayType.DecayEntry decayEntry && DatapackRegister.isValidRepairItem(server, pos, itemStack))
            return (((DecayingBlockEntity) Objects.requireNonNull(server.getBlockEntity(pos))).repair(itemStack, decayEntry));
        return false;
    }
}
