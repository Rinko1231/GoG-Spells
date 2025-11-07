package com.rinko1231.gogspells.init;

import com.rinko1231.gogspells.GoGSpells;

import com.rinko1231.gogspells.entity.*;

import com.rinko1231.gogspells.entity.projectile.ArachneAcidOrb;
import com.rinko1231.gogspells.entity.projectile.ArachnePoisonArrow;
import com.rinko1231.gogspells.entity.projectile.ArachnePoisonCloud;
import com.rinko1231.gogspells.entity.projectile.MagicWebProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;



public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, "gogspells");


    public static final RegistryObject<EntityType<SummonedEnderDragonGirl>> SUMMONED_ENDER_DRAGON_GIRL =
            ENTITIES.register("summoned_ender_dragon_girl",
                    () -> EntityType.Builder.<SummonedEnderDragonGirl>of(SummonedEnderDragonGirl::new, MobCategory.MISC)
                            .sized(0.6F, 2.2F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_ender_dragon_girl")).toString()));
    public static final RegistryObject<EntityType<SummonedNineTails>> SUMMONED_NINE_TAILS =
            ENTITIES.register("summoned_nine_tails",
                    () -> EntityType.Builder.<SummonedNineTails>of(SummonedNineTails::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_nine_tails")).toString()));
    public static final RegistryObject<EntityType<SummonedWerecat>> SUMMONED_WERECAT =
            ENTITIES.register("summoned_werecat",
                    () -> EntityType.Builder.<SummonedWerecat>of(SummonedWerecat::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_werecat")).toString()));
    public static final RegistryObject<EntityType<SummonedSludgeGirl>> SUMMONED_SLUDGE_GIRL =
            ENTITIES.register("summoned_sludge_girl",
                    () -> EntityType.Builder.<SummonedSludgeGirl>of(SummonedSludgeGirl::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_sludge_girl")).toString()));
    public static final RegistryObject<EntityType<SummonedYukiOnna>> SUMMONED_YUKI_ONNA =
            ENTITIES.register("summoned_yuki_onna",
                    () -> EntityType.Builder.<SummonedYukiOnna>of(SummonedYukiOnna::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_yuki_onna")).toString()));
    public static final RegistryObject<EntityType<SummonedMummy>> SUMMONED_MUMMY =
            ENTITIES.register("summoned_mummy",
                    () -> EntityType.Builder.<SummonedMummy>of(SummonedMummy::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_mummy")).toString()));
    public static final RegistryObject<EntityType<SummonedGraveMite>> SUMMONED_GRAVEMITE =
            ENTITIES.register("summoned_gravemite",
                    () -> EntityType.Builder.<SummonedGraveMite>of(SummonedGraveMite::new, MobCategory.MISC)
                            .sized(0.4F, 0.3F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_gravemite")).toString()));
    public static final RegistryObject<EntityType<SummonedValkyrie>> SUMMONED_VALKYRIE =
            ENTITIES.register("summoned_valkyrie",
                    () -> EntityType.Builder.<SummonedValkyrie>of(SummonedValkyrie::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_valkyrie")).toString()));
    public static final RegistryObject<EntityType<SummonedWitch>> SUMMONED_WITCH =
            ENTITIES.register("summoned_witch",
                    () -> EntityType.Builder.<SummonedWitch>of(SummonedWitch::new, MobCategory.MISC)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_witch")).toString()));
    //
    public static final RegistryObject<EntityType<SummonedArachne>> SUMMONED_ARACHNE =
            ENTITIES.register("summoned_arachne",
                    () -> EntityType.Builder.<SummonedArachne>of(SummonedArachne::new, MobCategory.MISC)
                            .sized(1.4F, 1.6F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_arachne")).toString()));
    public static final RegistryObject<EntityType<SummonedCaveSpider>> SUMMONED_CAVE_SPIDER =
            ENTITIES.register("summoned_cave_spider",
                    () -> EntityType.Builder.<SummonedCaveSpider>of(SummonedCaveSpider::new, MobCategory.MISC)
                            .sized(0.7F, 0.5F)
                            .clientTrackingRange(8)
                            .build((GoGSpells.id( "summoned_cave_spider")).toString()));
    public static final RegistryObject<EntityType<MagicWebProjectile>> MAGIC_WEB_PROJECTILE =
            ENTITIES.register("magic_web_projectile",
                    () -> EntityType.Builder.<MagicWebProjectile>of(MagicWebProjectile::new, MobCategory.MISC)
                            .sized(0.3125F, 0.3125F)
                            .clientTrackingRange(4).updateInterval(10)
                            .build((GoGSpells.id( "magic_web_projectile")).toString()));
    public static final RegistryObject<EntityType<ArachnePoisonCloud>> ARACHNE_POISON_CLOUD =
            ENTITIES.register("arachne_poison_cloud",
                    () -> EntityType.Builder.<ArachnePoisonCloud>of(ArachnePoisonCloud::new, MobCategory.MISC)
                            .sized(4.0F, 1.2F)
                            .clientTrackingRange(64)
                            .build((GoGSpells.id( "arachne_poison_cloud")).toString()));
    public static final RegistryObject<EntityType<ArachnePoisonArrow>> ARACHNE_POISON_ARROW =
            ENTITIES.register("arachne_poison_arrow",
                    () -> EntityType.Builder.<ArachnePoisonArrow>of(ArachnePoisonArrow::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(64)
                            .build((GoGSpells.id( "arachne_poison_arrow")).toString()));
    public static final RegistryObject<EntityType<ArachneAcidOrb>> ARACHNE_ACID_ORB =
            ENTITIES.register("arachne_acid_orb",
                    () -> EntityType.Builder.<ArachneAcidOrb>of(ArachneAcidOrb::new, MobCategory.MISC)
                            .sized(0.75F, 0.75F)
                            .clientTrackingRange(64)
                            .build((GoGSpells.id( "arachne_acid_orb")).toString()));



    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}