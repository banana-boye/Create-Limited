package net.orion.create_limited.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.orion.create_limited.Data.Constants.CommonConstants;
import net.orion.create_limited.Data.Mod.Pack.DatapackRegister;
import net.orion.create_limited.Interfaces.DecayData;

@EventBusSubscriber(modid = CommonConstants.MOD_ID)
public class PlayerInteractionEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock rightClickBlock) {
        Player player = rightClickBlock.getEntity();
        Level level = rightClickBlock.getLevel();
        BlockPos pos = rightClickBlock.getPos();
        ItemStack itemStack = rightClickBlock.getItemStack();
        String itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();

        if (!level.isClientSide && DatapackRegister.getDecayValue(((ServerLevel) level), pos) instanceof Long value && DatapackRegister.isValidRepairItem((ServerLevel) level, pos, itemId))
            rightClickBlock.setCanceled(((DecayData) level.getBlockEntity(pos)).repair(itemStack));
    }
}
