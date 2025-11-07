package com.rinko1231.gogspells.entity;

import com.rinko1231.gogspells.entity.ai.HolyMeleeAttackGoal;
import com.rinko1231.gogspells.init.EntityRegistry;
import com.rinko1231.gogspells.init.NewSpellRegistry;
import com.rinko1231.gogspells.init.TagsRegistry;
import gaia.entity.AbstractAssistGaiaEntity;
import gaia.registry.GaiaRegistry;
import gaia.registry.GaiaTags;
import gaia.util.EnchantUtil;
import gaia.util.SharedEntityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class SummonedValkyrie extends AbstractAssistGaiaEntity implements PowerableMob, IMagicSummon {
    private static final EntityDataAccessor<Boolean> IS_BUFFED;
    private static final EntityDataAccessor<Boolean> ANNOYED;
    private static final EntityDataAccessor<Integer> ANIMATION_STATE;
    private final HolyMeleeAttackGoal meleeAttackGoal = new HolyMeleeAttackGoal(this, 1.3, true, 0.20d);
    private int aggression;
    private int aggressive;
    private int buffEffect;
    private boolean animationPlay;
    private int animationTimer;
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    @Override
    public boolean canAttackType(EntityType<?> type) {
        return true;
    }
    public SummonedValkyrie(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
        this.aggression = 0;
        this.aggressive = 0;
        this.buffEffect = 0;
        this.animationPlay = false;
        this.animationTimer = 0;
    }
    public SummonedValkyrie(Level level, LivingEntity owner) {
        this((EntityType<? extends Monster>) EntityRegistry.SUMMONED_VALKYRIE.get(), level);
        this.xpReward = 0;
        this.aggression = 60;
        this.aggressive = 4;
        this.setAnnoyed(true);
        this.buffEffect = 0;
        this.animationPlay = false;
        this.animationTimer = 0;
        this.setSummoner(owner);
    }
    protected boolean shouldDropLoot() {
        return false;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(6, new GenericFollowOwnerGoal(this, this::getSummoner, 1.6D, 15.0f, 5.0f, true, 25.0f));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == this.getSummoner())).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));

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

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(AttributeRegistry.HOLY_MAGIC_RESIST, 1.25f)
                .add(AttributeRegistry.LIGHTNING_MAGIC_RESIST, 1.25f)
                .add(Attributes.MAX_HEALTH, (double)160.0F).add(Attributes.FOLLOW_RANGE, (double)40.0F).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, (double)12.0F).add(Attributes.ARMOR, (double)12.0F).add(Attributes.KNOCKBACK_RESISTANCE, 0.2).add(Attributes.STEP_HEIGHT, (double)1.0F);
    }

    public int getGaiaLevel() {
        return 3;
    }

    @Override
    public void die(@NotNull DamageSource source) {
        this.onDeathHelper();
        super.die(source);
    }
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_BUFFED, false);
        builder.define(ANNOYED, false);
        builder.define(ANIMATION_STATE, 0);
    }

    public boolean isBuffed() {
        return (Boolean)this.entityData.get(IS_BUFFED);
    }

    public void setBuffed(boolean isBuffed) {
        this.entityData.set(IS_BUFFED, isBuffed);
    }

    public boolean isAnnoyed() {
        return (Boolean)this.entityData.get(ANNOYED);
    }

    public void setAnnoyed(boolean value) {
        this.entityData.set(ANNOYED, value);
    }

    public int getAnimationState() {
        return (Integer)this.entityData.get(ANIMATION_STATE);
    }

    public void setAnimationState(int state) {
        this.entityData.set(ANIMATION_STATE, state);
    }

    public float getBaseDefense() {
        return SharedEntityData.getBaseDefense3();
    }

    public boolean hurt(DamageSource source, float damage) {
        float input = this.getBaseDamage(source, damage);
        if (!this.isPowered()) {
            return !this.shouldIgnoreDamage(source) && super.hurt(source, input);
        } else {
            return !this.shouldIgnoreDamage(source) && source.isDirect() && super.hurt(source, input);
        }
    }


    public boolean isPowered() {
        return this.getHealth() < this.getMaxHealth() / 2.0F;
    }

    public boolean doHurtTarget(Entity entityIn) {
        if (Utils.doMeleeAttack(this, entityIn,
                ((AbstractSpell) NewSpellRegistry.SUMMON_VALKYRIE.get())
                        .getDamageSource(this, this.getSummoner()))) {
            if (entityIn instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entityIn;
                int effectTime = 5;
                int effectTime2 = 4;

                if (effectTime > 0) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.GUIDING_BOLT, 20));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, effectTime * 20, 0));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, effectTime2 * 20, 0));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public void aiStep() {
        Vec3 motion = this.getDeltaMovement();
        if (!this.onGround() && motion.y < (double)0.0F) {
            this.setDeltaMovement(motion.multiply((double)1.0F, 0.6, (double)1.0F));
        }

        if (!this.level().isClientSide && this.isPassenger()) {
            this.stopRiding();
        }

        if (!this.isAnnoyed()) {
                this.setAnnoyed(true);
                this.setGoals(0);
                this.setGoals(1);
                this.giveShield();

        }

        if (this.getHealth() < this.getMaxHealth() && !this.isAnnoyed()) {
            this.setAnnoyed(true);
            this.setGoals(0);
            this.setGoals(1);
            this.giveShield();
        }

        if (this.getHealth() <= this.getMaxHealth() * 0.25F && this.getHealth() > 0.0F && this.buffEffect == 0) {
            this.setGoals(1);
            this.setAnimationState(1);
            this.setBuffed(true);
            this.buffEffect = 1;
            this.animationPlay = true;
        }

        if (this.getHealth() > this.getMaxHealth() * 0.25F && this.buffEffect == 1) {
            this.buffEffect = 0;
            this.animationPlay = false;
            this.animationTimer = 0;
        }

        if (this.animationPlay) {
            if (this.animationTimer != 15) {
                ++this.animationTimer;
            } else {
                this.setBuff();
                this.setBuffed(false);
                this.setGoals(1);
                this.setAnimationState(0);
                this.animationPlay = false;
            }
        }

        if (this.isDeadOrDying()) {
            //do nothing
        } else {

            super.aiStep();
        }

    }




    private void setGoals(int id) {
        if (!this.level().isClientSide) {
            if (id == 2) {
                this.goalSelector.removeGoal(this.meleeAttackGoal);
            } else {
                this.goalSelector.addGoal(7, this.meleeAttackGoal);
            }
        }
    }

    private void giveShield() {
        ItemStack shield = new ItemStack((ItemLike) GaiaRegistry.IRON_SHIELD.get());
        this.setItemSlot(EquipmentSlot.OFFHAND, shield);
    }

    private void setBuff() {
        this.level().broadcastEntityEvent(this, (byte)7);
        this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 0));
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 0));
    }

    private void setCombatTask() {
        this.goalSelector.removeGoal(this.meleeAttackGoal);
        this.targetSelector.removeGoal(this.targetPlayerGoal);
        if (this.isAnnoyed()) {
            this.setGoals(0);
            this.setGoals(1);
        }

    }

    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }

    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data) {
        this.setGoals(1);
        if (this.random.nextInt(4) == 0) {
            this.setVariant(1);
        }

        if (this.random.nextInt(10) == 0) {
            this.setBaby(true);
        }

        this.populateDefaultEquipmentSlots(this.random, difficultyInstance);
        this.populateDefaultEquipmentEnchantments(levelAccessor, this.random, difficultyInstance);
        ItemStack swimmingBoots = new ItemStack(Items.LEATHER_BOOTS);
        this.setItemSlot(EquipmentSlot.FEET, swimmingBoots);
        swimmingBoots.enchant(EnchantUtil.getEnchantmentHolder(this, Enchantments.DEPTH_STRIDER), 3);
        this.setCombatTask();
        return data;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("annoyed", this.isAnnoyed());
        OwnerHelper.serializeOwner(tag, this.summonerUUID);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("annoyed")) {
            this.setAnnoyed(tag.getBoolean("annoyed"));
        }
        this.summonerUUID = OwnerHelper.deserializeOwner(tag);
        this.setCombatTask();
    }

    protected SoundEvent getAmbientSound() {
        return GaiaRegistry.VALKYRIE.getSay();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return GaiaRegistry.VALKYRIE.getHurt();
    }

    protected SoundEvent getDeathSound() {
        return GaiaRegistry.VALKYRIE.getDeath();
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }

    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    protected void checkFallDamage(double p_27754_, boolean p_27755_, BlockState state, BlockPos pos) {
    }

    public boolean fireImmune() {
        return true;
    }

    public static boolean checkValkyrieSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkDaysPassed(levelAccessor) && checkDaytime(levelAccessor) && checkTagBlocks(levelAccessor, pos, GaiaTags.GAIA_SPAWABLE_ON) && checkAboveSeaLevel(levelAccessor, pos) && checkGaiaDaySpawnRules(entityType, levelAccessor, spawnType, pos, random);
    }

    static {
        IS_BUFFED = SynchedEntityData.defineId(SummonedValkyrie.class, EntityDataSerializers.BOOLEAN);
        ANNOYED = SynchedEntityData.defineId(SummonedValkyrie.class, EntityDataSerializers.BOOLEAN);
        ANIMATION_STATE = SynchedEntityData.defineId(SummonedValkyrie.class, EntityDataSerializers.INT);
    }
}
