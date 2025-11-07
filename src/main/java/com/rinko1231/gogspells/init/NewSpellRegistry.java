package com.rinko1231.gogspells.init;



import com.rinko1231.gogspells.spell.blood.SummonMummySpell;
import com.rinko1231.gogspells.spell.ender.SummonEnderDragonGirlSpell;
import com.rinko1231.gogspells.spell.evocation.SummonWerecatSpell;
import com.rinko1231.gogspells.spell.evocation.SummonWitchSpell;
import com.rinko1231.gogspells.spell.fire.SummonNineTailsSpell;
import com.rinko1231.gogspells.spell.holy.SummonValkyrieSpell;
import com.rinko1231.gogspells.spell.ice.SummonYukiOnnaSpell;
import com.rinko1231.gogspells.spell.nature.SummonArachneSpell;
import com.rinko1231.gogspells.spell.nature.SummonSludgeGirlSpell;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


import java.util.function.Supplier;


public class NewSpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS =
            DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, "gogspells");


    public static final RegistryObject<AbstractSpell> SUMMON_ENDER_DRAGON_GIRL =
            SPELLS.register("summon_ender_dragon_girl", SummonEnderDragonGirlSpell::new);

    public static final RegistryObject<AbstractSpell> SUMMON_NINE_TAILS =
            SPELLS.register("summon_nine_tails", SummonNineTailsSpell::new);

    public static final RegistryObject<AbstractSpell> SUMMON_WERECAT =
            SPELLS.register("summon_werecat", SummonWerecatSpell::new);

    public static final RegistryObject<AbstractSpell> SUMMON_SLUDGE_GIRL =
            SPELLS.register("summon_sludge_girl", SummonSludgeGirlSpell::new);

    public static final RegistryObject<AbstractSpell> SUMMON_YUKI_ONNA =
            SPELLS.register("summon_yuki_onna", SummonYukiOnnaSpell::new);

    public static final RegistryObject<AbstractSpell> SUMMON_MUMMY =
            SPELLS.register("summon_mummy", SummonMummySpell::new);

    public static final RegistryObject<AbstractSpell> SUMMON_VALKYRIE =
            SPELLS.register("summon_valkyrie", SummonValkyrieSpell::new);

    public static final RegistryObject<AbstractSpell> SUMMON_WITCH =
            SPELLS.register("summon_witch", SummonWitchSpell::new);

    public static final RegistryObject<AbstractSpell> SUMMON_ARACHNE =
            SPELLS.register("summon_arachne", SummonArachneSpell::new);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }
}
