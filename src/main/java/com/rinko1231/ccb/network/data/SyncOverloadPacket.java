package com.rinko1231.ccb.network.data;

import com.rinko1231.ccb.CasterCuriosBonus;
import com.rinko1231.ccb.capability.OverloadClientData;
import com.rinko1231.ccb.capability.OverloadData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncOverloadPacket implements CustomPacketPayload {

    private int overload;
    private int silentTicks;
    private int maxOverload;

    public static final CustomPacketPayload.Type<SyncOverloadPacket> TYPE =
            new CustomPacketPayload.Type(CasterCuriosBonus.id( "sync_overload"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncOverloadPacket> STREAM_CODEC =
            CustomPacketPayload.codec(SyncOverloadPacket::write, SyncOverloadPacket::new);

    // 服务端构造
    public SyncOverloadPacket(OverloadData data) {
        this.overload = data.getOverload();
        this.silentTicks = data.getSilentTicks();
        this.maxOverload = data.getMaxOverload();
    }

    // 客户端构造
    public SyncOverloadPacket(FriendlyByteBuf buf) {
        this.overload = buf.readInt();
        this.silentTicks = buf.readInt();
        this.maxOverload = buf.readInt();
    }


    public void write(FriendlyByteBuf buf) {
        buf.writeInt(overload);
        buf.writeInt(silentTicks);
        buf.writeInt(maxOverload);
    }

    public static void handle(SyncOverloadPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            OverloadClientData.setData(packet.overload, packet.silentTicks, packet.maxOverload);
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
