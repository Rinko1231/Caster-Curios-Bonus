package com.rinko1231.ccb.item;

import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class SpellCuriosItem extends SimpleDescriptiveCurio implements IPresetSpellContainer {
    private List<SpellData> spellData = null;
    private SpellDataRegistryHolder[] spellDataRegistryHolders;

    public SpellCuriosItem(Item.Properties properties, String slotIdentifier, SpellDataRegistryHolder[] spellDataRegistryHolders) {
        super(properties, slotIdentifier);
        this.spellDataRegistryHolders = spellDataRegistryHolders;
    }


    public List<SpellData> getSpells() {
        if (this.spellData == null) {
            this.spellData = Arrays.stream(this.spellDataRegistryHolders)
                    .map(SpellDataRegistryHolder::getSpellData)
                    .toList();
            this.spellDataRegistryHolders = null;
        }
        return this.spellData;
    }

    public void initializeSpellContainer(ItemStack stack) {
        if (stack != null && !ISpellContainer.isSpellContainer(stack)) {
            List<SpellData> spells = this.getSpells();
            ISpellContainer spellContainer = ISpellContainer.create(spells.size(), true, false);
            spells.forEach(s -> spellContainer.addSpell(s.getSpell(), s.getLevel(), true, null));
            spellContainer.save(stack);
        }
    }
}
