package com.rinko1231.gogspells.entity;

import com.rinko1231.gogspells.init.NewSpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import java.util.UUID;

import static com.rinko1231.gogspells.init.EntityRegistry.SUMMONED_CAVE_SPIDER;

public class SummonedCaveSpider extends CaveSpider implements IMagicSummon {
    private static final int MAX_LIFE = 3600;
    private int life;
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    public SummonedCaveSpider(EntityType<? extends CaveSpider> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 0;
    }
    @Override
    public boolean canAttackType(EntityType<?> type) {
        return true;
    }
    protected boolean shouldDropLoot() {
        return false;
    }


    public SummonedCaveSpider(Level pLevel, LivingEntity owner) {
        this(SUMMONED_CAVE_SPIDER.get(), pLevel);
        this.xpReward = 0;
        this.setSummoner(owner);
    }

    public float maxUpStep() {
        return 1.0F;
    }

    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(7, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(8, new SpiderAttackGoal(this));
        this.goalSelector.addGoal(6, new GenericFollowOwnerGoal(this, this::getSummoner, (double)0.9F, 15.0F, 5.0F, false, 25.0F));
        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == this.getSummoner())).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, new GenericProtectOwnerTargetGoal(this, this::getSummoner));
    }
    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner != null) {
            SummonManager.setOwner(this, owner);
        }
        if (owner != null) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    @Nullable
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(this.level(), this.cachedSummoner, this.summonerUUID);
    }
    @Override
    public boolean isPreventingPlayerRest(@NotNull Player player) {
        return !this.isAlliedTo(player);
    }


    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    public void onRemovedFromLevel() {
        this.onRemovedHelper(this);
        super.onRemovedFromLevel();
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {

        super.readAdditionalSaveData(compoundTag);
        this.summonerUUID = OwnerHelper.deserializeOwner(compoundTag);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {

        super.addAdditionalSaveData(compoundTag);
        OwnerHelper.serializeOwner(compoundTag, this.summonerUUID);

    }

    public boolean doHurtTarget(Entity entity) {
        if (Utils.doMeleeAttack(this, entity,
                ((AbstractSpell) NewSpellRegistry.SUMMON_ARACHNE.get())
                        .getDamageSource(this, this.getSummoner()))) {
            if (entity instanceof LivingEntity) {
                int i = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 7;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 15;
                }
                if (i > 0) {
                    ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.POISON, i * 20, 0), this);
                }
            }

            return true;
        } else {
            return false;
        }
    }
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (!this.isPersistenceRequired()) {
                ++this.life;
            }
            if (this.life >= MAX_LIFE) {
                this.discard();
            }
        }

    }

    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
    }

    public void onUnSummon() {
        if (!this.level().isClientSide) {
            MagicManager.spawnParticles(this.level(), ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 25, 0.4, 0.8, 0.4, 0.03, false);
            this.setRemoved(RemovalReason.DISCARDED);
        }

    }
    static class SpiderAttackGoal extends MeleeAttackGoal {
        public SpiderAttackGoal(Spider spider) {
            super(spider, (double)1.0F, true);
        }

        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return this.shouldIgnoreDamage(pSource) ? false : super.hurt(pSource, pAmount);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, (double)12.0F).add(Attributes.FOLLOW_RANGE, (double)20.0F).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.STEP_HEIGHT, (double)1.0F).add(Attributes.ATTACK_DAMAGE, (double)6.0F);
    }
}
