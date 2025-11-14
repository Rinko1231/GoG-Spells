package com.rinko1231.gogspells.compat.traveloptics.entity;

import com.gametechbc.traveloptics.entity.projectiles.coral_bolt.BlueCoralBoltProjectile;
import com.gametechbc.traveloptics.entity.projectiles.coral_bolt.PinkCoralBoltProjectile;
import com.gametechbc.traveloptics.entity.projectiles.coral_bolt.RedCoralBoltProjectile;
import com.gametechbc.traveloptics.entity.projectiles.coral_bolt.YellowCoralBoltProjectile;
import com.gametechbc.traveloptics.entity.projectiles.hydroshot.HydroshotProjectile;

import com.rinko1231.gogspells.api.AnotherMagicManager;
import com.rinko1231.gogspells.api.MagicSpell;
import com.rinko1231.gogspells.compat.traveloptics.init.EntityInit;
import com.rinko1231.gogspells.compat.traveloptics.init.SpellInit;
import com.rinko1231.gogspells.entity.ai.GenericProtectOwnerTargetGoal;
import gaia.entity.AbstractGaiaEntity;
import gaia.entity.goal.MobAttackGoal;
import gaia.registry.GaiaRegistry;
import gaia.registry.GaiaTags;
import gaia.util.RangedUtil;
import gaia.util.SharedEntityData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.effect.SummonTimer;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.rinko1231.gogspells.entity.ai.FlexibleRangedAttackGoal;

import java.util.Objects;
import java.util.UUID;

import static com.gametechbc.traveloptics.api.init.TravelopticsAttributes.AQUA_MAGIC_RESIST;
import static com.rinko1231.gogspells.init.MobEffectRegistry.SUMMON_SIREN_TIMER;
import static net.minecraft.world.item.Items.BOW;

public class SummonedSiren extends AbstractGaiaEntity implements RangedAttackMob, MagicSummon {
    private final RangedBowAttackGoal<SummonedSiren> bowAttackGoal = new RangedBowAttackGoal(this, (double)1.25F, 20, 15.0F);
    private final MobAttackGoal mobAttackGoal = new MobAttackGoal(this, (double)1.25F, true);
    private final FlexibleRangedAttackGoal rangedAttackGoal = new FlexibleRangedAttackGoal(this, 1.00, 40, 60, 10.0F);
    private final AnotherMagicManager anotherMagicManager = new AnotherMagicManager();
    public enum SirenClass {
        SABER, ARCHER, CASTER
    }
    private SirenClass sirenClass;
    private byte inWaterTimer;
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;

    public SummonedSiren(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.inWaterTimer = 0;
        this.xpReward=0;
        this.initClassFromWeapon(new ItemStack(GaiaRegistry.METAL_DAGGER.get()));
    }
    public SummonedSiren(Level level, LivingEntity owner) {
        this(EntityInit.SUMMONED_SIREN.get(),level);
        this.setSummoner(owner);
        this.xpReward=0;
        this.initClassFromWeapon(new ItemStack(GaiaRegistry.METAL_DAGGER.get()));
    }
    public SummonedSiren(Level level, LivingEntity owner, SirenClass sirenClassSetting) {
        this(level, owner);
        this.xpReward=0;
        if (sirenClassSetting == SirenClass.SABER) {
            this.initClassFromWeapon(new ItemStack(GaiaRegistry.METAL_DAGGER.get()));
        } else if (sirenClassSetting == SirenClass.ARCHER) {
            this.initClassFromWeapon(new ItemStack(BOW));
        } else if (sirenClassSetting == SirenClass.CASTER) {
            this.initClassFromWeapon(new ItemStack(GaiaRegistry.CAVE_SPIDER_STAFF.get()));
            anotherMagicManager.addSpell(new MagicSpell("castHydroshot", 20, 2, this::castHydroshot));
        } else {
            this.initClassFromWeapon(new ItemStack(GaiaRegistry.METAL_DAGGER.get()));
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new GenericFollowOwnerGoal(this, this::getSummoner, (double)0.9F, 15.0F, 5.0F, false, 25.0F));
        //7: Attack
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == this.getSummoner())).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));
    }

    private void initClassFromWeapon(ItemStack weapon) {
        this.setItemSlot(EquipmentSlot.MAINHAND, weapon);

        Item item = weapon.getItem();
        if (item == GaiaRegistry.METAL_DAGGER.get()) {
            this.sirenClass = SirenClass.SABER;
        } else if (item == BOW) {
            this.sirenClass = SirenClass.ARCHER;
        } else if (item == GaiaRegistry.CAVE_SPIDER_STAFF.get()) {
            this.sirenClass = SirenClass.CASTER;
        } else {
            // 默认作为Saber
            this.sirenClass = SirenClass.SABER;
        }

        this.applyClassGoals();
    }
    private void applyClassGoals() {
        this.goalSelector.removeGoal(this.mobAttackGoal);
        this.goalSelector.removeGoal(this.bowAttackGoal);
        this.goalSelector.removeGoal(this.rangedAttackGoal);

        switch (this.sirenClass) {
            case SABER -> this.goalSelector.addGoal(7,this.mobAttackGoal);
            case ARCHER -> this.goalSelector.addGoal(7, this.bowAttackGoal);
            case CASTER -> this.goalSelector.addGoal(7, this.rangedAttackGoal);
        }
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, (double)40.0F)
                .add(AQUA_MAGIC_RESIST.get(),1.25f)
                .add(Attributes.FOLLOW_RANGE, (double)26.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.25F)
                .add(Attributes.ATTACK_DAMAGE, (double)4.0F)
                .add(Attributes.ARMOR, (double)4.0F)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3)
                .add((Attribute) ForgeMod.STEP_HEIGHT_ADDITION.get(), (double)1.0F);
    }
    public void onUnSummon() {
        if (!this.level().isClientSide) {
            io.redspace.ironsspellbooks.capabilities.magic.MagicManager.spawnParticles(this.level(), ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 25, 0.4, 0.8, 0.4, 0.03, false);
            this.setRemoved(RemovalReason.DISCARDED);
        }

    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public int maxVariants() {
        return 1;
    }

    public float getBaseDefense() {
        return SharedEntityData.getBaseDefense1();
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
        this.onRemovedHelper(this, (SummonTimer)SUMMON_SIREN_TIMER.get());
        super.onRemovedFromWorld();
    }
    @Override
    public boolean isPreventingPlayerRest(@NotNull Player player) {
        return !this.isAlliedTo(player);
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
        return Utils.doMeleeAttack(this, entityIn,
                ((AbstractSpell) SpellInit.SUMMON_SIREN.get())
                        .getDamageSource(this, this.getSummoner()));
    }
    protected boolean shouldDropLoot() {
        return false;
    }
    public void aiStep() {
        if (!this.level().isClientSide && this.isInWater()) {
            if (this.inWaterTimer <= 100) {
                ++this.inWaterTimer;
            } else {
                this.level().broadcastEntityEvent(this, (byte)8);
                this.heal(this.getMaxHealth() * 0.1F);
                this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0));
                this.inWaterTimer = 0;
            }
        }
        anotherMagicManager.tick();
        super.aiStep();
    }

    public boolean canAttackType(EntityType<?> type) {
        return true;
    }


    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData) {
        if (isHalloween() && this.random.nextFloat() < 0.25F) {
            this.setVariant(1);
        }
        this.populateDefaultEquipmentSlots(this.random, this.level().getCurrentDifficultyAt(this.blockPosition()));
        return groupData;
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        OwnerHelper.serializeOwner(tag, this.summonerUUID);
        if (this.sirenClass != null) {
            tag.putString("SirenClass", this.sirenClass.name());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.summonerUUID = OwnerHelper.deserializeOwner(tag);

        // 恢复职业
        if (tag.contains("SirenClass")) {
            try {
                this.sirenClass = SirenClass.valueOf(tag.getString("SirenClass"));
            } catch (IllegalArgumentException e) {
                this.sirenClass = SirenClass.SABER;
            }
        } else {
            this.sirenClass = SirenClass.SABER;
        }

        // 根据职业恢复对应的装备与逻辑
        switch (this.sirenClass) {
            case SABER -> this.initClassFromWeapon(new ItemStack(GaiaRegistry.METAL_DAGGER.get()));
            case ARCHER -> this.initClassFromWeapon(new ItemStack(Items.BOW));
            case CASTER -> {
                this.initClassFromWeapon(new ItemStack(GaiaRegistry.CAVE_SPIDER_STAFF.get()));
                // 重新注册施法
                anotherMagicManager.addSpell(new MagicSpell("castHydroshot", 20, 2, this::castHydroshot));
            }
        }
    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (target.isAlive()) {
            switch (this.sirenClass) {
                case ARCHER -> {
                    switch (this.random.nextInt(4)) {
                        case 0, 1, 2 -> RangedUtil.rangedAttack(target, this, distanceFactor);
                        case 3 -> this.castCoralBarrage(target, distanceFactor);
                    }
                }
                case CASTER ->
                        {
                            MagicSpell spell = anotherMagicManager.getRandomAvailableSpell();
                            if (spell != null) {
                                this.swing(InteractionHand.MAIN_HAND);
                                spell.cast(target, distanceFactor);
                            }
                        }
            }
        }

    }

    public void castCoralBarrage(LivingEntity target, float distanceFactor) {
        Level level = this.level();
        LivingEntity shooter = this;

        if (target == null || !target.isAlive()) {
            return;
        }

        AbstractMagicProjectile coralBolt = null;

        switch (this.random.nextInt(4)) {
            case 0 -> coralBolt = new BlueCoralBoltProjectile(level, shooter);
            case 1 -> coralBolt = new PinkCoralBoltProjectile(level, shooter);
            case 2 -> coralBolt = new YellowCoralBoltProjectile(level, shooter);
            case 3 -> coralBolt = new RedCoralBoltProjectile(level, shooter);
        }

        if (coralBolt != null) {
            coralBolt.setOwner(shooter);
            coralBolt.setPos(
                    shooter.getX(),
                    shooter.getEyeY() - 0.2F,
                    shooter.getZ()
            );
            double d0 = target.getX() - shooter.getX();
            double d1 = target.getY(0.3333333333333333) - coralBolt.getY();
            double d2 = target.getZ() - shooter.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            coralBolt.setNoGravity(false);
            coralBolt.shoot(d0, d1 + d3 * 0.2D, d2, 1.0F, (float)(1.5f));

            coralBolt.setDamage(this.getATK());

            level.addFreshEntity(coralBolt);
        }
    }
    public void castHydroshot(LivingEntity target, float distanceFactor) {
        this.playSound((SoundEvent) SoundRegistry.POISON_ARROW_CAST.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        Vec3 from = this.getEyePosition();
        Vec3 to = target.getEyePosition();
        Vec3 direction = to.subtract(from).normalize();
        HydroshotProjectile hydroshotProjectile = new HydroshotProjectile(this.level(), this);
        hydroshotProjectile.setSlownessAmplifier(0);
        Vec3 spawnPos = from.add(direction.scale(0.25D));
        this.lookAt(target, 30.0F, 30.0F);
        hydroshotProjectile.setPos(spawnPos.x, spawnPos.y - hydroshotProjectile.getBoundingBox().getYsize() * 0.5D, spawnPos.z);
        hydroshotProjectile.shoot(
                direction.x,
                direction.y,
                direction.z,
                0.8F,
                0.1F
        );
        hydroshotProjectile.setDamage(this.getATK()*0.8f);
        this.level().addFreshEntity(hydroshotProjectile);
    }

    public float getATK() {
        float ATK = 8;
        if (this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE) != null)
            ATK = (float) Objects.requireNonNull(this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)).getValue();
        return ATK;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {

        ItemStack bootsSwimming = new ItemStack(Items.LEATHER_BOOTS);
        bootsSwimming.enchant(Enchantments.DEPTH_STRIDER, 2);
        this.setItemSlot(EquipmentSlot.FEET, bootsSwimming);

        // 仅 Archer 会获得药箭
        if (this.sirenClass == SirenClass.ARCHER) {
            Potion arrowPotion;
            if (random.nextBoolean()) {
                arrowPotion = Potions.SLOWNESS;
            } else {
                arrowPotion = Potions.WEAKNESS;
            }

            ItemStack tippedArrow = PotionUtils.setPotion(new ItemStack(Items.TIPPED_ARROW), arrowPotion);
            this.setItemSlot(EquipmentSlot.OFFHAND, tippedArrow);
        } else {
            // 非Archer副手为空
            this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        }
    }



    public boolean isPushedByFluid() {
        return false;
    }

    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return effectInstance.getEffect() != MobEffects.POISON && super.canBeAffected(effectInstance);
    }



    protected SoundEvent getAmbientSound() {
        return GaiaRegistry.SIREN.getSay();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return GaiaRegistry.SIREN.getHurt();
    }

    protected SoundEvent getDeathSound() {
        return GaiaRegistry.SIREN.getDeath();
    }

    protected int getFireImmuneTicks() {
        return 10;
    }

    public int getMaxSpawnClusterSize() {
        return 2;
    }

    public static boolean checkSirenSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkDaysPassed(levelAccessor) && checkDaytime(levelAccessor) && checkTagBlocks(levelAccessor, pos, GaiaTags.GAIA_SPAWABLE_ON) && checkAboveY(pos, levelAccessor.getSeaLevel() - 8) && checkGaiaDaySpawnRules(entityType, levelAccessor, spawnType, pos, random);
    }
}
