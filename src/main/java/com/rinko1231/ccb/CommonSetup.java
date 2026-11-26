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
import org.jetbrains.annotations.NotNull;

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
        syncOverload(newPlayer);
    }

    /**
     * 登录时需要把服务端的过载数据（含上限）同步给客户端，
     * 否则客户端 OverloadClientData 一直是 0，tooltip 显示不出正确数值。
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            syncOverload(sp);
        }
    }

    private static void syncOverload(ServerPlayer player) {
        player.getCapability(OverloadProvider.CAPABILITY).ifPresent(data -> data.sync(player));
    }

    //死亡重生后连接已就绪，再同步一次（Clone 事件里发包有时早于客户端准备好）。

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.@NotNull PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            syncOverload(sp);
        }
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer sp)) return;
        sp.getCapability(OverloadProvider.CAPABILITY).ifPresent(cap -> cap.tick(sp));
    }


}
