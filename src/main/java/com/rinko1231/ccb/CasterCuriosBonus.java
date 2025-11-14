package com.rinko1231.ccb;



import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.*;

import io.redspace.ironsspellbooks.compat.Curios;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("removal")//闭嘴
@Mod(CasterCuriosBonus.MOD_ID)
public class CasterCuriosBonus {
    public static final String MOD_ID = "caster_curios_bonus";
    public static final String MODID = "caster_curios_bonus"; //下划线很烦

    public CasterCuriosBonus() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CasterCuriosBonusConfig.SPEC, "CasterCuriosBonusConfig.toml");
        MinecraftForge.EVENT_BUS.register(this);
        ItemReg.ITEMS.register(modEventBus);
        TabInit.TABS.register(modEventBus);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
    }

    //好用的ResourceLocation
    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    //饰品槽注册
    private void enqueueIMC(InterModEnqueueEvent event) {
        Curios.registerCurioSlot("charm", 1, false, (ResourceLocation) null);
        Curios.registerCurioSlot("hands", 1, false, (ResourceLocation) null);
        Curios.registerCurioSlot("head", 1, false, (ResourceLocation) null);
        Curios.registerCurioSlot("necklace", 2, false, (ResourceLocation) null);
        Curios.registerCurioSlot("body", 1, false, (ResourceLocation) null);
    }

}
