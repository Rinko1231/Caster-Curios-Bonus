package com.rinko1231.ccb.utils;


import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;


import java.util.Map;
import java.util.Optional;

public class SpellPowerHelper {

    /**
     * 读取玩家的所有基础法强，减 1 后累加
     */
    public static double getTotalBaseSchoolExtraSpellPower(LivingEntity entity) {
        double total = 0;

        Holder<Attribute>[] attrs = new Holder[]{
                AttributeRegistry.FIRE_SPELL_POWER,
                AttributeRegistry.ICE_SPELL_POWER,
                AttributeRegistry.LIGHTNING_SPELL_POWER,
                AttributeRegistry.BLOOD_SPELL_POWER,
                AttributeRegistry.NATURE_SPELL_POWER,
                AttributeRegistry.ENDER_SPELL_POWER,
                AttributeRegistry.EVOCATION_SPELL_POWER,
                AttributeRegistry.ELDRITCH_SPELL_POWER,
                AttributeRegistry.HOLY_SPELL_POWER,
                AttributeRegistry.SPELL_POWER
        };

        for (Holder<Attribute> holder : attrs) {
            AttributeInstance inst = entity.getAttribute(holder);
            if (inst != null) {
                total += inst.getValue() - inst.getBaseValue(); // 去掉默认值（一般是 1.0）
            }
        }

        return total;
    }


    /**
     * 读取额外 mod 的自定义法强
     * @param entity 玩家
     * @return 累加值
     */
    public static double getExtraSchoolExtraSpellPower(LivingEntity entity) {
        Map<String, String> extraMap = CasterCuriosBonusConfig.getExtraSchoolMap();
        double total = 0;

        for (Map.Entry<String, String> entry : extraMap.entrySet()) {
            String modId = entry.getKey();
            String attrName = entry.getValue();

            // 新版 ResourceLocation 仍然一样
            ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(modId, attrName + "_spell_power");

            // 关键：获取 Holder<Attribute>
            Optional<Holder.Reference<Attribute>> optHolder = BuiltInRegistries.ATTRIBUTE.getHolder(rl);
            if (optHolder.isEmpty()) continue;

            Holder<Attribute> holder = optHolder.get();

            // 1.21 的 getAttribute 接受 Holder<Attribute>
            AttributeInstance instance = entity.getAttribute(holder);
            if (instance != null) {
                total += instance.getValue() - instance.getBaseValue(); // 去掉默认值（1.0 或其他）
            }
        }

        return total;
    }
    /**
     * 获取玩家总法强 = 基础 + 配置额外
     */
    public static double getTotalExtraSpellPower(LivingEntity entity) {
        double base = getTotalBaseSchoolExtraSpellPower(entity);
        double extra = getExtraSchoolExtraSpellPower(entity);
        return base + extra;
    }
}