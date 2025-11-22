package com.rinko1231.ccb.item.head;


import com.rinko1231.ccb.CasterCuriosBonus;
import com.rinko1231.ccb.capability.OverloadClientData;
import com.rinko1231.ccb.capability.OverloadData;
import com.rinko1231.ccb.init.MobEffectReg;
import com.rinko1231.ccb.init.ModAttachments;
import com.rinko1231.ccb.init.SpellReg;
import com.rinko1231.ccb.item.SpellCuriosItem;


import com.rinko1231.ccb.network.data.SyncOverloadPacket;
import io.redspace.ironsspellbooks.api.events.SpellCooldownAddedEvent;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;

import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;


@EventBusSubscriber(modid = MODID)
public class RecklessUtterance extends SpellCuriosItem  {
    public RecklessUtterance() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.EPIC),
                "head",
                new SpellDataRegistryHolder[]{
                        new SpellDataRegistryHolder(SpellReg.ARCANE_OVERDRIVE_SPELL, 1)
        });
    }
/*

    @SubscribeEvent
    public static void onSpellSelectionInit(SpellSelectionManager.SpellSelectionEvent event) {
        Player player = event.getEntity();
        if (player == null) return;

        // 获取 Curios 库存
        CuriosApi.getCuriosInventory(player).ifPresent(inv -> {

            // 找所有佩戴的 RecklessUtterance 饰品
            var curios = inv.findCurios(itemStack ->
                    itemStack.getItem() instanceof RecklessUtterance
            );

            if (curios.isEmpty()) return;

            for (SlotResult slotResult : curios) {
                ItemStack curiosStack = slotResult.stack();
                if (curiosStack.isEmpty()) continue;

                ISpellContainer container = ISpellContainer.get(curiosStack);
                if (container == null) continue;

                List<SpellSlot> spells = container.getActiveSpells();
                if (spells.isEmpty()) continue;

                String slotId = slotResult.slotContext().identifier();

                for (int i = 0; i < spells.size(); i++) {
                    SpellData spell = spells.get(i).spellData();
                    event.addSelectionOption(spell, slotId, i);
                }
            }
        });
    }*/



    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        Player player = event.getEntity();
        if (player == null) return;

        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof RecklessUtterance)) {
            return;
        }

        int overload = OverloadClientData.getOverload();
        int silentTicks = OverloadClientData.getSilentTicks();
        float seconds = silentTicks / 20f;
            // 本地化 key: tooltip.item.caster_curios_bonus.reckless_utterance.overload
            Component messageOverload = Component.translatable(
                    "tooltip.item.caster_curios_bonus.reckless_utterance.overload",
                    overload
            ).withStyle(ChatFormatting.RED);
            event.getToolTip().add(messageOverload);

            Component messageSilent = Component.translatable(
                    "tooltip.item.caster_curios_bonus.reckless_utterance.silent",
                    String.format("%.1f", seconds)
            ).withStyle(ChatFormatting.DARK_RED);
            event.getToolTip().add(messageSilent);

    }

    @SubscribeEvent
    public static void inASilentWay(SpellPreCastEvent event) {
        OverloadData data = event.getEntity().getData(ModAttachments.OVERLOAD);
            if (data.getSilentTicks() > 0) {
                float secondsLeft = data.getSilentTicks() / 20f;

                // 使用本地化文本
                Component message = Component.translatable(
                        "message.caster_curios_bonus.reckless_utterance.silent_cast_blocked",
                        String.format("%.1f", secondsLeft)
                ).withStyle(ChatFormatting.RED);

                event.getEntity().displayClientMessage(message, true);
                event.setCanceled(true);
            }

    }

    @SubscribeEvent
    public static void JustCast(SpellOnCastEvent event)
    {
        if(event.getEntity().hasEffect(MobEffectReg.ARCANE_OVERDRIVE)){
            var manaAttr = event.getEntity().getAttribute(AttributeRegistry.MAX_MANA);
            if (manaAttr == null) return;
            double maxMana = manaAttr.getValue();
            int originalManaCost = event.getOriginalManaCost();
            int spellLevel = event.getSpellLevel();
            int addOverload = (int) ((originalManaCost/maxMana) *100D * (0.7 + 0.1 * spellLevel));
            event.setManaCost(0);
            OverloadData data = event.getEntity().getData(ModAttachments.OVERLOAD);
            data.addOverload(addOverload);
            OverloadData dataOverload = event.getEntity().getData(ModAttachments.OVERLOAD); // 示例
            PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new SyncOverloadPacket(dataOverload));
        }
    }

    @SubscribeEvent
    public static void onSpellCooldownAddedEvent(SpellCooldownAddedEvent.Pre event)
    {
        if (event.getCastSource() == CastSource.SCROLL) return;
        Player serverPlayer = event.getEntity();
        String spellId = event.getSpell().getSpellId();
        if(serverPlayer.hasEffect(MobEffectReg.ARCANE_OVERDRIVE) && !spellId.equals(CasterCuriosBonus.id("arcane_overdrive").toString())) {
            event.setEffectiveCooldown( 0 );
        }
    }
}
