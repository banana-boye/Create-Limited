package net.orion.create_limited.Data.Mod.Pack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.orion.create_limited.Data.Constants.CommonConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record DecayType(Map<String, DecayEntry> blockDecayValueMap) {
    public static final Codec<DecayType> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.unboundedMap(Codec.STRING, DecayEntry.CODEC)
                .fieldOf("values")
                .forGetter(DecayType::blockDecayValueMap)
        ).apply(instance, DecayType::new)
    );

    public record DecayEntry(String replaceWith, TagOrItemOrItemListType repairItems, long decayTime, int decayPhases, DecayEnumType decayEnumType, int amountConsumed) {
        public static final Codec<DecayEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.STRING.optionalFieldOf("replaceWith", "minecraft:air").forGetter(DecayEntry::replaceWith),
                TagOrItemOrItemListType.CODEC.fieldOf("repairItems").forGetter(DecayEntry::repairItems),
                Codec.LONG.fieldOf("decayProgressTime").forGetter(DecayEntry::decayTime),
                Codec.INT.optionalFieldOf("decayPhases", 10).forGetter(DecayEntry::decayPhases),
                DecayEnumType.CODEC.optionalFieldOf("type", DecayEnumType.SINGLE).forGetter(DecayEntry::decayEnumType),
                Codec.INT.optionalFieldOf("amountConsumed", 1).forGetter(DecayEntry::amountConsumed)
            ).apply(instance, DecayEntry::new)
        );
    }

    public record TagOrItemOrItemListType(List<String> items) {
        public static final Codec<TagOrItemOrItemListType> CODEC =
                Codec.list(Codec.STRING).xmap(TagOrItemOrItemListType::new, TagOrItemOrItemListType::items);
    }

    public enum DecayEnumType {
        SINGLE("single"),
        MULTI("multi"),
        COMPONENT("component");

        public final String type;

        DecayEnumType(String type) {
            this.type = type.toLowerCase();
        }

        public static final Codec<DecayEnumType> CODEC = Codec.STRING.xmap(
            DecayEnumType::fromString,
            decayEnumType -> decayEnumType.type
        );

        private static DecayEnumType fromString(String name) {
            return Arrays.stream(values())
                    .filter(e -> e.type.equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown DecayEnumType: " + name));
        }
    }
}