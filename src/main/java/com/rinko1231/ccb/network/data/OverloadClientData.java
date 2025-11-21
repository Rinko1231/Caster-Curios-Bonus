package com.rinko1231.ccb.network.data;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class OverloadClientData {
    private static final OverloadSyncedData emptyData = new OverloadSyncedData(-999);
    private static final Map<Integer, OverloadSyncedData> lookup = new HashMap<>();

    public static void setSyncedData(OverloadSyncedData data) {
        lookup.put(data.getPlayerId(), data);
    }

    public static OverloadSyncedData get(LivingEntity entity) {
        if (entity instanceof Player player) {
            return lookup.getOrDefault(player.getId(), emptyData);
        }
        return emptyData;
    }
}
