package com.rinko1231.ccb.item.body;


import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.ItemReg;
import com.rinko1231.ccb.utils.MyUtils;
import io.redspace.ironsspellbooks.api.events.SpellCooldownAddedEvent;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.SlotContext;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class AllByMovement extends SimpleDescriptiveCurio {

    public AllByMovement() {
        super(ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.RARE),"body");
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
    }

    @SubscribeEvent
    public static void onSpellCooldownAddedEvent(SpellCooldownAddedEvent.Pre event)
    {
        if (event.getCastSource() == CastSource.SCROLL) return;
        Player serverPlayer = event.getEntity();
        if (!MyUtils.isEquipCurios(serverPlayer, ItemReg.ALL_BY_MOVEMENT.get())) return;

        String spellId = event.getSpell().getSpellId();
        boolean isStepSpell = CasterCuriosBonusConfig.allByMovementStepSpells.get().contains(spellId);

        if (isStepSpell) {
            event.setEffectiveCooldown( (int) (event.getEffectiveCooldown() * 0.5) );
        }
    }
}
