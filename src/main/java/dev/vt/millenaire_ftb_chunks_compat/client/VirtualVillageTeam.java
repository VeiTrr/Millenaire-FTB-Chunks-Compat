package dev.vt.millenaire_ftb_chunks_compat.client;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamMessage;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.property.TeamProperties;
import dev.ftb.mods.ftbteams.api.property.TeamProperty;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyValue;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class VirtualVillageTeam implements Team {
    private final UUID id;
    private final String name;
    private final Color4I color;
    private final Component componentName;
    private final TeamPropertyCollection properties;

    public VirtualVillageTeam(String villageName, String culture, int colorARGB) {
        this.id = UUID.nameUUIDFromBytes(("millenaire_village:" + villageName).getBytes(StandardCharsets.UTF_8));
        this.name = villageName + " (" + culture + ")";
        this.color = Color4I.rgb(colorARGB & 0x00FFFFFF).withAlpha(150);
        this.componentName = Component.literal(this.name).withStyle(Style.EMPTY.withColor(this.color.rgb()));

        this.properties = new TeamPropertyCollection() {
            @Override
            public <T> T get(TeamProperty<T> property) {
                return VirtualVillageTeam.this.getProperty(property);
            }

            @Override
            public <T> void set(TeamProperty<T> property, T t) {
            }

            @Override
            public int size() {
                return 2;
            }

            @Override
            public <T> void forEach(BiConsumer<TeamProperty<T>, TeamPropertyValue<T>> biConsumer) {
            }

            @Override
            public TeamPropertyCollection copy() {
                return this;
            }

            @Override
            public TeamPropertyCollection copyIf(Predicate<TeamProperty<?>> predicate) {
                return this;
            }

            @Override
            public void updateFrom(TeamPropertyCollection teamPropertyCollection) {
            }
        };
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getTeamId() {
        return id;
    }

    @Override
    public String getShortName() {
        return name;
    }

    @Override
    public UUID getOwner() {
        return id;
    }

    @Override
    public TeamPropertyCollection getProperties() {
        return properties;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(TeamProperty<T> property) {
        if (property == TeamProperties.COLOR) {
            return (T) color;
        }
        if (property == TeamProperties.DISPLAY_NAME) {
            return (T) name;
        }
        return property.getDefaultValue();
    }

    @Override
    public <T> void setProperty(TeamProperty<T> property, T t) {
    }

    @Override
    public boolean isServerTeam() {
        return true;
    }

    @Override
    public boolean isClientTeam() {
        return true;
    }

    @Override
    public List<TeamMessage> getMessageHistory() {
        return Collections.emptyList();
    }

    @Override
    public TeamRank getRankForPlayer(UUID uuid) {
        return TeamRank.MEMBER;
    }

    @Override
    public Component getName() {
        return componentName;
    }

    @Override
    public void sendMessage(UUID uuid, String s) {
    }

    @Override
    public void sendMessage(UUID uuid, Component component) {
    }

    @Override
    public List<Component> getTeamInfo() {
        return Collections.singletonList(componentName);
    }

    @Override
    public Map<UUID, TeamRank> getPlayersByRank(TeamRank teamRank) {
        return Collections.emptyMap();
    }

    @Override
    public Set<UUID> getMembers() {
        if (Minecraft.getInstance().player != null) {
            return Collections.singleton(Minecraft.getInstance().player.getUUID());
        }
        return Collections.emptySet();
    }

    @Override
    public String getTypeTranslationKey() {
        return "ftbteams.team_type.server";
    }

    @Override
    public CompoundTag getExtraData() {
        return new CompoundTag();
    }

    @Override
    public void markDirty() {
    }

    @Override
    public Collection<ServerPlayer> getOnlineMembers() {
        return Collections.emptyList();
    }

    @Override
    public Component getColoredName() {
        return componentName;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Team createParty(String s, Color4I color4I) {
        return this;
    }

    @Override
    public <T> void syncOnePropertyToAll(MinecraftServer minecraftServer, TeamProperty<T> teamProperty, T t) {
    }

    @Override
    public <T> void syncOnePropertyToTeam(TeamProperty<T> teamProperty, T t) {
    }
}
