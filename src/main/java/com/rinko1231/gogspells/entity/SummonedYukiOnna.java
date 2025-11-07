package com.rinko1231.gogspells.entity;

import com.rinko1231.gogspells.entity.ai.FlexibleRangedAttackGoal;
import com.rinko1231.gogspells.entity.ai.GenericProtectOwnerTargetGoal;
import com.rinko1231.gogspells.init.EntityRegistry;
import com.rinko1231.gogspells.init.NewSpellRegistry;
import gaia.entity.AbstractAssistGaiaEntity;
import gaia.entity.goal.MobAttackGoal;
import gaia.registry.GaiaRegistry;
import gaia.registry.GaiaSounds;
import gaia.registry.GaiaTags;

import gaia.util.SharedEntityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;

import io.redspace.ironsspellbooks.effect.SummonTimer;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleProjectile;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

import static com.rinko1231.gogspells.init.MobEffectRegistry.SUMMON_ARACHNE_TIMER;
import static io.redspace.ironsspellbooks.registries.MobEffectRegistry.CHILLED;
import static net.minecraft.world.damagesource.DamageTypes.FELL_OUT_OF_WORLD;

public class SummonedYukiOnna extends AbstractAssistGaiaEntity implements RangedAttackMob, MagicSummon {
    private static final EntityDataAccessor<Boolean> SHOOTING;
    private static final UUID KNOCKBACK_MODIFIER_UUID;
    private static final AttributeModifier KNOCKBACK_MODIFIER;
    @Override
    public boolean canAttackType(EntityType<?> type) {
        return true;
    }

    static {
        SHOOTING = SynchedEntityData.defineId(SummonedYukiOnna.class, EntityDataSerializers.BOOLEAN);
        KNOCKBACK_MODIFIER_UUID = UUID.fromString("D2EF3144-4329-4118-860A-80D2820C2CF1");
        KNOCKBACK_MODIFIER = new AttributeModifier(KNOCKBACK_MODIFIER_UUID, "Knockback boost", (double)2.0F, AttributeModifier.Operation.ADDITION);
    }
    private final FlexibleRangedAttackGoal bowAttackGoal = new FlexibleRangedAttackGoal(this, 0.75, 25, 60, 15.0F);
    private final MobAttackGoal mobAttackGoal = new MobAttackGoal(this, 1.000, true);
    //private final AvoidEntityGoal<Player> avoidPlayerGoal = new AvoidEntityGoal(this, Player.class, 20.0F, 1.275, 1.3);
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    private int switchHealth;

    public SummonedYukiOnna(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
        this.switchHealth = 0;
    }

    public SummonedYukiOnna(Level level, LivingEntity owner) {
        this((EntityType<? extends Monster>) EntityRegistry.SUMMONED_YUKI_ONNA.get(), level);
        this.xpReward = 0;
        this.switchHealth = 0;
        this.setSummoner(owner);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(AttributeRegistry.ICE_MAGIC_RESIST.get(), 1.25f)
                .add(Attributes.MAX_HEALTH, (double) 80.0F).add(Attributes.FOLLOW_RANGE, (double) 40.0F).add(Attributes.MOVEMENT_SPEED, 0.275).add(Attributes.ATTACK_DAMAGE, (double) 8.0F).add(Attributes.ARMOR, (double) 8.0F).add(Attributes.KNOCKBACK_RESISTANCE, (double) 0.25F).add((Attribute) ForgeMod.STEP_HEIGHT_ADDITION.get(), (double) 1.0F);
    }

    public static boolean checkYukiOnnaSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkDaysPassed(levelAccessor) && checkDaytime(levelAccessor) && checkTagBlocks(levelAccessor, pos, GaiaTags.GAIA_SPAWABLE_ON) && checkAboveSeaLevel(levelAccessor, pos) && checkGaiaDaySpawnRules(entityType, levelAccessor, spawnType, pos, random);
    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (target.isAlive()) {
            icicleShoot(target, this, distanceFactor);
        }

    }
    public void icicleShoot(LivingEntity target, LivingEntity shooter, float distanceFactor) {
        shooter.playSound((SoundEvent) GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
        double d0 = target.getX() - shooter.getX();
        double d1 = target.getEyeY() - shooter.getEyeY();
        double d2 = target.getZ() - shooter.getZ();
        double f1 = (double) Mth.sqrt(distanceFactor) * (double)0.5F;
        Vec3 vec3 = new Vec3(d0 + shooter.getRandom().nextGaussian() * f1, d1, d2 + shooter.getRandom().nextGaussian() * f1).normalize();
        IcicleProjectile icicleProjectile = new IcicleProjectile(shooter.level(), shooter);
        icicleProjectile.setPos(shooter.position().add((double)0.0F, (double)shooter.getEyeHeight() - shooter.getBoundingBox().getYsize() * (double)0.5F, (double)0.0F));
        icicleProjectile.setOwner(shooter);
        icicleProjectile.shoot(vec3);
        if(shooter instanceof MagicSummon magicSummon) {
            icicleProjectile.setDamage(0.66f * SpellRegistry.ICICLE_SPELL.get().getDamageSource(magicSummon.getSummoner()).spell().getSpellPower(1, magicSummon.getSummoner()));
        }
        shooter.level().addFreshEntity(icicleProjectile);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new GenericFollowOwnerGoal(this, this::getSummoner, 1.5D, 15.0f, 5.0f, false, 25.0f));

        this.goalSelector.addGoal(9, new RandomStrollGoal(this, (double) 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == this.getSummoner())).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));


    }

    public int getGaiaLevel() {
        return 2;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOOTING, false);
    }

    public boolean isShooting() {
        return (Boolean) this.entityData.get(SHOOTING);
    }

    public void setShooting(boolean flag) {
        this.entityData.set(SHOOTING, flag);
    }

    public float getBaseDefense() {
        return SharedEntityData.getBaseDefense2();
    }

    protected boolean shouldDropLoot() {
        return false;
    }

    public void onUnSummon() {
        if (!this.level().isClientSide) {
            MagicManager.spawnParticles(this.level(), ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 25, 0.4, 0.8, 0.4, 0.03, false);
            this.setRemoved(RemovalReason.DISCARDED);
        }

    }

    @Override
    public boolean isPreventingPlayerRest(@NotNull Player player) {
        return !this.isAlliedTo(player);
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
    public void die(@NotNull DamageSource source) {
        if(!source.is(FELL_OUT_OF_WORLD)) {
            FrozenHumanoid frozenHumanoid = new FrozenHumanoid(this.level(), this);
            frozenHumanoid.setSummoner(this.getSummoner());
            double ATK = 8;
            if(this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)!=null)
                ATK = Objects.requireNonNull(this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)).getValue();
            frozenHumanoid.setShatterDamage((float)ATK);
            frozenHumanoid.setDeathTimer(100);
            this.level().addFreshEntity(frozenHumanoid);
            this.deathTime = 1000;
            //frozenHumanoid.playSound((SoundEvent) SoundRegistry.FROSTBITE_FREEZE.get(), 2.0F, (float)Utils.random.nextInt(9, 11) * 0.1F);
        }
        this.onDeathHelper();
        super.die(source);
    }

    public boolean hurt(DamageSource source, float damage) {
        float input = this.getBaseDamage(source, damage);
        return !this.shouldIgnoreDamage(source) && super.hurt(source, input);
    }

    public boolean doHurtTarget(Entity entityIn) {
        if (Utils.doMeleeAttack(this, entityIn,
                ((AbstractSpell) NewSpellRegistry.SUMMON_YUKI_ONNA.get())
                        .getDamageSource(this, this.getSummoner()))) {
            if (entityIn instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entityIn;
                int effectTime = 12;

                if (effectTime > 0) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, effectTime * 20, 3));
                    livingEntity.setTicksFrozen(livingEntity.getTicksFrozen() + 40);
                    livingEntity.addEffect(new MobEffectInstance(CHILLED.get(), 20, 0));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void aiStep() {
        if (this.getHealth() < this.getMaxHealth() * 0.35F && this.switchHealth == 0) {
            switch (this.random.nextInt(2)) {
                case 0:
                    this.setGoals(1);
                    this.setShooting(true);
                    this.switchHealth = 1;
                    break;
                case 1:
                    this.switchHealth = 2;
            }
        }

        if (this.getHealth() >= this.getMaxHealth() * 0.35F && this.switchHealth == 1) {
            this.setGoals(0);
            this.setShooting(false);
            this.switchHealth = 0;
        }


        super.aiStep();
    }

    private void setGoals(int id) {
        if (id == 1) {
            this.goalSelector.removeGoal(this.mobAttackGoal);
            this.goalSelector.addGoal(7, this.bowAttackGoal);
        } else {
            this.goalSelector.removeGoal(this.bowAttackGoal);
            this.goalSelector.addGoal(7, this.mobAttackGoal);
        }

    }

    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
        if (!this.isBaby()) {
            if (random.nextInt(4) == 0) {
                AttributeInstance attributeinstance = this.getAttribute(Attributes.ATTACK_KNOCKBACK);
                attributeinstance.removeModifier(KNOCKBACK_MODIFIER);
                ItemStack weapon = new ItemStack((ItemLike) GaiaRegistry.FAN.get(), 1);
                weapon.enchant(Enchantments.KNOCKBACK, 3);
                this.setHandOrKnockback(weapon);
            } else {
                this.setHandOrKnockback(ItemStack.EMPTY);
            }
        }

    }

    protected void setHandOrKnockback(ItemStack stack) {
        AttributeInstance attributeinstance = this.getAttribute(Attributes.ATTACK_KNOCKBACK);
        attributeinstance.removeModifier(KNOCKBACK_MODIFIER);
        if (stack.isEmpty()) {
            attributeinstance.addTransientModifier(KNOCKBACK_MODIFIER);
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        }

    }

    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data) {
        this.populateDefaultEquipmentSlots(this.random, difficultyInstance);
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
        this.setGoals(0);
    }

    protected SoundEvent getAmbientSound() {
        return GaiaRegistry.YUKI_ONNA.getSay();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return GaiaRegistry.YUKI_ONNA.getHurt();
    }

    protected SoundEvent getDeathSound() {
        return GaiaRegistry.YUKI_ONNA.getDeath();
    }

    protected int getFireImmuneTicks() {
        return 20;
    }

    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }
}
