package lc.minelc.roblox.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import lc.lcspigot.commands.Command;
import lc.minelc.roblox.RobloxCrossplayPlugin;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

public class TestCommand implements Command {

    private final RobloxCrossplayPlugin plugin;

    public TestCommand(RobloxCrossplayPlugin playersPlugin) {
        this.plugin = playersPlugin;
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        final Player player = (Player)sender;
        final EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();

        final GameProfile profile = new GameProfile(UUID.randomUUID(), ".PepitoGamer");
        profile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTY4MDQ0NjY2ODM4NCwKICAicHJvZmlsZUlkIiA6ICI2NmI0ZDRlMTFlNmE0YjhjYTFkN2Q5YzliZTBhNjQ5OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJBcmFzdG9vWXNmIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZhODliZDY4YWRmMzFjODBkODlhMGJjYmQ5YTBkY2RhZDE3MmIxNWMxZDc1YzAxNWFiZTBkZWY4ZDEzNzE3ZGEiCiAgICB9CiAgfQp9", "azJU2p3owf7bvAPnO85TKF0taCsQCyRllrJEfn6FzTn01NXgJgmbLST/jpNcFbK82T+nm4JlF7uRDsJtXzcFVfAPIqrOzEElzNylXd0es8KDDBs49wJS0mDD3KTK/H0oDsEv3rwFof44gyyyZaEoGZkezymXBzlXsDeDT3zVWrBKCOETvK5skwVtVPI3fNckL/40Clro/GnxOnUEZHPzjxs60XxsaEt0QJb1GfY3W4pJ4XSdiV7YA+BM1je9hUOSD7yBOTjQAEl/UWr+Gl5LgJKfJ6Z2eot5N53C9s8OPOx/iQ3AIataFM4XD9AkaQYugDH4ku1aLng9mYpMehlGvrm0q8QO/+xIOkqAEDvSNaTY8gyyXlhLx2huVzTMDMFC+dStLpxGRqecP0k+4V9vev92BSGjUiD3bGA1Jv/WdwwoQvbml3FYEWh3uMwSrm4xyur7MO8aPsGW4l9o1HKOusjZGVHYG+jvV9VTWnyGTk1cMSuEjDE5N3PPnY3j9aI8WMZdPEK/020805a7y3IiI+AhKRfCdQsU/P6KyvmiEFU1Z+WpSgu7l2tUbPho25ApPKOd1vFixXcgZKKo8scSlDI/ecj0qDtlQI6gPyi0PdIgXd6d/wH6DQwXKX/3aIeK93xSEemR/fCL0d1JqbuAVCKfQCEE6WI/jxy97XaNNXk="));

        final EntityPlayer npc = new EntityPlayer(MinecraftServer.getServer(), ((WorldServer)entityPlayer.getWorld()), profile, new PlayerInteractManager(entityPlayer.world));
        npc.getBukkitEntity().setDisplayName("§c§lROBLOX §fPepitoGamer");
        npc.setCustomName("§c§lROBLOX §fPepitoGamer");
        npc.setCustomNameVisible(true);

        npc.listName = CraftChatMessage.fromString("§c§lROBLOX §fPepitoGamer")[0];
        npc.locX = entityPlayer.locX;
        npc.locY = entityPlayer.locY;
        npc.locZ = entityPlayer.locZ;

        npc.aK = entityPlayer.aK;
        npc.yaw = entityPlayer.yaw;
        npc.pitch = entityPlayer.pitch;
    
        final PacketPlayOutPlayerInfo tab = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc);
        final PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(npc);
        final PacketPlayOutEntityHeadRotation head = new PacketPlayOutEntityHeadRotation(npc, (byte)(npc.yaw * 256 / 360));

        entityPlayer.playerConnection.sendPacket(tab);
        entityPlayer.playerConnection.sendPacket(spawn);
        entityPlayer.playerConnection.sendPacket(head);

        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            final Packet<?> movePacket = moveEntity(npc, 0.001D, 0, 0);
            entityPlayer.playerConnection.sendPacket(movePacket);
        }, 0, 1);
    }

    private Packet<?> moveEntity(final EntityPlayer npc, final double blocksX, final double blocksY, final double blocksZ) {
        npc.locX += blocksX;
        npc.locY += blocksY;
        npc.locZ += blocksZ;
        if (blocksX > 8 || blocksY > 8 || blocksZ > 8) {
            return new PacketPlayOutEntityTeleport(npc);
        }
        Bukkit.broadcastMessage("A " + (byte)(blocksX * 4096D));
        return new PacketPlayOutEntity.PacketPlayOutRelEntityMove(npc.getId(), (byte)(blocksX * 4096D), (byte)(blocksY * 4096D), (byte)(blocksZ * 4096D), true);
    }
}