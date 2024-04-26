package lc.minelc.roblox.storage;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.v1_8_R3.EntityPlayer;

public final class RobloxPlayers {
    private static final RobloxPlayers INSTANCE = new RobloxPlayers();

    private final Map<String, EntityPlayer> players = new HashMap<>();

    public EntityPlayer getPlayer(final String name) {
        return players.get(name);
    }

    public void join(final String name, final EntityPlayer player) {
        this.players.put(name, player);
    }

    public EntityPlayer quit(final String name) {
        return players.remove(name);
    }

    public static RobloxPlayers getInstance() {
        return INSTANCE;
    }
}