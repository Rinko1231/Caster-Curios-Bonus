package com.rinko1231.ccb.init;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.rinko1231.ccb.item.charm.CrimsonPledge;
import com.rinko1231.ccb.item.charm.Eureka;
import com.rinko1231.ccb.item.hand.GiantKiller;
import com.rinko1231.ccb.item.head.ManaOverflow;
import com.rinko1231.ccb.item.necklace.MindToMatter;
import com.rinko1231.ccb.item.body.AllByMovement;
import com.rinko1231.ccb.item.ring.NightHunter;
import com.rinko1231.ccb.item.ring.VenomousAssault;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

import static com.rinko1231.ccb.CasterCuriosBonus.MOD_ID;

public class ItemReg {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> MIND_TO_MATTER = ITEMS.register("mind_to_matter",
            () -> new MindToMatter() {
                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
                    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                    builder.put(AttributeRegistry.MANA_REGEN.get(),
                            new AttributeModifier(uuid, "mind_to_matter_mana_regen_bonus", 0.10F, AttributeModifier.Operation.MULTIPLY_BASE));
                    return builder.build();
                }


            });
    public static final RegistryObject<Item> GIANT_KILLER = ITEMS.register("giant_killer",
            () -> new GiantKiller() {
                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
                    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                    builder.put(Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(uuid, "giant_killer_atk_bonus", 1.5F, AttributeModifier.Operation.ADDITION));
                    builder.put(Attributes.MOVEMENT_SPEED,
                            new AttributeModifier(uuid, "giant_killer_spd_bonus", 0.1F, AttributeModifier.Operation.MULTIPLY_BASE));
                    return builder.build();
                }



            });
    public static final RegistryObject<Item> NIGHT_HUNTER = ITEMS.register("night_hunter",
            () -> new NightHunter() {
                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
                    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                    builder.put(AttributeRegistry.BLOOD_SPELL_POWER.get(),
                            new AttributeModifier(uuid, "night_hunter_blood_power_bonus", 0.08F, AttributeModifier.Operation.MULTIPLY_BASE));
                    builder.put(AttributeRegistry.ENDER_SPELL_POWER.get(),
                            new AttributeModifier(uuid, "night_hunter_ender_power_bonus", 0.08F, AttributeModifier.Operation.MULTIPLY_BASE));
                    return builder.build();
                }



            });
    public static final RegistryObject<Item> EUREKA = ITEMS.register("eureka",
            () -> new Eureka() {
                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
                    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                    builder.put(AttributeRegistry.MANA_REGEN.get(),
                            new AttributeModifier(uuid, "eureka_mana_regen_bonus", 0.10F, AttributeModifier.Operation.MULTIPLY_BASE));
                    return builder.build();
                }


            });
    public static final RegistryObject<Item> MANA_OVERFLOW = ITEMS.register("mana_overflow",
            () -> new ManaOverflow() {
                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
                    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                    builder.put(AttributeRegistry.CAST_TIME_REDUCTION.get(),
                            new AttributeModifier(uuid, "mana_overflow_cd_reduction", 0.10F, AttributeModifier.Operation.MULTIPLY_BASE));
                    builder.put(AttributeRegistry.SPELL_POWER.get(),
                            new AttributeModifier(uuid, "mana_overflow_sp", 0.10F, AttributeModifier.Operation.MULTIPLY_BASE));
                    return builder.build();
                }



            });
    public static final RegistryObject<Item> ALL_BY_MOVEMENT = ITEMS.register("all_by_movement",
            () -> new AllByMovement() {
                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
                    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                    builder.put(Attributes.MOVEMENT_SPEED,
                            new AttributeModifier(uuid, "all_by_movement_spd_bonus", 0.20F, AttributeModifier.Operation.MULTIPLY_TOTAL));
                    return builder.build();
                }



            });
    public static final RegistryObject<Item> CRIMSON_PLEDGE = ITEMS.register("crimson_pledge",
            () -> new CrimsonPledge() {
                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
                    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                    builder.put(AttributeRegistry.BLOOD_SPELL_POWER.get(),
                            new AttributeModifier(uuid, "crimson_pledge_blood_sp_bonus", 0.10F, AttributeModifier.Operation.MULTIPLY_TOTAL));
                    builder.put(Attributes.ARMOR,
                            new AttributeModifier(uuid, "crimson_pledge_armor_loss", -0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));
                    return builder.build();
                }



            });
    public static final RegistryObject<Item> VENOMOUS_ASSAULT = ITEMS.register("venomous_assault",
            () -> new VenomousAssault() {
                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
                    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                    builder.put(AttributeRegistry.NATURE_SPELL_POWER.get(),
                            new AttributeModifier(uuid, "venomous_assault_nature_power_bonus", 0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));
                    builder.put(AttributeRegistry.NATURE_MAGIC_RESIST.get(),
                            new AttributeModifier(uuid, "venomous_assault_nature_resist_bonus", 0.10F, AttributeModifier.Operation.MULTIPLY_BASE));
                    return builder.build();
                }


            });
}