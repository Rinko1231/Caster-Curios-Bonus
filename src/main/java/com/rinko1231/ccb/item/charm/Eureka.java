package com.rinko1231.ccb.item.charm;


import com.rinko1231.ccb.CasterCuriosBonus;
import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.utils.SpellPowerHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

//法强转cd
@EventBusSubscriber(modid = MODID)
public class Eureka extends SimpleDescriptiveCurio {

    //private static final UUID EUREKA_POWER_TO_CD_UUID = UUID.fromString("7e6a1e8a-0e8e-4f6b-8a5c-1b7c7f5947a5");
    private int tickCounter = 0;

    public Eureka() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.EPIC), "charm");
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);

        if (!(slotContext.entity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        tickCounter++;
        if (tickCounter < 20) return; //每秒刷新一次
        tickCounter = 0;

        var cdAttr = player.getAttribute(AttributeRegistry.COOLDOWN_REDUCTION);
        if (cdAttr == null) return;

        // 移除旧的 modifier
        cdAttr.removeModifier(CasterCuriosBonus.id("eureka_cd_bonus"));

        // 实时计算加成
        double powerBonus = SpellPowerHelper.getTotalExtraSpellPower(player);
        double extraCD = powerBonus * CasterCuriosBonusConfig.EurekaExtraCDMultiplier.get();

        if (extraCD > 0) {
            AttributeModifier modifier = new AttributeModifier(
                    CasterCuriosBonus.id("eureka_cd_bonus"),
                    extraCD,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            );
            cdAttr.addTransientModifier(modifier);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        super.onUnequip(slotContext, newStack, stack);

        if (!(slotContext.entity() instanceof ServerPlayer player)) return;

        var cdAttr = player.getAttribute(AttributeRegistry.COOLDOWN_REDUCTION);
        if (cdAttr != null) {
            cdAttr.removeModifier(CasterCuriosBonus.id("eureka_cd_bonus"));
        }
    }


    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        // 只对 Eureka 生效
        if (!(stack.getItem() instanceof Eureka)) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        if (player == null) return;


        double powerBonus = SpellPowerHelper.getTotalExtraSpellPower(player);
        double extraCD = powerBonus * CasterCuriosBonusConfig.EurekaExtraCDMultiplier.get();

        double percent = extraCD * 100.0;

        Component line = Component.translatable(
                "tooltip.item.caster_curios_bonus.eureka.cd_bonus",
                String.format("%.1f", percent)
        ).withStyle(ChatFormatting.AQUA);

        event.getToolTip().add(line);
    }
}