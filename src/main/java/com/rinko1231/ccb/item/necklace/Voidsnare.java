package com.rinko1231.ccb.item.necklace;

import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.ItemReg;
import com.rinko1231.ccb.utils.MyUtils;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;


import java.util.Collection;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

@EventBusSubscriber(modid = MODID)
public class Voidsnare extends SimpleDescriptiveCurio {

    public Voidsnare() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.RARE),
                "necklace");
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity deadEntity = event.getEntity();
        if (deadEntity.level().isClientSide()) return;

        if (CasterCuriosBonusConfig.VoidsnareSpellDamageIsNecessary.get()) {
            if(!(event.getSource() instanceof SpellDamageSource)) return;
        }
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        if (!MyUtils.isEquipCurios(player, ItemReg.VOIDSNARE.get())) return;

        Level world = deadEntity.level();
        double deadX = deadEntity.getX();
        double deadY = deadEntity.getY();
        double deadZ = deadEntity.getZ();

        Collection<ItemEntity> drops = event.getDrops();
        if (drops.isEmpty()) return;

        for (ItemEntity drop : drops) {
            double distanceSq = drop.distanceToSqr(player);
            if (distanceSq > CasterCuriosBonusConfig.VoidsnareMinDropTeleportingRadius.get()*CasterCuriosBonusConfig.VoidsnareMinDropTeleportingRadius.get()) {
                MyUtils.spawnParticlesWithRange(
                        (ServerLevel) world,
                        ParticleHelper.UNSTABLE_ENDER,
                        deadX,
                        deadY + 0.5,
                        deadZ,
                        10,
                        0.2, 0.2, 0.2,
                        0.15,
                        false,
                        32
                );

                drop.setPos(player.getX(), player.getY() + 0.5, player.getZ());
                //drop.teleportTo(player.getX(), player.getY() + 0.5, player.getZ());
                //drop.setDeltaMovement(0, 0.1, 0);
            }
        }
    }
}