package com.rinko1231.ccb.init;

import com.rinko1231.ccb.spell.ArcaneOverdriveSpell;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

public class SpellReg {
    public static final DeferredRegister<AbstractSpell> SPELLS =
            DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, MODID);

    public static final RegistryObject<AbstractSpell> ARCANE_OVERDRIVE_SPELL =
            SPELLS.register("arcane_overdrive", ArcaneOverdriveSpell::new);
}
