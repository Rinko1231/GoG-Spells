package com.rinko1231.gogspells.entity;

import gaia.entity.AbstractGaiaEntity;
import gaia.entity.Witch;
import gaia.registry.GaiaRegistry;
import gaia.util.SharedEntityData;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
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
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
//TODO
/*
public class SummonedWitch extends AbstractGaiaEntity implements RangedAttackMob {
    private static final EntityDataAccessor<Boolean> IS_DRINKING;
    private static final EntityDataAccessor<Boolean> IS_RIDING;
    private static final ResourceLocation DRINKING_ID;
    private static final AttributeModifier SPEED_MODIFIER_DRINKING;
    protected final FlyingMoveControl flyingControl;
    protected final MoveControl normalControl;
    private int spawn;
    private int usingTime;

    public SummonedWitch(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
        this.flyingControl = new FlyingMoveControl(this, 20, true);
        this.normalControl = new MoveControl(this);
        this.spawn = 0;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.275, 60, 10.0F));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, this.targetPlayerGoal = new NearestAttackableTargetGoal(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, (double)80.0F).add(Attributes.FOLLOW_RANGE, (double)20.0F).add(Attributes.MOVEMENT_SPEED, 0.275).add(Attributes.ATTACK_DAMAGE, (double)8.0F).add(Attributes.ARMOR, (double)8.0F).add(Attributes.KNOCKBACK_RESISTANCE, (double)0.25F).add(Attributes.FLYING_SPEED, (double)0.6F).add(Attributes.STEP_HEIGHT, (double)1.0F);
    }

    public int getGaiaLevel() {
        return 2;
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
        return (Boolean)this.getEntityData().get(IS_DRINKING);
    }

    public void setRidingBroom(boolean value) {
        this.getEntityData().set(IS_RIDING, value);
    }

    public boolean isRidingBroom() {
        return (Boolean)this.getEntityData().get(IS_RIDING);
    }

    public int maxVariants() {
        return 1;
    }

    public float getBaseDefense() {
        return SharedEntityData.getBaseDefense2();
    }

    public boolean hurt(DamageSource source, float damage) {
        float input = this.getBaseDamage(source, damage);
        return super.hurt(source, input);
    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (!this.isDrinkingPotion()) {
            Vec3 vec3 = target.getDeltaMovement();
            double d0 = target.getX() + vec3.x - this.getX();
            double d1 = target.getEyeY() - (double)1.1F - this.getY();
            double d2 = target.getZ() + vec3.z - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            Holder<Potion> potion = Potions.HARMING;
            if (target instanceof Raider) {
                if (target.getHealth() <= 4.0F) {
                    potion = Potions.HEALING;
                } else {
                    potion = Potions.REGENERATION;
                }

                this.setTarget((LivingEntity)null);
            } else if (d3 >= (double)8.0F && !target.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                potion = Potions.SLOWNESS;
            } else if (target.getHealth() >= 8.0F && !target.hasEffect(MobEffects.POISON)) {
                potion = Potions.POISON;
            } else if (d3 <= (double)3.0F && !target.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
                potion = Potions.WEAKNESS;
            }

            ThrownPotion thrownpotion = new ThrownPotion(this.level(), this);
            ItemStack potionStack = new ItemStack(Items.SPLASH_POTION);
            potionStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
            thrownpotion.setItem(potionStack);
            thrownpotion.setXRot(thrownpotion.getXRot() + 20.0F);
            thrownpotion.shoot(d0, d1 + d3 * 0.2, d2, 0.75F, 8.0F);
            if (!this.isSilent()) {
                this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
            }

            this.level().addFreshEntity(thrownpotion);
        }

    }

    public void aiStep() {
        Vec3 motion = this.getDeltaMovement();
        if (!this.onGround() && motion.y < (double)0.0F) {
            this.setDeltaMovement(motion.multiply((double)1.0F, 0.6, (double)1.0F));
        }

        this.beaconMonster(6, (entity) -> {
            if (entity instanceof Zombie || entity instanceof Skeleton) {
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1, true, true));
            }

        });
        if (!this.level().isClientSide && this.isPassenger() && this.isRidingBroom()) {
            this.stopRiding();
        }

        motion = this.getDeltaMovement();
        if (motion.x > (double)0.0F || motion.y > (double)0.0F || motion.z > (double)0.0F) {
            for(int i = 0; i < 2; ++i) {
                this.level().addParticle(ParticleTypes.WITCH, this.getX() + (this.random.nextDouble() - (double)0.5F) * (double)this.getBbWidth(), this.getY() + this.random.nextDouble() * (double)this.getBbHeight(), this.getZ() + (this.random.nextDouble() - (double)0.5F) * (double)this.getBbWidth(), (double)0.0F, (double)0.0F, (double)0.0F);
            }
        }

        if (this.getHealth() < this.getMaxHealth() * 0.75F && this.getHealth() > 0.0F && this.spawn == 0) {
            this.level().broadcastEntityEvent(this, (byte)9);
            if (!this.level().isClientSide) {
                this.setSpawn(0);
            }

            this.spawn = 1;
        }

        if (this.getHealth() < this.getMaxHealth() * 0.25F && this.getHealth() > 0.0F && this.spawn == 1) {
            this.level().broadcastEntityEvent(this, (byte)9);
            if (!this.level().isClientSide) {
                this.setSpawn(1);
            }

            this.spawn = 2;
        }

        if (this.isDrinkingPotion()) {
            if (this.usingTime-- <= 0) {
                this.setUsingItem(false);
                ItemStack itemstack = this.getMainHandItem();
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                if (itemstack.is(Items.POTION)) {
                    List<MobEffectInstance> list = ((PotionContents)itemstack.get(DataComponents.POTION_CONTENTS)).customEffects();
                    if (list != null) {
                        for(MobEffectInstance mobeffectinstance : list) {
                            this.addEffect(new MobEffectInstance(mobeffectinstance));
                        }
                    }
                }

                this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(DRINKING_ID);
            }
        } else {
            Holder<Potion> potion = null;
            if (this.random.nextFloat() < 0.15F && this.isEyeInFluid(FluidTags.WATER) && !this.hasEffect(MobEffects.WATER_BREATHING)) {
                potion = Potions.WATER_BREATHING;
            } else if (this.random.nextFloat() < 0.15F && (this.isOnFire() || this.getLastDamageSource() != null && this.getLastDamageSource().is(DamageTypeTags.IS_FIRE)) && !this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                potion = Potions.FIRE_RESISTANCE;
            } else if (this.random.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
                potion = Potions.HEALING;
            } else if (this.random.nextFloat() < 0.5F && this.getTarget() != null && !this.hasEffect(MobEffects.MOVEMENT_SPEED) && this.getTarget().distanceToSqr(this) > (double)121.0F) {
                potion = Potions.SWIFTNESS;
            }

            if (potion != null) {
                ItemStack potionStack = new ItemStack(Items.POTION);
                potionStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
                this.setItemSlot(EquipmentSlot.MAINHAND, potionStack);
                this.usingTime = this.getMainHandItem().getUseDuration(this);
                this.setUsingItem(true);
                if (!this.isSilent()) {
                    this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_DRINK, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
                }

                AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                attributeinstance.removeModifier(DRINKING_ID);
                attributeinstance.addTransientModifier(SPEED_MODIFIER_DRINKING);
            }
        }

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
        }

    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    protected ResourceKey<LootTable> getDefaultLootTable() {
        return this.random.nextInt(2) == 0 ? super.getDefaultLootTable() : EntityType.WITCH.getDefaultLootTable();
    }

    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(random.nextInt(2) == 0 ? (ItemLike)GaiaRegistry.ZOMBIE_STAFF.get() : (ItemLike)GaiaRegistry.SKELETON_STAFF.get()));
        if (random.nextInt(2) == 0) {
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack((ItemLike)GaiaRegistry.BROOM.get()));
        }

    }

    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data) {
        data = super.finalizeSpawn(levelAccessor, difficultyInstance, spawnType, data);
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

    public static boolean checkWitchSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkDaysPassed(levelAccessor) && checkAboveSeaLevel(levelAccessor, pos) && checkMonsterSpawnRules(entityType, levelAccessor, spawnType, pos, random);
    }

    static {
        IS_DRINKING = SynchedEntityData.defineId(SummonedWitch.class, EntityDataSerializers.BOOLEAN);
        IS_RIDING = SynchedEntityData.defineId(SummonedWitch.class, EntityDataSerializers.BOOLEAN);
        DRINKING_ID = ResourceLocation.fromNamespaceAndPath("grimoireofgaia", "drinking");
        SPEED_MODIFIER_DRINKING = new AttributeModifier(DRINKING_ID, (double)-0.25F, AttributeModifier.Operation.ADD_VALUE);
    }
}
*/