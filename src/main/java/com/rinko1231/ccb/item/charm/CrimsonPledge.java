package com.rinko1231.ccb.item.charm;

import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.ItemReg;
import com.rinko1231.ccb.utils.MyUtils;
import net.minecraft.world.entity.Entity;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;


import java.util.List;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;
import static io.redspace.ironsspellbooks.damage.DamageSources.isFriendlyFireBetween;

@EventBusSubscriber(modid = MODID)
public class CrimsonPledge extends SimpleDescriptiveCurio {


    public CrimsonPledge() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.RARE), "charm");
    }

   @SubscribeEvent
    public static void onSpellLifestealShare(LivingDamageEvent.Post event) {
        DamageSource source = event.getSource();
        if (!(source instanceof SpellDamageSource spellSource)) return;
        if (spellSource.getLifestealPercent() <= 0) return;

        Entity attackerEntity = source.getEntity();
        if (!(attackerEntity instanceof LivingEntity attacker)) return;

        if (!(attacker instanceof ServerPlayer player)) return;

        boolean hasCrimsonPledge = MyUtils.isEquipCurios(
                player, ItemReg.CRIMSON_PLEDGE.get()
        );
        if (!hasCrimsonPledge) return;

        float lifestealAmount = spellSource.getLifestealPercent() * event.getOriginalDamage();
        if (lifestealAmount <= 0) return;


        double radius = CasterCuriosBonusConfig.crimsonPledgeHealRadius.get();
        List<LivingEntity> allies = player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(radius),
                e -> e != player && isFriendlyFireBetween(player, e)
        );

        for (LivingEntity ally : allies) {
            ally.heal(lifestealAmount * CasterCuriosBonusConfig.crimsonPledgeHealSharePercent.get().floatValue());
            //player.level().broadcastEntityEvent(ally, (byte) 7);
        }
    }
}