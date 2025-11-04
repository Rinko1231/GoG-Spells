package com.rinko1231.gogspells.init;

import com.rinko1231.gogspells.GoGSpells;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class TagsRegistry {

    public static final TagKey<EntityType<?>> CURIOS_DROP;

    static {
        CURIOS_DROP = TagKey.create(Registries.ENTITY_TYPE, GoGSpells.id( "curios_drop"));

     }

    public TagsRegistry() {
    }

    private static TagKey<DamageType> create(String tag) {
        return TagKey.create(Registries.DAMAGE_TYPE, GoGSpells.id( tag));
    }
}
