package com.rinko1231.gogspells.entity;

import com.rinko1231.gogspells.GoGSpells;
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
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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

public class SummonedWerecat extends AbstractGaiaEntity implements IMagicSummon {
    private static final ResourceLocation KNOCKBACK_ID = GoGSpells.id( "werecat_knockback_modifier");
    private static final AttributeModifier KNOCKBACK_MODIFIER;
    private static final EntityDataAccessor<Boolean> FLEEING;

    static {
        KNOCKBACK_MODIFIER = new AttributeModifier(KNOCKBACK_ID, (double) 2.0F, AttributeModifier.Operation.ADD_VALUE);
        FLEEING = SynchedEntityData.defineId(SummonedWerecat.class, EntityDataSerializers.BOOLEAN);
    }

    private final LeapAtTargetGoal leapAtTargetGoal = new LeapAtTargetGoal(this, 0.4F);
    private final MobAttackGoal mobAttackGoal = new MobAttackGoal(this, (double) 1.25F, true);
    private final AvoidEntityGoal<Player> avoidPlayerGoal = new AvoidEntityGoal(this, Player.class, 20.0F, (double) 1.25F, 1.3);
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    private int switchHealth = 0;

    public SummonedWerecat(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public SummonedWerecat(Level level, LivingEntity owner) {
        this((EntityType<? extends Monster>) EntityRegistry.SUMMONED_WERECAT.get(), level);
        this.xpReward = 0;
        this.setSummoner(owner);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(AttributeRegistry.EVOCATION_MAGIC_RESIST, 1.25f)
                .add(Attributes.MAX_HEALTH, (double) 40.0F)
                .add(Attributes.FOLLOW_RANGE, (double) 40.0F).add(Attributes.MOVEMENT_SPEED, (double) 0.25F).add(Attributes.ATTACK_DAMAGE, (double) 4.0F).add(Attributes.ARMOR, (double) 4.0F).add(Attributes.KNOCKBACK_RESISTANCE, 0.3).add(Attributes.STEP_HEIGHT, (double) 1.0F);
    }


    public static boolean checkWerecatSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkDaysPassed(levelAccessor) && checkAboveSeaLevel(levelAccessor, pos) && checkMonsterSpawnRules(entityType, levelAccessor, spawnType, pos, random);
    }

    protected boolean shouldDropLoot() {
        return false;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new GenericFollowOwnerGoal(this, this::getSummoner, 1.5D, 15.0f, 5.0f, false, 25.0f));
        // 7 8 Attack
        this.goalSelector.addGoal(9, new RandomStrollGoal(this, (double) 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == this.getSummoner())).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLEEING, false);
    }
    @Override
    public boolean canAttackType(EntityType<?> type) {
        return true;
    }
    public boolean isFleeing() {
        return (Boolean) this.entityData.get(FLEEING);
    }

    public void setFleeing(boolean flag) {
        this.entityData.set(FLEEING, flag);
    }

    public int maxVariants() {
        return 1;
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
    @Override
    public boolean isPreventingPlayerRest(@NotNull Player player) {
        return !this.isAlliedTo(player);
    }

    public void onRemovedFromLevel() {
        this.onRemovedHelper(this);
        super.onRemovedFromLevel();
    }

    @Override
    public void die(@NotNull DamageSource source) {
        this.onDeathHelper();
        super.die(source);
    }

    public boolean hurt(DamageSource source, float damage) {
        float input = this.getBaseDamage(source, damage);
        return !this.shouldIgnoreDamage(source) && super.hurt(source, input);
    }

    public boolean doHurtTarget(Entity entityIn) {
        if ( Utils.doMeleeAttack(this, entityIn,
                ((AbstractSpell) NewSpellRegistry.SUMMON_WERECAT.get())
                        .getDamageSource(this, this.getSummoner()))) {
            if (entityIn instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entityIn;
                int effectTime = 12;

                if (effectTime > 0) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, effectTime * 20, 1));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public void aiStep() {
        if (this.getHealth() < this.getMaxHealth() * 0.20F && this.switchHealth == 0) {
            switch (this.random.nextInt(2)) {
                case 0:
                    this.setGoals(1);
                    this.setFleeing(true);
                    this.switchHealth = 1;
                    break;
                case 1:
                    this.switchHealth = 2;
            }
        }

        if (this.getHealth() > this.getMaxHealth() * 0.25F && this.switchHealth == 1) {
            this.setGoals(0);
            this.setFleeing(false);
            this.switchHealth = 0;
        }

        super.aiStep();
    }

    private void setGoals(int id) {
        if (id == 1) {
            //this.goalSelector.addGoal(1, this.avoidPlayerGoal);
            this.goalSelector.removeGoal(this.leapAtTargetGoal);
            this.goalSelector.removeGoal(this.mobAttackGoal);
        } else {
            this.goalSelector.addGoal(7, this.leapAtTargetGoal);
            this.goalSelector.addGoal(8, this.mobAttackGoal);
            //this.goalSelector.removeGoal(this.avoidPlayerGoal);
        }

    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data) {
        if (this.random.nextInt(4) == 0) {
            this.setVariant(1);
        }

        AttributeInstance attributeinstance = this.getAttribute(Attributes.ATTACK_KNOCKBACK);
        if (attributeinstance != null) {
            attributeinstance.removeModifier(KNOCKBACK_ID);
        }
        if (attributeinstance != null) {
            attributeinstance.addTransientModifier(KNOCKBACK_MODIFIER);
        }
        this.setGoals(0);
        return data;
    }


    public void addAdditionalSaveData(CompoundTag tag) {

        super.addAdditionalSaveData(tag);
        OwnerHelper.serializeOwner(tag, this.summonerUUID);
    }

    public void readAdditionalSaveData(CompoundTag tag) {

        super.readAdditionalSaveData(tag);
        this.summonerUUID = OwnerHelper.deserializeOwner(tag);
    }

    protected SoundEvent getAmbientSound() {
        return GaiaRegistry.WERECAT.getSay();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return GaiaRegistry.WERECAT.getHurt();
    }

    protected SoundEvent getDeathSound() {
        return GaiaRegistry.WERECAT.getDeath();
    }

    protected int getFireImmuneTicks() {
        return 10;
    }

    public int getMaxSpawnClusterSize() {
        return 2;
    }
}
