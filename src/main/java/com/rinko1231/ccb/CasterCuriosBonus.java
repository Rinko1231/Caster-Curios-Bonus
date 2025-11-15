package com.rinko1231.ccb;



import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.*;

import io.redspace.ironsspellbooks.compat.Curios;
import net.minecraft.resources.ResourceLocation;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;


@Mod(CasterCuriosBonus.MOD_ID)
public class CasterCuriosBonus {
    public static final String MOD_ID = "caster_curios_bonus";
    public static final String MODID = "caster_curios_bonus"; //下划线很烦

    public CasterCuriosBonus(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON,  CasterCuriosBonusConfig.SPEC, "CasterCuriosBonusConfig.toml");
        //NeoForge.EVENT_BUS.register(this);
        ItemReg.ITEMS.register(modEventBus);
        TabInit.TABS.register(modEventBus);
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
    }

    //好用的ResourceLocation
    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    //饰品槽注册
    /*
    private void enqueueIMC(InterModEnqueueEvent event) {
        Curios.registerCurioSlot("charm", 1, false, (ResourceLocation) null);
        Curios.registerCurioSlot("hands", 1, false, (ResourceLocation) null);
        Curios.registerCurioSlot("head", 1, false, (ResourceLocation) null);
        Curios.registerCurioSlot("necklace", 2, false, (ResourceLocation) null);
        Curios.registerCurioSlot("body", 1, false, (ResourceLocation) null);
    }
    */

}
