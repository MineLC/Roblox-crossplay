package lc.minelc.roblox.tasks.types;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import lc.minelc.roblox.storage.RobloxPlayers;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

public final class JoinPlayersTask {

    public void execute(final String[] playerNames) {
        final List<EntityPlayer> minecraftPlayers = MinecraftServer.getServer().getPlayerList().players;

        final WorldServer world = ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle();
        final EntityPlayer[] robloxPlayers = new EntityPlayer[playerNames.length];
        int i = 0;

        for (final String playerName : playerNames) {
            final EntityPlayer roblox = createRobloxPlayer(world, playerName);
            RobloxPlayers.getInstance().join(playerName, roblox);
            robloxPlayers[i++] = roblox; 
        }

        final PacketPlayOutPlayerInfo tab = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, robloxPlayers);

        for (final EntityPlayer player : minecraftPlayers) {
            player.playerConnection.networkManager.handle(tab);

            for (final EntityPlayer robloxPlayer : robloxPlayers) {
                player.playerConnection.networkManager.handle(new PacketPlayOutNamedEntitySpawn(robloxPlayer));
                player.playerConnection.networkManager.handle(new PacketPlayOutEntityHeadRotation(robloxPlayer, (byte)(robloxPlayer.yaw * 256 / 360)));
            }
        }
    }

    private EntityPlayer createRobloxPlayer(WorldServer world, String name) {
        final GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        profile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTY4MDQ0NjY2ODM4NCwKICAicHJvZmlsZUlkIiA6ICI2NmI0ZDRlMTFlNmE0YjhjYTFkN2Q5YzliZTBhNjQ5OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJBcmFzdG9vWXNmIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZhODliZDY4YWRmMzFjODBkODlhMGJjYmQ5YTBkY2RhZDE3MmIxNWMxZDc1YzAxNWFiZTBkZWY4ZDEzNzE3ZGEiCiAgICB9CiAgfQp9", "azJU2p3owf7bvAPnO85TKF0taCsQCyRllrJEfn6FzTn01NXgJgmbLST/jpNcFbK82T+nm4JlF7uRDsJtXzcFVfAPIqrOzEElzNylXd0es8KDDBs49wJS0mDD3KTK/H0oDsEv3rwFof44gyyyZaEoGZkezymXBzlXsDeDT3zVWrBKCOETvK5skwVtVPI3fNckL/40Clro/GnxOnUEZHPzjxs60XxsaEt0QJb1GfY3W4pJ4XSdiV7YA+BM1je9hUOSD7yBOTjQAEl/UWr+Gl5LgJKfJ6Z2eot5N53C9s8OPOx/iQ3AIataFM4XD9AkaQYugDH4ku1aLng9mYpMehlGvrm0q8QO/+xIOkqAEDvSNaTY8gyyXlhLx2huVzTMDMFC+dStLpxGRqecP0k+4V9vev92BSGjUiD3bGA1Jv/WdwwoQvbml3FYEWh3uMwSrm4xyur7MO8aPsGW4l9o1HKOusjZGVHYG+jvV9VTWnyGTk1cMSuEjDE5N3PPnY3j9aI8WMZdPEK/020805a7y3IiI+AhKRfCdQsU/P6KyvmiEFU1Z+WpSgu7l2tUbPho25ApPKOd1vFixXcgZKKo8scSlDI/ecj0qDtlQI6gPyi0PdIgXd6d/wH6DQwXKX/3aIeK93xSEemR/fCL0d1JqbuAVCKfQCEE6WI/jxy97XaNNXk="));

        final EntityPlayer roblox = new EntityPlayer(MinecraftServer.getServer(), world, profile, new PlayerInteractManager(world));
        roblox.getBukkitEntity().setDisplayName("§c§lROBLOX §f" + name);
        roblox.setCustomName("§c§lROBLOX §f" + name);
        roblox.setCustomNameVisible(true);

        roblox.listName = CraftChatMessage.fromString("§c§lROBLOX §f"+ name)[0];
        roblox.locX = world.getSpawn().getX();
        roblox.locY = world.getSpawn().getY();
        roblox.locZ = world.getSpawn().getZ();
    
        return roblox;
    }
}