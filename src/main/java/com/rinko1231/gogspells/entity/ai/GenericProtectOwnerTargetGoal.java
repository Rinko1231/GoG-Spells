package com.rinko1231.gogspells.entity.ai;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

public class GenericProtectOwnerTargetGoal extends TargetGoal {
    private final Supplier<Entity> owner;
    private int intervalToCheck;
    private final int maxIntensity = 100;
    private int currentIntensity;

    public GenericProtectOwnerTargetGoal(Mob entity, Supplier<Entity> getOwner) {
        super(entity, false);
        this.owner = getOwner;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        Object i = this.owner.get();
        if (i instanceof LivingEntity owner) {
            if (--this.intervalToCheck <= 0) {
                List<Mob> entities = owner.level().getEntitiesOfClass(Mob.class, owner.getBoundingBox().inflate((double)16.0F, (double)8.0F, (double)16.0F), (potentionalAggressor) -> {
                    boolean var10000;
                    if (potentionalAggressor.getTarget() != null) {
                        label22: {
                            if (!potentionalAggressor.getTarget().getUUID().equals(owner.getUUID())) {
                                LivingEntity patt0$temp = potentionalAggressor.getTarget();
                                if (!(patt0$temp instanceof MagicSummon)) {
                                    break label22;
                                }

                                MagicSummon summon = (MagicSummon)patt0$temp;
                                if (summon.getSummoner() == null || !summon.getSummoner().getUUID().equals(owner.getUUID())) {
                                    break label22;
                                }
                            }

                            if (Utils.hasLineOfSight(this.mob.level(), this.mob.getEyePosition(), potentionalAggressor.getEyePosition(), false)) {
                                var10000 = true;
                                return var10000;
                            }
                        }
                    }

                    var10000 = false;
                    return var10000;
                });
                if (entities.isEmpty()) {
                    this.currentIntensity = Math.max(0, this.currentIntensity - 10);
                    return false;
                } else {
                    this.mob.setTarget((LivingEntity)entities.stream().min(Comparator.comparingDouble((o) -> o.distanceToSqr(owner))).orElse((Mob)entities.get(0)));
                    return true;
                }
            } else {
                int timestamp = owner.getLastHurtByMobTimestamp();
                int tick = owner.tickCount;
                int combatIntervalModifier = (int) Mth.clamp((long)((tick - timestamp) / 5), 0, 200);
                int intensityModifier = 100 - this.currentIntensity;
                this.intervalToCheck = 20 + combatIntervalModifier + intensityModifier;
                return false;
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.currentIntensity = 100;
        super.start();
    }
}
