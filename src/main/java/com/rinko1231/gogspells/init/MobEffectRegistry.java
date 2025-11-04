package com.rinko1231.gogspells.init;




import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


import static com.rinko1231.gogspells.GoGSpells.MOD_ID;

public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER;


    //public static final DeferredHolder<MobEffect, MobEffect>  FROZEN;

    //public static final DeferredHolder<MobEffect, MobEffect>  FLASHBANGED;

    //public static final DeferredHolder<MobEffect, MobEffect>  SUMMON_BLAZE_TIMER;


    static {
        MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, MOD_ID);

        //SUMMON_BLAZE_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("summon_blaze_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 12495141));


    }

    public MobEffectRegistry() {
    }

    public static void register(IEventBus eventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }
}