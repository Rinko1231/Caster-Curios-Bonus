package com.rinko1231.ccb;

import com.rinko1231.ccb.capability.OverloadData;

import com.rinko1231.ccb.init.ModAttachments;
import com.rinko1231.ccb.network.data.SyncOverloadPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;


import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

@EventBusSubscriber(modid = MODID)
public class CommonSetup {

/*
    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {

        ServerPlayer player = event.getPlayer();
        String msg = event.getMessage().getString().trim().toLowerCase();

        OverloadData data = player.getData(ModAttachments.OVERLOAD);

        switch (msg) {
            case "overload add" -> {
                data.addOverload(50);
                player.displayClientMessage(Component.literal(
                        "Try to add → " + data.getOverload()
                ),false);
                PacketDistributor.sendToPlayer(player, new SyncOverloadPacket(data));
                player.displayClientMessage(Component.literal(
                        "Added 50 overload → " + data.getOverload()
                ),false);
            }

            case "overload check" -> {
                player.displayClientMessage(Component.literal(
                        "Overload: " + data.getOverload() + " / " + data.getMaxOverload()
                ),false);
            }
        }
    }
    */

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        if (player instanceof ServerPlayer sp) {
            OverloadData data = sp.getData(ModAttachments.OVERLOAD);
            data.tick(sp);
        }
}

    /**
     * 登录时需要把服务端的过载数据（含上限）同步给客户端，
     * 否则客户端 OverloadClientData一直是0，tooltip显示不出正确数值。
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            syncOverload(sp);
        }
    }

    /**
     * 死亡重生 / 从末地返回后重新同步一次。
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            syncOverload(sp);
        }
    }

    private static void syncOverload(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new SyncOverloadPacket(player.getData(ModAttachments.OVERLOAD)));
    }
}
