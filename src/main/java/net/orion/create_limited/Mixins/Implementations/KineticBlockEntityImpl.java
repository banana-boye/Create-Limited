package net.orion.create_limited.Mixins.Implementations;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
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

@Mixin(KineticBlockEntity.class)
public abstract class KineticBlockEntityImpl implements DecayingBlockEntity {

    @Unique
    int create_Limited$decay = -1;

    @Unique
    long create_Limited$seconds = 0;

    @Unique
    int create_Limited$ticker = 0;

    @Shadow
    public abstract float getGeneratedSpeed();

    @Unique
    @Override
    public float create_Limited$getGeneratedSpeedImpl() {
        return getGeneratedSpeed();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "com/simibubi/create/content/kinetics/base/KineticEffectHandler.tick ()V"))
    public void lazyTickTail(CallbackInfo ci) {
        this.lazyTickImpl(((BlockEntityAccessor) this).getWorldPosition());
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
    public void setCreate_Limited$ticker(int i) {
        this.create_Limited$ticker = i;
    }

    @Override
    public int getCreate_Limited$ticker() {
        return this.create_Limited$ticker;
    }

    @Override
    public void setCreate_Limited$seconds(long i) {
        this.create_Limited$seconds = i;
    }

    @Override
    public long getCreate_Limited$seconds() {
        return this.create_Limited$seconds;
    }

    @Override
    public void setCreate_Limited$decay(int i) {
        this.create_Limited$decay = i;
    }

    @Override
    public int getCreate_Limited$decay() {
        return this.create_Limited$decay;
    }
}
