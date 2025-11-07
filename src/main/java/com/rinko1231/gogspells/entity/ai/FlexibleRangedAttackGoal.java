package com.rinko1231.gogspells.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FlexibleRangedAttackGoal extends Goal {
        private final Mob mob;
        private final RangedAttackMob rangedAttackMob;
        @Nullable
        private LivingEntity target;
        private int attackTime = -1;
        private final double speedModifier;
        private int seeTime;
        private final int attackIntervalMin;
        private final int attackIntervalMax;
        private final float attackRadius;
        private final float attackRadiusSqr;

        //走位参数
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;

        public FlexibleRangedAttackGoal(RangedAttackMob rangedAttackMob, double speedModifier, int attackInterval, float attackRadius) {
            this(rangedAttackMob, speedModifier, attackInterval, attackInterval, attackRadius);
        }

        public FlexibleRangedAttackGoal(RangedAttackMob rangedAttackMob, double speedModifier, int attackIntervalMin, int attackIntervalMax, float attackRadius) {
            if (!(rangedAttackMob instanceof LivingEntity)) {
                throw new IllegalArgumentException("RangedAttackGoal requires Mob implements RangedAttackMob");
            } else {
                this.rangedAttackMob = rangedAttackMob;
                this.mob = (Mob) rangedAttackMob;
                this.speedModifier = speedModifier;
                this.attackIntervalMin = attackIntervalMin;
                this.attackIntervalMax = attackIntervalMax;
                this.attackRadius = attackRadius;
                this.attackRadiusSqr = attackRadius * attackRadius;
                this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            }
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                this.target = livingentity;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.target.isAlive() && (this.canUse() || !this.mob.getNavigation().isDone());
        }

        @Override
        public void stop() {
            this.target = null;
            this.seeTime = 0;
            this.attackTime = -1;
            this.strafingTime = -1;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.target == null) return;

            double d0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
            boolean canSee = this.mob.getSensing().hasLineOfSight(this.target);
            boolean seenRecently = this.seeTime > 0;

            if (canSee != seenRecently) {
                this.seeTime = 0;
            }

            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            // 当距离合适且持续看到目标时，停下并开始走位
            if (d0 <= (double) this.attackRadiusSqr && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.mob.getNavigation().moveTo(this.target, this.speedModifier);
                this.strafingTime = -1;
            }


            if (this.strafingTime >= 40) {
                if (this.mob.getRandom().nextFloat() < 0.3F) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if (this.mob.getRandom().nextFloat() < 0.3F) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }

            // 控制走位的具体移动
            if (this.strafingTime > -1) {
                boolean isFlying = mob.getMoveControl() instanceof FlyingMoveControl;
                if (isFlying) {
                    handleFlyingStrafe(target, d0);
                } else {
                    handleGroundStrafe(target, d0);
                }
            } else {
                this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            }

            // 攻击逻辑
            if (--this.attackTime == 0) {
                if (!canSee) {
                    return;
                }
                float f = (float) Math.sqrt(d0) / this.attackRadius;
                float f1 = Mth.clamp(f, 0.1F, 1.0F);
                this.rangedAttackMob.performRangedAttack(this.target, f1);
                this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
            } else if (this.attackTime < 0) {
                this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d0) / (double) this.attackRadius, (double) this.attackIntervalMin, (double) this.attackIntervalMax));
            }
        }


    private void handleFlyingStrafe(LivingEntity target, double distSqr) {
        Vec3 targetPos = target.position();
        double radius = Math.sqrt(this.attackRadiusSqr) * 0.8; // 飞行环绕半径
        double angleSpeed = 0.1; // 环绕角速度（越大越快）
        double heightOffset = 1.75 + this.mob.getRandom().nextGaussian() * 0.25; // 随机高度波动

        // 根据当前 tick 计算环绕偏移
        double angle = (this.mob.tickCount * angleSpeed) * (this.strafingClockwise ? 1 : -1);
        double offsetX = Math.cos(angle) * radius;
        double offsetZ = Math.sin(angle) * radius;

        Vec3 dest = new Vec3(targetPos.x + offsetX, targetPos.y + heightOffset, targetPos.z + offsetZ);
        this.mob.getNavigation().moveTo(dest.x, dest.y, dest.z, this.speedModifier);

        // 朝向目标
        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }
    private void handleGroundStrafe(LivingEntity target, double distSqr) {
        if (distSqr > (double)(this.attackRadiusSqr * 0.75F)) {
            this.strafingBackwards = false;
        } else if (distSqr < (double)(this.attackRadiusSqr * 0.25F)) {
            this.strafingBackwards = true;
        }

        float forward = this.strafingBackwards ? -0.5F : 0.5F;
        float sideways = this.strafingClockwise ? 0.5F : -0.5F;
        this.mob.getMoveControl().strafe(forward, sideways);
        this.mob.lookAt(target, 30.0F, 30.0F);
    }


    }
