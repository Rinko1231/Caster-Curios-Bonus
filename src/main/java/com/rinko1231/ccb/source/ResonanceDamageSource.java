package com.rinko1231.ccb.source;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.world.entity.Entity;

public class ResonanceDamageSource extends SpellDamageSource {

    private final boolean isResonance = true;

    public ResonanceDamageSource(Entity direct, Entity causing, AbstractSpell spell) {
        super(direct, causing, null, spell);
    }


    public boolean isResonance() {
        return isResonance;
    }
}
