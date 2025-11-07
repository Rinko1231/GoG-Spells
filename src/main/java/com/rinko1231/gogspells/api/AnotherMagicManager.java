package com.rinko1231.gogspells.api;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnotherMagicManager {
    private final List<MagicSpell> spells = new ArrayList<>();
    private final Random random = new Random();

    public void addSpell(MagicSpell spell) {
        spells.removeIf(s -> s.getId().equals(spell.getId()));
        spells.add(spell);
    }

    public void tick() {
        spells.forEach(MagicSpell::tickCooldown);
    }

    public @Nullable MagicSpell getRandomAvailableSpell() {
        List<MagicSpell> available = spells.stream()
                .filter(MagicSpell::isReady)
                .toList();
        if (available.isEmpty()) return null;

        int totalWeight = available.stream().mapToInt(MagicSpell::getWeight).sum();
        int r = random.nextInt(totalWeight);
        for (MagicSpell spell : available) {
            r -= spell.getWeight();
            if (r < 0) return spell;
        }
        return available.get(available.size() - 1);
    }
}
