package com.rinko1231.ccb.item.necklace;

import com.rinko1231.ccb.CasterCuriosBonus;
import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

@EventBusSubscriber(modid = MODID)
public class MindToMatter extends SimpleDescriptiveCurio {

    //private static final UUID HTM_MANA_TO_HEALTH_BONUS_UUID = UUID.fromString("caa6c35c-7b4c-4af5-99ce-9151a8fbb403");
    private int tickCounterHTM = 0;
    public MindToMatter() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.EPIC), Curios.NECKLACE_SLOT);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);

        if (!(slotContext.entity() instanceof net.minecraft.world.entity.player.Player player)) return;


        tickCounterHTM++;
        if (tickCounterHTM < 10) return; //每半秒刷新一次
        tickCounterHTM = 0;

        var healthAttr = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttr == null) return;

        // 为了实时更新，只能移除旧加成了（
        healthAttr.removeModifier(CasterCuriosBonus.id("Heart_to_Matter_hp_bonus"));

        var manaAttr = player.getAttribute(AttributeRegistry.MAX_MANA);
        if (manaAttr == null) return;

        double maxMana = manaAttr.getValue();

        double extraHealth = maxMana * CasterCuriosBonusConfig.mindToMatterHPMultiplier.get();

        if (extraHealth > 0) {
            AttributeModifier modifier = new AttributeModifier(
                    CasterCuriosBonus.id("Heart_to_Matter_hp_bonus"),
                    extraHealth,
                    AttributeModifier.Operation.ADD_VALUE
            );
            healthAttr.addTransientModifier(modifier);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        super.onUnequip(slotContext, newStack, stack);

        if (!(slotContext.entity() instanceof ServerPlayer player)) return;

        var healthAttr = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttr != null) {
            healthAttr.removeModifier(CasterCuriosBonus.id("Heart_to_Matter_hp_bonus"));
        }

        //移除加成后同步生命值
        double currentHealth = player.getHealth();
        double maxHealth = player.getMaxHealth();
        if (currentHealth > maxHealth) {
            player.setHealth((float) maxHealth);
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();


        if (!(stack.getItem() instanceof MindToMatter)) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        if (player == null) return;


        var manaAttr = player.getAttribute(AttributeRegistry.MAX_MANA);
        if (manaAttr == null) return;

        double maxMana = manaAttr.getValue();

        double extraHealth = maxMana * CasterCuriosBonusConfig.mindToMatterHPMultiplier.get();

        Component line = Component.translatable(
                "tooltip.item.caster_curios_bonus.mind_to_matter.health_bonus",
                String.format("%.1f", extraHealth)
        ).withStyle(ChatFormatting.AQUA);

        event.getToolTip().add(line);
    }
}