package com.rinko1231.ccb.init;


import com.rinko1231.ccb.network.data.SyncOverloadPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;


@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = MODID)

public class PayloadReg {

        @SubscribeEvent
        public static void register(final RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar payloadRegistrar = event.registrar(MODID).versioned("1.0.0").optional();
            //PARTICLES
            payloadRegistrar.playToClient(
                    SyncOverloadPacket.TYPE,
                    SyncOverloadPacket.STREAM_CODEC,
                    SyncOverloadPacket::handle);
            //payloadRegistrar.playToClient(ClientboundBloodSiphonReverseParticles.TYPE, ClientboundBloodSiphonReverseParticles.STREAM_CODEC, ClientboundBloodSiphonReverseParticles::handle);

  }
}