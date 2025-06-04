package net.orion.create_limited.Data.Mod.Pack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

    public record DecayEntry(String replaceWith, TagOrItemOrItemListType repairItems, long decayTime, int decayPhases, int amountConsumed) {
        public static final Codec<DecayEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.STRING.optionalFieldOf("replaceWith", "minecraft:air").forGetter(DecayEntry::replaceWith),
                TagOrItemOrItemListType.CODEC.fieldOf("repairItems").forGetter(DecayEntry::repairItems),
                Codec.LONG.fieldOf("decayProgressTime").forGetter(DecayEntry::decayTime),
                Codec.INT.optionalFieldOf("decayPhases", 10).forGetter(DecayEntry::decayPhases),
                Codec.INT.optionalFieldOf("amountConsumed", 1).forGetter(DecayEntry::amountConsumed)
            ).apply(instance, DecayEntry::new)
        );
    }

    public record TagOrItemOrItemListType(List<String> items) {
        public static final Codec<TagOrItemOrItemListType> CODEC =
                Codec.list(Codec.STRING).xmap(TagOrItemOrItemListType::new, TagOrItemOrItemListType::items);
    }
}