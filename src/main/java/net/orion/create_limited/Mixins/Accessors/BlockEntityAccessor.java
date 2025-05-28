package net.orion.create_limited.Mixins.Accessors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockEntity.class)
public interface BlockEntityAccessor {

    @Accessor
    Level getLevel();

    @Accessor
    BlockPos getWorldPosition();
}