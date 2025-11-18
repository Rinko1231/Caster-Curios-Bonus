package com.rinko1231.ccb.capability;

import com.rinko1231.ccb.config.CasterCuriosBonusConfig;
import com.rinko1231.ccb.init.MobEffectReg;
import com.rinko1231.ccb.init.ModAttachments;
import com.rinko1231.ccb.network.data.SyncOverloadPacket;
import io.redspace.ironsspellbooks.api.network.ISerializable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;

public class OverloadData implements ISerializable, INBTSerializable<CompoundTag> {

    private int overload = 0;
    private int silentTicks = 0;
    private int maxOverload = 200;

    public OverloadData() { }

    public OverloadData(ServerPlayer player) { }

    public int getOverload() { return overload; }
    public int getMaxOverload() { return maxOverload; }
    public int getSilentTicks() { return silentTicks; }

    public void addOverload(int v) {
        overload = Math.min(overload + v, maxOverload);
    }
    public void setOverload(int t) {
        this.overload = Math.clamp(t,0,maxOverload);

    }
    public void setSilentTicks(int t) {
        this.silentTicks = t;
        if (t == 0) overload = 0;
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

            player.removeEffect(MobEffectReg.ARCANE_OVERDRIVE);
            player.displayClientMessage(
                    Component.translatable("message.caster_curios_bonus.reckless_utterance.become_silent")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }

        if (silentTicks != oldSilent) {
            OverloadData data = player.getData(ModAttachments.OVERLOAD);
            PacketDistributor.sendToPlayer(player, new SyncOverloadPacket(data));
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var nbt = new CompoundTag();
        nbt.putInt("o", overload);
        nbt.putInt("s", silentTicks);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        overload = nbt.getInt("o");
        silentTicks = nbt.getInt("s");
    }

    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.overload);
        buffer.writeInt(this.silentTicks);
        buffer.writeInt(this.maxOverload);
    }

    public void readFromBuffer(FriendlyByteBuf buffer) {
        this.overload = buffer.readInt();
        this.silentTicks = buffer.readInt();
        this.maxOverload = buffer.readInt();
    }
}
