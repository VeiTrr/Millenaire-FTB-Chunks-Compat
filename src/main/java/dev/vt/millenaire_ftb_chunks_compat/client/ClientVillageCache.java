package dev.vt.millenaire_ftb_chunks_compat.client;

import dev.vt.millenaire_ftb_chunks_compat.api.VillageData;
import dev.vt.millenaire_ftb_chunks_compat.millenaire.MillenaireAdapter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientVillageCache {
    private static final Map<String, VirtualVillageTeam> TEAM_CACHE = new HashMap<>();

    public static VirtualVillageTeam getVillageTeam(ResourceKey<Level> dimensionKey, int chunkX, int chunkZ) {
        if (dimensionKey == null) return null;
        long packed = (long) chunkX & 0xFFFFFFFFL | ((long) chunkZ & 0xFFFFFFFFL) << 32;
        List<VillageData> villages = MillenaireAdapter.getActiveVillages(dimensionKey);

        for (VillageData village : villages) {
            if (village.getExactChunks().contains(packed)) {
                return TEAM_CACHE.computeIfAbsent(village.getName(), key ->
                        new VirtualVillageTeam(village.getName(), village.getCulture(), village.getColorARGB())
                );
            }
        }

        return null;
    }
}
