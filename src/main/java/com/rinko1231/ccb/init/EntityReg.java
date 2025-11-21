package com.rinko1231.ccb.init;

import com.rinko1231.ccb.CasterCuriosBonus;
import com.rinko1231.ccb.entity.ZephyrBoosterEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

public class EntityReg {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<ZephyrBoosterEntity>> ZEPHYR_BOOSTER_ENTITY =
            ENTITIES.register("zephyr_booster_entity",
                    () -> EntityType.Builder.<ZephyrBoosterEntity>of(ZephyrBoosterEntity::new, MobCategory.MISC)
                            .sized(0.0F, 0.0F)
                            .setTrackingRange(64)
                            .fireImmune()
                            .noSummon()
                            .noSave()
                            .build(CasterCuriosBonus.id(
                                    "zephyr_booster_entity").toString())
            );
}