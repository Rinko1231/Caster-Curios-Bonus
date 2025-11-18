package com.rinko1231.ccb.item.hand;

import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.ItemReg;
import com.rinko1231.ccb.utils.MyUtils;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class GiantKiller extends SimpleDescriptiveCurio {


    public GiantKiller() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.RARE), "hands");
    }

    @SubscribeEvent
    public static void onSpellDamage(SpellDamageEvent event) {
        if (!(event.getSpellDamageSource().getEntity() instanceof ServerPlayer player)) return;

        boolean hasGiantKiller = MyUtils.isEquipCurios(
                player, ItemReg.GIANT_KILLER.get()
        );

        if (!hasGiantKiller) return;

        LivingEntity target = event.getEntity();

        if (target == null || target == player) return;

        double targetVolume = getBoxSize(target);
        double playerVolume = getBoxSize(player);

        if (playerVolume <= 0 || targetVolume <= 0) return;

        // 体积比
        double ratio = targetVolume / playerVolume;

        if (ratio > 1.0) {
            double bonus = (ratio - 1.0) * CasterCuriosBonusConfig.giantKillerDamageScaling.get();
            double maxMultiplier = CasterCuriosBonusConfig.giantKillerMaxMultiplier.get();
            double finalMultiplier = Math.min(1.0 + bonus, maxMultiplier);

            float newAmount = (float)(event.getAmount() * finalMultiplier);
            event.setAmount(newAmount);
        }
    }

    private static double getBoxSize(LivingEntity entity) {
        var bb = entity.getBoundingBox();
        double width = bb.getXsize();
        double height = bb.getYsize();
        double depth = bb.getZsize();
        return width * height * depth;
    }
}