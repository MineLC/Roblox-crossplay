package lc.minelc.roblox.network.serializer;

import java.util.List;
import java.util.Set;

import lc.lcspigot.roblox.BlockData;
import lc.lcspigot.roblox.RobloxData;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public final class JsonSerializer {

    public String createFormat() {
        final Set<BlockData> blocks = RobloxData.getInstance().getBlocks();
        final StringBuilder builder = new StringBuilder(blocks.size() * 32 + MinecraftServer.getServer().getPlayerList().players.size() * 32);

        builder.append('{');
        appendPlayers(builder);
        builder.append(',');
        appendPlayers("joins", builder, RobloxData.getInstance().getJoins());
        builder.append(',');
        appendPlayers("quits", builder, RobloxData.getInstance().getQuits());
        builder.append(',');
        appendBlocks(builder);
        builder.append('}');

        return builder.toString();
    }

    public void appendPlayers(final StringBuilder builder) {
        final List<EntityPlayer> players = MinecraftServer.getServer().getPlayerList().players;
        int remainPlayers = players.size();
        builder.append("\"players\":[");
        if (remainPlayers == 0) {
            builder.append(']');
            return;
        }
        for (final EntityPlayer player : players) {
            --remainPlayers;
            builder.append('\"');
            builder.append(roundDown2(player.locX));
            builder.append(',');
            builder.append(roundDown2(player.locY));
            builder.append(',');
            builder.append(roundDown2(player.locZ));
            builder.append(',');
            builder.append(roundDown2(player.aK));
            builder.append(',');
            builder.append(roundDown2(player.pitch));
            builder.append(',');
            builder.append(player.getName());
            if (remainPlayers == 0) {
                builder.append("\"]");        
                continue;
            }
            builder.append("\",");
        }
    }

    public void appendBlocks(final StringBuilder builder) {
        final Set<BlockData> blocks = RobloxData.getInstance().getBlocks();
        int remainBlocks = blocks.size();
        builder.append("\"changedBlocks\": [");
        if (remainBlocks == 0) {
            builder.append(']');
            return;
        }
        for (final BlockData data : blocks) {
            --remainBlocks;
            builder.append('\"');
            builder.append(data.x());
            builder.append(',');
            builder.append(data.y());
            builder.append(',');
            builder.append(data.z());
            builder.append(',');
            builder.append(data.getMaterial().name());
            if (remainBlocks == 0) {
                builder.append("\"]");        
                continue;
            }
            builder.append("\",");
        }
    }

    public void appendPlayers(final String key, final StringBuilder builder, final Set<EntityPlayer> players) {
        builder.append('"');
        builder.append(key);
        builder.append('"');
        builder.append(':');
        builder.append('[');
        int remainPlayers = players.size();
        if (remainPlayers == 0) {
            builder.append(']');
            return;
        }
        for (final EntityPlayer player : players) {
            --remainPlayers;
            builder.append('"');
            builder.append(player.getName());
            if (remainPlayers == 0) {
                builder.append("\"]");        
                continue;
            }
            builder.append("\",");
        }
    }

    private static float roundDown2(double d) {
        return (float) (((long)(d * 1e2)) / 1e2);
    }
}