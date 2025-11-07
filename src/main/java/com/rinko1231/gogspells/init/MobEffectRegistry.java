package com.rinko1231.gogspells.init;




import io.redspace.ironsspellbooks.effect.SummonTimer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


import static com.rinko1231.gogspells.GoGSpells.MOD_ID;
public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER;

    public static final RegistryObject<MobEffect> SUMMON_ARACHNE_TIMER;
    public static final RegistryObject<MobEffect> SUMMON_ENDER_DRAGON_GIRL_TIMER;
    public static final RegistryObject<MobEffect> SUMMON_MUMMY_TIMER;
    public static final RegistryObject<MobEffect> SUMMON_NINE_TAILS_TIMER;
    public static final RegistryObject<MobEffect> SUMMON_SLUDGE_GIRL_TIMER;
    public static final RegistryObject<MobEffect> SUMMON_VALKYRIE_TIMER;
    public static final RegistryObject<MobEffect> SUMMON_WERECAT_TIMER;
    public static final RegistryObject<MobEffect> SUMMON_WITCH_TIMER;
    public static final RegistryObject<MobEffect> SUMMON_YUKI_ONNA_TIMER;



    static {
        MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, MOD_ID);

        SUMMON_ARACHNE_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("summon_arachne_timer",
                () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 12495141));
        SUMMON_ENDER_DRAGON_GIRL_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("summon_ender_dragon_girl_timer",
                () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 10395294));
        SUMMON_MUMMY_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("summon_mummy_timer",
                () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 9876543));
        SUMMON_NINE_TAILS_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("summon_nine_tails_timer",
                () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 14520608));
        SUMMON_SLUDGE_GIRL_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("summon_sludge_girl_timer",
                () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 7841275));
        SUMMON_VALKYRIE_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("summon_valkyrie_timer",
                () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 15644859));
        SUMMON_WERECAT_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("summon_werecat_timer",
                () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 11240357));
        SUMMON_WITCH_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("summon_witch_timer",
                () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 8947848));
        SUMMON_YUKI_ONNA_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("summon_yuki_onna_timer",
                () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 14671871));
    }

    public MobEffectRegistry() {
    }

    public static void register(IEventBus eventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }
}