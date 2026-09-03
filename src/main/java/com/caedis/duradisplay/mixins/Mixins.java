package com.caedis.duradisplay.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

public enum Mixins implements IMixins {

    // spotless:off
    DURADISPLAY(new MixinBuilder().setPhase(Phase.EARLY)
        .addClientMixins(
            "minecraft.MixinRenderItem",
            "minecraft.MixinGuiScreen",
            "minecraft.MixinGuiContainerCreative",
            "minecraft.MixinGuiContainer"
        )),
    NEI(new MixinBuilder().setPhase(Phase.LATE)
        .addClientMixins("nei.MixinItemsGrid")
        .addRequiredMod(TargetedMod.NOT_ENOUGH_ITEMS));
    // spotless:on

    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder;
    }

    @Nonnull
    @Override
    public MixinBuilder getBuilder() {
        return builder;
    }
}
