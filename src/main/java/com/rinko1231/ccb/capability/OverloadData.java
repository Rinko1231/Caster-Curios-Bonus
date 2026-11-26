package com.rinko1231.ccb.capability;

import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.MobEffectReg;

import com.rinko1231.ccb.network.CCBMessages;
import com.rinko1231.ccb.network.data.OverloadSyncedData;
import com.rinko1231.ccb.network.data.SyncOverloadPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class OverloadData{

    private int overload = 0;
    private int silentTicks = 0;

    //private boolean overloading = false;

    
    public int getOverload() {
        return overload;
    }

    
    public void addOverload(int amount) {
        overload = Math.min(overload + amount, getMaxOverload());
    }

    
    public void setOverload(int value) {
        overload = Math.max(0, Math.min(value, getMaxOverload()));
    }

    /**
     * 上限始终以服务端当前配置为准，不缓存到字段里，配置改动后立即生效。
     */
    public int getMaxOverload() {
        return CasterCuriosBonusConfig.recklessUtteranceMaxOverload.get();
    }
/*
    
    public void setOverloading(boolean value) {
        overloading=value;
    }

    
    public boolean getOverloading() {
        return overloading;
    }
*/
    
    public int getSilentTicks() {
        return silentTicks;
    }

    
    public void setSilentTicks(int ticks) {
        silentTicks = Math.max(0, ticks);
        if (silentTicks == 0) overload = 0; // 疲倦结束自动清零
    }

    public void tick(ServerPlayer player) {

        int oldSilent = silentTicks;
        int max = getMaxOverload();

        // 配置调小后，避免旧的过载值溢出
        if (overload > max) {
            overload = max;
        }

        if (silentTicks > 0) {
            silentTicks--;
            if (silentTicks == 0) {
                overload = 0;
            }
        } else if (overload >= max) {
            silentTicks = CasterCuriosBonusConfig.recklessUtteranceSilentTicks.get();
            overload = max;

            player.removeEffect(MobEffectReg.ARCANE_OVERDRIVE.get());
            player.displayClientMessage(
                    Component.translatable("message.caster_curios_bonus.reckless_utterance.become_silent")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }

        if (silentTicks != oldSilent) {
            sync(player);
        }
    }

    public void sync(ServerPlayer player) {
        OverloadSyncedData data = new OverloadSyncedData(player.getId());
        data.setOverload(overload);
        data.setMaxOverload(getMaxOverload());
        data.setSilentTicks(silentTicks);

        CCBMessages.sendToPlayer(new SyncOverloadPacket(data), player);
    }


    // ---------- NBT 存储 ----------
    
    public void saveNBT(CompoundTag tag) {
        tag.putInt("Overload", overload);
        tag.putInt("SilentTicks", silentTicks);
        //tag.putBoolean("isOverloading", overloading);
    }

    
    public void loadNBT(CompoundTag tag) {
        overload = Math.max(0, Math.min(tag.getInt("Overload"), getMaxOverload()));
        silentTicks = tag.getInt("SilentTicks");
        //overloading = tag.getBoolean("isOverloading");
    }



}
