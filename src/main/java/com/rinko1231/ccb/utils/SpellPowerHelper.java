package com.rinko1231.ccb.utils;


import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class SpellPowerHelper {

    /**
     * 读取玩家的所有基础法强，减 1 后累加
     */
    public static double getTotalBaseSchoolExtraSpellPower(LivingEntity entity) {
        double total = 0;

        // 常规 Iron's Spellbooks 法强
        Attribute[] attrs = new Attribute[]{
                AttributeRegistry.FIRE_SPELL_POWER.get(),
                AttributeRegistry.ICE_SPELL_POWER.get(),
                AttributeRegistry.LIGHTNING_SPELL_POWER.get(),
                AttributeRegistry.BLOOD_SPELL_POWER.get(),
                AttributeRegistry.NATURE_SPELL_POWER.get(),
                AttributeRegistry.ENDER_SPELL_POWER.get(),
                AttributeRegistry.EVOCATION_SPELL_POWER.get(),
                AttributeRegistry.ELDRITCH_SPELL_POWER.get(),
                AttributeRegistry.HOLY_SPELL_POWER.get(),
                AttributeRegistry.SPELL_POWER.get()
        };

        for (Attribute attr : attrs) {
            AttributeInstance instance = entity.getAttribute(attr);
            if (instance != null) {
                total += instance.getValue() - 1; // 减掉默认值 1
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
            @SuppressWarnings("removal")//闭嘴
            ResourceLocation rl = new ResourceLocation(modId, attrName + "_spell_power");
            Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(rl);
            if (attr != null) {
                AttributeInstance instance = entity.getAttribute(attr);
                if (instance != null) {
                    total += instance.getValue() - 1; // 去掉默认值
                }
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