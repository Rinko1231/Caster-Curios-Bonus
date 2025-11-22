package com.rinko1231.ccb;



import com.mojang.logging.LogUtils;
import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.*;

import com.rinko1231.ccb.item.charm.Eureka;
import com.rinko1231.ccb.item.head.RecklessUtterance;
import com.rinko1231.ccb.item.necklace.MindToMatter;
import com.rinko1231.ccb.network.CCBMessages;

import com.rinko1231.ccb.network.data.OverloadClientData;
import com.rinko1231.ccb.network.data.OverloadSyncedData;
import com.rinko1231.ccb.utils.SpellPowerHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.compat.Curios;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

@SuppressWarnings("removal")//闭嘴
@Mod(CasterCuriosBonus.MOD_ID)
public class CasterCuriosBonus {
    public static final String MOD_ID = "caster_curios_bonus";
    public static final String MODID = "caster_curios_bonus"; //下划线很烦
    public static final Logger LOGGER = LogUtils.getLogger();
    public CasterCuriosBonus() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CasterCuriosBonusConfig.SPEC, "CasterCuriosBonusConfig.toml");
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addPackFinders);
        ItemReg.ITEMS.register(modEventBus);
        TabInit.TABS.register(modEventBus);
        SpellReg.SPELLS.register(modEventBus);
        EntityReg.ENTITIES.register(modEventBus);
        MobEffectReg.register(modEventBus);
        CCBMessages.register();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
    }

    //好用的ResourceLocation
    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation(MOD_ID, path);
    }
    public void init(FMLCommonSetupEvent event) {


    }
    //饰品槽注册
    private void enqueueIMC(InterModEnqueueEvent event) {
        Curios.registerCurioSlot("charm", 1, false, (ResourceLocation) null);
        Curios.registerCurioSlot("hands", 1, false, (ResourceLocation) null);
        Curios.registerCurioSlot("head", 1, false, (ResourceLocation) null);
        Curios.registerCurioSlot("necklace", 2, false, (ResourceLocation) null);
        Curios.registerCurioSlot("body", 1, false, (ResourceLocation) null);
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
        Pack pack = Pack.readMetaAndCreate(id, displayName, false, (path) -> new PathPackResources(path, true, resourcePath), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN);
        event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
    }


    @SubscribeEvent
    public void EurekaOnItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        // 只对 Eureka 生效
        if (!(stack.getItem() instanceof Eureka)) {
            return;
        }

        Player player = event.getEntity();
        if (player == null) return;


        double powerBonus = SpellPowerHelper.getTotalExtraSpellPower(player);
        double extraCD = powerBonus * CasterCuriosBonusConfig.EurekaExtraCDMultiplier.get();

        double percent = extraCD * 100.0;

        Component line = Component.translatable(
                "tooltip.item.caster_curios_bonus.eureka.cd_bonus",
                String.format("%.1f", percent)
        ).withStyle(ChatFormatting.AQUA);

        event.getToolTip().add(line);
    }
    @SubscribeEvent
    public void MindToMatterOnItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();


        if (!(stack.getItem() instanceof MindToMatter)) {
            return;
        }

        Player player = event.getEntity();
        if (player == null) return;


        var manaAttr = player.getAttribute(AttributeRegistry.MAX_MANA.get());
        if (manaAttr == null) return;

        double maxMana = manaAttr.getValue();

        double extraHealth = maxMana * CasterCuriosBonusConfig.mindToMatterHPMultiplier.get();

        Component line = Component.translatable(
                "tooltip.item.caster_curios_bonus.mind_to_matter.health_bonus",
                String.format("%.1f", extraHealth)
        ).withStyle(ChatFormatting.AQUA);

        event.getToolTip().add(line);
    }

    @SubscribeEvent
    public void RecklessOnTooltip(ItemTooltipEvent event) {
        Player player = event.getEntity();
        if (player == null) return;

        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof RecklessUtterance)) {
            return;
        }

        OverloadSyncedData data = OverloadClientData.get(player);
        int overload = data.getOverload();
        int silentTicks = data.getSilentTicks();
        float silentSeconds = silentTicks / 20f;
        // 本地化 key: tooltip.item.caster_curios_bonus.reckless_utterance.overload
        Component messageOverload = Component.translatable(
                "tooltip.item.caster_curios_bonus.reckless_utterance.overload",
                overload
        ).withStyle(ChatFormatting.RED);
        event.getToolTip().add(messageOverload);

        Component messageSilent = Component.translatable(
                "tooltip.item.caster_curios_bonus.reckless_utterance.silent",
                String.format("%.1f", silentSeconds)
        ).withStyle(ChatFormatting.DARK_RED);
        event.getToolTip().add(messageSilent);

    }

}
