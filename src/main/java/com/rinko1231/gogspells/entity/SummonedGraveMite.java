package com.rinko1231.gogspells.entity;

import com.rinko1231.gogspells.init.EntityRegistry;
import com.rinko1231.gogspells.init.NewSpellRegistry;
import gaia.config.GaiaConfig;
import gaia.registry.GaiaRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SummonedGraveMite extends PathfinderMob implements IMagicSummon, OwnableEntity {
    private static final int MAX_LIFE = 1200;
    private int life;
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    public SummonedGraveMite(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
    }
    @Override
    public boolean canAttackType(EntityType<?> type) {
        return true;
    }
    public LivingEntity getOwner() {
        return cachedSummoner;
    }
    public UUID getOwnerUUID()
    {
        return summonerUUID;
    }
    public SummonedGraveMite(Level level, LivingEntity owner) {
        this((EntityType<? extends PathfinderMob>) EntityRegistry.SUMMONED_GRAVEMITE.get(), level);
        this.xpReward = 0;
        this.setSummoner(owner);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        //this.goalSelector.addGoal(6, new GenericFollowOwnerGoal(this, this::getSummoner, 1.0D, 15.0f, 5.0f, false, 25.0f));
        this.goalSelector.addGoal(7, new MeleeAttackGoal(this, (double)1.25F, true));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
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
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.13F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, (double)1.0F).add(Attributes.MOVEMENT_SPEED, (double)0.25F).add(Attributes.ATTACK_DAMAGE, (double)1.0F);
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

    public void onRemovedFromLevel() {
        this.onRemovedHelper(this);
        super.onRemovedFromLevel();
    }
    public boolean hurt(DamageSource source, float damage) {
        return !this.shouldIgnoreDamage(source) && super.hurt(source, damage);
    }
    @Override
    public boolean doHurtTarget(Entity entityIn) {
        return Utils.doMeleeAttack(this, entityIn,
                ((AbstractSpell) NewSpellRegistry.SUMMON_MUMMY.get())
                        .getDamageSource(this, this));
    }
    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }

    protected SoundEvent getAmbientSound() {
        return GaiaRegistry.GRAVEMITE.getSay();
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return GaiaRegistry.GRAVEMITE.getHurt();
    }

    protected SoundEvent getDeathSound() {
        return GaiaRegistry.GRAVEMITE.getDeath();
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(GaiaRegistry.GRAVEMITE.getStep(), 0.15F, 1.0F);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.life = tag.getInt("Lifetime");
        this.summonerUUID = OwnerHelper.deserializeOwner(tag);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Lifetime", this.life);
        OwnerHelper.serializeOwner(tag, this.summonerUUID);
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isPassenger()) {
            this.stopRiding();
        }

        if (!this.level().isClientSide) {
            if (!this.isPersistenceRequired()) {
                ++this.life;
            }

            if (this.life >= MAX_LIFE) {
                this.discard();
            }
        }

    }

    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return effectInstance.getEffect() != MobEffects.POISON && super.canBeAffected(effectInstance);
    }

    public static boolean checkMiteSpawnRules(EntityType<? extends PathfinderMob> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return ((Boolean) GaiaConfig.COMMON.disableYRestriction.get() || pos.getY() > levelAccessor.getSeaLevel()) && checkMobSpawnRules(entityType, levelAccessor, spawnType, pos, random);
    }
}
