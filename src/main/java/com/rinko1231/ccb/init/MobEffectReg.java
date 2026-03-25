package com.rinko1231.ccb.init;

import com.rinko1231.ccb.effect.ArcaneOverdriveEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.rinko1231.ccb.CasterCuriosBonus.MOD_ID;

public class MobEffectReg {

        public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER;



        public static final RegistryObject<MobEffect> ARCANE_OVERDRIVE;



        static {
            MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, MOD_ID);

            ARCANE_OVERDRIVE = MOB_EFFECT_DEFERRED_REGISTER.register("arcane_overdrive", () -> (new ArcaneOverdriveEffect(MobEffectCategory.BENEFICIAL, 12495141)));


        }

        public MobEffectReg() {
        }

        public static void register(IEventBus eventBus) {
            MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
        }
    }