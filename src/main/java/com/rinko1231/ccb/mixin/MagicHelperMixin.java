package com.rinko1231.ccb.mixin;

import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.ItemReg;
import com.rinko1231.ccb.utils.MyUtils;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import io.redspace.ironsspellbooks.network.ClientboundSyncCooldown;
import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MagicManager.class, remap = false)
public abstract class MagicHelperMixin {

    @Inject(method = "addCooldown", at = @At("HEAD"), cancellable = true)
    private void injectAddCooldown(ServerPlayer serverPlayer, AbstractSpell spell, CastSource castSource, CallbackInfo ci) {
        if (castSource == CastSource.SCROLL) return;

        if (!MyUtils.isEquipCurios(serverPlayer, ItemReg.ALL_BY_MOVEMENT.get())) return;

        String spellId = spell.getSpellId();
        boolean isStepSpell = CasterCuriosBonusConfig.allByMovementStepSpells.get().contains(spellId);

        if (isStepSpell) {
            int effectiveCooldown = MagicManager.getEffectiveSpellCooldown(spell, serverPlayer, castSource);
            int reduced = (int)(effectiveCooldown * 0.5); // 冷却减半

            MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
            PlayerCooldowns cooldowns = magicData.getPlayerCooldowns();
            cooldowns.addCooldown(spell, reduced);
            Messages.sendToPlayer(new ClientboundSyncCooldown(spell.getSpellId(), reduced), serverPlayer);

            // 阻止原方法执行
            ci.cancel();
        }
    }
}