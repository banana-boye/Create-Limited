package net.orion.create_limited.Mixins.Accessors;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(SmartBlockEntity.class)
public interface SmartBlockEntityAccessor {

    @Accessor
    int getLazyTickRate();
}
