package com.rinko1231.gogspells.entity;

import com.rinko1231.gogspells.api.AnotherMagicManager;
import com.rinko1231.gogspells.api.MagicSpell;
import com.rinko1231.gogspells.entity.ai.FlexibleRangedAttackGoal;
import com.rinko1231.gogspells.entity.projectile.ArachneAcidOrb;
import com.rinko1231.gogspells.entity.projectile.ArachnePoisonArrow;
import com.rinko1231.gogspells.entity.projectile.MagicWebProjectile;
import com.rinko1231.gogspells.init.EntityRegistry;
import com.rinko1231.gogspells.init.NewSpellRegistry;
import com.rinko1231.gogspells.utils.MyUtils;
import gaia.entity.AbstractGaiaEntity;
import gaia.entity.goal.MobAttackGoal;
import gaia.registry.GaiaRegistry;
import gaia.util.EnchantUtil;
import gaia.util.SharedEntityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

import static io.redspace.ironsspellbooks.registries.MobEffectRegistry.SPIDER_ASPECT;

public class SummonedArachne extends AbstractGaiaEntity implements RangedAttackMob, IMagicSummon {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    private static final EntityDataAccessor<Integer> ATTACK_TYPE;
    private final AnotherMagicManager anotherMagicManager = new AnotherMagicManager();
    private final FlexibleRangedAttackGoal rangedAttackGoal = new FlexibleRangedAttackGoal(this, 1.00, 20, 60, 10.0F);
    private final MobAttackGoal collideAttackGoal = new MobAttackGoal(this, 1.275, true);
    private int switchHealth = 0;
    private int spawn = 0;
    private int spawnTimer = 0;
    private boolean animationPlay = false;
    private int animationTimer = 0;
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    private int switchTimer = 0; // 计时器
    private static final int SWITCH_INTERVAL = 200; // 每200tick切换一次
    @Override
    public boolean canAttackType(EntityType<?> type) {
        return true;
    }
    public SummonedArachne(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
        anotherMagicManager.addSpell(new MagicSpell("castMagicWeb", 20, 2, this::castMagicWeb));
        anotherMagicManager.addSpell(new MagicSpell("castAcidOrbSpell", 300, 3, this::castAcidOrbSpell));
        anotherMagicManager.addSpell(new MagicSpell("castPoisonArrowSpell", 400, 3, this::castPoisonArrowSpell));
    }
    public SummonedArachne(Level level, LivingEntity owner) {
        this((EntityType<? extends Monster>) EntityRegistry.SUMMONED_ARACHNE.get(), level);
        this.xpReward = 0;
        this.setSummoner(owner);
        anotherMagicManager.addSpell(new MagicSpell("castMagicWeb", 20, 2, this::castMagicWeb));
        anotherMagicManager.addSpell(new MagicSpell("castAcidOrbSpell", 300, 3, this::castAcidOrbSpell));
        anotherMagicManager.addSpell(new MagicSpell("castPoisonArrowSpell", 400, 3, this::castPoisonArrowSpell));
    }
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new GenericFollowOwnerGoal(this, this::getSummoner, (double)0.9F, 15.0F, 5.0F, false, 25.0F));


        this.goalSelector.addGoal(8, new RandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == this.getSummoner())).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));
    }

    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, (double)40.0F)
                .add(AttributeRegistry.NATURE_MAGIC_RESIST, 1.25f)
                .add(Attributes.FOLLOW_RANGE, (double)20.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.25F)
                .add(Attributes.ATTACK_DAMAGE, (double)4.0F)
                .add(Attributes.ARMOR, (double)4.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3)
                .add(Attributes.STEP_HEIGHT, (double)1.0F);
    }
    public void onUnSummon() {
        if (!this.level().isClientSide) {
            io.redspace.ironsspellbooks.capabilities.magic.MagicManager.spawnParticles(this.level(), ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 25, 0.4, 0.8, 0.4, 0.03, false);
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

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS_ID, (byte)0);
        builder.define(ATTACK_TYPE, 0);
    }

    public int getAttackType() {
        return (Integer)this.entityData.get(ATTACK_TYPE);
    }

    public void setAttackType(int type) {
        this.entityData.set(ATTACK_TYPE, type);
    }

    public float getBaseDefense() {
        return SharedEntityData.getBaseDefense1();
    }

    public boolean hurt(DamageSource source, float damage) {
        float input = this.getBaseDamage(source, damage);
        return !this.shouldIgnoreDamage(source) && super.hurt(source, input);
    }

    public void castMagicWeb(LivingEntity target, float distanceFactor) {
        this.playSound((SoundEvent)SoundRegistry.POISON_ARROW_CAST.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        Vec3 from = this.getEyePosition();
        Vec3 to = target.getEyePosition();
        Vec3 direction = to.subtract(from).normalize();
        MagicWebProjectile web = new MagicWebProjectile(this.level(), this);
        Vec3 spawnPos = from.add(direction.scale(0.25D)); // 0.5格前方生成
        this.lookAt(target, 30.0F, 30.0F);
        web.setPos(spawnPos.x, spawnPos.y - web.getBoundingBox().getYsize() * 0.5D, spawnPos.z);
        web.shoot(
                direction.x,
                direction.y,
                direction.z,
                0.8F,
                0.1F
        );
        web.setDamage(this.getATK()*0.8f);
        this.level().addFreshEntity(web);
        this.setAttackType(1);
        this.animationPlay = true;
        this.animationTimer = 0;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        MagicSpell spell = anotherMagicManager.getRandomAvailableSpell();
        if (spell != null) {
            spell.cast(target, distanceFactor);
        }
    }
    public void castPoisonArrowSpell(LivingEntity target, float dist) {
        Vec3 from = this.getEyePosition();
        Vec3 to = target.getEyePosition();
        Vec3 direction = to.subtract(from).normalize();
        this.level().playSound(
                null,
                this.blockPosition(),
                SoundRegistry.POISON_ARROW_CAST.get(),
                SoundSource.NEUTRAL,
                1.0F,
                1.0F + (this.getRandom().nextFloat() - 0.5F) * 0.2F
        );
        ArachnePoisonArrow poisonArrow = new ArachnePoisonArrow(this.level(), this);
        Vec3 spawnPos = from.add(direction.scale(0.5D)); // 0.5格前方生成
        this.lookAt(target, 30.0F, 30.0F);
        poisonArrow.setPos(spawnPos.x, spawnPos.y - poisonArrow.getBoundingBox().getYsize() * 0.5D, spawnPos.z);
        poisonArrow.shoot(
                direction.x,
                direction.y,
                direction.z,
                2.0F,
                0.1F
        );
        poisonArrow.setDamage(0.80f * (float) this.getATK());
        poisonArrow.setAoeDamage(0.10f * (float) this.getATK());
        this.level().addFreshEntity(poisonArrow);
        this.setAttackType(1);
        this.animationPlay = true;
        this.animationTimer = 0;
    }

    public void castAcidOrbSpell(LivingEntity target, float dist) {
        Vec3 from = this.getEyePosition();
        Vec3 to = target.getEyePosition();
        Vec3 direction = to.subtract(from).normalize();
        this.level().playSound(
                null,
                this.blockPosition(),
                SoundRegistry.ACID_ORB_CAST.get(),
                SoundSource.NEUTRAL,
                1.0F,
                1.0F + (this.getRandom().nextFloat() - 0.5F) * 0.2F
        );
        ArachneAcidOrb orb = new ArachneAcidOrb(this.level(), this);
        Vec3 spawnPos = from.add(direction.scale(0.5D)); // 0.5格前方生成
        this.lookAt(target, 30.0F, 30.0F);
        orb.setPos(spawnPos.x, spawnPos.y - orb.getBoundingBox().getYsize() * 0.5D, spawnPos.z);
        orb.shoot(
                direction.x,
                direction.y,
                direction.z,
                0.85F,
                0.1F
        );
        orb.setDeltaMovement(orb.getDeltaMovement().add((double)0.0F, 0.2, (double)0.0F));
        orb.setExplosionRadius((float)MyUtils.maxCap(6,3+0.1*this.getATK()));
        orb.setRendLevel(3);
        orb.setRendDuration(10*20);
        this.level().addFreshEntity(orb);
        this.setAttackType(1);
        this.animationPlay = true;
        this.animationTimer = 0;
    }

    public boolean doHurtTarget(Entity entityIn) {
        if (Utils.doMeleeAttack(this, entityIn,
                ((AbstractSpell) NewSpellRegistry.SUMMON_ARACHNE.get())
                        .getDamageSource(this, this.getSummoner()))) {
            if (entityIn instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entityIn;
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
            }
            return true;
        } else {
            return false;
        }
    }
    protected boolean shouldDropLoot() {
        return false;
    }

    public void aiStep() {
        this.beaconMonster(6, (entity) -> {
            if (entity instanceof SummonedCaveSpider) {
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 0, true, true));
            }


        });
        // 定时切换逻辑
        if (!this.level().isClientSide) {
            ++this.switchTimer;
            if (this.switchTimer >= SWITCH_INTERVAL) {
                this.switchTimer = 0; // 重置计时

                if (this.switchHealth == 0) {
                    // 切换为近战模式
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(GaiaRegistry.METAL_DAGGER.get()));
                    this.setGoals(1);
                    this.switchHealth = 1;

                } else {

                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(GaiaRegistry.CAVE_SPIDER_STAFF.get()));
                    this.setGoals(0);
                    this.switchHealth = 0;
                }
            }
        }


        if (this.getHealth() < this.getMaxHealth() * 0.33F) {
            this.addEffect(new MobEffectInstance(SPIDER_ASPECT, -1, 0, true, false));
        }

        if (this.getHealth() < this.getMaxHealth() * 0.75F && this.getHealth() > 0.0F && this.spawn == 0) {
            this.setAttackType(2);
            if (this.spawnTimer != 30) {
                ++this.spawnTimer;
            }

            if (this.spawnTimer == 30) {
                this.level().broadcastEntityEvent(this, (byte)9);
                this.setAttackType(0);
                if (!this.level().isClientSide) {
                    this.setSpawn(0);
                }

                this.spawnTimer = 0;
                this.spawn = 1;
            }
        }

        if (this.getHealth() < this.getMaxHealth() * 0.25F && this.getHealth() > 0.0F && this.spawn == 1) {
            this.setAttackType(2);
            if (this.spawnTimer != 30) {
                ++this.spawnTimer;
            }

            if (this.spawnTimer == 30) {
                this.level().broadcastEntityEvent(this, (byte)9);
                this.setAttackType(0);
                if (!this.level().isClientSide) {
                    this.setSpawn(0);
                }

                this.spawnTimer = 0;
                this.spawn = 2;
            }
        }

        if (this.animationPlay) {
            if (this.animationTimer != 20) {
                ++this.animationTimer;
            } else {
                this.setAttackType(0);
                this.animationPlay = false;
            }
        }

        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
        anotherMagicManager.tick();
        super.aiStep();
    }

    private void setSpawn(int id) {
        if ( id == 0) {
            SummonedCaveSpider caveSpider = new SummonedCaveSpider(this.level(),this);
            if (caveSpider != null) {
                caveSpider.moveTo(this.blockPosition(), 0.0F, 0.0F);

                caveSpider.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(this.getATK()*0.25f);
                caveSpider.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(this.getMaxHealth()*0.25f);
                caveSpider.setHealth(caveSpider.getMaxHealth());

                caveSpider.finalizeSpawn((ServerLevel)this.level(), this.level().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null);
                this.level().addFreshEntity(caveSpider);
            }
        }
        this.level().broadcastEntityEvent(this, (byte)6);
    }

    private void setCombatTask() {
        ItemStack itemstack = this.getMainHandItem();
        if (itemstack.is((Item)GaiaRegistry.CAVE_SPIDER_STAFF.get())) {
            this.setGoals(0);
        } else {
            this.setGoals(1);
        }

    }

    private void setGoals(int id) {
        if (id == 1) {
            this.goalSelector.removeGoal(this.rangedAttackGoal);
            this.goalSelector.addGoal(7, this.collideAttackGoal);
        } else {
            this.goalSelector.removeGoal(this.collideAttackGoal);
            this.goalSelector.addGoal(7, this.rangedAttackGoal);
            this.setAttackType(0);
            this.animationPlay = false;
            this.animationTimer = 0;
        }

    }

    public boolean isClimbing() {
        return ((Byte)this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean value) {
        byte b0 = (Byte)this.entityData.get(DATA_FLAGS_ID);
        if (value) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    protected ResourceKey<LootTable> getDefaultLootTable() {
        return this.random.nextInt(2) == 0 ? super.getDefaultLootTable() : EntityType.WITCH.getDefaultLootTable();
    }

    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
        ItemStack staffStack = new ItemStack((ItemLike)GaiaRegistry.CAVE_SPIDER_STAFF.get());
        staffStack.enchant(EnchantUtil.getEnchantmentHolder(this, Enchantments.KNOCKBACK), 2);
        this.setItemSlot(EquipmentSlot.MAINHAND, staffStack);
    }

    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data) {
        data = super.finalizeSpawn(levelAccessor, difficultyInstance, spawnType, data);
        this.populateDefaultEquipmentSlots(this.random, difficultyInstance);
        this.setCombatTask();
        return data;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("WeaponType", this.getAttackType());
        OwnerHelper.serializeOwner(tag, this.summonerUUID);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("WeaponType")) {
            this.setAttackType(tag.getInt("WeaponType"));
        }
        this.summonerUUID = OwnerHelper.deserializeOwner(tag);
        this.setCombatTask();
    }
    public float getATK() {
        float ATK = 8;
        if (this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE) != null)
            ATK = (float) Objects.requireNonNull(this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)).getValue();
        return ATK;
    }

    protected SoundEvent getAmbientSound() {
        return GaiaRegistry.ARACHNE.getSay();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return GaiaRegistry.ARACHNE.getHurt();
    }

    protected SoundEvent getDeathSound() {
        return GaiaRegistry.ARACHNE.getDeath();
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public void makeStuckInBlock(BlockState state, Vec3 speedMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, speedMultiplier);
        }

    }

    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return effectInstance.getEffect() != MobEffects.POISON && super.canBeAffected(effectInstance);
    }

    public boolean fireImmune() {
        return true;
    }

    public int getMaxSpawnClusterSize() {
        return 2;
    }

    public static boolean checkArachneSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkDaysPassed(levelAccessor) && checkBelowSeaLevel(levelAccessor, pos) && checkMonsterSpawnRules(entityType, levelAccessor, spawnType, pos, random);
    }

    static {
        DATA_FLAGS_ID = SynchedEntityData.defineId(SummonedArachne.class, EntityDataSerializers.BYTE);
        ATTACK_TYPE = SynchedEntityData.defineId(SummonedArachne.class, EntityDataSerializers.INT);
    }
}
