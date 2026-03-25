package com.rinko1231.ccb;

import com.rinko1231.ccb.capability.OverloadProvider;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class CommonSetup {

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof ServerPlayer) {
            event.addCapability(OverloadProvider.ID, new OverloadProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!(event.getEntity() instanceof ServerPlayer newPlayer)) return;

        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(OverloadProvider.CAPABILITY).ifPresent(
                oldStore ->
                        newPlayer.getCapability(OverloadProvider.CAPABILITY)
                                .ifPresent(newStore -> {
                                    CompoundTag tag = new CompoundTag();
                                    oldStore.saveNBT(tag);
                                    newStore.loadNBT(tag);
                                }));
        event.getOriginal().invalidateCaps();
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer sp)) return;
        sp.getCapability(OverloadProvider.CAPABILITY).ifPresent(cap -> cap.tick(sp));
    }


}
