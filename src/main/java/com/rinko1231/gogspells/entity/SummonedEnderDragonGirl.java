package com.rinko1231.gogspells.entity;

import com.rinko1231.gogspells.GoGSpells;
import com.rinko1231.gogspells.init.EntityRegistry;
import com.rinko1231.gogspells.init.NewSpellRegistry;
import gaia.entity.AbstractAssistGaiaEntity;
import gaia.entity.goal.MobAttackGoal;
import gaia.registry.GaiaRegistry;
import gaia.registry.GaiaSounds;
import gaia.util.SharedEntityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.entity.spells.dragon_breath.DragonBreathProjectile;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.redspace.ironsspellbooks.registries.MobEffectRegistry.BLIGHT;
import static io.redspace.ironsspellbooks.registries.MobEffectRegistry.TRUE_INVISIBILITY;

public class SummonedEnderDragonGirl extends AbstractAssistGaiaEntity implements IMagicSummon {
    private static final ResourceLocation SPEED_ID = GoGSpells.id("summoned_ender_dragon_speed");
    private static final AttributeModifier SPEED_MODIFIER_ATTACKING;
    private static final EntityDataAccessor<Boolean> SCREAMING;
    private static final EntityDataAccessor<Boolean> BEAM_FLAG;

    static {
        BEAM_FLAG = SynchedEntityData.defineId(SummonedEnderDragonGirl.class, EntityDataSerializers.BOOLEAN);
        SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_ID, 0.15, AttributeModifier.Operation.ADD_VALUE);
        SCREAMING = SynchedEntityData.defineId(SummonedEnderDragonGirl.class, EntityDataSerializers.BOOLEAN);
    }
    public boolean isBreathing() {
        return this.getEntityData().get(BEAM_FLAG);
    }

    public void setBreathing(boolean flag) {
        this.getEntityData().set(BEAM_FLAG, flag);
    }

    public void doBreathAttack() {
        if (!this.level().isClientSide) {
            List<DragonBreathProjectile> existing = this.level().getEntitiesOfClass(
                    DragonBreathProjectile.class,
                    this.getBoundingBox().inflate(2.0),
                    p -> p.getOwner() == this
            );
            if (!existing.isEmpty()) {
                existing.forEach(DragonBreathProjectile::setDealDamageActive);
                return;
            }
            double ATK = 6;
            if(this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)!=null)
                ATK = Objects.requireNonNull(this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)).getValue();
            DragonBreathProjectile cone = new DragonBreathProjectile(this.level(), this);
            cone.setPos(this.getX(), this.getEyeY() * 0.7 + this.getY(), this.getZ());
            cone.setDamage(1.0f+0.50f*(float)ATK);
            this.level().addFreshEntity(cone);
        }
    }
    public void stopBreathAttack()
    {
        this.level().getEntitiesOfClass(
                DragonBreathProjectile.class,
                this.getBoundingBox().inflate(6.0),
                p -> p.getOwner() == this
        ).forEach(Entity::discard);
    }

    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    private int targetChangeTime;

    public SummonedEnderDragonGirl(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
    }

    public SummonedEnderDragonGirl(Level level, LivingEntity owner) {
        this((EntityType<? extends Monster>) EntityRegistry.SUMMONED_ENDER_DRAGON_GIRL.get(), level);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setSummoner(owner);
    }
    protected boolean shouldDropLoot() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, (double) 80.0F)
                .add(Attributes.FOLLOW_RANGE, (double) 40.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.275)
                .add(Attributes.ATTACK_DAMAGE, (double) 8.0F)
                .add(Attributes.ARMOR, (double) 8.0F)
                .add(AttributeRegistry.ENDER_MAGIC_RESIST, 1.25f)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double) 0.25F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double) 1.0F)
                .add(Attributes.STEP_HEIGHT, (double) 1.5F);
    }

    public void onUnSummon() {
        if (!this.level().isClientSide) {
            MagicManager.spawnParticles(this.level(), ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 25, 0.4, 0.8, 0.4, 0.03, false);
            this.stopBreathAttack();
            this.setRemoved(RemovalReason.DISCARDED);
        }

    }
    public void remove(Entity.RemovalReason pReason) {
        this.stopBreathAttack();
        super.remove(pReason);
    }
    @Override
    public boolean isAlliedTo(@NotNull Entity other) {
        return super.isAlliedTo(other) || this.isAlliedHelper(other);
    }



    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData) {


        return pSpawnData;
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
        this.stopBreathAttack();
        super.onRemovedFromLevel();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(7, new DragonBreathAttackGoal(this));
        this.goalSelector.addGoal(8, new MobAttackGoal(this, (double) 1.0F, false));
        this.goalSelector.addGoal(6, new GenericFollowOwnerGoal(this, this::getSummoner, 1.6D, 15.0f, 5.0f, true, 15.0f));
        this.goalSelector.addGoal(9, new TeleportAttackGoal(this));
        this.goalSelector.addGoal(10, new RandomStrollGoal(this, (double) 1.0F));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == this.getSummoner())).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));
    }

    public boolean isAngryAt(@NotNull LivingEntity livingEntity) {
        if (!this.canAttack(livingEntity)) {
            return false;
        } else {
            return this.getTarget() != null && livingEntity.getType() == EntityType.PLAYER;
        }
    }

    public int getGaiaLevel() {
        return 2;
    }

    @Override
    public void die(@NotNull DamageSource source) {
        this.onDeathHelper();
        super.die(source);
    }

    public void setTarget(@Nullable LivingEntity livingEntity) {
        AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (livingEntity == null) {
            this.targetChangeTime = 0;
            this.entityData.set(SCREAMING, false);
            if (attributeinstance != null) {
                attributeinstance.removeModifier(SPEED_ID);
            }
        } else {
            this.targetChangeTime = this.tickCount;
            this.entityData.set(SCREAMING, true);
            if (attributeinstance != null && !attributeinstance.hasModifier(SPEED_ID)) {
                attributeinstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
            }
        }

        super.setTarget(livingEntity);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BEAM_FLAG, false);
        builder.define(SCREAMING, false);
    }

    public boolean isScreaming() {
        return (Boolean) this.entityData.get(SCREAMING);
    }


    @Override
    public boolean isPreventingPlayerRest(@NotNull Player player) {
        return !this.isAlliedTo(player);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity victim) {
        return Utils.doMeleeAttack(this, victim,
                ((AbstractSpell) NewSpellRegistry.SUMMON_ENDER_DRAGON_GIRL.get())
                        .getDamageSource(this, this.getSummoner()));
    }


    public boolean hurt(@NotNull DamageSource source, float damage) {
        float input = this.getBaseDamage(source, damage);
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!source.isDirect()) {
            Entity entity = source.getDirectEntity();
            boolean flag1;
            if (entity instanceof ThrownPotion) {
                flag1 = this.hurtWithCleanWater(source, (ThrownPotion) entity, input);
            } else {
                flag1 = false;
            }

            for (int i = 0; i < 64; ++i) {
                if (this.teleportRandomly()) {
                    return true;
                }
            }

            return flag1;
        } else {
            boolean flag = !this.shouldIgnoreDamage(source) && super.hurt(source, input);
            if (!this.level().isClientSide() && !(source.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
                this.teleportRandomly();
            }

            return flag;
        }
    }

    private boolean hurtWithCleanWater(DamageSource pSource, ThrownPotion pPotion, float pAmount) {
        ItemStack itemstack = pPotion.getItem();
        PotionContents potioncontents = (PotionContents) itemstack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return potioncontents.is(Potions.WATER) && super.hurt(pSource, pAmount);
    }

    public float getBaseDefense() {
        return SharedEntityData.getBaseDefense2();
    }

    public void aiStep() {
        if (!this.level().isClientSide) {
            for (int i = 0; i < 2; ++i) {
                this.level().addParticle(ParticleTypes.PORTAL, this.getRandomX((double) 0.5F), this.getRandomY() - (double) 0.25F, this.getRandomZ((double) 0.5F), (this.random.nextDouble() - (double) 0.5F) * (double) 2.0F, -this.random.nextDouble(), (this.random.nextDouble() - (double) 0.5F) * (double) 2.0F);
            }
        }

        this.jumping = false;
        super.aiStep();
    }

    protected void customServerAiStep() {
        if (this.isInWaterOrRain()) {
            this.hurt(this.damageSources().drown(), 1.0F);
        }

        super.customServerAiStep();
    }

    protected boolean teleportRandomly() {
        if (!this.level().isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - (double) 0.5F) * (double) 64.0F;
            double d1 = this.getY() + (double) (this.random.nextInt(64) - 32);
            double d2 = this.getZ() + (this.random.nextDouble() - (double) 0.5F) * (double) 64.0F;
            return this.teleport(d0, d1, d2);
        } else {
            return false;
        }
    }

    private boolean teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(x, y, z);

        while (blockpos$mutableblockpos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(blockpos$mutableblockpos).blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
        boolean flag = blockstate.blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            EntityTeleportEvent.EnderEntity event = EventHooks.onEnderTeleport(this, x, y, z);
            if (event.isCanceled()) {
                return false;
            } else {
                boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
                if (flag2 && !this.isSilent()) {
                    this.level().playSound((Player) null, this.xo, this.yo, this.zo, (SoundEvent) GaiaSounds.ENDER_DRAGON_GIRL_TELEPORT.get(), this.getSoundSource(), 1.0F, 1.0F);
                    this.playSound((SoundEvent) GaiaSounds.ENDER_DRAGON_GIRL_TELEPORT.get(), 1.0F, 1.0F);
                }

                return flag2;
            }
        } else {
            return false;
        }
    }

    boolean teleportTowards(Entity entity) {
        Vec3 vec3 = new Vec3(this.getX() - entity.getX(), this.getY((double) 0.5F) - entity.getEyeY(), this.getZ() - entity.getZ());
        vec3 = vec3.normalize();
        double d0 = (double) 16.0F;
        double d1 = this.getX() + (this.random.nextDouble() - (double) 0.5F) * (double) 8.0F - vec3.x * (double) 16.0F;
        double d2 = this.getY() + (double) (this.random.nextInt(16) - 8) - vec3.y * (double) 16.0F;
        double d3 = this.getZ() + (this.random.nextDouble() - (double) 0.5F) * (double) 8.0F - vec3.z * (double) 16.0F;
        return this.teleport(d1, d2, d3);
    }

    public boolean causeFallDamage(float distance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    protected void checkFallDamage(double p_27754_, boolean p_27755_, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    protected SoundEvent getAmbientSound() {
        return this.isScreaming() ? (SoundEvent) GaiaSounds.ENDER_DRAGON_GIRL_SCREAM.get() : GaiaRegistry.ENDER_DRAGON_GIRL.getSay();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return GaiaRegistry.ENDER_DRAGON_GIRL.getHurt();
    }

    protected SoundEvent getDeathSound() {
        return GaiaRegistry.ENDER_DRAGON_GIRL.getDeath();
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        OwnerHelper.serializeOwner(tag, this.summonerUUID);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.summonerUUID = OwnerHelper.deserializeOwner(tag);
    }

    public boolean fireImmune() {
        return true;
    }

    public int getMaxSpawnClusterSize() {
        return 2;
    }

    static class TeleportAttackGoal extends Goal {
        private final SummonedEnderDragonGirl mob;
        private LivingEntity target;
        private int teleportCooldown;

        public TeleportAttackGoal(SummonedEnderDragonGirl mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity t = this.mob.getTarget();
            if (t == null || !t.isAlive()) return false;
            this.target = t;
            return true;
        }

        @Override
        public void tick() {
            if (mob.isBreathing()) {
                return; // 正在喷射龙息时不传送
            }
            if (this.target == null) return;

            double distSqr = this.mob.distanceToSqr(this.target);

            // 如果距离太远，尝试传送靠近
            if (distSqr > 256.0D && --teleportCooldown <= 0) {
                if (this.mob.teleportTowards(this.target)) {
                    teleportCooldown = this.adjustedTickDelay(160); // 8秒冷却
                }
            }

            // 如果距离太近，偶尔随机传送
            else if (distSqr < 16.0D && this.mob.getRandom().nextInt(100) < 1) {
                //要不加个回响打击
                //if(this.mob.getRandom().nextDouble() < 0.33)
                //    this.mob.addEffect();
                this.mob.teleportRandomly();
                if(this.mob.getHealth() < this.mob.getMaxHealth() * 0.33F)
                    this.mob.addEffect(new MobEffectInstance(TRUE_INVISIBILITY, 40, 0,false,false));
                teleportCooldown = this.adjustedTickDelay(160);
            }

            // 面向目标
            this.mob.getLookControl().setLookAt(this.target, 10.0F, 10.0F);
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.target.isAlive() && !this.mob.isPassenger();
        }

        @Override
        public void stop() {
            this.target = null;
        }
    }

    static class DragonBreathAttackGoal extends Goal {
        private final SummonedEnderDragonGirl mob;
        private LivingEntity target;
        private int breathTick; // 喷射持续计时
        private int cooldown;   // 冷却计时

        public DragonBreathAttackGoal(SummonedEnderDragonGirl mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (cooldown > 0) {
                cooldown--;
                return false;
            }

            LivingEntity t = mob.getTarget();
            if (t == null || !t.isAlive()) return false;

            double distSqr = mob.distanceToSqr(t);
            if (distSqr < 49.0D) {
                this.target = t;
                return true;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return breathTick > 0 && target != null && target.isAlive();
        }

        @Override
        public void start() {
            breathTick = 60; // 3秒
            mob.setBreathing(true);

            // 喷前先朝向目标
            mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            mob.lookAt(target, 30.0F, 30.0F);

            // 稍等再喷，确保方向同步
            mob.doBreathAttack();
        }

        @Override
        public void stop() {
            mob.setBreathing(false);
            mob.stopBreathAttack();
            cooldown = 80; // 4s 冷却
            target = null;
            breathTick = 0;
        }

        @Override
        public void tick() {
            if (target == null) return;

            // 每tick朝向目标
            mob.getLookControl().setLookAt(target, 15.0F, 15.0F);
            mob.lookAt(target, 15.0F, 15.0F);

            double distSqr = mob.distanceToSqr(target);
            if (distSqr > 49.0) {
                stop();
                return;
            }

            // 每10tick更新一次
            if (breathTick % 10 == 0) {
                mob.doBreathAttack();
            }

            breathTick--;
            if (breathTick <= 0) {
                stop();
            }
        }
    }


}
