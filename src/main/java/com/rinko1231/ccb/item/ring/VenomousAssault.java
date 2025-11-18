package com.rinko1231.ccb.item.ring;

import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.ItemReg;
import com.rinko1231.ccb.utils.MyUtils;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;


import static com.rinko1231.ccb.CasterCuriosBonus.MODID;
import static io.redspace.ironsspellbooks.api.registry.SchoolRegistry.NATURE;
import static net.minecraft.world.effect.MobEffects.POISON;

@EventBusSubscriber(modid = MODID)
public class VenomousAssault extends SimpleDescriptiveCurio {

    public VenomousAssault() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.EPIC), "ring");
    }

    @SubscribeEvent
    public static void onSpellDamage(SpellDamageEvent event) {
        SpellDamageSource spellSource = event.getSpellDamageSource();
        LivingEntity target = event.getEntity();
        Entity attackerEntity = spellSource.getEntity();
        if (!(attackerEntity instanceof LivingEntity attacker)) return;

        boolean hasCurio = MyUtils.isEquipCurios(attacker, ItemReg.VENOMOUS_ASSAULT.get());
        if (!hasCurio) return;

        AbstractSpell spell = spellSource.spell();
        if (spell == null) return;

        SchoolType school = spell.getSchoolType();
        if (school == NATURE.get()) return;

        MobEffectInstance poison = target.getEffect(POISON);
        if (poison == null) return;

        int poisonDuration = poison.getDuration();
        if (poisonDuration < 0) return;
        target.removeEffect(POISON);

        // 计算额外伤害
        float extraDamage = poisonDuration / 20.0f;
        float maxHealth = target.getMaxHealth();
        if (maxHealth > 20.0f) {
            extraDamage = Math.min(extraDamage, maxHealth * 0.2f);
            extraDamage = MyUtils.maxCap(CasterCuriosBonusConfig.venomousAssaultDamageValueLimit.get().floatValue(), extraDamage);
        } // 不超过20%生命值
        if (extraDamage <= 0) return;

        // 移除无敌帧
        target.invulnerableTime = 0;

        target.hurt(target.damageSources().indirectMagic(attacker, attacker), extraDamage);


        if (attacker.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR,
                    target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(),
                    8, 0.3, 0.5, 0.3, 0.1);
        }
    }
}
