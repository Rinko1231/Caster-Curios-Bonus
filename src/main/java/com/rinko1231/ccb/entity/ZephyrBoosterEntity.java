package com.rinko1231.ccb.entity;

import com.rinko1231.ccb.init.EntityReg;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class ZephyrBoosterEntity extends Entity {

    private static final int MAX_LIFE = 25;
    private int life = 0;

    private UUID attachedPlayerUUID = null;

    public ZephyrBoosterEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public ZephyrBoosterEntity(Level level, Player player) {
        this(EntityReg.ZEPHYR_BOOSTER_ENTITY.get(), level);
        this.attachedPlayerUUID = player.getUUID();

        this.setPos(player.getX(), -65, player.getZ());
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;

        Player player = findAttachedPlayer();
        if (player == null) {
            this.discard();
            return;
        }

        if (player.isFallFlying()) {
            Vec3 look = player.getLookAngle();
            Vec3 motion = player.getDeltaMovement();

            double d0 = 1.5;
            double d1 = 0.1;

            player.setDeltaMovement(
                    motion.add(
                            look.x * d1 + (look.x * d0 - motion.x) * 0.5,
                            look.y * d1 + (look.y * d0 - motion.y) * 0.5,
                            look.z * d1 + (look.z * d0 - motion.z) * 0.5
                    )
            );
            if (player instanceof ServerPlayer sp) {
                sp.connection.send(new ClientboundSetEntityMotionPacket(sp));
            }
        }

        life++;
        if (life > MAX_LIFE) {
            this.discard();
        }
    }

    private Player findAttachedPlayer() {
        if (attachedPlayerUUID == null) return null;
        return this.level().getPlayerByUUID(attachedPlayerUUID);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return false;
    }

}
