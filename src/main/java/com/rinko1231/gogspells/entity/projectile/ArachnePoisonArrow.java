package com.rinko1231.gogspells.entity.projectile;

import com.rinko1231.gogspells.init.EntityRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.poison_arrow.PoisonArrow;

import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class ArachnePoisonArrow extends PoisonArrow {
    private static final EntityDataAccessor<Boolean> IN_GROUND;
    public int shakeTime;
    protected boolean hasEmittedPoison;
    protected boolean inGround;
    protected float aoeDamage;

    public ArachnePoisonArrow(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ArachnePoisonArrow(Level levelIn, LivingEntity shooter) {
        this(EntityRegistry.ARACHNE_POISON_ARROW.get(), levelIn);
        this.setOwner(shooter);
    }

    public void tick() {
        if (this.shakeTime > 0) {
            --this.shakeTime;
        }

        if (!this.inGround) {
            super.tick();
        } else {
            if (this.tickCount > 300) {
                this.discard();
                return;
            }

            if (this.shouldFall()) {
                this.inGround = false;
                this.setDeltaMovement(this.getDeltaMovement().normalize().scale((double)0.05F));
            }
        }

    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(IN_GROUND, false);
    }


    public void setAoeDamage(float damage) {
        this.aoeDamage = damage;
    }

    public float getAoeDamage() {
        return this.aoeDamage;
    }

    private boolean shouldFall() {
        return this.inGround && this.level().noCollision((new AABB(this.position(), this.position())).inflate(0.06));
    }

    protected void onHitBlock(BlockHitResult pResult) {
        BlockState blockstate = this.level().getBlockState(pResult.getBlockPos());
        blockstate.onProjectileHit(this.level(), blockstate, pResult, this);

        Vec3 vec3 = pResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale((double)0.05F);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.shakeTime = 7;
        if (!this.level().isClientSide && !this.hasEmittedPoison) {
            this.createPoisonCloud(pResult.getLocation());
        }

    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!this.level().isClientSide) {
            Entity entity = entityHitResult.getEntity();
            boolean hit = DamageSources.applyDamage(entity, this.getDamage(), ((AbstractSpell) SpellRegistry.POISON_ARROW_SPELL.get()).getDamageSource(this, this.getOwner()));
            boolean ignore = entity.getType() == EntityType.ENDERMAN;
            if (hit) {
                if (!ignore) {
                    if (!this.level().isClientSide && !this.hasEmittedPoison) {
                        this.createPoisonCloud(entity.position());
                    }

                    if (entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity)entity;
                        livingEntity.setArrowCount(livingEntity.getArrowCount() + 1);
                    }
                }

                this.discard();
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(-0.1));
                this.setYRot(this.getYRot() + 180.0F);
                this.yRotO += 180.0F;
            }

        }
    }

    public void createPoisonCloud(Vec3 location) {
        if (!this.level().isClientSide) {
            ArachnePoisonCloud cloud = new ArachnePoisonCloud(this.level());
            cloud.setOwner(this.getOwner());
            cloud.setDuration(200);
            cloud.setDamage(this.aoeDamage);
            cloud.moveTo(location);
            this.level().addFreshEntity(cloud);
            this.hasEmittedPoison = true;
        }

    }

    public void trailParticles() {
        Vec3 vec3 = this.position().subtract(this.getDeltaMovement().scale((double)2.0F));
        this.level().addParticle(ParticleHelper.ACID, vec3.x, vec3.y, vec3.z, (double)0.0F, (double)0.0F, (double)0.0F);
    }

    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level(), ParticleHelper.ACID, x, y, z, 15, 0.03, 0.03, 0.03, 0.2, true);
    }

    public float getSpeed() {
        return 2.5F;
    }

    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    static {
        IN_GROUND = SynchedEntityData.defineId(ArachnePoisonArrow.class, EntityDataSerializers.BOOLEAN);
    }
}
