package com.rinko1231.gogspells.config;


import net.neoforged.neoforge.common.ModConfigSpec;


public class GoGSpellsConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec SPEC;

    public static ModConfigSpec.BooleanValue summonEnderDragonGirlAllowLooting;
    public static ModConfigSpec.DoubleValue summonedEnderDragonGirlBaseHealth;
    public static ModConfigSpec.DoubleValue summonedEnderDragonGirlBonusHealthSpellPowerRatio;
    public static ModConfigSpec.DoubleValue summonedEnderDragonGirlBaseAttackDamage;
    public static ModConfigSpec.DoubleValue summonedEnderDragonGirlBonusATKSpellPowerRatio;
    public static ModConfigSpec.DoubleValue gaiaBlessingExtraDropRateEnderDragonGirl;

    public static ModConfigSpec.BooleanValue summonNineTailsAllowLooting;
    public static ModConfigSpec.DoubleValue summonedNineTailsBaseHealth;
    public static ModConfigSpec.DoubleValue summonedNineTailsBonusHealthSpellPowerRatio;
    public static ModConfigSpec.DoubleValue summonedNineTailsBaseAttackDamage;
    public static ModConfigSpec.DoubleValue summonedNineTailsBonusATKSpellPowerRatio;
    public static ModConfigSpec.DoubleValue gaiaBlessingExtraDropRateNineTails;

    public static ModConfigSpec.BooleanValue summonWerecatAllowLooting;
    public static ModConfigSpec.DoubleValue summonedWerecatBaseHealth;
    public static ModConfigSpec.DoubleValue summonedWerecatBonusHealthSpellPowerRatio;
    public static ModConfigSpec.DoubleValue summonedWerecatBaseAttackDamage;
    public static ModConfigSpec.DoubleValue summonedWerecatBonusATKSpellPowerRatio;

    public static ModConfigSpec.BooleanValue summonSludgeGirlAllowLooting;
    public static ModConfigSpec.DoubleValue summonedSludgeGirlBaseHealth;
    public static ModConfigSpec.DoubleValue summonedSludgeGirlBonusHealthSpellPowerRatio;
    public static ModConfigSpec.DoubleValue summonedSludgeGirlBaseAttackDamage;
    public static ModConfigSpec.DoubleValue summonedSludgeGirlBonusATKSpellPowerRatio;
    public static ModConfigSpec.DoubleValue gaiaBlessingExtraDropRateSludgeGirl;

    public static ModConfigSpec.BooleanValue summonYukiOnnaAllowLooting;
    public static ModConfigSpec.DoubleValue summonedYukiOnnaBaseHealth;
    public static ModConfigSpec.DoubleValue summonedYukiOnnaBonusHealthSpellPowerRatio;
    public static ModConfigSpec.DoubleValue summonedYukiOnnaBaseAttackDamage;
    public static ModConfigSpec.DoubleValue summonedYukiOnnaBonusATKSpellPowerRatio;
    public static ModConfigSpec.DoubleValue gaiaBlessingExtraDropRateYukiOnna;

    public static ModConfigSpec.BooleanValue summonMummyAllowLooting;
    public static ModConfigSpec.DoubleValue summonedMummyBaseHealth;
    public static ModConfigSpec.DoubleValue summonedMummyBonusHealthSpellPowerRatio;
    public static ModConfigSpec.DoubleValue summonedMummyBaseAttackDamage;
    public static ModConfigSpec.DoubleValue summonedMummyBonusATKSpellPowerRatio;
    public static ModConfigSpec.DoubleValue gaiaBlessingExtraDropRateMummy;

    public static ModConfigSpec.BooleanValue summonValkyrieAllowLooting;
    public static ModConfigSpec.DoubleValue summonedValkyrieBaseHealth;
    public static ModConfigSpec.DoubleValue summonedValkyrieBonusHealthSpellPowerRatio;
    public static ModConfigSpec.DoubleValue summonedValkyrieBaseAttackDamage;
    public static ModConfigSpec.DoubleValue summonedValkyrieBonusATKSpellPowerRatio;
    public static ModConfigSpec.DoubleValue gaiaBlessingExtraDropRateValkyrie;

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

        BUILDER.pop();
        
        BUILDER.push("1");

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
        
        BUILDER.push("MISC");

        //build.define







        SPEC = BUILDER.build();
    }



}