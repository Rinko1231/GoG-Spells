package com.rinko1231.gogspells.compat.traveloptics.spell;

import com.rinko1231.gogspells.GoGSpells;
import com.rinko1231.gogspells.compat.traveloptics.entity.SummonedSiren;
import com.rinko1231.gogspells.config.GoGSpellsConfig;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
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

import static com.gametechbc.traveloptics.api.init.TravelopticsSchools.AQUA_RESOURCE;
import static com.rinko1231.gogspells.compat.traveloptics.entity.SummonedSiren.SirenClass.ARCHER;

import static com.rinko1231.gogspells.init.MobEffectRegistry.SUMMON_SIREN_TIMER;

@AutoSpellConfig
public class SummonSirenSpell extends AbstractSpell {
        private final ResourceLocation spellId = GoGSpells.id("summon_siren");
        private final DefaultConfig defaultConfig;

        public SummonSirenSpell() {
            this.defaultConfig = (new DefaultConfig())
                    .setMinRarity(SpellRarity.UNCOMMON)
                    .setSchoolResource(AQUA_RESOURCE)
                    .setMaxLevel(5)
                    .setAllowCrafting(false)
                    .setCooldownSeconds((double) 160.0F).build();
            this.manaCostPerLevel = 10;
            this.baseSpellPower = 10;
            this.spellPowerPerLevel = 5;
            this.castTime = 20;
            this.baseManaCost = 100;
        }

        public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
            return List.of(
                    Component.translatable("ui.irons_spellbooks.summon_count", new Object[]{Utils.stringTruncation(this.getSummonCount(spellLevel), 1)}),
                    Component.translatable("ui.irons_spellbooks.hp",
                            new Object[]{Utils.stringTruncation((double) this.getSummonHealth(spellLevel, caster), 1)}),
                    Component.translatable("ui.irons_spellbooks.damage",
                            new Object[]{Utils.stringTruncation((double) this.getSummonDamage(spellLevel, caster), 1)})
            );
        }

        @Override
        public boolean allowLooting() {
            return GoGSpellsConfig.summonSirenAllowLooting.get();
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
            return GoGSpellsConfig.summonedSirenBonusHealthSpellPowerRatio.get().floatValue() * this.getSpellPower(spellLevel, caster);
        }

        public float extraATKBasedOnSpellPower(int spellLevel, LivingEntity caster) {
            return GoGSpellsConfig.summonedSirenBonusATKSpellPowerRatio.get().floatValue() * this.getSpellPower(spellLevel, caster);
        }

        public float getSummonHealth(int spellLevel, LivingEntity caster) {
            return GoGSpellsConfig.summonedSirenBaseHealth.get().floatValue() + this.extraHealthBasedOnSpellPower(spellLevel, caster);
        }

        public float getSummonDamage(int spellLevel, LivingEntity caster) {
            return GoGSpellsConfig.summonedSirenBaseAttackDamage.get().floatValue() + this.extraATKBasedOnSpellPower(spellLevel, caster);
        }

        public int getSummonCount(int spellLevel) {
            return 3;
        }

        public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

            int summonTime = 12000;
            {
                SummonedSiren summonedSirenSaber = new SummonedSiren(world, entity, SummonedSiren.SirenClass.SABER);

                summonedSirenSaber.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue((double) this.getSummonDamage(spellLevel, entity));
                summonedSirenSaber.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue((double) this.getSummonHealth(spellLevel, entity)*1.3f);
                summonedSirenSaber.setHealth(summonedSirenSaber.getMaxHealth());

                summonedSirenSaber.moveTo(entity.getEyePosition().add(new Vec3(Utils.getRandomScaled((double) 2.0F), (double) 1.0F, Utils.getRandomScaled((double) 2.0F))));
                summonedSirenSaber.finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(summonedSirenSaber.getOnPos()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null);
                //SummonedSiren creature = (SummonedSiren) ((SpellSummonEvent) NeoForge.EVENT_BUS.post(new SpellSummonEvent(entity, summonedSiren, this.spellId, spellLevel))).getCreature();

                summonedSirenSaber.addEffect(new MobEffectInstance(SUMMON_SIREN_TIMER.get(), summonTime, 0, false, false, false));
                world.addFreshEntity(summonedSirenSaber);
            }
            {
                SummonedSiren summonedSirenArcher = new SummonedSiren(world, entity, ARCHER);

                summonedSirenArcher.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue((double) this.getSummonDamage(spellLevel, entity));
                summonedSirenArcher.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue((double) this.getSummonHealth(spellLevel, entity));
                summonedSirenArcher.setHealth(summonedSirenArcher.getMaxHealth());

                summonedSirenArcher.moveTo(entity.getEyePosition().add(new Vec3(Utils.getRandomScaled((double) 2.0F), (double) 1.0F, Utils.getRandomScaled((double) 2.0F))));
                summonedSirenArcher.finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(summonedSirenArcher.getOnPos()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null);
                //SummonedSiren creature = (SummonedSiren) ((SpellSummonEvent) NeoForge.EVENT_BUS.post(new SpellSummonEvent(entity, summonedSiren, this.spellId, spellLevel))).getCreature();

                summonedSirenArcher.addEffect(new MobEffectInstance(SUMMON_SIREN_TIMER.get(), summonTime, 0, false, false, false));
                world.addFreshEntity(summonedSirenArcher);

            }
            {
                SummonedSiren summonedSirenCaster = new SummonedSiren(world, entity, SummonedSiren.SirenClass.CASTER);

                summonedSirenCaster.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue((double) this.getSummonDamage(spellLevel, entity));
                summonedSirenCaster.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue((double) this.getSummonHealth(spellLevel, entity));
                summonedSirenCaster.setHealth(summonedSirenCaster.getMaxHealth());

                summonedSirenCaster.moveTo(entity.getEyePosition().add(new Vec3(Utils.getRandomScaled((double) 2.0F), (double) 1.0F, Utils.getRandomScaled((double) 2.0F))));
                summonedSirenCaster.finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(summonedSirenCaster.getOnPos()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null);
                //SummonedSiren creature = (SummonedSiren) ((SpellSummonEvent) NeoForge.EVENT_BUS.post(new SpellSummonEvent(entity, summonedSiren, this.spellId, spellLevel))).getCreature();

                summonedSirenCaster.addEffect(new MobEffectInstance(SUMMON_SIREN_TIMER.get(), summonTime, 0, false, false, false));
                world.addFreshEntity(summonedSirenCaster);

            }


            int effectAmplifier = spellLevel - 1;
            if (entity.hasEffect(SUMMON_SIREN_TIMER.get())) {
                effectAmplifier += entity.getEffect(SUMMON_SIREN_TIMER.get()).getAmplifier() + 1;
            }

            entity.addEffect(new MobEffectInstance(SUMMON_SIREN_TIMER.get(), summonTime, effectAmplifier, false, false, true));
            super.onCast(world, spellLevel, entity, castSource, playerMagicData);
        }


    }
