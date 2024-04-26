package lc.minelc.roblox.network.deserializer;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import lc.minelc.roblox.storage.RobloxPlayers;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public final class JsonDeserializer {

    private static final Gson GSON = new Gson();
    private final JsonFormat format;

    public JsonDeserializer(String json) {
        this.format = GSON.fromJson(json, JsonFormat.class);
    }

    public MovePlayer[] getPlayersData() {
        final String[] playersData = format.players();
        final MovePlayer[] players = new MovePlayer[playersData.length];

        for (int i = 0; i < playersData.length; i++) {
            final String[] info = StringUtils.split(playersData[i], ',');
            final float x = Float.parseFloat(info[0]);
            final float y = Float.parseFloat(info[1]);
            final float z = Float.parseFloat(info[2]);
            final float yaw = Float.parseFloat(info[3]);
            final float pitch = Float.parseFloat(info[4]);
            final EntityPlayer robloxPlayer = RobloxPlayers.getInstance().getPlayer(info[5]);

            players[i] = new MovePlayer(x, y, z, yaw, pitch, robloxPlayer);
        }
        return players;
    }

    public EntityPlayer[] quitPlayers() {
        final String[] playersNames = format.quits();
        final EntityPlayer[] players = new EntityPlayer[playersNames.length];

        for (int i = 0; i < playersNames.length; i++) {
            players[i] = RobloxPlayers.getInstance().quit(playersNames[i]);
        }
        return players;
    }


    public String[] getPlayerJoins() {
        return format.joins();
    }

    public record MovePlayer(
        float x, float y, float z, float yaw, float pitch, EntityPlayer player
    ) {}
}