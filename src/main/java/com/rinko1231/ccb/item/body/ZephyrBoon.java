package com.rinko1231.ccb.item.body;

import com.rinko1231.ccb.entity.ZephyrBoosterEntity;
import com.rinko1231.ccb.init.ItemReg;
import com.rinko1231.ccb.utils.MyUtils;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class ZephyrBoon extends SimpleDescriptiveCurio {

    public ZephyrBoon() {
        super(ItemPropertiesHelper
                .equipment().stacksTo(1)
                .rarity(Rarity.RARE),"body");
    }

    @SubscribeEvent
    public static void onSpellCastLetsFly(SpellOnCastEvent event)
    {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        if (!MyUtils.isEquipCurios(player, ItemReg.ZEPHYR_BOON.get())) return;

        if (player.isFallFlying()) {
            ZephyrBoosterEntity booster = new ZephyrBoosterEntity(player.level(), player);
            player.level().addFreshEntity(booster);
        }
    }

}

