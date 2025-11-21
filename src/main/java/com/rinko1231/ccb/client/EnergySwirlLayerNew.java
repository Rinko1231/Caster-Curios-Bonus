package com.rinko1231.ccb.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.rinko1231.ccb.init.MobEffectReg;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(Dist.CLIENT)
public class EnergySwirlLayerNew {
    //public static final ResourceLocation EVASION_TEXTURE = ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "textures/entity/evasion.png");
    public static final ResourceLocation CHARGE_TEXTURE = IronsSpellbooks.id( "textures/entity/charged.png");
    private static final int COLOR = RenderHelper.colorf(0.8F, 0.8F, 0.8F);

    public EnergySwirlLayerNew() {
    }

    private static RenderType getRenderType(ResourceLocation texture, float f) {
        return RenderType.energySwirl(texture, f * 0.02F % 1.0F, f * 0.01F % 1.0F);
    }

    private static boolean shouldRender(LivingEntity entity) {
        return entity.hasEffect(MobEffectReg.ARCANE_OVERDRIVE);
    }

    public static class Vanilla extends RenderLayer<Player, HumanoidModel<Player>> {
        public static ModelLayerLocation ENERGY_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "energy_layer"), "main");

        private final HumanoidModel<Player> model;
        private final ResourceLocation TEXTURE;

        private final DeferredHolder<MobEffect, MobEffect> shouldRender;

        public Vanilla(RenderLayerParent pRenderer, ResourceLocation texture, DeferredHolder<MobEffect, MobEffect> shouldRender) {
            super(pRenderer);
            this.model = new HumanoidModel(Minecraft.getInstance().getEntityModels().bakeLayer(ENERGY_LAYER));
            this.TEXTURE = texture;
            this.shouldRender = shouldRender;
        }


        public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, Player pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            if (EnergySwirlLayerNew.shouldRender(pLivingEntity)) {
                float f = (float)pLivingEntity.tickCount + pPartialTicks;
                HumanoidModel<Player> entitymodel = this.model();
                VertexConsumer vertexconsumer = pBuffer.getBuffer(EnergySwirlLayerNew.getRenderType(this.TEXTURE, f));
                ((HumanoidModel)this.getParentModel()).copyPropertiesTo(entitymodel);
                entitymodel.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, EnergySwirlLayerNew.COLOR);
            }

        }

        protected boolean shouldRender(Player entity) {
            return true;
        }

        protected HumanoidModel<Player> model() {
            return this.model;
        }
    }


    public static class Geo extends GeoRenderLayer<AbstractSpellCastingMob> {
        private final ResourceLocation TEXTURE;
        private final Long shouldRenderFlag;

        public Geo(GeoEntityRenderer<AbstractSpellCastingMob> entityRendererIn, ResourceLocation texture, Long shouldRenderFlag) {
            super(entityRendererIn);
            this.TEXTURE = texture;
            this.shouldRenderFlag = shouldRenderFlag;
        }

        public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, RenderType renderType2, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            if (EnergySwirlLayerNew.shouldRender(animatable)) {
                float f = (float)animatable.tickCount + partialTick;
                RenderType renderType = EnergySwirlLayerNew.getRenderType(this.TEXTURE, f);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
                poseStack.pushPose();
                bakedModel.getBone("body").ifPresent((rootBone) -> rootBone.getChildBones().forEach((bone) -> bone.updateScale(1.1F, 1.1F, 1.1F)));
                this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, vertexconsumer, true, partialTick, packedLight, OverlayTexture.NO_OVERLAY, EnergySwirlLayerNew.COLOR);
                bakedModel.getBone("body").ifPresent((rootBone) -> rootBone.getChildBones().forEach((bone) -> bone.updateScale(1.0F, 1.0F, 1.0F)));
                poseStack.popPose();
            }

        }
    }
}
