package com.rinko1231.ccb.effect;

import com.rinko1231.ccb.capability.OverloadProvider;

import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class ArcaneOverdriveEffect extends MagicMobEffect {
    public ArcaneOverdriveEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    public void removeAttributeModifiers(@NotNull LivingEntity livingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(livingEntity, pAttributeMap, pAmplifier);
        if(livingEntity instanceof ServerPlayer serverPlayer)
            serverPlayer.getCapability(OverloadProvider.CAPABILITY).ifPresent(data -> {
                data.setOverload(0);
            data.sync(serverPlayer);
        });
    }
    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return;
        //仅限高贵的史蒂夫使用
        if (!(entity instanceof Player)) {
            entity.removeEffect(this);
        }
    }

    public void addAttributeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
       /* if(pLivingEntity instanceof ServerPlayer serverPlayer)
            serverPlayer.getCapability(OverloadProvider.CAPABILITY).ifPresent(data -> {
                data.setOverloading(true);  // 服务端状态


            });*/
    }

}
