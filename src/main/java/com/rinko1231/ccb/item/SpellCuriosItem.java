package com.rinko1231.ccb.item;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainerMutable;
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

    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack != null) {
            if (!ISpellContainer.isSpellContainer(itemStack)) {
                List<SpellData> spells = this.getSpells();
                ISpellContainerMutable spellContainer = ISpellContainer.create(spells.size(), true, false).mutableCopy();
                spells.forEach((spellData) -> spellContainer.addSpell(spellData.getSpell(), spellData.getLevel(), true));
                ISpellContainer.set(itemStack, spellContainer.toImmutable());
            }

        }
    }
}
