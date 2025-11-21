package com.rinko1231.ccb;



import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.*;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static io.redspace.ironsspellbooks.IronsSpellbooks.LOGGER;


@Mod(CasterCuriosBonus.MOD_ID)
public class CasterCuriosBonus {
    public static final String MOD_ID = "caster_curios_bonus";
    public static final String MODID = "caster_curios_bonus"; //下划线很烦

    public CasterCuriosBonus(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON,  CasterCuriosBonusConfig.SPEC, "CasterCuriosBonusConfig.toml");
        //NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addPackFinders);
        ItemReg.ITEMS.register(modEventBus);
        TabInit.TABS.register(modEventBus);
        SpellReg.SPELLS.register(modEventBus);
        EntityReg.ENTITIES.register(modEventBus);
        MobEffectReg.register(modEventBus);
        ModAttachments.register(modEventBus);
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
    }

    //好用的ResourceLocation
    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public void addPackFinders(AddPackFindersEvent event) {
        LOGGER.debug("addPackFinders");

        try {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                addBuiltinPack(event, "legacy_ccb_item_texture", Component.literal("Legacy CCB Item Texture"));
            }
        } catch (IOException var3) {
            LOGGER.error("Failed to load a builtin resource pack! If you are seeing this message, please report an issue to the author");
        }

    }

    private static void addBuiltinPack(AddPackFindersEvent event, String filename, Component displayName) throws IOException {
        filename = "builtin_resource_packs/" + filename;
        String id = "builtin/" + filename;
        Path resourcePath = ModList.get().getModFileById(MODID).getFile().findResource(new String[]{filename});
        Pack pack = Pack.readMetaAndCreate(new PackLocationInfo(id, displayName, PackSource.BUILT_IN, Optional.empty()), BuiltInPackSource.fromName((path) -> new PathPackResources(path, resourcePath)), PackType.CLIENT_RESOURCES, new PackSelectionConfig(false, Pack.Position.TOP, false));
        event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
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
