package com.rinko1231.ccb.item.ring;

import com.rinko1231.ccb.init.ItemReg;
import com.rinko1231.ccb.utils.MyUtils;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;


import static com.rinko1231.ccb.CasterCuriosBonus.MODID;


@EventBusSubscriber(modid = MODID)
public class NightHunter extends SimpleDescriptiveCurio {

    public NightHunter() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.RARE), "ring");
    }


    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        boolean hasNightHunter = MyUtils.isEquipCurios(
                player, ItemReg.NIGHT_HUNTER.get()
        );
        if (!hasNightHunter) return;

        if (event.getEntity() == null || event.getEntity() == player) return;

        player.addEffect(new MobEffectInstance(MobEffectRegistry.TRUE_INVISIBILITY, 5 * 20, 0, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10 * 20, 0, false, true, true));

    }
}