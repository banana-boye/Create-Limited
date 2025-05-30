package net.orion.create_limited.Mixins.Implementations;

import com.simibubi.create.content.kinetics.waterwheel.WaterWheelStructuralBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.orion.create_limited.Events.PlayerInteractionEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterWheelStructuralBlock.class)
public abstract class WaterWheelStructuralBlockImpl {

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    public void onUse(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (PlayerInteractionEvents.handleUse(stack, WaterWheelStructuralBlock.getMaster(level, pos, state), level)) cir.setReturnValue(ItemInteractionResult.SUCCESS);
    }
}
