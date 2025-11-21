package com.rinko1231.ccb.network.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncOverloadPacket {

    private final OverloadSyncedData data;

    public SyncOverloadPacket(OverloadSyncedData data) {
        this.data = data;
    }

    public SyncOverloadPacket(FriendlyByteBuf buf) {
        this.data = OverloadSyncedData.read(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        data.write(buf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            OverloadClientData.setSyncedData(data);
        });
        ctx.setPacketHandled(true);
        return true;
    }
}
