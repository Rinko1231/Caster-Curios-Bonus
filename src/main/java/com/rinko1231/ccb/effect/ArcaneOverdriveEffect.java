package com.rinko1231.ccb.effect;



import com.rinko1231.ccb.capability.OverloadData;
import com.rinko1231.ccb.init.ModAttachments;
import com.rinko1231.ccb.network.data.SyncOverloadPacket;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class ArcaneOverdriveEffect extends MagicMobEffect {
    public ArcaneOverdriveEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return true;
        //仅限高贵的史蒂夫使用
        return entity instanceof Player;
    }

    @Override
    public void onEffectRemoved(@NotNull LivingEntity entity,  int amplifier) {
        super.onEffectRemoved(entity, amplifier);
        if(entity instanceof ServerPlayer serverPlayer)
        {
            OverloadData data = serverPlayer.getData(ModAttachments.OVERLOAD);
            data.setOverload(0);
            PacketDistributor.sendToPlayer(serverPlayer, new SyncOverloadPacket(data));
        }
    }
}
