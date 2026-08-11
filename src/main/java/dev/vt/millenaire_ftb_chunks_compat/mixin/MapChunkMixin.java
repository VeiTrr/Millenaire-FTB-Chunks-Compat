package dev.vt.millenaire_ftb_chunks_compat.mixin;

import dev.ftb.mods.ftbchunks.client.map.MapRegion;
import dev.ftb.mods.ftbchunks.client.map.RenderMapImageTask;
import dev.ftb.mods.ftblibrary.math.XZ;
import dev.ftb.mods.ftbteams.api.Team;
import dev.vt.millenaire_ftb_chunks_compat.Config;
import dev.vt.millenaire_ftb_chunks_compat.client.ClientVillageCache;
import dev.vt.millenaire_ftb_chunks_compat.client.VirtualVillageTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Date;
import java.util.Optional;

@Pseudo
@Mixin(targets = "dev.ftb.mods.ftbchunks.client.map.MapChunk", remap = false)
public abstract class MapChunkMixin {

    private static final Date DUMMY_DATE = new Date();

    @Shadow
    public MapRegion region;

    @Shadow
    public abstract XZ getActualPos();

    private boolean villageTextureTriggered = false;

    @Inject(method = "getTeam", at = @At("HEAD"), cancellable = true)
    private void onGetTeam(CallbackInfoReturnable<Optional<Team>> cir) {
        if (!Config.showVillageBoundaries && !Config.showVillageIcons) {
            return;
        }

        try {
            RenderMapImageTask.setAlwaysRenderChunksOnMap(true);

            XZ pos = this.getActualPos();
            if (pos != null) {
                VirtualVillageTeam team = ClientVillageCache.getVillageTeam(pos.x(), pos.z());
                if (team != null) {
                    if (!villageTextureTriggered && this.region != null) {
                        villageTextureTriggered = true;
                        this.region.update(true); // Re-render map region texture with team color tint
                    }
                    cir.setReturnValue(Optional.of(team));
                }
            }
        } catch (Throwable ignored) {
        }
    }

    @Inject(method = "getClaimedDate", at = @At("HEAD"), cancellable = true)
    private void onGetClaimedDate(CallbackInfoReturnable<Optional<Date>> cir) {
        if (!Config.showVillageBoundaries && !Config.showVillageIcons) {
            return;
        }

        try {
            XZ pos = this.getActualPos();
            if (pos != null) {
                VirtualVillageTeam team = ClientVillageCache.getVillageTeam(pos.x(), pos.z());
                if (team != null) {
                    cir.setReturnValue(Optional.of(DUMMY_DATE));
                }
            }
        } catch (Throwable ignored) {
        }
    }
}
