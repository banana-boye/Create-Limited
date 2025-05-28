package net.orion.create_limited.Mixins;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.orion.create_limited.Data.Constants.CommonConstants;
import net.orion.create_limited.Data.Mod.Pack.DatapackRegister;
import net.orion.create_limited.Data.Mod.Pack.DecayType;
import net.orion.create_limited.Mixins.Accessors.BlockEntityAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;

@Mixin(KineticBlockEntity.class)
public abstract class KineticBlockEntityImpl {

    @Unique
    int decay = 0;

    @Unique
    long seconds = 0;

    @Unique
    int ticker = 0;

    @Shadow
    public abstract float getGeneratedSpeed();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "com/simibubi/create/content/kinetics/base/KineticEffectHandler.tick ()V"))
    public void lazyTickTail(CallbackInfo ci) {
        Level level = ((BlockEntityAccessor) this).getLevel();
        BlockPos blockPos = ((BlockEntityAccessor) this).getWorldPosition();
        if (!(level instanceof ServerLevel server) || !(DatapackRegister.getDecayValue(server, blockPos) instanceof Long value) || getGeneratedSpeed() == 0) return;

        int id = blockPos.hashCode();
        this.ticker += 1;

        if (this.ticker == 20) {
            this.seconds += 1;
            this.ticker = 0;
        }

        if (value <= seconds) {
            decay += 1;
            seconds = 0;
        };

        if (decay < 10) level.destroyBlockProgress(id, blockPos, decay);
        else {
            seconds = 0;
            ticker = 0;
            level.destroyBlockProgress(id, blockPos, 0);
            level.destroyBlock(blockPos, false);
        }
    }

    @Inject(method = "remove", at = @At("TAIL"))
    public void removeTail(CallbackInfo ci) {
        BlockPos blockPos = ((BlockEntityAccessor) this).getWorldPosition();
        ((BlockEntityAccessor) this).getLevel().destroyBlockProgress(blockPos.hashCode(), blockPos, 0);
    }

    @Inject(method = "write", at = @At("HEAD"))
    public void writeHead(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        compound.putInt(CommonConstants.MOD_ID + "_decay", decay);
        compound.putLong(CommonConstants.MOD_ID + "_seconds", seconds);
    }

    @Inject(method = "read", at = @At("HEAD"))
    public void readHead(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        this.decay = compound.getInt(CommonConstants.MOD_ID + "_decay");
        this.seconds = compound.getLong(CommonConstants.MOD_ID + "_seconds");
    }
}
