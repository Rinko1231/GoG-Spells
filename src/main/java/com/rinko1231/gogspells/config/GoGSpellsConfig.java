package com.rinko1231.gogspells.config;


import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class GoGSpellsConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.BooleanValue summonEnderDragonGirlAllowLooting;
    public static ForgeConfigSpec.DoubleValue summonedEnderDragonGirlBaseHealth;
    public static ForgeConfigSpec.DoubleValue summonedEnderDragonGirlBonusHealthSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue summonedEnderDragonGirlBaseAttackDamage;
    public static ForgeConfigSpec.DoubleValue summonedEnderDragonGirlBonusATKSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue gaiaBlessingExtraDropRateEnderDragonGirl;

    public static ForgeConfigSpec.BooleanValue summonNineTailsAllowLooting;
    public static ForgeConfigSpec.DoubleValue summonedNineTailsBaseHealth;
    public static ForgeConfigSpec.DoubleValue summonedNineTailsBonusHealthSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue summonedNineTailsBaseAttackDamage;
    public static ForgeConfigSpec.DoubleValue summonedNineTailsBonusATKSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue gaiaBlessingExtraDropRateNineTails;

    public static ForgeConfigSpec.BooleanValue summonWerecatAllowLooting;
    public static ForgeConfigSpec.DoubleValue summonedWerecatBaseHealth;
    public static ForgeConfigSpec.DoubleValue summonedWerecatBonusHealthSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue summonedWerecatBaseAttackDamage;
    public static ForgeConfigSpec.DoubleValue summonedWerecatBonusATKSpellPowerRatio;

    public static ForgeConfigSpec.BooleanValue summonSludgeGirlAllowLooting;
    public static ForgeConfigSpec.DoubleValue summonedSludgeGirlBaseHealth;
    public static ForgeConfigSpec.DoubleValue summonedSludgeGirlBonusHealthSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue summonedSludgeGirlBaseAttackDamage;
    public static ForgeConfigSpec.DoubleValue summonedSludgeGirlBonusATKSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue gaiaBlessingExtraDropRateSludgeGirl;

    public static ForgeConfigSpec.BooleanValue summonYukiOnnaAllowLooting;
    public static ForgeConfigSpec.DoubleValue summonedYukiOnnaBaseHealth;
    public static ForgeConfigSpec.DoubleValue summonedYukiOnnaBonusHealthSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue summonedYukiOnnaBaseAttackDamage;
    public static ForgeConfigSpec.DoubleValue summonedYukiOnnaBonusATKSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue gaiaBlessingExtraDropRateYukiOnna;

    public static ForgeConfigSpec.BooleanValue summonMummyAllowLooting;
    public static ForgeConfigSpec.DoubleValue summonedMummyBaseHealth;
    public static ForgeConfigSpec.DoubleValue summonedMummyBonusHealthSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue summonedMummyBaseAttackDamage;
    public static ForgeConfigSpec.DoubleValue summonedMummyBonusATKSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue gaiaBlessingExtraDropRateMummy;

    public static ForgeConfigSpec.BooleanValue summonValkyrieAllowLooting;
    public static ForgeConfigSpec.DoubleValue summonedValkyrieBaseHealth;
    public static ForgeConfigSpec.DoubleValue summonedValkyrieBonusHealthSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue summonedValkyrieBaseAttackDamage;
    public static ForgeConfigSpec.DoubleValue summonedValkyrieBonusATKSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue gaiaBlessingExtraDropRateValkyrie;

    public static ForgeConfigSpec.BooleanValue summonWitchAllowLooting;
    public static ForgeConfigSpec.DoubleValue summonedWitchBaseHealth;
    public static ForgeConfigSpec.DoubleValue summonedWitchBonusHealthSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue summonedWitchBaseAttackDamage;
    public static ForgeConfigSpec.DoubleValue summonedWitchBonusATKSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue gaiaBlessingExtraDropRateWitch;

    public static ForgeConfigSpec.BooleanValue summonArachneAllowLooting;
    public static ForgeConfigSpec.DoubleValue summonedArachneBaseHealth;
    public static ForgeConfigSpec.DoubleValue summonedArachneBonusHealthSpellPowerRatio;
    public static ForgeConfigSpec.DoubleValue summonedArachneBaseAttackDamage;
    public static ForgeConfigSpec.DoubleValue summonedArachneBonusATKSpellPowerRatio;

    static {


        BUILDER.comment("GoG Spells Config");

        BUILDER.push("Summon Ender Dragon Girl Spell");

        summonEnderDragonGirlAllowLooting = BUILDER
                .comment("Whether the Spell can be obtained from loot chests")
                .define("summonEnderDragonGirlAllowLooting",false);
        summonedEnderDragonGirlBaseHealth = BUILDER
                .defineInRange("summonedEnderDragonGirlBaseHealth", 35.0f ,1, Integer.MAX_VALUE);
        summonedEnderDragonGirlBonusHealthSpellPowerRatio = BUILDER
                .comment("Determines how much bonus health summoned creatures gain from your Spell Power.")
                .defineInRange("summonedEnderDragonGirlBonusHealthSpellPowerRatio", 0.3f,0, Integer.MAX_VALUE);
        summonedEnderDragonGirlBaseAttackDamage = BUILDER
                .defineInRange("summonedEnderDragonGirlBaseAttackDamage", 6.0F ,0,  Integer.MAX_VALUE);
        summonedEnderDragonGirlBonusATKSpellPowerRatio = BUILDER
                .comment("Determines how much bonus attack damage summoned creatures gain from your Spell Power.")
                .defineInRange("summonedEnderDragonGirlBonusATKSpellPowerRatio", 0.3f,0, Integer.MAX_VALUE);
        gaiaBlessingExtraDropRateEnderDragonGirl = BUILDER
                .defineInRange("gaiaBlessingExtraDropRateEnderDragonGirl", 0.1, 0, 1.0);

        BUILDER.pop();

        BUILDER.push("Summon Nine Tails Spell");

        summonNineTailsAllowLooting = BUILDER
                .comment("Whether the Spell can be obtained from loot chests")
                .define("summonNineTailsAllowLooting",false);
        summonedNineTailsBaseHealth = BUILDER
                .defineInRange("summonedNineTailsBaseHealth", 35.0f ,1, Integer.MAX_VALUE);
        summonedNineTailsBonusHealthSpellPowerRatio = BUILDER
                .comment("Determines how much bonus health summoned creatures gain from your Spell Power.")
                .defineInRange("summonedNineTailsBonusHealthSpellPowerRatio", 0.3f,0, Integer.MAX_VALUE);
        summonedNineTailsBaseAttackDamage = BUILDER
                .defineInRange("summonedNineTailsBaseAttackDamage", 6.0F ,0,  Integer.MAX_VALUE);
        summonedNineTailsBonusATKSpellPowerRatio = BUILDER
                .comment("Determines how much bonus attack damage summoned creatures gain from your Spell Power.")
                .defineInRange("summonedNineTailsBonusATKSpellPowerRatio", 0.3f,0, Integer.MAX_VALUE);
        gaiaBlessingExtraDropRateNineTails = BUILDER
                .defineInRange("gaiaBlessingExtraDropRateNineTails", 0.1, 0, 1.0);

        BUILDER.pop();

        BUILDER.push("Summon Werecat Spell");

        summonWerecatAllowLooting = BUILDER
                .comment("Whether the Spell can be obtained from loot chests")
                .define("summonWerecatAllowLooting",false);
        summonedWerecatBaseHealth = BUILDER
                .defineInRange("summonedWerecatBaseHealth", 18.0f ,1, Integer.MAX_VALUE);
        summonedWerecatBonusHealthSpellPowerRatio = BUILDER
                .comment("Determines how much bonus health summoned creatures gain from your Spell Power.")
                .defineInRange("summonedWerecatBonusHealthSpellPowerRatio", 0.2f,0, Integer.MAX_VALUE);
        summonedWerecatBaseAttackDamage = BUILDER
                .defineInRange("summonedWerecatBaseAttackDamage", 4.0F ,0,  Integer.MAX_VALUE);
        summonedWerecatBonusATKSpellPowerRatio = BUILDER
                .comment("Determines how much bonus attack damage summoned creatures gain from your Spell Power.")
                .defineInRange("summonedWerecatBonusATKSpellPowerRatio", 0.2f,0, Integer.MAX_VALUE);

        BUILDER.pop();

        BUILDER.push("Summon Sludge Girl Spell");

        summonSludgeGirlAllowLooting = BUILDER
                .comment("Whether the Spell can be obtained from loot chests")
                .define("summonSludgeGirlAllowLooting",false);
        summonedSludgeGirlBaseHealth = BUILDER
                .defineInRange("summonedSludgeGirlBaseHealth", 18.0f ,1, Integer.MAX_VALUE);
        summonedSludgeGirlBonusHealthSpellPowerRatio = BUILDER
                .comment("Determines how much bonus health summoned creatures gain from your Spell Power.")
                .defineInRange("summonedSludgeGirlBonusHealthSpellPowerRatio", 0.2f,0, Integer.MAX_VALUE);
        summonedSludgeGirlBaseAttackDamage = BUILDER
                .defineInRange("summonedSludgeGirlBaseAttackDamage", 3.5F ,0,  Integer.MAX_VALUE);
        summonedSludgeGirlBonusATKSpellPowerRatio = BUILDER
                .comment("Determines how much bonus attack damage summoned creatures gain from your Spell Power.")
                .defineInRange("summonedSludgeGirlBonusATKSpellPowerRatio", 0.2f,0, Integer.MAX_VALUE);
        gaiaBlessingExtraDropRateSludgeGirl = BUILDER
                .defineInRange("gaiaBlessingExtraDropRateSludgeGirl", 0.1, 0, 1.0);

        BUILDER.pop();

        BUILDER.push("Summon Yuki Onna Spell");

        summonYukiOnnaAllowLooting = BUILDER
                .comment("Whether the Spell can be obtained from loot chests")
                .define("summonYukiOnnaAllowLooting",false);
        summonedYukiOnnaBaseHealth = BUILDER
                .defineInRange("summonedYukiOnnaBaseHealth", 35.0f ,1, Integer.MAX_VALUE);
        summonedYukiOnnaBonusHealthSpellPowerRatio = BUILDER
                .comment("Determines how much bonus health summoned creatures gain from your Spell Power.")
                .defineInRange("summonedYukiOnnaBonusHealthSpellPowerRatio", 0.3f,0, Integer.MAX_VALUE);
        summonedYukiOnnaBaseAttackDamage = BUILDER
                .defineInRange("summonedYukiOnnaBaseAttackDamage", 6.0F ,0,  Integer.MAX_VALUE);
        summonedYukiOnnaBonusATKSpellPowerRatio = BUILDER
                .comment("Determines how much bonus attack damage summoned creatures gain from your Spell Power.")
                .defineInRange("summonedYukiOnnaBonusATKSpellPowerRatio", 0.3f,0, Integer.MAX_VALUE);
        gaiaBlessingExtraDropRateYukiOnna = BUILDER
                .defineInRange("gaiaBlessingExtraDropRateYukiOnna", 0.1, 0, 1.0);

        BUILDER.pop();
        
        BUILDER.push("Summon Mummy Spell");

        summonMummyAllowLooting = BUILDER
                .comment("Whether the Spell can be obtained from loot chests")
                .define("summonMummyAllowLooting",false);
        summonedMummyBaseHealth = BUILDER
                .defineInRange("summonedMummyBaseHealth", 24.0f ,1, Integer.MAX_VALUE);
        summonedMummyBonusHealthSpellPowerRatio = BUILDER
                .comment("Determines how much bonus health summoned creatures gain from your Spell Power.")
                .defineInRange("summonedMummyBonusHealthSpellPowerRatio", 0.3f,0, Integer.MAX_VALUE);
        summonedMummyBaseAttackDamage = BUILDER
                .defineInRange("summonedMummyBaseAttackDamage", 3.0F ,0,  Integer.MAX_VALUE);
        summonedMummyBonusATKSpellPowerRatio = BUILDER
                .comment("Determines how much bonus attack damage summoned creatures gain from your Spell Power.")
                .defineInRange("summonedMummyBonusATKSpellPowerRatio", 0.2f,0, Integer.MAX_VALUE);
        gaiaBlessingExtraDropRateMummy = BUILDER
                .defineInRange("gaiaBlessingExtraDropRateMummy", 0.1, 0, 1.0);


        BUILDER.pop();
        
        BUILDER.push("Summon Valkyrie Spell");

        summonValkyrieAllowLooting = BUILDER
                .comment("Whether the Spell can be obtained from loot chests")
                .define("summonValkyrieAllowLooting",false);
        summonedValkyrieBaseHealth = BUILDER
                .defineInRange("summonedValkyrieBaseHealth", 75.0f ,1, Integer.MAX_VALUE);
        summonedValkyrieBonusHealthSpellPowerRatio = BUILDER
                .comment("Determines how much bonus health summoned creatures gain from your Spell Power.")
                .defineInRange("summonedValkyrieBonusHealthSpellPowerRatio", 0.4f,0, Integer.MAX_VALUE);
        summonedValkyrieBaseAttackDamage = BUILDER
                .defineInRange("summonedValkyrieBaseAttackDamage", 10.0F ,0,  Integer.MAX_VALUE);
        summonedValkyrieBonusATKSpellPowerRatio = BUILDER
                .comment("Determines how much bonus attack damage summoned creatures gain from your Spell Power.")
                .defineInRange("summonedValkyrieBonusATKSpellPowerRatio", 0.3f,0, Integer.MAX_VALUE);
        gaiaBlessingExtraDropRateValkyrie = BUILDER
                .defineInRange("gaiaBlessingExtraDropRateValkyrie", 0.1, 0, 1.0);

        BUILDER.pop();
        
        BUILDER.push("Walpurgis Night / Summon Witch(GoG) Spell");

        summonWitchAllowLooting = BUILDER
                .comment("Whether the Spell can be obtained from loot chests")
                .define("summonWitchAllowLooting",false);
        summonedWitchBaseHealth = BUILDER
                .defineInRange("summonedWitchBaseHealth", 25.0f ,1, Integer.MAX_VALUE);
        summonedWitchBonusHealthSpellPowerRatio = BUILDER
                .comment("Determines how much bonus health summoned creatures gain from your Spell Power.")
                .defineInRange("summonedWitchBonusHealthSpellPowerRatio", 0.3f,0, Integer.MAX_VALUE);
        summonedWitchBaseAttackDamage = BUILDER
                .defineInRange("summonedWitchBaseAttackDamage", 6.0F ,0,  Integer.MAX_VALUE);
        summonedWitchBonusATKSpellPowerRatio = BUILDER
                .comment("Determines how much bonus attack damage summoned creatures gain from your Spell Power.")
                .defineInRange("summonedWitchBonusATKSpellPowerRatio", 0.5f,0, Integer.MAX_VALUE);
        gaiaBlessingExtraDropRateWitch = BUILDER
                .defineInRange("gaiaBlessingExtraDropRateWitch", 0.1, 0, 1.0);

        BUILDER.pop();
        
        BUILDER.push("Summon Arachne Spell");

        summonArachneAllowLooting = BUILDER
                .comment("Whether the Spell can be obtained from loot chests")
                .define("summonArachneAllowLooting",false);
        summonedArachneBaseHealth = BUILDER
                .defineInRange("summonedArachneBaseHealth", 18.0f ,1, Integer.MAX_VALUE);
        summonedArachneBonusHealthSpellPowerRatio = BUILDER
                .comment("Determines how much bonus health summoned creatures gain from your Spell Power.")
                .defineInRange("summonedArachneBonusHealthSpellPowerRatio", 0.3f,0, Integer.MAX_VALUE);
        summonedArachneBaseAttackDamage = BUILDER
                .defineInRange("summonedArachneBaseAttackDamage", 5.0F ,0,  Integer.MAX_VALUE);
        summonedArachneBonusATKSpellPowerRatio = BUILDER
                .comment("Determines how much bonus attack damage summoned creatures gain from your Spell Power.")
                .defineInRange("summonedArachneBonusATKSpellPowerRatio", 0.2f,0, Integer.MAX_VALUE);
        
        BUILDER.pop();
        
        BUILDER.push("MISC");

        //build.define







        SPEC = BUILDER.build();
    }

    public static void setup() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "GoGSpellsConfig.toml");
    }


}