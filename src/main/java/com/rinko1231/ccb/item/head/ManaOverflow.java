package com.rinko1231.ccb.item.head;

import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.ItemReg;
import com.rinko1231.ccb.utils.MyUtils;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;

import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;


import static com.rinko1231.ccb.CasterCuriosBonus.MODID;


@EventBusSubscriber(modid = MODID)
public class ManaOverflow extends SimpleDescriptiveCurio {

    public ManaOverflow() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.RARE), "head");
    }

    @SubscribeEvent
    public static void onSpellCast(SpellOnCastEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!MyUtils.isEquipCurios(
                player, ItemReg.MANA_OVERFLOW.get()
        )) return;
        int baseCost = event.getOriginalManaCost();
        int newCost = (int) (baseCost * CasterCuriosBonusConfig.manaOverflowExtraCostMultiplier.get()); // +40%
        event.setManaCost(newCost);
    }

    @SubscribeEvent
    public static void onModifySpellLevel(ModifySpellLevelEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!MyUtils.isEquipCurios(
                player, ItemReg.MANA_OVERFLOW.get()
        )) return;

        event.addLevels(CasterCuriosBonusConfig.manaOverflowBonusSpellLevel.get());
    }


    @SubscribeEvent
    public static void onSpellDamage(SpellDamageEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!MyUtils.isEquipCurios(
                player, ItemReg.MANA_OVERFLOW.get()
        )) return;
        float base = event.getOriginalAmount();
        event.setAmount(base * CasterCuriosBonusConfig.manaOverflowExtraDamageMultiplier.get().floatValue());
    }


    @SubscribeEvent
    public static void onSpellHeal(SpellHealEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!MyUtils.isEquipCurios(
                player, ItemReg.MANA_OVERFLOW.get()
        )) return;
        float base = event.getHealAmount();
        event.getTargetEntity().heal(base * (CasterCuriosBonusConfig.manaOverflowExtraDamageMultiplier.get().floatValue()-1.0f)); // 额外+30%
    }
}
