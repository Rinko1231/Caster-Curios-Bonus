package com.rinko1231.ccb;

import com.rinko1231.ccb.client.EnergySwirlLayerNew;
import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.EntityReg;
import com.rinko1231.ccb.item.charm.Eureka;
import com.rinko1231.ccb.item.head.RecklessUtterance;
import com.rinko1231.ccb.item.necklace.MindToMatter;
import com.rinko1231.ccb.network.data.OverloadClientData;
import com.rinko1231.ccb.network.data.OverloadSyncedData;
import com.rinko1231.ccb.utils.SpellPowerHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.renderer.entity.NoopRenderer;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

@Mod.EventBusSubscriber(
        modid = MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = {Dist.CLIENT}
)
public class ClientSetup {
    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityReg.ZEPHYR_BOOSTER_ENTITY.get(), NoopRenderer::new);}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.AddLayers event) {
        addLayerToPlayerSkin(event, "default");
        addLayerToPlayerSkin(event, "slim");
    }
    private static void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, String skinName) {
        EntityRenderer<? extends Player> render = event.getSkin(skinName);
        if (render instanceof LivingEntityRenderer livingRenderer) {

            livingRenderer.addLayer(
                    new EnergySwirlLayerNew.Vanilla(
                            (RenderLayerParent<Player, HumanoidModel<Player>>) livingRenderer,
                            EnergySwirlLayerNew.CHARGE_TEXTURE
                    )
            );
        }

    }

}
