package com.rinko1231.ccb.capability;

import com.rinko1231.ccb.CasterCuriosBonus;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class OverloadProvider implements ICapabilitySerializable<CompoundTag> {

    public static final ResourceLocation ID = CasterCuriosBonus.id( "overload");

    private final OverloadData backend = new OverloadData();
    private final LazyOptional<OverloadData> optional = LazyOptional.of(() -> backend);

    public static OverloadData get(ServerPlayer player) {
        return player.getCapability(CAPABILITY).orElseThrow(
                () -> new IllegalStateException("Overload capability missing")
        );
    }

    // Capability instance
    public static Capability<OverloadData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    // ---------- NBT ----------
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        backend.saveNBT(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        backend.loadNBT(nbt);
    }
}
