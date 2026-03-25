package com.rinko1231.ccb.init;

import com.rinko1231.ccb.CasterCuriosBonus;
import com.rinko1231.ccb.entity.ZephyrBoosterEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

public class EntityReg {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<ZephyrBoosterEntity>> ZEPHYR_BOOSTER_ENTITY =
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