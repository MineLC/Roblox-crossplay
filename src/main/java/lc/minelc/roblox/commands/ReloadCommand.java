package lc.minelc.roblox.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import lc.lcspigot.commands.Command;
import lc.minelc.roblox.RobloxCrossplayPlugin;
import lc.minelc.roblox.network.RobloxThread;

public final class ReloadCommand implements Command {

    private final RobloxCrossplayPlugin plugin;

    public ReloadCommand(RobloxCrossplayPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (!checkPermission(sender, "roblox.reload")) {
            return;
        }
        plugin.reloadConfig();
        RobloxThread.stopThread();
        final FileConfiguration config = plugin.getConfig();

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> RobloxThread.iniciate(config.getString("roblox-out-uri"), config.getString("roblox-in-uri")), 35);
        sender.sendMessage("Plugin reloaded!");
    }
}