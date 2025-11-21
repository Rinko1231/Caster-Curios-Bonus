package com.rinko1231.ccb.utils;


import io.redspace.ironsspellbooks.api.entity.NoKnockbackProjectile;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import static io.redspace.ironsspellbooks.api.util.Utils.checkEntityIntersecting;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class MyUtils {
/*
    public static ModConfigSpec.BooleanValue summon114514AllowLooting;
    public static ModConfigSpec.DoubleValue summoned114514BaseHealth;
    public static ModConfigSpec.DoubleValue summoned114514BonusHealthSpellPowerRatio;
    public static ModConfigSpec.DoubleValue summoned114514BaseAttackDamage;
    public static ModConfigSpec.DoubleValue summoned114514BonusATKSpellPowerRatio;

            summon114514AllowLooting = BUILDER
                .comment("Whether the Spell can be obtained from loot chests")
                .define("summon114514AllowLooting",false);
        summoned114514BaseHealth = BUILDER
                .defineInRange("summoned114514BaseHealth", 18.0f ,1, Integer.MAX_VALUE);
        summoned114514BonusHealthSpellPowerRatio = BUILDER
                .comment("Determines how much bonus health summoned creatures gain from your Spell Power.")
                .defineInRange("summoned114514BonusHealthSpellPowerRatio", 0.2f,0, Integer.MAX_VALUE);
        summoned114514BaseAttackDamage = BUILDER
                .defineInRange("summoned114514BaseAttackDamage", 4.0F ,0,  Integer.MAX_VALUE);
        summoned114514BonusATKSpellPowerRatio = BUILDER
                .comment("Determines how much bonus attack damage summoned creatures gain from your Spell Power.")
                .defineInRange("summoned114514BonusATKSpellPowerRatio", 0.2f,0, Integer.MAX_VALUE);

    */
    // 贯穿：按距离从近到远返回命中的所有实体
    public static List<EntityHitResult> raycastAllEntities(Level level,
                                                    Entity originEntity,
                                                    float distance,
                                                    boolean stopAtBlocks,
                                                    float bbInflation) {
        Vec3 start = originEntity.getEyePosition();
        Vec3 end = originEntity.getLookAngle().normalize().scale(distance).add(start);
        return raycastAllEntities(level, originEntity, start, end, stopAtBlocks, bbInflation, MyUtils::canHitWithRaycast);
    }

    // 贯穿（带自定义过滤器）
    public static List<EntityHitResult> raycastAllEntities(Level level,
                                                           Entity originEntity,
                                                           Vec3 start,
                                                           Vec3 end,
                                                           boolean stopAtBlocks,
                                                           float bbInflation,
                                                           Predicate<? super Entity> filter) {
        BlockHitResult blockHitResult;
        if (stopAtBlocks) {
            blockHitResult = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, originEntity));
            // 如果先撞到方块，就把“end”截断到方块命中点
            if (blockHitResult.getType() != HitResult.Type.MISS) {
                end = blockHitResult.getLocation();
            }
        }

        AABB range = originEntity.getBoundingBox().expandTowards(end.subtract(start));
        List<EntityHitResult> hits = new ArrayList<>();

        for (Entity target : level.getEntities(originEntity, range, filter)) {

            HitResult hit = checkEntityIntersecting(target, start, end, bbInflation);
            if (hit.getType() == HitResult.Type.ENTITY) {
                if (hit instanceof EntityHitResult ehr) {
                    hits.add(ehr);
                } else {
                    hits.add(new EntityHitResult(target, hit.getLocation()));
                }
            }
        }

        // 按距离从近到远排序
        hits.sort(Comparator.comparingDouble(h -> h.getLocation().distanceToSqr(start)));
        return hits;
    }


    // 简便重载：不考虑方块（纯穿透），命中所有实体
    public static List<EntityHitResult> raycastAllEntities(Level level,
                                                    Entity originEntity,
                                                    float distance) {
        return raycastAllEntities(level, originEntity, distance, false, 0.0F);
    }

    public static Vec3 firstBlockHitOrMax(LivingEntity entity, double maxDist) {
        Vec3 start = entity.getEyePosition();
        Vec3 look  = entity.getLookAngle().normalize();
        Vec3 end   = start.add(look.scale(maxDist));

        ClipContext ctx = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity);
        BlockHitResult blockHit = entity.level().clip(ctx);

        return (blockHit.getType() != HitResult.Type.MISS) ? blockHit.getLocation() : end;
    }

    private static boolean canHitWithRaycast(Entity entity) {
        return entity.isPickable() && entity.isAlive();
    }

    //我是笨蛋我需要
    public static int maxCap(int cap, int b)
    {
        return min(cap, b);
    }
    public static float maxCap(float cap, float b)
    {
        return min(cap, b);
    }
    public static double maxCap(double cap, double b)
    {
        return min(cap, b);
    }
    public static int minCap(int cap, int b)
    {
        return max(cap, b);
    }
    public static float minCap(float cap, float b)
    {
        return max(cap, b);
    }
    public static double minCap(double cap, double b)
    {
        return max(cap, b);
    }


    public static boolean isEquipCurios(LivingEntity livingEntity, Item item) {
        if (livingEntity ==null) return false;
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(livingEntity).stream().findAny();
        return curiosInventory
                .map(iCuriosItemHandler -> iCuriosItemHandler.isEquipped(item))
                .orElse(false);
    }
    public static Holder<Attribute> getHolder(ResourceLocation rl) {
        return BuiltInRegistries.ATTRIBUTE.getHolder(rl).orElse(null);
    }

    public static Holder<Attribute> getHolder(Attribute attribute) {
        ResourceLocation rl = BuiltInRegistries.ATTRIBUTE.getKey(attribute);
        if (rl == null) return null;
        return getHolder(rl);
    }

    public static boolean JustApplyDamageWithoutChecking(Entity target, float baseAmount, DamageSource damageSource) {
        if (target instanceof LivingEntity livingTarget) {
            if (damageSource instanceof SpellDamageSource spellDamageSource) {
                SpellDamageEvent e = new SpellDamageEvent(livingTarget, baseAmount, spellDamageSource);
                if (((SpellDamageEvent) NeoForge.EVENT_BUS.post(e)).isCanceled()) {
                    return false;
                }

                baseAmount = e.getAmount();
                float adjustedDamage = baseAmount * DamageSources.getResist(livingTarget, spellDamageSource.spell().getSchoolType());
                if (damageSource.getDirectEntity() instanceof NoKnockbackProjectile) {
                    DamageSources.ignoreNextKnockback(livingTarget);
                }

                Entity var8 = damageSource.getEntity();
                if (var8 instanceof LivingEntity livingAttacker) {
                    livingAttacker.setLastHurtMob(target);
                }
                return livingTarget.hurt(damageSource, adjustedDamage);
            }
        }
        return target.hurt(damageSource, baseAmount);
    }

    /**
     * 只给指定范围内的玩家发送粒子
     *
     * @param level 目标世界
     * @param particle 粒子类型
     * @param x 粒子中心X
     * @param y 粒子中心Y
     * @param z 粒子中心Z
     * @param count 粒子数量
     * @param deltaX X方向随机偏移
     * @param deltaY Y方向随机偏移
     * @param deltaZ Z方向随机偏移
     * @param speed 粒子速度
     * @param force 是否强制可见
     * @param radius 生效半径（超出此半径的玩家不会接收粒子）
     */
    public static void spawnParticlesWithRange(ServerLevel level, ParticleOptions particle,
                                               double x, double y, double z,
                                               int count, double deltaX, double deltaY, double deltaZ,
                                               double speed, boolean force, double radius) {

        double radiusSq = radius * radius;

        for (ServerPlayer player : level.getPlayers(playerEntity ->
                playerEntity.distanceToSqr(x, y, z) <= radiusSq)) {
            level.sendParticles(player, particle, force, x, y, z, count, deltaX, deltaY, deltaZ, speed);
        }
    }

}



