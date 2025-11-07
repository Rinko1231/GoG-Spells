package com.rinko1231.gogspells.spell.evocation;

import com.rinko1231.gogspells.GoGSpells;
import com.rinko1231.gogspells.config.GoGSpellsConfig;
import com.rinko1231.gogspells.entity.SummonedWitch;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

import static com.rinko1231.gogspells.init.MobEffectRegistry.SUMMON_WITCH_TIMER;

@AutoSpellConfig
public class SummonWitchSpell extends AbstractSpell {
    private final ResourceLocation spellId = GoGSpells.id("summon_witch");
    private final DefaultConfig defaultConfig;

    public SummonWitchSpell() {
        this.defaultConfig = (new DefaultConfig())
                .setMinRarity(SpellRarity.EPIC)
                .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
                .setMaxLevel(5)
                .setAllowCrafting(false)
                .setCooldownSeconds((double) 150.0F).build();
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 5;
        this.castTime = 20;
        this.baseManaCost = 100;
    }

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                //Component.translatable("ui.irons_spellbooks.summon_count", new Object[]{Utils.stringTruncation(spellLevel , 1)}),
                Component.translatable("ui.irons_spellbooks.hp",
                        new Object[]{Utils.stringTruncation( (double) this.getSummonHealth(spellLevel, caster) , 1)}),
                Component.translatable("ui.irons_spellbooks.damage",
                        new Object[]{Utils.stringTruncation( (double) this.getSummonDamage(spellLevel, caster) , 1)})
        );
    }

    @Override
    public boolean allowLooting() {
        return GoGSpellsConfig.summonWitchAllowLooting.get();
    }

    public CastType getCastType() {
        return CastType.LONG;
    }

    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.EVOKER_PREPARE_SUMMON);
    }

    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.EVOKER_CAST_SPELL);
    }

    public float extraHealthBasedOnSpellPower(int spellLevel, LivingEntity caster) {
        return GoGSpellsConfig.summonedWitchBonusHealthSpellPowerRatio.get().floatValue() * this.getSpellPower(spellLevel, caster);
    }
    public float extraATKBasedOnSpellPower(int spellLevel, LivingEntity caster) {
        return GoGSpellsConfig.summonedWitchBonusATKSpellPowerRatio.get().floatValue() * this.getSpellPower(spellLevel, caster);
    }
    public float getSummonHealth(int spellLevel, LivingEntity caster)
    {
        return GoGSpellsConfig.summonedWitchBaseHealth.get().floatValue() + this.extraHealthBasedOnSpellPower(spellLevel, caster);
    }
    public float getSummonDamage(int spellLevel, LivingEntity caster)
    {
        return GoGSpellsConfig.summonedWitchBaseAttackDamage.get().floatValue() + this.extraATKBasedOnSpellPower(spellLevel, caster);
    }

    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

            int summonTime = 12000;

            SummonedWitch summonedWitch = new SummonedWitch(world, entity);

            summonedWitch.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue((double)this.getSummonDamage(spellLevel, entity));
            summonedWitch.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue((double)this.getSummonHealth(spellLevel, entity));
            summonedWitch.setHealth(summonedWitch.getMaxHealth());


            summonedWitch.moveTo(entity.getEyePosition().add(new Vec3(Utils.getRandomScaled((double) 2.0F), (double) 1.0F, Utils.getRandomScaled((double) 2.0F))));
            summonedWitch.finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(summonedWitch.getOnPos()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null);
            //SummonedWitch creature = (SummonedWitch) ((SpellSummonEvent) NeoForge.EVENT_BUS.post(new SpellSummonEvent(entity, summonedWitch, this.spellId, spellLevel))).getCreature();

        summonedWitch.addEffect(new MobEffectInstance(SUMMON_WITCH_TIMER.get(), summonTime, 0, false, false, false));
        world.addFreshEntity(summonedWitch);


        int effectAmplifier = spellLevel - 1;
        if (entity.hasEffect(SUMMON_WITCH_TIMER.get())) {
            effectAmplifier += entity.getEffect(SUMMON_WITCH_TIMER.get()).getAmplifier() + 1;
        }

        entity.addEffect(new MobEffectInstance(SUMMON_WITCH_TIMER.get(), summonTime, effectAmplifier, false, false, true));
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }


}
