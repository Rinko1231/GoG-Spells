package com.rinko1231.gogspells.entity.projectile;

import com.rinko1231.gogspells.init.EntityRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;

import io.redspace.ironsspellbooks.entity.spells.acid_orb.AcidOrb;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class ArachneAcidOrb extends AcidOrb {
    int rendLevel;
    int rendDuration;

    public ArachneAcidOrb(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ArachneAcidOrb(Level level, LivingEntity shooter) {
        this(EntityRegistry.ARACHNE_ACID_ORB.get(), level);
        this.setOwner(shooter);
    }

    public int getRendLevel() {
        return this.rendLevel;
    }

    public void setRendLevel(int rendLevel) {
        this.rendLevel = rendLevel;
    }

    public int getRendDuration() {
        return this.rendDuration;
    }

    public void setRendDuration(int rendDuration) {
        this.rendDuration = rendDuration;
    }

    public void trailParticles() {
        Vec3 vec3 = this.position().subtract(this.getDeltaMovement().scale((double)2.0F));
        this.level().addParticle(ParticleHelper.ACID, vec3.x, vec3.y, vec3.z, (double)0.0F, (double)0.0F, (double)0.0F);
    }

    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level(), ParticleHelper.ACID, x, y, z, 55, 0.08, 0.08, 0.08, 0.3, true);
        MagicManager.spawnParticles(this.level(), ParticleHelper.ACID_BUBBLE, x, y, z, 25, 0.08, 0.08, 0.08, 0.3, false);
    }

    public float getSpeed() {
        return 1.0F;
    }

    protected void onHit(HitResult hitresult) {
        //super super
        HitResult.Type hitresult$type = hitresult.getType();
        if (hitresult$type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)hitresult);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, hitresult.getLocation(), GameEvent.Context.of(this, (BlockState)null));
        } else if (hitresult$type == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult)hitresult;
            this.onHitBlock(blockhitresult);
            BlockPos blockpos = blockhitresult.getBlockPos();
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level().getBlockState(blockpos)));
        }

        //super
        if (!this.level().isClientSide) {
            this.impactParticles(this.getX(), this.getY(), this.getZ());
            this.getImpactSound().ifPresent(this::doImpactSound);
        }

        if (!this.level().isClientSide) {
            float explosionRadius = 2.5F;

            for(Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate((double)explosionRadius))) {
                double distance = entity.position().distanceTo(hitresult.getLocation());
                if (distance < (double)explosionRadius && Utils.hasLineOfSight(this.level(), hitresult.getLocation(), entity.getEyePosition(), true) && entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity)entity;
                    if (livingEntity != this.getOwner() ) {
                        this.applyEffect(livingEntity);
                    }
                }
            }

            this.discard();
        }

    }
    public void applyEffect(LivingEntity target) {
        if (DamageSources.isFriendlyFireBetween(this.getOwner(),target)) return;
        target.addEffect(new MobEffectInstance((MobEffect)MobEffectRegistry.REND.get(), this.getRendDuration(), this.getRendLevel()));
    }

    public Optional<SoundEvent> getImpactSound() {
        return Optional.of((SoundEvent)SoundRegistry.ACID_ORB_IMPACT.get());
    }

}
