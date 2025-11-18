package com.rinko1231.ccb.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;

public class OverloadDataSerializer implements IAttachmentSerializer<CompoundTag, OverloadData> {

    @Override
    public OverloadData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {

        OverloadData data;

        if (holder instanceof ServerPlayer sp) {
            data = new OverloadData(sp);
        } else {
            data = new OverloadData();
        }

        data.deserializeNBT(provider, tag);
        return data;
    }

    @Override
    public CompoundTag write(OverloadData data, HolderLookup.Provider provider) {

        return data.serializeNBT(provider);
    }
}
