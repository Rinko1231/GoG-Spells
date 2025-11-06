package com.rinko1231.gogspells.entity;

import com.rinko1231.gogspells.init.EntityRegistry;
import com.rinko1231.gogspells.init.NewSpellRegistry;
import gaia.entity.AbstractGaiaEntity;
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
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static io.redspace.ironsspellbooks.registries.MobEffectRegistry.BLIGHT;

public class SummonedMummy extends AbstractGaiaEntity implements IMagicSummon {
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    public SummonedMummy(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
    public SummonedMummy(Level level, LivingEntity owner) {
        this((EntityType<? extends Monster>) EntityRegistry.SUMMONED_MUMMY.get(), level);
        this.xpReward = 0;
        this.setSummoner(owner);
    }
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new GenericFollowOwnerGoal(this, this::getSummoner, 1.5D, 15.0f, 5.0f, false, 25.0f));

        this.goalSelector.addGoal(7, new MeleeAttackGoal(this, (double)1.25F, true));
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
                .add(AttributeRegistry.BLOOD_MAGIC_RESIST, 1.50f)
                .add(Attributes.MAX_HEALTH, (double)40.0F).add(Attributes.FOLLOW_RANGE, (double)20.0F).add(Attributes.MOVEMENT_SPEED, (double)0.25F).add(Attributes.ATTACK_DAMAGE, (double)4.0F).add(Attributes.ARMOR, (double)4.0F).add(Attributes.KNOCKBACK_RESISTANCE, (double)0.5F).add(Attributes.STEP_HEIGHT, (double)1.0F);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
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
    @Override
    public void die(@NotNull DamageSource source) {
        this.onDeathHelper();
        super.die(source);
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
    public boolean hurt(DamageSource source, float damage) {
        float input = this.getBaseDamage(source, damage);
        Entity entity = source.getDirectEntity();
        if (entity instanceof Mob && this.random.nextBoolean() && !this.level().isClientSide && !this.shouldIgnoreDamage(source)) {
            this.setSpawn(0);
        }

        return !this.shouldIgnoreDamage(source) && super.hurt(source, input);
    }

    public boolean doHurtTarget(Entity entityIn) {
        if (Utils.doMeleeAttack(this, entityIn,
                ((AbstractSpell) NewSpellRegistry.SUMMON_MUMMY.get())
                        .getDamageSource(this, this.getSummoner()))) {
            if (entityIn instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entityIn;
                int effectTime = 12;

                if (effectTime > 0) {
                    livingEntity.addEffect(new MobEffectInstance(BLIGHT, effectTime * 20, 0));
                }
            }

            float effectiveDifficulty = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < effectiveDifficulty * 0.3F) {
                entityIn.setRemainingFireTicks(40 * (int)effectiveDifficulty);
            }

            return true;
        } else {
            return false;
        }
    }

    public void aiStep() {
        super.aiStep();
    }

    private void setSpawn(int id) {
        if (this.level().getDifficulty() != Difficulty.PEACEFUL) {
            BlockPos blockpos = this.blockPosition().offset(-1 + this.random.nextInt(3), 1, -1 + this.random.nextInt(3));
            if (id == 0) {
                SummonedGraveMite mite = new SummonedGraveMite(this.level(), this);
                if (mite != null) {
                    mite.moveTo(blockpos, 0.0F, 0.0F);
                    EventHooks.finalizeMobSpawn(mite, (ServerLevel)this.level(), this.level().getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null);
                    this.level().addFreshEntity(mite);
                }
            }
        }

    }

    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data) {
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
        return GaiaRegistry.MUMMY.getSay();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return GaiaRegistry.MUMMY.getHurt();
    }

    protected SoundEvent getDeathSound() {
        return GaiaRegistry.MUMMY.getDeath();
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    public int getMaxSpawnClusterSize() {
        return 2;
    }

    public static boolean checkMummySpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkDaysPassed(levelAccessor) && checkAboveSeaLevel(levelAccessor, pos) && checkMonsterSpawnRules(entityType, levelAccessor, spawnType, pos, random);
    }
}
