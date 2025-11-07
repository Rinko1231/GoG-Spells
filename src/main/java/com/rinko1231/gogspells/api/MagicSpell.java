package com.rinko1231.gogspells.api;

import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiConsumer;

public class MagicSpell {
    private final String id;
    private final int cooldownTicks;
    private final int weight;
    private final BiConsumer<LivingEntity, Float> action; // 执行逻辑
    private int cooldownRemaining;

    public MagicSpell(String id, int cooldownTicks, int weight, BiConsumer<LivingEntity, Float> action) {
        this.id = id;
        this.cooldownTicks = cooldownTicks;
        this.weight = weight;
        this.action = action;
    }

    public boolean isReady() {
        return cooldownRemaining <= 0;
    }

    public void cast(LivingEntity target, float distanceFactor) {
        if (action != null) {
            action.accept(target, distanceFactor);
            cooldownRemaining = cooldownTicks;
        }
    }

    public void tickCooldown() {
        if (cooldownRemaining > 0) cooldownRemaining--;
    }

    public int getWeight() {
        return weight;
    }

    public String getId() {
        return id;
    }
}
