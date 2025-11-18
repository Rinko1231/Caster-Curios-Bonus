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
    private int maxOverload = CasterCuriosBonusConfig.recklessUtteranceMaxOverload.get();  // 默认值，可调
    private int silentTicks = 0;

    //private boolean overloading = false;

    
    public int getOverload() {
        return overload;
    }

    
    public void addOverload(int amount) {
        overload = Math.min(overload + amount, maxOverload);
    }

    
    public void setOverload(int value) {
        overload = Math.max(0, Math.min(value, maxOverload));
    }

    
    public int getMaxOverload() {
        return maxOverload;
    }
/*
    
    public void setOverloading(boolean value) {
        overloading=value;
    }

    
    public boolean getOverloading() {
        return overloading;
    }
*/
    
    public void setMaxOverload(int value) {
        maxOverload = Math.max(1, value);
        overload = Math.min(overload, maxOverload);
    }

    
    public int getSilentTicks() {
        return silentTicks;
    }

    
    public void setSilentTicks(int ticks) {
        silentTicks = Math.max(0, ticks);
        if (silentTicks == 0) overload = 0; // 疲倦结束自动清零
    }

    public void tick(ServerPlayer player) {

        int oldSilent = silentTicks;

        if (silentTicks > 0) {
            silentTicks--;
            if (silentTicks == 0) {
                overload = 0;
            }
        } else if (overload >= maxOverload) {
            silentTicks = CasterCuriosBonusConfig.recklessUtteranceSilentTicks.get();
            overload = maxOverload;

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
        data.setMaxOverload(maxOverload);
        data.setSilentTicks(silentTicks);

        CCBMessages.sendToPlayer(new SyncOverloadPacket(data), player);
    }


    // ---------- NBT 存储 ----------
    
    public void saveNBT(CompoundTag tag) {
        tag.putInt("Overload", overload);
        tag.putInt("MaxOverload", maxOverload);
        tag.putInt("SilentTicks", silentTicks);
        //tag.putBoolean("isOverloading", overloading);
    }

    
    public void loadNBT(CompoundTag tag) {
        overload = tag.getInt("Overload");
        maxOverload = tag.getInt("MaxOverload");
        silentTicks = tag.getInt("SilentTicks");
        //overloading = tag.getBoolean("isOverloading");
    }



}
