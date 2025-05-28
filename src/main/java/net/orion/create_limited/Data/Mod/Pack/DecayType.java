package net.orion.create_limited.Data.Mod.Pack;

import com.mojang.datafixers.util.Either;
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

    public record DecayEntry(TagOrItemOrItemListType repairItems, long decayTime) {
        public static final Codec<DecayEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                TagOrItemOrItemListType.CODEC.fieldOf("repairItems").forGetter(DecayEntry::repairItems),
                Codec.LONG.fieldOf("decayTime").forGetter(DecayEntry::decayTime)
            ).apply(instance, DecayEntry::new)
        );
    }

    public record TagOrItemOrItemListType(List<String> items, boolean isTag) {
        public static final Codec<TagOrItemOrItemListType> CODEC =
            Codec.either(
                Codec.STRING,
                Codec.list(Codec.STRING)
            ).xmap(
                either -> {
                    if (either.left().isPresent()) {
                        String value = either.left().get();
                        if (value.startsWith("#"))
                            return new TagOrItemOrItemListType(List.of(value.substring(1)), true);
                        else
                            return new TagOrItemOrItemListType(List.of(value), false);
                    } else {
                        return new TagOrItemOrItemListType(either.right().get(), false);
                    }
                },
                obj -> {
                    if (obj.isTag)
                        return Either.left("#" + obj.items.get(0));
                    else if (obj.items.size() == 1)
                        return Either.left(obj.items.get(0));
                    else
                        return Either.right(obj.items);
                }
            );
    }
}