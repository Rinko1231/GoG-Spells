package com.rinko1231.gogspells.init;

import com.rinko1231.gogspells.GoGSpells;

import com.rinko1231.gogspells.entity.*;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, "gogspells");


    public static final DeferredHolder<EntityType<?>, EntityType<SummonedEnderDragonGirl>> SUMMONED_ENDER_DRAGON_GIRL =
            ENTITIES.register("summoned_ender_dragon_girl",
                    () -> EntityType.Builder.<SummonedEnderDragonGirl>of(SummonedEnderDragonGirl::new, MobCategory.MISC)
                            .sized(0.6F, 2.2F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_ender_dragon_girl")).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedNineTails>> SUMMONED_NINE_TAILS =
            ENTITIES.register("summoned_nine_tails",
                    () -> EntityType.Builder.<SummonedNineTails>of(SummonedNineTails::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_nine_tails")).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedWerecat>> SUMMONED_WERECAT =
            ENTITIES.register("summoned_werecat",
                    () -> EntityType.Builder.<SummonedWerecat>of(SummonedWerecat::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_werecat")).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedSludgeGirl>> SUMMONED_SLUDGE_GIRL =
            ENTITIES.register("summoned_sludge_girl",
                    () -> EntityType.Builder.<SummonedSludgeGirl>of(SummonedSludgeGirl::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_sludge_girl")).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedYukiOnna>> SUMMONED_YUKI_ONNA =
            ENTITIES.register("summoned_yuki_onna",
                    () -> EntityType.Builder.<SummonedYukiOnna>of(SummonedYukiOnna::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_yuki_onna")).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedMummy>> SUMMONED_MUMMY =
            ENTITIES.register("summoned_mummy",
                    () -> EntityType.Builder.<SummonedMummy>of(SummonedMummy::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_mummy")).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedGraveMite>> SUMMONED_GRAVEMITE =
            ENTITIES.register("summoned_gravemite",
                    () -> EntityType.Builder.<SummonedGraveMite>of(SummonedGraveMite::new, MobCategory.MISC)
                            .sized(0.4F, 0.3F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_gravemite")).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedValkyrie>> SUMMONED_VALKYRIE =
            ENTITIES.register("summoned_valkyrie",
                    () -> EntityType.Builder.<SummonedValkyrie>of(SummonedValkyrie::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_valkyrie")).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedWitch>> SUMMONED_WITCH =
            ENTITIES.register("summoned_witch",
                    () -> EntityType.Builder.<SummonedWitch>of(SummonedWitch::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_witch")).toString()));



    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}