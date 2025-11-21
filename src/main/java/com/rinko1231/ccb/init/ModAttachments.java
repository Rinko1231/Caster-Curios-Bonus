package com.rinko1231.ccb.init;

import com.rinko1231.ccb.capability.OverloadData;

import com.rinko1231.ccb.capability.OverloadDataSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.rinko1231.ccb.CasterCuriosBonus.MODID;

public class ModAttachments {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<OverloadData>> OVERLOAD;

    static {
        OVERLOAD = ATTACHMENT_TYPES.register("overload", () ->
                AttachmentType.builder(holder -> {
                            if (holder instanceof ServerPlayer sp) {
                                return new OverloadData(sp);
                            }
                            return new OverloadData();
                        })
                        .serialize(new OverloadDataSerializer())
                        .build()
        );
    }

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
