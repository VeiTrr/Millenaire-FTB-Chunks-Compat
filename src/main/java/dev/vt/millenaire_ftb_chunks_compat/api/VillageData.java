package dev.vt.millenaire_ftb_chunks_compat.api;

import net.minecraft.core.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class VillageData {
    private final String name;
    private final String culture;
    private final BlockPos centerPos;
    private final int radiusBlocks;
    private final int colorARGB;
    private final Set<Long> exactChunks;

    public VillageData(String name, String culture, BlockPos centerPos, int radiusBlocks, int colorARGB) {
        this.name = name;
        this.culture = culture;
        this.centerPos = centerPos;
        this.radiusBlocks = radiusBlocks;
        this.colorARGB = colorARGB;
        this.exactChunks = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getCulture() {
        return culture;
    }

    public BlockPos getCenterPos() {
        return centerPos;
    }

    public int getRadiusBlocks() {
        return radiusBlocks;
    }

    public int getColorARGB() {
        return colorARGB;
    }

    public Set<Long> getExactChunks() {
        return exactChunks;
    }

    public void addChunk(int chunkX, int chunkZ) {
        long key = (long) chunkX & 0xFFFFFFFFL | ((long) chunkZ & 0xFFFFFFFFL) << 32;
        exactChunks.add(key);
    }
}
