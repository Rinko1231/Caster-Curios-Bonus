package com.rinko1231.ccb.item.hand;

import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.ItemReg;
import com.rinko1231.ccb.source.ResonanceDamageSource;
import com.rinko1231.ccb.utils.MyUtils;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.Vec3;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.joml.Vector3f;

import java.util.List;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

@EventBusSubscriber(modid = MODID)
public class CalamityResonator extends SimpleDescriptiveCurio {

    public CalamityResonator() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.RARE),
                "hands");
    }

    @SubscribeEvent
    public static void onSpellDamage(SpellDamageEvent event) {
        if (event.getEntity().level().isClientSide) return;
        SpellDamageSource spellSource = event.getSpellDamageSource();
        if (spellSource.spell() == null) return;
        LivingEntity target = event.getEntity();
        if (!(spellSource.getEntity() instanceof Player player)) return;

        Item resonator = ItemReg.CALAMITY_RESONATOR.get();

        if (!MyUtils.isEquipCurios(player, resonator)) return;

        if (player.getCooldowns().isOnCooldown(resonator)) return;

        if (spellSource instanceof ResonanceDamageSource resonance && resonance.isResonance()) {
            return;
        }
        Vec3 pos = target.position();
        float radius = CasterCuriosBonusConfig.calamityResonatorSplashRadius.get().floatValue();
        Vector3f color = spellSource.spell().getSchoolType().getTargetingColor();

        MyUtils.spawnParticlesWithRange(
                (ServerLevel) player.level(),
                new BlastwaveParticleOptions(
                        color,
                        radius * 2.0F
                ),
                pos.x, pos.y, pos.z,
                1, 0, 0, 0, 0, true, 32
        );

        float splashPercent = CasterCuriosBonusConfig.calamityResonatorSplashPercent.get().floatValue(); // 25%溅射
        float damage = event.getAmount() * splashPercent;

        List<LivingEntity> nearby = target.level().getEntitiesOfClass(
                LivingEntity.class,
                target.getBoundingBox().inflate(radius),
                e -> e != target && e.isAlive() && !DamageSources.isFriendlyFireBetween(player, e)
        );
        if (!nearby.isEmpty()) {
            ResonanceDamageSource resonanceSource =
                    new ResonanceDamageSource(player, player, spellSource.spell());
            for (LivingEntity other : nearby) {
                other.invulnerableTime=0;
                MyUtils.JustApplyDamageWithoutChecking(other, damage, resonanceSource);
            }
            player.getCooldowns().addCooldown(resonator, CasterCuriosBonusConfig.calamityResonatorCooldownTicks.get()); // 3s
        }
    }

}
