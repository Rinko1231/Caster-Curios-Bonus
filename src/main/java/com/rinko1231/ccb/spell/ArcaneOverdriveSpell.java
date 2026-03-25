package com.rinko1231.ccb.spell;

import com.rinko1231.ccb.CasterCuriosBonus;
import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.MobEffectReg;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;


@AutoSpellConfig
public class ArcaneOverdriveSpell extends AbstractSpell {
    private final ResourceLocation spellId = CasterCuriosBonus.id("arcane_overdrive");
    private final DefaultConfig defaultConfig;

    public ArcaneOverdriveSpell() {
        this.defaultConfig = (new DefaultConfig())
                .setMinRarity(SpellRarity.LEGENDARY)
                .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
                .setMaxLevel(1)
                .setAllowCrafting(false)
                .setCooldownSeconds((double) 300.0F)
                .build();
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 0;
    }

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.effect_length", new Object[]{Utils.timeFromTicks(10 * 20.0F, 1)}));
    }
    @Override
    public boolean requiresLearning() {
        return false;
    }

    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }

    @Override
    public boolean allowLooting() {
        return false;
    }

    public CastType getCastType() {
        return CastType.INSTANT;
    }

    private int getEffectDuration(int spellLevel, LivingEntity entity) {
        return CasterCuriosBonusConfig.recklessUtteranceArcaneOverdriveDurationTicks.get();
    }

    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MobEffectReg.ARCANE_OVERDRIVE.get(), this.getEffectDuration(spellLevel, entity), 0, false, true));
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
}
