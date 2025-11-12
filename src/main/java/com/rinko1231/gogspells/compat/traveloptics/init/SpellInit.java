package com.rinko1231.gogspells.compat.traveloptics.init;

import com.rinko1231.gogspells.compat.traveloptics.spell.SummonSirenSpell;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SpellInit {
    public static final DeferredRegister<AbstractSpell> TO_SPELLS =
            DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, "gogspells");


    public static final RegistryObject<AbstractSpell> SUMMON_SIREN =
            TO_SPELLS.register("summon_siren", SummonSirenSpell::new);
}