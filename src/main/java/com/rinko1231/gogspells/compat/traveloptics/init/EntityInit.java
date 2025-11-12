package com.rinko1231.gogspells.compat.traveloptics.init;

import com.rinko1231.gogspells.GoGSpells;

import com.rinko1231.gogspells.compat.traveloptics.entity.SummonedSiren;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> TO_ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, "gogspells");


    public static final RegistryObject<EntityType<SummonedSiren>> SUMMONED_SIREN =
            TO_ENTITIES.register("summoned_siren",
                    () -> EntityType.Builder.<SummonedSiren>of(SummonedSiren::new, MobCategory.MISC)
                            .sized(0.6F, 2.2F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id("summoned_siren")).toString()));
}