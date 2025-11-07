package com.rinko1231.gogspells.entity;

import com.rinko1231.gogspells.api.AnotherMagicManager;
import com.rinko1231.gogspells.api.MagicSpell;
import com.rinko1231.gogspells.entity.ai.FlexibleRangedAttackGoal;
import com.rinko1231.gogspells.init.EntityRegistry;
import com.rinko1231.gogspells.init.NewSpellRegistry;
import gaia.entity.AbstractGaiaEntity;
import gaia.registry.GaiaRegistry;
import gaia.util.SharedEntityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.entity.spells.ExtendedEvokerFang;
import io.redspace.ironsspellbooks.entity.spells.firefly_swarm.FireflySwarmProjectile;
import io.redspace.ironsspellbooks.entity.spells.magic_missile.MagicMissileProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class SummonedWitch extends AbstractGaiaEntity implements RangedAttackMob, IMagicSummon {
    private static final EntityDataAccessor<Boolean> IS_DRINKING;
    private static final EntityDataAccessor<Boolean> IS_RIDING;
    private static final ResourceLocation DRINKING_ID;
    private static final AttributeModifier SPEED_MODIFIER_DRINKING;

    static {
        IS_DRINKING = SynchedEntityData.defineId(SummonedWitch.class, EntityDataSerializers.BOOLEAN);
        IS_RIDING = SynchedEntityData.defineId(SummonedWitch.class, EntityDataSerializers.BOOLEAN);
        DRINKING_ID = ResourceLocation.fromNamespaceAndPath("grimoireofgaia", "drinking");
        SPEED_MODIFIER_DRINKING = new AttributeModifier(DRINKING_ID, (double) -0.25F, AttributeModifier.Operation.ADD_VALUE);
    }

    protected final FlyingMoveControl flyingControl;
    protected final MoveControl normalControl;
    private final AnotherMagicManager anotherMagicManager = new AnotherMagicManager();
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    private int spawn;
    private int usingTime;

    public SummonedWitch(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
        this.flyingControl = new FlyingMoveControl(this, 20, true);
        this.normalControl = new MoveControl(this);
        this.spawn = 0;
        anotherMagicManager.addSpell(new MagicSpell("ThrowPotion", 150, 3, this::ThrowPotion));
        anotherMagicManager.addSpell(new MagicSpell("castMagicMissileSpell", 100, 3, this::castMagicMissileSpell));
        anotherMagicManager.addSpell(new MagicSpell("castFireflySwarmSpell", 500, 4, this::castFireflySwarmSpell));
        anotherMagicManager.addSpell(new MagicSpell("castEvokerFangStrikeSpell", 160, 4, this::castEvokerFangStrikeSpell));
    }

    public SummonedWitch(Level level, LivingEntity owner) {
        this((EntityType<? extends Monster>) EntityRegistry.SUMMONED_WITCH.get(), level);
        this.xpReward = 0;
        anotherMagicManager.addSpell(new MagicSpell("ThrowPotion", 150, 3, this::ThrowPotion));
        anotherMagicManager.addSpell(new MagicSpell("castMagicMissileSpell", 100, 3, this::castMagicMissileSpell));
        anotherMagicManager.addSpell(new MagicSpell("castFireflySwarmSpell", 500, 4, this::castFireflySwarmSpell));
        anotherMagicManager.addSpell(new MagicSpell("castEvokerFangStrikeSpell", 160, 4, this::castEvokerFangStrikeSpell));
        this.setSummoner(owner);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(AttributeRegistry.EVOCATION_MAGIC_RESIST, 1.25)
                .add(AttributeRegistry.ELDRITCH_MAGIC_RESIST, 1.25)
                .add(Attributes.MAX_HEALTH, (double) 80.0F)
                .add(Attributes.FOLLOW_RANGE, (double) 20.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.275)
                .add(Attributes.ATTACK_DAMAGE, (double) 8.0F)
                .add(Attributes.ARMOR, (double) 8.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double) 0.25F)
                .add(Attributes.FLYING_SPEED, (double) 0.6F)
                .add(Attributes.STEP_HEIGHT, (double) 1.0F);
    }

    public static boolean checkWitchSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkDaysPassed(levelAccessor) && checkAboveSeaLevel(levelAccessor, pos) && checkMonsterSpawnRules(entityType, levelAccessor, spawnType, pos, random);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new GenericFollowOwnerGoal(this, this::getSummoner, 1.5D, 15.0f, 5.0f, this.isRidingBroom(), 25.0f));

        this.goalSelector.addGoal(7, new FlexibleRangedAttackGoal(this, 1.20, 80, 10.0F));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, (double) 1.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == this.getSummoner())).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));
    }

    public int getGaiaLevel() {
        return 2;
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
        builder.define(IS_DRINKING, false);
        builder.define(IS_RIDING, false);
    }

    public void setUsingItem(boolean value) {
        this.getEntityData().set(IS_DRINKING, value);
    }

    public boolean isDrinkingPotion() {
        return (Boolean) this.getEntityData().get(IS_DRINKING);
    }

    public boolean isRidingBroom() {
        return (Boolean) this.getEntityData().get(IS_RIDING);
    }

    public void setRidingBroom(boolean value) {
        this.getEntityData().set(IS_RIDING, value);
    }

    public int maxVariants() {
        return 1;
    }

    public float getBaseDefense() {
        return SharedEntityData.getBaseDefense2();
    }

    public boolean hurt(DamageSource source, float damage) {
        float input = this.getBaseDamage(source, damage);
        return !this.shouldIgnoreDamage(source) && super.hurt(source, input);
    }
    @Override
    public boolean canAttackType(EntityType<?> type) {
        return true;
    }
    public boolean doHurtTarget(Entity entityIn) {
        return Utils.doMeleeAttack(this, entityIn,
                ((AbstractSpell) NewSpellRegistry.SUMMON_WITCH.get())
                        .getDamageSource(this, this.getSummoner()));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (this.isDrinkingPotion()) return;

        MagicSpell spell = anotherMagicManager.getRandomAvailableSpell();
        if (spell != null) {
            spell.cast(target, distanceFactor);
        }
    }

    public void ThrowPotion(LivingEntity target, float distanceFactor) {
        Vec3 vec3 = target.getDeltaMovement();
        double d0 = target.getX() + vec3.x - this.getX();
        double d1 = target.getEyeY() - (double) 1.1F - this.getY();
        double d2 = target.getZ() + vec3.z - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        Holder<Potion> potion = Potions.HARMING;
        if (d3 >= (double) 8.0F && !target.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            potion = Potions.SLOWNESS;
        } else if (target.getHealth() >= 8.0F && !target.hasEffect(MobEffects.POISON)) {
            potion = Potions.POISON;
        } else if (d3 <= (double) 3.0F && !target.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
            potion = Potions.WEAKNESS;
        }

        ThrownPotion thrownpotion = new ThrownPotion(this.level(), this);
        ItemStack potionStack = new ItemStack(Items.SPLASH_POTION);
        potionStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        thrownpotion.setItem(potionStack);
        thrownpotion.setXRot(thrownpotion.getXRot() + 20.0F);
        thrownpotion.shoot(d0, d1 + d3 * 0.2, d2, 0.75F, 8.0F);
        if (!this.isSilent()) {
            this.level().playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        }

        this.level().addFreshEntity(thrownpotion);

    }

    public void castFireflySwarmSpell(LivingEntity target, float dist)
    {
        this.level().playSound(
                null,
                this.blockPosition(),
                SoundRegistry.FIREFLY_SPELL_PREPARE.get(),
                SoundSource.NEUTRAL,
                1.0F,
                1.0F + (this.getRandom().nextFloat() - 0.5F) * 0.2F
        );
        FireflySwarmProjectile fireflies = new FireflySwarmProjectile(this.level(), this, target, (float)this.getATK()*0.65f);
        fireflies.moveTo(target.position().add((double)0.0F, (double)0.5F, (double)0.0F));
        this.level().addFreshEntity(fireflies);
    }
    public void castMagicMissileSpell(LivingEntity target, float dist) {
        Vec3 from = this.getEyePosition();
        Vec3 to = target.getEyePosition();
        Vec3 direction = to.subtract(from).normalize();

        MagicMissileProjectile magicMissileProjectile = new MagicMissileProjectile(this.level(), this);
        Vec3 spawnPos = from.add(direction.scale(0.5D)); // 0.5格前方生成
        //this.lookAt(target, 30.0F, 30.0F);
        magicMissileProjectile.setPos(spawnPos.x, spawnPos.y - magicMissileProjectile.getBoundingBox().getYsize() * 0.5D, spawnPos.z);
        magicMissileProjectile.shoot(
                direction.x,
                direction.y,
                direction.z,
                2.5F,
                0.1F
        );
        magicMissileProjectile.setDamage(0.80f * (float) this.getATK());
        this.level().addFreshEntity(magicMissileProjectile);
    }

    public void castEvokerFangStrikeSpell(LivingEntity target, Float dist) {
        if (this.level().isClientSide || target == null) return;
        Level world = this.level();

        Vec3 toTarget = target.position().subtract(this.position());
        toTarget = new Vec3(toTarget.x, 0.0, toTarget.z).normalize();

        Vec3 start = this.getEyePosition().add(toTarget.scale(1.5D));

        int count = 8;
        //this.lookAt(target, 30.0F, 30.0F);
        for (int i = 0; i < count; ++i) {
            Vec3 spawn = start.add(toTarget.scale(i));

            spawn = new Vec3(spawn.x, getGroundLevel(world, spawn, 8), spawn.z);

            if (!world.getBlockState(BlockPos.containing(spawn).below()).isAir()) {
                int delay = i / 3;

                float yaw = (float) (Mth.atan2(toTarget.z, toTarget.x) * (180F / Math.PI)) - 90.0F;

                ExtendedEvokerFang fang = new ExtendedEvokerFang(
                        world,
                        spawn.x, spawn.y, spawn.z,
                        yaw * ((float) Math.PI / 180F),
                        delay,
                        this,
                        4.0f + (float) this.getATK() * 0.25f
                );

                world.addFreshEntity(fang);
            }
        }
    }

    private int getGroundLevel(Level level, Vec3 start, int maxSteps) {
        if (!level.getBlockState(BlockPos.containing(start)).isAir()) {
            for (int i = 0; i < maxSteps; ++i) {
                start = start.add((double) 0.0F, (double) 1.0F, (double) 0.0F);
                if (level.getBlockState(BlockPos.containing(start)).isAir()) {
                    break;
                }
            }
        }

        Vec3 lower = level.clip(new ClipContext(start, start.add((double) 0.0F, (double) (maxSteps * -2), (double) 0.0F), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getLocation();
        return (int) lower.y;
    }

    public double getATK() {
        double ATK = 8;
        if (this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE) != null)
            ATK = Objects.requireNonNull(this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)).getValue();
        return ATK;
    }

    public void aiStep() {
        Vec3 motion = this.getDeltaMovement();
        if (!this.onGround() && motion.y < (double) 0.0F) {
            this.setDeltaMovement(motion.multiply((double) 1.0F, 0.6, (double) 1.0F));
        }
/*
        this.beaconMonster(6, (entity) -> {
            if (entity instanceof Zombie || entity instanceof Skeleton) {
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1, true, true));
            }

        });*/
        if (!this.level().isClientSide && this.isPassenger() && this.isRidingBroom()) {
            this.stopRiding();
        }

        motion = this.getDeltaMovement();
        if (motion.x > (double) 0.0F || motion.y > (double) 0.0F || motion.z > (double) 0.0F) {
            for (int i = 0; i < 2; ++i) {
                this.level().addParticle(ParticleTypes.WITCH, this.getX() + (this.random.nextDouble() - (double) 0.5F) * (double) this.getBbWidth(), this.getY() + this.random.nextDouble() * (double) this.getBbHeight(), this.getZ() + (this.random.nextDouble() - (double) 0.5F) * (double) this.getBbWidth(), (double) 0.0F, (double) 0.0F, (double) 0.0F);
            }
        }
/*
        if (this.getHealth() < this.getMaxHealth() * 0.75F && this.getHealth() > 0.0F && this.spawn == 0) {
            this.level().broadcastEntityEvent(this, (byte)9);
            if (!this.level().isClientSide) {
                this.setSpawn(0);
            }

            this.spawn = 1;
        }*/
/*
        if (this.getHealth() < this.getMaxHealth() * 0.25F && this.getHealth() > 0.0F && this.spawn == 1) {
            this.level().broadcastEntityEvent(this, (byte)9);
            if (!this.level().isClientSide) {
                this.setSpawn(1);
            }

            this.spawn = 2;
        }*/

        anotherMagicManager.tick();
        super.aiStep();
    }

    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack stack) {
        if (equipmentSlot == EquipmentSlot.OFFHAND) {
            if (stack.is((Item) GaiaRegistry.BROOM.get())) {
                this.moveControl = this.flyingControl;
                this.setRidingBroom(true);
            } else {
                this.moveControl = this.normalControl;
                this.setRidingBroom(false);
            }
        }

        super.setItemSlot(equipmentSlot, stack);
    }

    private void setSpawn(int id) {
        return;
        /*
        if (!this.level().isClientSide) {
            BlockPos blockpos = this.blockPosition().offset(-1 + this.random.nextInt(3), 1, -1 + this.random.nextInt(3));
            Monster monster = id == 0 ? (Monster)EntityType.ZOMBIE.create(this.level()) : (Monster)EntityType.SKELETON.create(this.level());
            if (monster != null) {
                monster.moveTo(blockpos, 0.0F, 0.0F);
                EventHooks.finalizeMobSpawn(monster, (ServerLevel)this.level(), this.level().getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null);
                monster.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)GaiaRegistry.HEADGEAR_MOB.get()));
                monster.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
                monster.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
                monster.setDropChance(EquipmentSlot.FEET, 0.0F);
                monster.setDropChance(EquipmentSlot.LEGS, 0.0F);
                monster.setDropChance(EquipmentSlot.CHEST, 0.0F);
                monster.setDropChance(EquipmentSlot.HEAD, 0.0F);
                this.level().addFreshEntity(monster);
            }
        }*/

    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        OwnerHelper.serializeOwner(tag, this.summonerUUID);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.summonerUUID = OwnerHelper.deserializeOwner(tag);
    }

    protected ResourceKey<LootTable> getDefaultLootTable() {
        return this.random.nextInt(2) == 0 ? super.getDefaultLootTable() : EntityType.WITCH.getDefaultLootTable();
    }

    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(random.nextInt(2) == 0 ? (ItemLike) GaiaRegistry.ZOMBIE_STAFF.get() : (ItemLike) GaiaRegistry.SKELETON_STAFF.get()));
        if (random.nextInt(2) == 0) {
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack((ItemLike) GaiaRegistry.BROOM.get()));
        }

    }

    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data) {
        if (this.random.nextInt(4) == 0) {
            this.setVariant(1);
        }

        this.populateDefaultEquipmentSlots(this.random, difficultyInstance);
        return data;
    }

    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return effectInstance.getEffect() != MobEffects.POISON && effectInstance.getEffect() != MobEffects.HARM && super.canBeAffected(effectInstance);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return GaiaRegistry.WITCH.getSay();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return GaiaRegistry.WITCH.getHurt();
    }

    protected SoundEvent getDeathSound() {
        return GaiaRegistry.WITCH.getDeath();
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }
}
