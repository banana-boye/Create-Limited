package net.orion.create_limited.Interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.orion.create_limited.Data.Constants.CommonConstants;
import net.orion.create_limited.Data.Mod.Pack.DatapackRegister;
import net.orion.create_limited.Mixins.Accessors.BlockEntityAccessor;
import org.spongepowered.asm.mixin.Unique;

public interface DecayingBlockEntity {


    @Unique
    int getCreate_Limited$decay();
    @Unique
    void setCreate_Limited$decay(int i);

    @Unique
    long getCreate_Limited$seconds();
    @Unique
    void setCreate_Limited$seconds(long i);

    @Unique
    int getCreate_Limited$ticker();
    @Unique
    void setCreate_Limited$ticker(int i);


    @Unique
    float create_Limited$getGeneratedSpeedImpl();

    @Unique
    default void lazyTickImpl(BlockPos blockPos) {
        Level level = ((BlockEntityAccessor) this).getLevel();
        if (!(level instanceof ServerLevel server) || !(DatapackRegister.getDecayValue(server, blockPos) instanceof Long value) || create_Limited$getGeneratedSpeedImpl() == 0) return;

        int id = blockPos.hashCode();
        setCreate_Limited$ticker(getCreate_Limited$ticker() + 1);

        if (getCreate_Limited$ticker() == 20) {
            setCreate_Limited$seconds(getCreate_Limited$seconds() + 1);
            setCreate_Limited$ticker(0);
        }

        if (value <= getCreate_Limited$seconds()) {
            setCreate_Limited$decay(getCreate_Limited$decay() + 1);
            setCreate_Limited$seconds(0);
        }

        if (getCreate_Limited$decay() < 9) level.destroyBlockProgress(id, blockPos, getCreate_Limited$decay());
        else {
            setCreate_Limited$seconds(0);
            setCreate_Limited$ticker(0);
            level.destroyBlockProgress(id, blockPos, -1);
            level.destroyBlock(blockPos, false);
        }
    }

    @Unique
    default void removeImpl() {
        BlockPos blockPos = ((BlockEntityAccessor) this).getWorldPosition();
        ((BlockEntityAccessor) this).getLevel().destroyBlockProgress(blockPos.hashCode(), blockPos, -1);
    }

    @Unique
    default void writeImpl(CompoundTag compound) {
        compound.putInt(CommonConstants.MOD_ID + "_decay", getCreate_Limited$decay());
        compound.putLong(CommonConstants.MOD_ID + "_seconds", getCreate_Limited$seconds());
    }

    @Unique
    default void readImpl(CompoundTag compound) {
        setCreate_Limited$decay(compound.getInt(CommonConstants.MOD_ID + "_decay"));
        setCreate_Limited$seconds(compound.getLong(CommonConstants.MOD_ID + "_seconds"));
    }

    @Unique
    default boolean repair(ItemStack itemStack) {
        if (getCreate_Limited$decay() == -1) return false;
        setCreate_Limited$decay(getCreate_Limited$decay() - 1);
        BlockPos blockPos = ((BlockEntityAccessor) this).getWorldPosition();
        ((BlockEntityAccessor) this).getLevel().destroyBlockProgress(blockPos.hashCode(), blockPos, getCreate_Limited$decay());
        itemStack.shrink(1);
        return true;
    }
}
