package com.rinko1231.ccb.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rinko1231.ccb.init.MobEffectReg;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.function.Predicate;

import static io.redspace.ironsspellbooks.render.EnergySwirlLayer.Vanilla.ENERGY_LAYER;

@OnlyIn(Dist.CLIENT)
public class EnergySwirlLayerNew {
    //public static final ResourceLocation EVASION_TEXTURE = ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "textures/entity/evasion.png");
    public static final ResourceLocation CHARGE_TEXTURE = IronsSpellbooks.id("textures/entity/charged.png");

    public EnergySwirlLayerNew() {
    }

    private static RenderType getRenderType(ResourceLocation texture, float f) {
        return RenderType.energySwirl(texture, f * 0.02F % 1.0F, f * 0.01F % 1.0F);
    }

    private static boolean shouldRender(LivingEntity entity, Predicate<LivingEntity> shouldRenderFlag) {
        return shouldRenderFlag.test(entity);
    }

    public static class Vanilla extends RenderLayer<Player, HumanoidModel<Player>> {

        private final HumanoidModel<Player> model;
        private final ResourceLocation TEXTURE;

        public Vanilla(RenderLayerParent<Player, HumanoidModel<Player>> parent, ResourceLocation texture) {
            super(parent);
            this.model = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ENERGY_LAYER));
            this.TEXTURE = texture;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                           Player player, float limbSwing, float limbSwingAmount,
                           float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

            if (!shouldRender(player)) return;

            float f = player.tickCount + partialTicks;
            HumanoidModel<Player> model = this.model();
            VertexConsumer vc = buffer.getBuffer(EnergySwirlLayerNew.getRenderType(this.TEXTURE, f));

            this.getParentModel().copyPropertiesTo(model);
            model.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY,
                    0.8f, 0.8f, 0.8f, 1.0f);
        }

        protected boolean shouldRender(Player player) {
            return player.hasEffect(MobEffectReg.ARCANE_OVERDRIVE.get());
        }

        protected HumanoidModel<Player> model() {
            return this.model;
        }
    }


    public static class Geo extends GeoRenderLayer<AbstractSpellCastingMob> {
        private final ResourceLocation TEXTURE;
        private final Predicate<LivingEntity> shouldRenderFlag;

        public Geo(GeoEntityRenderer<AbstractSpellCastingMob> entityRendererIn, ResourceLocation texture, Predicate<LivingEntity> shouldRenderFlag) {
            super(entityRendererIn);
            this.TEXTURE = texture;
            this.shouldRenderFlag = shouldRenderFlag;
        }

        public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, RenderType renderType2, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            if (EnergySwirlLayerNew.shouldRender(animatable, this.shouldRenderFlag)) {
                float f = (float)animatable.tickCount + partialTick;
                RenderType renderType = EnergySwirlLayerNew.getRenderType(this.TEXTURE, f);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
                poseStack.pushPose();
                bakedModel.getBone("body").ifPresent((rootBone) -> rootBone.getChildBones().forEach((bone) -> bone.updateScale(1.1F, 1.1F, 1.1F)));
                this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, vertexconsumer, true, partialTick, packedLight, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
                bakedModel.getBone("body").ifPresent((rootBone) -> rootBone.getChildBones().forEach((bone) -> bone.updateScale(1.0F, 1.0F, 1.0F)));
                poseStack.popPose();
            }

        }
    }
}
