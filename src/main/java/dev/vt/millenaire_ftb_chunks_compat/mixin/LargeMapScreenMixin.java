package dev.vt.millenaire_ftb_chunks_compat.mixin;

import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftbchunks.client.map.MapRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "dev.ftb.mods.ftbchunks.client.gui.LargeMapScreen", remap = false)
public abstract class LargeMapScreenMixin {

    @Shadow
    private MapDimension dimension;

    @Inject(method = "onInit", at = @At("HEAD"))
    private void onInitMapScreen(CallbackInfoReturnable<Boolean> cir) {
        try {
            if (this.dimension != null) {
                for (MapRegion region : this.dimension.getRegions().values()) {
                    region.update(true); // Trigger FTB Chunks map texture re-bake with injected village colors
                }
            }
        } catch (Throwable ignored) {
        }
    }
}
