package com.rinko1231.ccb.network.data;

import net.minecraft.network.FriendlyByteBuf;

public class OverloadSyncedData {
    private final int playerId;
    private int overload;
    private int maxOverload;
    private int silentTicks;

    public OverloadSyncedData(int playerId) {
        this.playerId = playerId;
    }

    public static OverloadSyncedData read(FriendlyByteBuf buf) {
        OverloadSyncedData d = new OverloadSyncedData(buf.readInt());
        d.overload = buf.readInt();
        d.maxOverload = buf.readInt();
        d.silentTicks = buf.readInt();
        return d;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(playerId);
        buf.writeInt(overload);
        buf.writeInt(maxOverload);
        buf.writeInt(silentTicks);
    }

    public int getPlayerId() { return playerId; }
    public int getOverload() { return overload; }
    public int getMaxOverload() { return maxOverload; }
    public int getSilentTicks() { return silentTicks; }

    public void setOverload(int overload) { this.overload = overload; }
    public void setMaxOverload(int maxOverload) { this.maxOverload = maxOverload; }
    public void setSilentTicks(int silentTicks) { this.silentTicks = silentTicks; }
}

