package com.rinko1231.gogspells.entity.projectile;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import java.util.Optional;

import static com.rinko1231.gogspells.init.EntityRegistry.ARACHNE_POISON_CLOUD;

public class ArachnePoisonCloud extends AoeEntity {
    private DamageSource damageSource;

    public ArachnePoisonCloud(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ArachnePoisonCloud(Level level) {
        this(ARACHNE_POISON_CLOUD.get(), level);
    }

    public void applyEffect(LivingEntity target) {
        if (DamageSources.isFriendlyFireBetween(this.getOwner(),target)) return;
        if (this.damageSource == null) {
            this.damageSource = new DamageSource(DamageSources.getHolderFromResource(target, ISSDamageTypes.POISON_CLOUD), this, this.getOwner());
        }
        DamageSources.ignoreNextKnockback(target);
        target.hurt(this.damageSource, this.getDamage());
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 120, (int)this.getDamage()));
    }

    public float getParticleCount() {
        return 0.15F;
    }

    public Optional<ParticleOptions> getParticle() {
        return Optional.of(ParticleHelper.POISON_CLOUD);
    }
}
