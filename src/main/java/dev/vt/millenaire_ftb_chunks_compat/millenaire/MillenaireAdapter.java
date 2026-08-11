package dev.vt.millenaire_ftb_chunks_compat.millenaire;

import dev.vt.millenaire_ftb_chunks_compat.Config;
import dev.vt.millenaire_ftb_chunks_compat.Mil_ftb_c_compat;
import dev.vt.millenaire_ftb_chunks_compat.api.VillageData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.millenaire.building.BuildingInstance;
import org.millenaire.culture.Culture;
import org.millenaire.culture.ModCultures;
import org.millenaire.village.Village;
import org.millenaire.village.VillageManager;
import org.millenaire.village.VillageSavedData;

import java.util.*;

public class MillenaireAdapter {

    public static List<VillageData> getActiveVillages() {
        List<VillageData> result = new ArrayList<>();

        try {
            MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
            if (server != null) {
                ServerLevel overworld = server.overworld();
                if (overworld != null) {
                    VillageSavedData vsd = VillageSavedData.get(overworld);
                    if (vsd != null) {
                        VillageManager vm = vsd.getVillageManager();
                        if (vm != null && vm.getAllVillages() != null) {
                            for (Village village : vm.getAllVillages()) {
                                VillageData data = convertMillenaireVillage(village);
                                if (data != null) {
                                    result.add(data);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            Mil_ftb_c_compat.LOGGER.error("[MillenaireAdapter] Error in getActiveVillages", t);
        }

        return result;
    }

    private static VillageData convertMillenaireVillage(Village village) {
        if (village == null) return null;
        try {
            String name = village.getVillageName();
            if (name == null) name = "Millenaire Village";

            String cultureKey = "unknown";
            ResourceLocation cultureId = village.getCultureId();
            if (cultureId != null) {
                cultureKey = cultureId.getPath();
            }

            BlockPos centerPos = village.getCenter();
            if (centerPos == null) {
                BuildingInstance th = village.getTownhall();
                if (th != null) {
                    centerPos = th.getOrigin();
                }
            }

            if (centerPos == null && village.computeBounds() != null) {
                centerPos = BlockPos.containing(village.computeBounds().getCenter());
            }

            if (centerPos == null && Minecraft.getInstance().player != null) {
                centerPos = Minecraft.getInstance().player.blockPosition();
            }

            int color = getCultureColor(cultureKey);
            VillageData data = new VillageData(name, cultureKey, centerPos, 90, color);

            Set<ChunkPos> chunkSet = village.getLoadedChunks();
            if (chunkSet == null || chunkSet.isEmpty()) {
                chunkSet = village.computeVillageChunks();
            }

            if (chunkSet != null) {
                for (ChunkPos cp : chunkSet) {
                    if (cp != null) {
                        data.addChunk(cp.x, cp.z);
                    }
                }
            }

            if (data.getExactChunks().isEmpty()) {
                populateChunksFromRadius(data);
            }

            return data;
        } catch (Throwable t) {
            Mil_ftb_c_compat.LOGGER.error("[MillenaireAdapter] Error converting village object", t);
        }
        return null;
    }

    private static void populateChunksFromRadius(VillageData data) {
        BlockPos center = data.getCenterPos();
        if (center == null) return;
        int radius = data.getRadiusBlocks();
        int centerChunkX = center.getX() >> 4;
        int centerChunkZ = center.getZ() >> 4;
        int chunkRadius = (radius + 15) >> 4;

        for (int cx = centerChunkX - chunkRadius; cx <= centerChunkX + chunkRadius; cx++) {
            for (int cz = centerChunkZ - chunkRadius; cz <= centerChunkZ + chunkRadius; cz++) {
                int blockX = (cx << 4) + 8;
                int blockZ = (cz << 4) + 8;
                double dx = blockX - center.getX();
                double dz = blockZ - center.getZ();
                if (dx * dx + dz * dz <= radius * radius) {
                    data.addChunk(cx, cz);
                }
            }
        }
    }

    public static int getCultureColor(String cultureKey) {
        if (cultureKey == null) return Config.defaultCultureColor;
        String key = cultureKey.toLowerCase(Locale.ROOT);

        for (Map.Entry<String, Integer> entry : Config.cultureColors.entrySet()) {
            if (key.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        try {
            Map<ResourceLocation, Culture> allCultures = ModCultures.getAllCultures();
            if (allCultures != null) {
                for (Map.Entry<ResourceLocation, Culture> entry : allCultures.entrySet()) {
                    ResourceLocation rl = entry.getKey();
                    if (rl != null && key.contains(rl.getPath().toLowerCase(Locale.ROOT))) {
                        return generateColorFromKey(rl.getPath());
                    }
                }
            }
        } catch (Throwable ignored) {
        }

        return generateColorFromKey(key);
    }

    private static int generateColorFromKey(String key) {
        int hash = Math.abs(key.hashCode());
        float hue = (hash % 360) / 360.0f;
        int rgb = java.awt.Color.HSBtoRGB(hue, 0.75f, 0.95f);
        return 0xFF000000 | (rgb & 0x00FFFFFF);
    }
}
