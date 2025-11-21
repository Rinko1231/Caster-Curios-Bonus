package com.rinko1231.ccb.item.body;


import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;


public class AllByMovement extends SimpleDescriptiveCurio {

    public AllByMovement() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.RARE),"body");
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
    }


}
