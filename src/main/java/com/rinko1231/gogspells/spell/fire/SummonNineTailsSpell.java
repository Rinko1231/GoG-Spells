package com.rinko1231.gogspells.spell.fire;

import com.rinko1231.gogspells.GoGSpells;
import com.rinko1231.gogspells.config.GoGSpellsConfig;
import com.rinko1231.gogspells.entity.SummonedNineTails;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;


@AutoSpellConfig
public class SummonNineTailsSpell extends AbstractSpell {
    private final ResourceLocation spellId = GoGSpells.id("summon_nine_tails");
    private final DefaultConfig defaultConfig;

    public SummonNineTailsSpell() {
        this.defaultConfig = (new DefaultConfig())
                .setMinRarity(SpellRarity.EPIC)
                .setSchoolResource(SchoolRegistry.FIRE_RESOURCE)
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
        return GoGSpellsConfig.summonNineTailsAllowLooting.get();
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
        return GoGSpellsConfig.summonedNineTailsBonusHealthSpellPowerRatio.get().floatValue() * this.getSpellPower(spellLevel, caster);
    }

    public float extraATKBasedOnSpellPower(int spellLevel, LivingEntity caster) {
        return GoGSpellsConfig.summonedNineTailsBonusATKSpellPowerRatio.get().floatValue() * this.getSpellPower(spellLevel, caster);
    }

    public float getSummonHealth(int spellLevel, LivingEntity caster)
    {
        return GoGSpellsConfig.summonedNineTailsBaseHealth.get().floatValue() + this.extraHealthBasedOnSpellPower(spellLevel, caster);
    }
    public float getSummonDamage(int spellLevel, LivingEntity caster)
    {
        return GoGSpellsConfig.summonedNineTailsBaseAttackDamage.get().floatValue() + this.extraATKBasedOnSpellPower(spellLevel, caster);
    }

    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        PlayerRecasts recasts = playerMagicData.getPlayerRecasts();
        if (!recasts.hasRecastForSpell(this)) {
            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();
            int summonTime = 12000;

            SummonedNineTails summonedNineTails = new SummonedNineTails(world, entity);

            summonedNineTails.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue((double)this.getSummonDamage(spellLevel, entity));
            summonedNineTails.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue((double)this.getSummonHealth(spellLevel, entity));
            summonedNineTails.setHealth(summonedNineTails.getMaxHealth());

            summonedNineTails.moveTo(entity.getEyePosition().add(new Vec3(Utils.getRandomScaled((double) 2.0F), (double) 1.0F, Utils.getRandomScaled((double) 2.0F))));
            summonedNineTails.finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(summonedNineTails.getOnPos()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null);
            SummonedNineTails creature = (SummonedNineTails) ((SpellSummonEvent) NeoForge.EVENT_BUS.post(new SpellSummonEvent(entity, summonedNineTails, this.spellId, spellLevel))).getCreature();

            world.addFreshEntity(creature);
            SummonManager.initSummon(entity, creature, summonTime, summonedEntitiesCastData);


            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, this.getRecastCount(spellLevel, entity), summonTime, castSource, summonedEntitiesCastData);
            recasts.addRecast(recastInstance, playerMagicData);
        }

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public ICastDataSerializable getEmptyCastData() {
        return new SummonedEntitiesCastData();
    }

    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (SummonManager.recastFinishedHelper(serverPlayer, recastInstance, recastResult, castDataSerializable)) {
            super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
        }
    }

    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }
}
