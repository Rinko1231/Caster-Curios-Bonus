package com.rinko1231.ccb.item.head;

import com.rinko1231.ccb.capability.OverloadProvider;
import com.rinko1231.ccb.init.MobEffectReg;
import com.rinko1231.ccb.init.SpellReg;
import com.rinko1231.ccb.item.SpellCuriosItem;

import com.rinko1231.ccb.network.CCBMessages;
import com.rinko1231.ccb.network.data.OverloadClientData;
import com.rinko1231.ccb.network.data.OverloadSyncedData;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.compat.Curios;

import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;


@Mod.EventBusSubscriber(modid = MODID)
public class RecklessUtterance extends SpellCuriosItem  {
    public RecklessUtterance() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.EPIC),
                "head",
                new SpellDataRegistryHolder[]{
                        new SpellDataRegistryHolder(SpellReg.ARCANE_OVERDRIVE_SPELL, 1)
        });
    }

    @SubscribeEvent
    public static void onSpellSelectionInit(SpellSelectionManager.SpellSelectionEvent event) {
        Player player = event.getEntity();
        if (player == null) return;

        ItemStack curiosStack =  (ItemStack) CuriosApi.getCuriosHelper().findCurio(player, "head", 0).map(SlotResult::stack).orElse((ItemStack) null);
        if (curiosStack == null) return;
        if (!(curiosStack.getItem() instanceof RecklessUtterance)) {
            return;
        }

        ISpellContainer iSpellContainer = ISpellContainer.get(curiosStack);
        if (iSpellContainer == null) return;

        // 将饰品里的法术注入 SpellSelectionManager
        @NotNull List<SpellSlot> spells = iSpellContainer.getActiveSpells();
        for (int i = 0; i < spells.size(); i++) {
            SpellData spell = spells.get(i).spellData();

            String slotId = curiosStack.getItem().getDescriptionId() + "_curio";

            event.addSelectionOption(spell, slotId, i);
        }
    }

    @SubscribeEvent
    public static void inASilentWay(SpellPreCastEvent event) {
        event.getEntity().getCapability(OverloadProvider.CAPABILITY).ifPresent(data -> {
            if (data.getSilentTicks() > 0) {
                float secondsLeft = data.getSilentTicks() / 20f;

                Component message = Component.translatable(
                        "message.caster_curios_bonus.reckless_utterance.silent_cast_blocked",
                        String.format("%.1f", secondsLeft)
                ).withStyle(ChatFormatting.RED);

                event.getEntity().displayClientMessage(message, true);
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void JustCast(SpellOnCastEvent event)
    {
        if(event.getEntity().hasEffect(MobEffectReg.ARCANE_OVERDRIVE.get())){
            var manaAttr = event.getEntity().getAttribute(AttributeRegistry.MAX_MANA.get());
            if (manaAttr == null) return;
            double maxMana = manaAttr.getValue();
            int originalManaCost = event.getOriginalManaCost();
            int spellLevel = event.getSpellLevel();
            int addOverload = (int) ((originalManaCost/maxMana) *100D * (0.7 + 0.1 * spellLevel));
            event.setManaCost(0);
            event.getEntity().getCapability(OverloadProvider.CAPABILITY).ifPresent(data -> {
                data.addOverload(addOverload);
                data.sync((ServerPlayer) event.getEntity());
            });
        }
    }

}
