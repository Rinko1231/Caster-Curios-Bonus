package com.rinko1231.ccb.network;


import com.rinko1231.ccb.CasterCuriosBonus;


import com.rinko1231.ccb.network.data.OverloadSyncedData;
import com.rinko1231.ccb.network.data.SyncOverloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class CCBMessages {

    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {

        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(CasterCuriosBonus.id("messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        // 注册 SyncOverloadPacket
        net.messageBuilder(SyncOverloadPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncOverloadPacket::toBytes)
                .decoder(SyncOverloadPacket::new)
                .consumerMainThread(SyncOverloadPacket::handle)
                .add();
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}
