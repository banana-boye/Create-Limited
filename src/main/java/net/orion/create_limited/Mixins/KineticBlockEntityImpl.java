package net.orion.create_limited.Mixins;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.orion.create_limited.Data.Constants.CommonConstants;
import net.orion.create_limited.Data.Mod.Pack.DatapackRegister;
import net.orion.create_limited.Mixins.Accessors.BlockEntityAccessor;
import net.orion.create_limited.Interfaces.DecayData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KineticBlockEntity.class)
public abstract class KineticBlockEntityImpl implements DecayData {

    @Unique
    int create_Limited$decay = -1;

    @Unique
    long create_Limited$seconds = 0;

    @Unique
    int create_Limited$ticker = 0;

    @Shadow
    public abstract float getGeneratedSpeed();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "com/simibubi/create/content/kinetics/base/KineticEffectHandler.tick ()V"))
    public void lazyTickTail(CallbackInfo ci) {
        Level level = ((BlockEntityAccessor) this).getLevel();
        BlockPos blockPos = ((BlockEntityAccessor) this).getWorldPosition();
        if (!(level instanceof ServerLevel server) || !(DatapackRegister.getDecayValue(server, blockPos) instanceof Long value) || getGeneratedSpeed() == 0) return;

        int id = blockPos.hashCode();
        this.create_Limited$ticker += 1;

        if (this.create_Limited$ticker == 20) {
            this.create_Limited$seconds += 1;
            this.create_Limited$ticker = 0;
        }

        if (value <= create_Limited$seconds) {
            create_Limited$decay += 1;
            create_Limited$seconds = 0;
        }

        if (create_Limited$decay < 9) level.destroyBlockProgress(id, blockPos, create_Limited$decay);
        else {
            create_Limited$seconds = 0;
            create_Limited$ticker = 0;
            level.destroyBlockProgress(id, blockPos, -1);
            level.destroyBlock(blockPos, false);
        }
    }

    @Inject(method = "remove", at = @At("TAIL"))
    public void removeTail(CallbackInfo ci) {
        BlockPos blockPos = ((BlockEntityAccessor) this).getWorldPosition();
        ((BlockEntityAccessor) this).getLevel().destroyBlockProgress(blockPos.hashCode(), blockPos, -1);
    }

    @Inject(method = "write", at = @At("HEAD"))
    public void writeHead(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        compound.putInt(CommonConstants.MOD_ID + "_decay", create_Limited$decay);
        compound.putLong(CommonConstants.MOD_ID + "_seconds", create_Limited$seconds);
    }

    @Inject(method = "read", at = @At("HEAD"))
    public void readHead(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        this.create_Limited$decay = compound.getInt(CommonConstants.MOD_ID + "_decay");
        this.create_Limited$seconds = compound.getLong(CommonConstants.MOD_ID + "_seconds");
    }

    @Unique
    @Override
    public boolean repair(ItemStack itemStack) {
        if (this.create_Limited$decay == -1) return false;
        this.create_Limited$decay -= 1;
        BlockPos blockPos = ((BlockEntityAccessor) this).getWorldPosition();
        ((BlockEntityAccessor) this).getLevel().destroyBlockProgress(blockPos.hashCode(), blockPos, this.create_Limited$decay);
        itemStack.shrink(1);
        return true;
    }
}
