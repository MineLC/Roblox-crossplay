package lc.minelc.roblox.tasks.types;

import java.util.List;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class QuitPlayersTask {

    public void execute(final EntityPlayer[] players) {
        final List<EntityPlayer> minecraftPlayers = MinecraftServer.getServer().getPlayerList().players;
        final PacketPlayOutPlayerInfo tab = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, players);

        for (final EntityPlayer player : minecraftPlayers) {
            player.playerConnection.networkManager.handle(tab);
        }
    }
}