package com.rinko1231.ccb;

import com.rinko1231.ccb.client.EnergySwirlLayerNew;
import com.rinko1231.ccb.init.EntityReg;
import com.rinko1231.ccb.init.MobEffectReg;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;


import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

@EventBusSubscriber(
        modid = MODID,
        bus = EventBusSubscriber.Bus.MOD,
        value = {Dist.CLIENT}
)
public class ClientSetup {
    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityReg.ZEPHYR_BOOSTER_ENTITY.get(), NoopRenderer::new);}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.AddLayers event) {
        addLayerToPlayerSkin(event, PlayerSkin.Model.SLIM);
        addLayerToPlayerSkin(event, PlayerSkin.Model.WIDE);
    }
    private static void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, PlayerSkin.Model skinName) {
        EntityRenderer<? extends Player> render = event.getSkin(skinName);
        if (render instanceof LivingEntityRenderer livingRenderer) {

            livingRenderer.addLayer(new EnergySwirlLayerNew.Vanilla(livingRenderer, EnergySwirlLayerNew.CHARGE_TEXTURE, MobEffectReg.ARCANE_OVERDRIVE));

        }

    }
}
