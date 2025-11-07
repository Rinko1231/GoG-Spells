package com.rinko1231.gogspells.entity;

import com.rinko1231.gogspells.entity.ai.GenericProtectOwnerTargetGoal;
import com.rinko1231.gogspells.init.EntityRegistry;
import com.rinko1231.gogspells.init.NewSpellRegistry;
import gaia.entity.AbstractGaiaEntity;
import gaia.entity.goal.MobAttackGoal;
import gaia.registry.GaiaRegistry;
import gaia.util.SharedEntityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;

import io.redspace.ironsspellbooks.effect.SummonTimer;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static com.rinko1231.gogspells.init.MobEffectRegistry.SUMMON_ARACHNE_TIMER;
import static net.minecraft.world.effect.MobEffects.REGENERATION;

public class SummonedSludgeGirl extends AbstractGaiaEntity implements MagicSummon {
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    @Override
    public boolean canAttackType(EntityType<?> type) {
        return true;
    }
    public SummonedSludgeGirl(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
    public SummonedSludgeGirl(Level level, LivingEntity owner) {
        this((EntityType<? extends Monster>) EntityRegistry.SUMMONED_SLUDGE_GIRL.get(), level);
        this.xpReward = 0;
        this.setSummoner(owner);
    }
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new GenericFollowOwnerGoal(this, this::getSummoner, 1.5D, 15.0f, 5.0f, false, 25.0f));

        this.goalSelector.addGoal(7, new MobAttackGoal(this, (double)1.25F, true));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == this.getSummoner())).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));
    }
    protected boolean shouldDropLoot() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, (double)40.0F)
                .add(Attributes.FOLLOW_RANGE, (double)40.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.25F)
                .add(Attributes.ATTACK_DAMAGE, (double)4.0F)
                .add(Attributes.ARMOR, (double)4.0F)
                .add(AttributeRegistry.NATURE_MAGIC_RESIST.get(), 1.25f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3)
                .add((Attribute) ForgeMod.STEP_HEIGHT_ADDITION.get(), (double)1.0F);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public int maxVariants() {
        return 2;
    }

    public float getBaseDefense() {
        return SharedEntityData.getBaseDefense1();
    }

    public void onUnSummon() {
        if (!this.level().isClientSide) {
            MagicManager.spawnParticles(this.level(), ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 25, 0.4, 0.8, 0.4, 0.03, false);
            this.setRemoved(RemovalReason.DISCARDED);
        }

    }

    public void remove(Entity.RemovalReason pReason) {
        super.remove(pReason);
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity other) {
        return super.isAlliedTo(other) || this.isAlliedHelper(other);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Nullable
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(this.level(), this.cachedSummoner, this.summonerUUID);
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }
    public void onRemovedFromWorld() {
        this.onRemovedHelper(this, (SummonTimer)SUMMON_ARACHNE_TIMER.get());
        super.onRemovedFromWorld();
    }
    @Override
    public boolean isPreventingPlayerRest(@NotNull Player player) {
        return !this.isAlliedTo(player);
    }


    public boolean hurt(DamageSource source, float damage) {
        float input = this.getBaseDamage(source, damage);
        return !this.shouldIgnoreDamage(source) && super.hurt(source, input);
    }


    public boolean doHurtTarget(Entity entityIn) {
        if (Utils.doMeleeAttack(this, entityIn,
                ((AbstractSpell) NewSpellRegistry.SUMMON_SLUDGE_GIRL.get())
                        .getDamageSource(this, this.getSummoner()))) {
            if (entityIn instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entityIn;
                int effectTime = 12;

                if (effectTime > 0) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, effectTime * 20, 1));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, effectTime * 20, 1));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public void die(DamageSource source) {
        this.onDeathHelper();
        NewSpawnLingeringCloud(List.of(new MobEffectInstance(MobEffects.POISON, 200, 0)));
        super.die(source);
    }

    protected void NewSpawnLingeringCloud(List<MobEffectInstance> effectInstances) {
        if (!effectInstances.isEmpty()) {
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            areaeffectcloud.setRadius(2.5F);
            areaeffectcloud.setOwner(this.getSummoner());
            areaeffectcloud.setRadiusOnUse(-0.5F);
            areaeffectcloud.setWaitTime(10);
            areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());
            for(MobEffectInstance mobeffectinstance : effectInstances) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }
            this.level().addFreshEntity(areaeffectcloud);
        }

    }

    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data) {

        if (this.random.nextInt(8) == 0) {
            switch (this.random.nextInt(2)) {
                case 0:
                    //if (ModList.get().isLoaded("thaumcraft")) {
                        this.setVariant(1);
                    //}
                case 1:
                    //if (ModList.get().isLoaded("tconstruct")) {
                        this.setVariant(2);
                    //}
            }
        }

        return data;
    }

    protected int getFireImmuneTicks() {
        return 10;
    }

    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return effectInstance.getEffect() != MobEffects.POISON && super.canBeAffected(effectInstance);
    }

    public void addAdditionalSaveData(CompoundTag tag) {

        super.addAdditionalSaveData(tag);
        OwnerHelper.serializeOwner(tag, this.summonerUUID);
    }

    public void readAdditionalSaveData(CompoundTag tag) {

        super.readAdditionalSaveData(tag);
        this.summonerUUID = OwnerHelper.deserializeOwner(tag);
    }
    @Override
    protected void customServerAiStep() {
        if (this.isInWaterOrRain()) {
            if(this.getHealth() <= this.getMaxHealth() * 0.33f)
                this.addEffect(new MobEffectInstance(REGENERATION,40, 0, false, false));
        }

        super.customServerAiStep();
    }

    protected SoundEvent getAmbientSound() {
        return GaiaRegistry.SLUDGE_GIRL.getSay();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return GaiaRegistry.SLUDGE_GIRL.getHurt();
    }

    protected SoundEvent getDeathSound() {
        return GaiaRegistry.SLUDGE_GIRL.getDeath();
    }

    public int getMaxSpawnClusterSize() {
        return 2;
    }

    public static boolean checkSludgeGirlSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkDaysPassed(levelAccessor) && checkAboveSeaLevel(levelAccessor, pos) && checkMonsterSpawnRules(entityType, levelAccessor, spawnType, pos, random);
    }
}
