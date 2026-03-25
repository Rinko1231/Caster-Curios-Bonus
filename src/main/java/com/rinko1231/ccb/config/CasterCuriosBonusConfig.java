package com.rinko1231.ccb.config;


import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CasterCuriosBonusConfig {

    public static ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // ==============================
    // Eureka
    // ==============================
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> EurekaExtraSchools;
    public static ForgeConfigSpec.DoubleValue EurekaExtraCDMultiplier;

    // ==============================
    // Mind to Matter
    // ==============================
    public static ForgeConfigSpec.DoubleValue mindToMatterHPMultiplier;

    // ==============================
    // Voidsnare
    // ==============================
    public static ForgeConfigSpec.DoubleValue VoidsnareMinDropTeleportingRadius;
    public static ForgeConfigSpec.BooleanValue VoidsnareSpellDamageIsNecessary;

    // ==============================
    // calamity resonator
    // ==============================
    public static ForgeConfigSpec.DoubleValue calamityResonatorSplashPercent;
    public static ForgeConfigSpec.DoubleValue calamityResonatorSplashRadius;
    public static ForgeConfigSpec.IntValue calamityResonatorCooldownTicks;

    // ==============================
    // Mana Overflow Settings
    // ==============================
    public static ForgeConfigSpec.DoubleValue manaOverflowExtraCostMultiplier;
    public static ForgeConfigSpec.DoubleValue manaOverflowExtraHealMultiplier;
    public static ForgeConfigSpec.DoubleValue manaOverflowExtraDamageMultiplier;
    public static ForgeConfigSpec.IntValue manaOverflowBonusSpellLevel;

    // ==============================
    // Crimson Pledge
    // ==============================
    public static ForgeConfigSpec.DoubleValue crimsonPledgeHealSharePercent;
    public static ForgeConfigSpec.DoubleValue crimsonPledgeHealRadius;

    // ==============================
    // Giant Killer
    // ==============================
    public static ForgeConfigSpec.DoubleValue giantKillerDamageScaling;
    public static ForgeConfigSpec.DoubleValue giantKillerMaxMultiplier;

    // ==============================
    // Venomous Assault
    // ==============================
    public static ForgeConfigSpec.DoubleValue venomousAssaultDamageMaxHealthPercentLimit;
    public static ForgeConfigSpec.DoubleValue venomousAssaultDamageValueLimit;

    // ==============================
    // Reckless Utterance
    // ==============================
    public static ForgeConfigSpec.IntValue recklessUtteranceMaxOverload;
    public static ForgeConfigSpec.IntValue recklessUtteranceSilentTicks;
    public static ForgeConfigSpec.IntValue recklessUtteranceArcaneOverdriveDurationTicks;

    // ==============================
    // All By Movement
    // ==============================
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> allByMovementStepSpells;

    static {
        BUILDER.push("Eureka");

        // -------- Eureka --------
        BUILDER.comment("List of additional spell school attributes (format: modid:school)");
        EurekaExtraSchools = BUILDER
                .defineList("Extra Schools", List.of("traveloptics:aqua"),
                        element -> element instanceof String);
        EurekaExtraCDMultiplier = BUILDER
                .comment("Multiplier for Extra CD from additional spell power")
                .defineInRange("EurekaExtraCDMultiplier", 0.33F, 0.0, 10.0);

        BUILDER.pop();

        // -------- Mind to Matter --------
        BUILDER.push("Mind to Matter Settings");
        mindToMatterHPMultiplier = BUILDER
                .comment("e.g. 0.015 means gaining 3 bonus HP when max mana is 200")
                .defineInRange("mindToMatterHPMultiplier", 0.015, 0.0, 1.0);


        BUILDER.pop();


        // -------- Voidsnare --------
        BUILDER.push("Voidsnare Settings");
        VoidsnareMinDropTeleportingRadius = BUILDER
                .defineInRange("VoidsnareMinDropTeleportingRadius", 5.0, 1.0, 128.0);
        VoidsnareSpellDamageIsNecessary = BUILDER
                .define("VoidsnareSpellDamageIsNecessary",true);

        BUILDER.pop();

        // -------- Calamity Resonator--------
        BUILDER.push("Calamity Resonator Settings");
        calamityResonatorSplashPercent = BUILDER
                .defineInRange("calamityResonatorSplashPercent", 0.25F, 0.0, 1.0);
        calamityResonatorSplashRadius = BUILDER
                .defineInRange("calamityResonatorSplashRadius", 1.5F, 0.01F, 32.0F);
        calamityResonatorCooldownTicks = BUILDER
                .defineInRange("calamityResonatorCooldownTicks", 40, 1, 1919810);


        BUILDER.pop();

        // -------- Reckless Utterance --------
        BUILDER.push("Reckless Utterance Settings");
        recklessUtteranceMaxOverload = BUILDER
                .defineInRange("recklessUtteranceMaxOverload", 200, 1, Integer.MAX_VALUE);
        recklessUtteranceSilentTicks = BUILDER
                .defineInRange("recklessUtteranceSilentTicks", 1200, 1, Integer.MAX_VALUE);
        recklessUtteranceArcaneOverdriveDurationTicks = BUILDER
                .defineInRange("recklessUtteranceArcaneOverdriveDurationTicks", 200, 1, 3000);


        BUILDER.pop();

        // -------- Venomous Assault --------
        BUILDER.push("Venomous Assault Settings");
        venomousAssaultDamageMaxHealthPercentLimit = BUILDER
                .comment("The maximum fraction of the target's max health that a single detonation may deal. "
                        + "For example, 0.5 means detonations against a 200-HP target can deal at most 100 damage.")
                .defineInRange("venomousAssaultDamageMaxHealthPercentLimit", 0.20, 0.0, 10.0);

        venomousAssaultDamageValueLimit = BUILDER
                .comment("A flat upper limit for the damage of a single detonation, applied after the max-health "
                        + "percentage cap. For example, a value of 70 means a single detonation can never exceed 70 damage, "
                        + "even against high-health targets.")
                .defineInRange("venomousAssaultDamageValueLimit", 114514, 20.0, Integer.MAX_VALUE);
        BUILDER.pop();

        // -------- Mana Overflow --------
        BUILDER.push("Mana Overflow");
        manaOverflowExtraCostMultiplier = BUILDER
                .comment("Extra mana cost multiplier")
                .defineInRange("Extra Mana Cost Multiplier", 1.5, 0.0, 10.0);

        manaOverflowExtraHealMultiplier = BUILDER
                .comment("Extra healing multiplier")
                .defineInRange("Extra Heal Multiplier", 1.3, 0.0, 10.0);

        manaOverflowExtraDamageMultiplier = BUILDER
                .comment("Extra damage multiplier")
                .defineInRange("Extra Damage Multiplier", 1.3, 0.0, 10.0);

        manaOverflowBonusSpellLevel = BUILDER
                .comment("Additional spell level from Mana Overflow (default = +1)")
                .defineInRange("Bonus Spell Level", 1, 0, 10);

        BUILDER.pop();

        // -------- Crimson Pledge --------
        BUILDER.push("Crimson Pledge Settings");
        crimsonPledgeHealSharePercent = BUILDER
                .comment("Percentage of lifesteal healing shared to nearby allies (e.g. 0.5 = 50%)")
                .defineInRange("Shared Heal Percent", 0.314, 0.0, 1.0);

        crimsonPledgeHealRadius = BUILDER
                .comment("Radius (in blocks) for sharing lifesteal healing")
                .defineInRange("Heal Share Radius", 5.0, 1.0, 64.0);

        BUILDER.pop();

        // -------- Giant Killer --------
        BUILDER.push("Giant Killer Settings");
        giantKillerDamageScaling = BUILDER
                .comment("e.g. 0.25 means dealing 1.5x damage to the enemy whose size is twice of you")
                .defineInRange("giantKillerDamageScaling", 0.25, 0.0, 1.0);
        giantKillerMaxMultiplier = BUILDER
                .comment("Maximum final damage multiplier applied by Giant Killer (e.g., 3.0 = up to 3x damage).")
                .defineInRange("giantKillerMaxMultiplier", 2.0, 1.0, 100.0);


        BUILDER.pop();

        // -------- All By Movement --------
        BUILDER.push("All By Movement Settings");
        allByMovementStepSpells = BUILDER
                .comment("List of step-like spells affected by All By Movement (format: modid:spell_id)")
                .defineList("Step Spells",
                        List.of(
                                "irons_spellbooks:frost_step",
                                "irons_spellbooks:blood_step",
                                "irons_spellbooks:burning_dash"
                        ),
                        element -> element instanceof String);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static Map<String, String> getExtraSchoolMap() {
        Map<String, String> map = new HashMap<>();
        for (String s : EurekaExtraSchools.get()) {
            if (s.contains(":")) {
                String[] split = s.split(":", 2);
                map.put(split[0].trim(), split[1].trim());
            }
        }
        return map;
    }
}
