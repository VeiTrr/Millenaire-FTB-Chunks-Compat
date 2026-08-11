package dev.vt.millenaire_ftb_chunks_compat.server;

import dev.ftb.mods.ftbchunks.api.ChunkTeamData;
import dev.ftb.mods.ftbchunks.api.ClaimedChunkManager;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamManager;
import dev.vt.millenaire_ftb_chunks_compat.Config;
import dev.vt.millenaire_ftb_chunks_compat.Mil_ftb_c_compat;
import dev.vt.millenaire_ftb_chunks_compat.api.VillageData;
import dev.vt.millenaire_ftb_chunks_compat.millenaire.MillenaireAdapter;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@EventBusSubscriber(modid = Mil_ftb_c_compat.MODID)
public class VillageClaimManager {

    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        // Only run FTB Chunks server team claiming if protection is explicitly enabled in config
        if (!Config.enableFtbProtection) {
            return;
        }

        tickCounter++;
        if (tickCounter % 100 != 0) { // Run sync every 5 seconds (100 ticks)
            return;
        }

        MinecraftServer server = event.getServer();
        if (server == null) return;

        if (!FTBTeamsAPI.api().isManagerLoaded() || !FTBChunksAPI.api().isManagerLoaded()) {
            return;
        }

        try {
            TeamManager teamManager = FTBTeamsAPI.api().getManager();
            ClaimedChunkManager chunkManager = FTBChunksAPI.api().getManager();
            CommandSourceStack source = server.createCommandSourceStack();

            List<VillageData> villages = MillenaireAdapter.getActiveVillages();
            for (VillageData village : villages) {
                syncVillageClaim(server, teamManager, chunkManager, source, village);
            }
        } catch (Throwable t) {
            Mil_ftb_c_compat.LOGGER.error("[VillageClaimManager] Error syncing village claims", t);
        }
    }

    private static void syncVillageClaim(MinecraftServer server, TeamManager teamManager, ClaimedChunkManager chunkManager, CommandSourceStack source, VillageData village) {
        String teamName = "millenaire_" + village.getName().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]", "_");
        String displayName = village.getName() + " (" + village.getCulture() + ")";
        int color = village.getColorARGB() & 0x00FFFFFF;

        UUID teamUUID = UUID.nameUUIDFromBytes(("millenaire:" + village.getName()).getBytes(StandardCharsets.UTF_8));

        Team team = teamManager.getTeamByID(teamUUID).orElse(null);
        if (team == null) {
            team = teamManager.getTeamByName(teamName).orElse(null);
        }

        if (team == null) {
            try {
                team = teamManager.createServerTeam(source, teamName, displayName, Color4I.rgb(color), teamUUID);
            } catch (Throwable t) {
                return;
            }
        }

        ChunkTeamData teamData = chunkManager.getOrCreateData(team);
        if (teamData == null) return;

        ResourceKey<Level> overworld = Level.OVERWORLD;

        for (Long chunkKey : village.getExactChunks()) {
            int cx = (int) (long) chunkKey;
            int cz = (int) (chunkKey >> 32);

            ChunkDimPos cdp = new ChunkDimPos(overworld, cx, cz);
            if (chunkManager.getChunk(cdp) == null) {
                try {
                    teamData.claim(source, cdp, false);
                } catch (Throwable ignored) {
                }
            }
        }
    }
}
