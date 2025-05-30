package net.orion.create_limited.Mixins.Implementations;

import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.orion.create_limited.Interfaces.DecayingBlockEntity;
import net.orion.create_limited.Mixins.Accessors.BlockEntityAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PoweredShaftBlockEntity.class)
public abstract class PoweredShaftBlockEntityImpl implements DecayingBlockEntity {

    @Unique
    float create_Limited$decay = -1;

    @Unique
    long create_Limited$seconds = 0;

    @Unique
    int create_Limited$ticker = 0;

    @Shadow
    public abstract float getGeneratedSpeed();

    @Shadow public BlockPos enginePos;

    @Unique
    @Override
    public float create_Limited$getGeneratedSpeedImpl() {
        return getGeneratedSpeed();
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void lazyTickTail(CallbackInfo ci) {
        if (this.enginePos == null) return;
        this.lazyTickImpl(((BlockEntityAccessor) this).getWorldPosition().subtract(this.enginePos));
    }

    @Inject(method = "remove", at = @At("TAIL"))
    public void removeTail(CallbackInfo ci) {
        this.removeImpl();
    }

    @Inject(method = "write", at = @At("HEAD"))
    public void writeHead(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        this.writeImpl(compound);
    }

    @Inject(method = "read", at = @At("HEAD"))
    public void readHead(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        this.readImpl(compound);
    }

    @Override
    public float getCreate_Limited$decay() {
        return this.create_Limited$decay;
    }

    @Override
    public void setCreate_Limited$decay(float i) {
        this.create_Limited$decay = i;
    }

    @Override
    public long getCreate_Limited$seconds() {
        return this.create_Limited$seconds;
    }

    @Override
    public void setCreate_Limited$seconds(long i) {
        this.create_Limited$seconds = i;
    }

    @Override
    public int getCreate_Limited$ticker() {
        return this.create_Limited$ticker;
    }

    @Override
    public void setCreate_Limited$ticker(int i) {
        this.create_Limited$ticker = i;
    }
}
