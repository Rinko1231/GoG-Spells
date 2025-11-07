package com.rinko1231.gogspells.entity.projectile;

import gaia.registry.GaiaRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

import static com.rinko1231.gogspells.init.EntityRegistry.MAGIC_WEB_PROJECTILE;
import static com.rinko1231.gogspells.init.NewSpellRegistry.SUMMON_ARACHNE;

public class MagicWebProjectile extends AbstractMagicProjectile implements ItemSupplier {
    public MagicWebProjectile(EntityType<? extends MagicWebProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public MagicWebProjectile(Level levelIn, LivingEntity shooter) {
        this(MAGIC_WEB_PROJECTILE.get(), levelIn);
        this.setOwner(shooter);
    }
    public ItemStack getItem() {
        return  new ItemStack((ItemLike) GaiaRegistry.PROJECTILE_WEB.get()) ;
    }

    public void tick() {
        super.tick();
        ProjectileUtil.rotateTowardsMovement(this, 0.2F);
        if (this.tickCount > 60) {
            this.discard();
        }

    }

    public float getSpeed() {
        return 0.50F;
    }


    public Optional<SoundEvent> getImpactSound() {
        return Optional.of((SoundEvent)SoundRegistry.ACID_ORB_IMPACT.get());
    }

    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.discard();
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity target = entityHitResult.getEntity();
        DamageSources.applyDamage(target, this.getDamage(), (SUMMON_ARACHNE.get()).getDamageSource(this, this.getOwner()));
        if(target instanceof LivingEntity livingEntity)
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));

        this.discard();
    }


    public void impactParticles(double x, double y, double z) {
        //MagicManager.spawnParticles(this.level(), ParticleTypes.LAVA, x, y, z, 5, 0.1, 0.1, 0.1, (double)0.25F, true);
    }

    public void trailParticles() {
        float yHeading = -((float)(Mth.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x) * (double)(180F / (float)Math.PI)) + 90.0F);
        float radius = 0.25F;
        int steps = 2;
        Vec3 vec = this.getDeltaMovement();
        double x2 = this.getX();
        double x1 = x2 - vec.x;
        double y2 = this.getY();
        double y1 = y2 - vec.y;
        double z2 = this.getZ();
        double z1 = z2 - vec.z;

        for(int j = 0; j < steps; ++j) {
            float offset = 1.0F / (float)steps * (float)j;
            double radians = (double)(((float)this.tickCount + offset) / 7.5F * 360.0F * ((float)Math.PI / 180F));
            Vec3 swirl = (new Vec3(Math.cos(radians) * (double)radius, Math.sin(radians) * (double)radius, (double)0.0F)).yRot(yHeading * ((float)Math.PI / 180F));
            double x = Mth.lerp((double)offset, x1, x2) + swirl.x;
            double y = Mth.lerp((double)offset, y1, y2) + swirl.y + (double)(this.getBbHeight() / 2.0F);
            double z = Mth.lerp((double)offset, z1, z2) + swirl.z;
            Vec3 jitter = Vec3.ZERO;
            this.level().addParticle(ParticleTypes.SPIT, x, y, z, jitter.x, jitter.y, jitter.z);
        }

    }
}
