package lc.minelc.roblox.tasks.types;

import java.util.List;

import lc.minelc.roblox.network.deserializer.JsonDeserializer;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;

public class MovePlayersTask {
    
    public void execute(final JsonDeserializer.MovePlayer[] robloxData) {
        final List<EntityPlayer> minecraftPlayers = MinecraftServer.getServer().getPlayerList().players;

        for (final JsonDeserializer.MovePlayer moveData : robloxData) {
            final EntityPlayer player = moveData.player();
            if (player.locX == moveData.x() && player.locY == moveData.y() && player.locZ == moveData.z()) {
                continue;
            }

            player.aK = moveData.yaw();
            player.yaw = moveData.yaw();
            player.pitch = moveData.pitch();

            final Packet<?> movePacket = moveEntity(
                player,
                player.locX - moveData.x(),
                player.locY - moveData.y(),
                player.locZ - moveData.z()
            );
            final PacketPlayOutEntityHeadRotation head = new PacketPlayOutEntityHeadRotation(player, (byte)(player.yaw * 256 / 360));

            final int viewDistance = 20;
            final double minX = player.locX - viewDistance;
            final double maxX = player.locX + viewDistance;
            final double minY = player.locX - viewDistance;
            final double maxY = player.locX + viewDistance;
            final double minZ = player.locX - viewDistance;
            final double maxZ = player.locX + viewDistance;

            for (final EntityPlayer minecraftPlayer : minecraftPlayers) {
                if (minecraftPlayer.locX >= minX && minecraftPlayer.locX <= maxX
                    && minecraftPlayer.locY >= minY && minecraftPlayer.locY <= maxY
                    && minecraftPlayer.locZ >= minZ && minecraftPlayer.locZ <= maxZ
                ) {
                    minecraftPlayer.playerConnection.networkManager.handle(movePacket);
                    minecraftPlayer.playerConnection.networkManager.handle(head);
                }
            }
        }
    }

    private Packet<?> moveEntity(final EntityPlayer roblox, final double blocksX, final double blocksY, final double blocksZ) {
        roblox.locX += blocksX;
        roblox.locY += blocksY;
        roblox.locZ += blocksZ;
        if (blocksX > 8 || blocksY > 8 || blocksZ > 8) {
            return new PacketPlayOutEntityTeleport(roblox);
        }
        return new PacketPlayOutEntity.PacketPlayOutRelEntityMove(roblox.getId(), (byte)(blocksX * 4096D), (byte)(blocksY * 4096D), (byte)(blocksZ * 4096D), true);
    }
}