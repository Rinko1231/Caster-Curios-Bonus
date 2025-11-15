package com.rinko1231.ccb.init;

import com.rinko1231.ccb.item.body.AllByMovement;
import com.rinko1231.ccb.item.charm.CrimsonPledge;
import com.rinko1231.ccb.item.charm.Eureka;
import com.rinko1231.ccb.item.hand.GiantKiller;
import com.rinko1231.ccb.item.head.ManaOverflow;
import com.rinko1231.ccb.item.necklace.MindToMatter;
import com.rinko1231.ccb.item.ring.NightHunter;
import com.rinko1231.ccb.item.ring.VenomousAssault;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.rinko1231.ccb.CasterCuriosBonus.MOD_ID;

public class ItemReg {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);

    public static final DeferredHolder<Item, Item> MIND_TO_MATTER = ITEMS.register("mind_to_matter",
            () -> new MindToMatter().withAttributes("necklace",
                    new AttributeContainer[]{
                            new AttributeContainer(AttributeRegistry.MANA_REGEN, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    })
    );
    public static final DeferredHolder<Item, Item> GIANT_KILLER = ITEMS.register("giant_killer",
            () -> new GiantKiller().withAttributes("hands",
                    new AttributeContainer[]{
                            new AttributeContainer(Attributes.ATTACK_DAMAGE, 1.5, AttributeModifier.Operation.ADD_VALUE),
                            new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    })
    );
    public static final DeferredHolder<Item, Item> NIGHT_HUNTER = ITEMS.register("night_hunter",
            () -> new NightHunter().withAttributes("ring",
                    new AttributeContainer[]{
                            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.08, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.ENDER_SPELL_POWER, 0.08, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    })
    );





    public static final DeferredHolder<Item, Item> MANA_OVERFLOW = ITEMS.register("mana_overflow",
            () -> new ManaOverflow().withAttributes("head",
                    new AttributeContainer[]{
                            new AttributeContainer(AttributeRegistry.CAST_TIME_REDUCTION, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    })
    );


    public static final DeferredHolder<Item, Item> ALL_BY_MOVEMENT = ITEMS.register("all_by_movement",
            () -> new AllByMovement().withAttributes("body",
                    new AttributeContainer[]{
                            new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.20, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    })
    );
    public static final DeferredHolder<Item, Item> EUREKA = ITEMS.register("eureka",
            () -> new Eureka().withAttributes("charm",
                    new AttributeContainer[]{
                            new AttributeContainer(AttributeRegistry.MANA_REGEN, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    })
    );

    public static final DeferredHolder<Item, Item> CRIMSON_PLEDGE = ITEMS.register("crimson_pledge",
            () -> new CrimsonPledge().withAttributes("charm",
                    new AttributeContainer[]{
                            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                            new AttributeContainer(Attributes.ARMOR, -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    })
    );


    public static final DeferredHolder<Item, Item> VENOMOUS_ASSAULT = ITEMS.register("venomous_assault",
            () -> new VenomousAssault().withAttributes("ring",
                    new AttributeContainer[]{
                            new AttributeContainer(AttributeRegistry.NATURE_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                            new AttributeContainer(AttributeRegistry.NATURE_MAGIC_RESIST, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    })
    );
}