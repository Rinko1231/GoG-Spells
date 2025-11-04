package com.rinko1231.gogspells.spell.blood;

import com.rinko1231.gogspells.GoGSpells;
import com.rinko1231.gogspells.config.GoGSpellsConfig;
import com.rinko1231.gogspells.entity.SummonedMummy;
import com.rinko1231.gogspells.utils.MyUtils;
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
public class SummonMummySpell extends AbstractSpell {
    private final ResourceLocation spellId = GoGSpells.id("summon_mummy");
    private final DefaultConfig defaultConfig;

    public SummonMummySpell() {
        this.defaultConfig = (new DefaultConfig())
                .setMinRarity(SpellRarity.UNCOMMON)
                .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
                .setMaxLevel(5)
                .setAllowCrafting(false)
                .setCooldownSeconds((double) 120.0F).build();
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 5;
        this.castTime = 20;
        this.baseManaCost = 50;
    }

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.summon_count",
                        new Object[]{Utils.stringTruncation(this.getSummonCount(spellLevel) , 1)}),
                Component.translatable("ui.irons_spellbooks.hp",
                        new Object[]{Utils.stringTruncation( (double) this.getSummonHealth(spellLevel, caster) , 1)}),
                Component.translatable("ui.irons_spellbooks.damage",
                        new Object[]{Utils.stringTruncation( (double) this.getSummonDamage(spellLevel, caster) , 1)})
        );
    }

    @Override
    public boolean allowLooting() {
        return GoGSpellsConfig.summonMummyAllowLooting.get();
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
        return GoGSpellsConfig.summonedMummyBonusHealthSpellPowerRatio.get().floatValue() * this.getSpellPower(spellLevel, caster);
    }
    public float extraATKBasedOnSpellPower(int spellLevel, LivingEntity caster) {
        return GoGSpellsConfig.summonedMummyBonusATKSpellPowerRatio.get().floatValue() * this.getSpellPower(spellLevel, caster);
    }
    public float getSummonHealth(int spellLevel, LivingEntity caster)
    {
        return GoGSpellsConfig.summonedMummyBaseHealth.get().floatValue() + this.extraHealthBasedOnSpellPower(spellLevel, caster);
    }
    public float getSummonDamage(int spellLevel, LivingEntity caster)
    {
        return GoGSpellsConfig.summonedMummyBaseAttackDamage.get().floatValue() + this.extraATKBasedOnSpellPower(spellLevel, caster);
    }
    public int getSummonCount(int spellLevel)
    {
        int summonCount = MyUtils.maxCap(5,(int)(spellLevel/2.5));
        return MyUtils.minCap(1,summonCount);
    }

    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        PlayerRecasts recasts = playerMagicData.getPlayerRecasts();
        if (!recasts.hasRecastForSpell(this)) {
            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();
            int summonTime = 12000;
            for (int i = 0; i < this.getSummonCount(spellLevel); ++i) {
                SummonedMummy summonedMummy = new SummonedMummy(world, entity);

                summonedMummy.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue((double)this.getSummonDamage(spellLevel, entity));
                summonedMummy.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue((double)this.getSummonHealth(spellLevel, entity));
                summonedMummy.setHealth(summonedMummy.getMaxHealth());

                summonedMummy.moveTo(entity.getEyePosition().add(new Vec3(Utils.getRandomScaled((double) 2.0F), (double) 1.0F, Utils.getRandomScaled((double) 2.0F))));
                summonedMummy.finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(summonedMummy.getOnPos()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null);
                SummonedMummy creature = (SummonedMummy) ((SpellSummonEvent) NeoForge.EVENT_BUS.post(new SpellSummonEvent(entity, summonedMummy, this.spellId, spellLevel))).getCreature();

                world.addFreshEntity(creature);
                SummonManager.initSummon(entity, creature, summonTime, summonedEntitiesCastData);
            }

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
