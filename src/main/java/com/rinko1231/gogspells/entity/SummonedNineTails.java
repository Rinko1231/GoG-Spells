package com.rinko1231.gogspells.entity;

import com.rinko1231.gogspells.entity.ai.FlamingStrikeMobAttackGoal;
import com.rinko1231.gogspells.entity.ai.FlexibleRangedAttackGoal;
import com.rinko1231.gogspells.entity.ai.GenericProtectOwnerTargetGoal;
import com.rinko1231.gogspells.init.EntityRegistry;
import com.rinko1231.gogspells.init.NewSpellRegistry;
import gaia.entity.AbstractGaiaEntity;
import gaia.registry.GaiaRegistry;
import gaia.registry.GaiaSounds;

import gaia.util.SharedEntityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;

import io.redspace.ironsspellbooks.effect.SummonTimer;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.entity.spells.firebolt.FireboltProjectile;
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

public class SummonedNineTails extends AbstractGaiaEntity implements RangedAttackMob, MagicSummon {
    private static final UUID KNOCKBACK_MODIFIER_UUID = UUID.fromString("E1F5906C-E05C-11EC-9D64-0242AC120002");
    private static final AttributeModifier KNOCKBACK_MODIFIER;
    private static final EntityDataAccessor<Boolean> THROWING;
    private static final EntityDataAccessor<Integer> WEAPON_TYPE;

    static {
        KNOCKBACK_MODIFIER = new AttributeModifier(KNOCKBACK_MODIFIER_UUID, "Knockback boost", (double)2.0F, AttributeModifier.Operation.ADDITION);
        THROWING = SynchedEntityData.defineId(SummonedNineTails.class, EntityDataSerializers.BOOLEAN);
        WEAPON_TYPE = SynchedEntityData.defineId(SummonedNineTails.class, EntityDataSerializers.INT);
    }

    private final FlexibleRangedAttackGoal bowAttackGoal = new FlexibleRangedAttackGoal(this, 0.75, 25, 60, 15.0F);
    private final FlamingStrikeMobAttackGoal mobAttackGoal = new FlamingStrikeMobAttackGoal(this, 1.275, true, 0.18);
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    private int switchHealth;
    private boolean animationPlay;
    private int animationTimer;

    public SummonedNineTails(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
        this.switchHealth = 0;
        this.animationPlay = false;
        this.animationTimer = 0;
    }

    public SummonedNineTails(Level level, LivingEntity owner) {
        this((EntityType<? extends Monster>) EntityRegistry.SUMMONED_NINE_TAILS.get(), level);
        this.xpReward = 0;
        this.switchHealth = 0;
        this.animationPlay = false;
        this.animationTimer = 0;
        this.setSummoner(owner);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(AttributeRegistry.FIRE_MAGIC_RESIST.get(), 1.25f)
                .add(Attributes.MAX_HEALTH, (double) 80.0F).add(Attributes.FOLLOW_RANGE, (double) 26.0F).add(Attributes.MOVEMENT_SPEED, 0.275).add(Attributes.ATTACK_DAMAGE, (double) 8.0F).add(Attributes.ARMOR, (double) 8.0F).add(Attributes.KNOCKBACK_RESISTANCE, (double) 0.25F).add((Attribute) ForgeMod.STEP_HEIGHT_ADDITION.get(), (double) 1.0F);
    }

    public static boolean checkNineTailsSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkDaysPassed(levelAccessor) && checkAboveSeaLevel(levelAccessor, pos) && checkMonsterSpawnRules(entityType, levelAccessor, spawnType, pos, random);
    }

    protected boolean shouldDropLoot() {
        return false;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new GenericFollowOwnerGoal(this, this::getSummoner, 1.5D, 15.0f, 5.0f, false, 25.0f));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, (double) 1.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == this.getSummoner())).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));

    }

    public void aiStep() {
        if (this.getHealth() < this.getMaxHealth() * 0.75F && this.switchHealth == 0) {
            if (this.getWeaponType() == 0) {
                if (this.random.nextInt(4) == 0) {
                    this.setWeaponType(2);
                } else {
                    this.setWeaponType(1);
                }
            }

            this.setEnchantedEquipment(this.getWeaponType());
            this.setGoals(1);
            this.switchHealth = 1;
        }

        if (this.getHealth() > this.getMaxHealth() * 0.75F && this.switchHealth == 1) {
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            this.setGoals(0);
            this.switchHealth = 0;
        }

        if (this.animationPlay) {
            if (this.animationTimer != 20) {
                ++this.animationTimer;
            } else {
                this.setThrowing(false);
                this.animationPlay = false;
            }
        }

        super.aiStep();
    }

    private void setGoals(int id) {
        if (id == 1) {
            this.goalSelector.removeGoal(this.bowAttackGoal);
            this.goalSelector.addGoal(7, this.mobAttackGoal);
        } else {
            this.goalSelector.removeGoal(this.mobAttackGoal);
            this.goalSelector.addGoal(7, this.bowAttackGoal);
            this.setThrowing(false);
            this.animationPlay = false;
            this.animationTimer = 0;
        }

    }

    private void setCombatTask() {
        if (!this.getMainHandItem().isEmpty() && !Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_KNOCKBACK)).hasModifier(KNOCKBACK_MODIFIER)) {
            this.setGoals(1);
        } else {
            this.setGoals(0);
        }

    }

    public int getGaiaLevel() {
        return 2;
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
    public void die(@NotNull DamageSource source) {
        this.onDeathHelper();
        super.die(source);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(THROWING, false);
        this.entityData.define(WEAPON_TYPE, 0);
    }

    public boolean isThrowing() {
        return (Boolean) this.entityData.get(THROWING);
    }

    public void setThrowing(boolean flag) {
        this.entityData.set(THROWING, flag);
    }

    public int getWeaponType() {
        return (Integer) this.entityData.get(WEAPON_TYPE);
    }

    public void setWeaponType(int type) {
        this.entityData.set(WEAPON_TYPE, type);
    }

    public float getBaseDefense() {
        return SharedEntityData.getBaseDefense2();
    }


    @Override
    public boolean isPreventingPlayerRest(@NotNull Player player) {
        return !this.isAlliedTo(player);
    }

    public boolean hurt(DamageSource source, float damage) {
        float input = this.getBaseDamage(source, damage);
        return !this.shouldIgnoreDamage(source) && super.hurt(source, input);
    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (target.isAlive()) {
            fireball(target, this, distanceFactor);
        }
    }
    @Override
    public boolean canAttackType(EntityType<?> type) {
        return true;
    }
    public void fireball(LivingEntity target, LivingEntity shooter, float distanceFactor) {
        shooter.playSound((SoundEvent) GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
        double d0 = target.getX() - shooter.getX();
        double d1 = target.getEyeY() - shooter.getEyeY();
        double d2 = target.getZ() - shooter.getZ();
        double f1 = (double) Mth.sqrt(distanceFactor) * (double)0.5F;
        Vec3 vec3 = new Vec3(d0 + shooter.getRandom().nextGaussian() * f1, d1, d2 + shooter.getRandom().nextGaussian() * f1).normalize();
        FireboltProjectile smallFireball = new FireboltProjectile(shooter.level(), shooter);
        smallFireball.setPos(shooter.position().add((double)0.0F, (double)shooter.getEyeHeight() - shooter.getBoundingBox().getYsize() * (double)0.5F, (double)0.0F));
        smallFireball.setOwner(shooter);
        smallFireball.shoot(vec3);
        if(shooter instanceof MagicSummon magicSummon) {
            smallFireball.setDamage(0.66f * SpellRegistry.FIREBOLT_SPELL.get().getDamageSource(magicSummon.getSummoner()).spell().getSpellPower(1, magicSummon.getSummoner()));
        }
        shooter.level().addFreshEntity(smallFireball);
        this.setThrowing(true);
        this.animationPlay = true;
        this.animationTimer = 0;
    }

    public boolean doHurtTarget(Entity entityIn) {
        if ( Utils.doMeleeAttack(this, entityIn,
                ((AbstractSpell) NewSpellRegistry.SUMMON_NINE_TAILS.get())
                        .getDamageSource(this, this.getSummoner()))) {
            entityIn.setRemainingFireTicks(120);
            return true;
        } else {
            return false;
        }
    }


    protected void setEnchantedEquipment(int id) {
        if (id == 1) {
            this.setHandOrKnockback(ItemStack.EMPTY);
        } else if (id == 2) {
            ItemStack weapon = new ItemStack((ItemLike) GaiaRegistry.FAN.get(), 1);
            weapon.enchant(Enchantments.KNOCKBACK, 2);
            this.setHandOrKnockback(weapon);
        }

    }

    protected void setHandOrKnockback(ItemStack stack) {
        AttributeInstance attributeinstance = this.getAttribute(Attributes.ATTACK_KNOCKBACK);
        if (attributeinstance != null) {
            attributeinstance.removeModifier(KNOCKBACK_MODIFIER);
        }
        if (stack.isEmpty()) {
            if (attributeinstance != null) {
                attributeinstance.addTransientModifier(KNOCKBACK_MODIFIER);
            }
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        }

    }

    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data) {
        this.setCombatTask();
        return data;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("WeaponType", this.getWeaponType());
        OwnerHelper.serializeOwner(tag, this.summonerUUID);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("WeaponType")) {
            int weaponType = tag.getInt("WeaponType");
            this.setWeaponType(weaponType);
        }
        this.summonerUUID = OwnerHelper.deserializeOwner(tag);
        this.setCombatTask();
    }

    protected SoundEvent getAmbientSound() {
        return GaiaRegistry.NINE_TAILS.getSay();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return GaiaRegistry.NINE_TAILS.getHurt();
    }

    protected SoundEvent getDeathSound() {
        return GaiaRegistry.NINE_TAILS.getDeath();
    }

    public boolean fireImmune() {
        return true;
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }
}
