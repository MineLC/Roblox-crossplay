package lc.minelc.roblox;

import org.bukkit.plugin.java.JavaPlugin;
import org.tinylog.Logger;

import lc.lcspigot.commands.CommandStorage;
import lc.lcspigot.configuration.LCConfig;
import lc.minelc.roblox.commands.ReloadCommand;
import lc.minelc.roblox.commands.TestCommand;
import lc.minelc.roblox.network.RobloxThread;

public final class RobloxCrossplayPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (!LCConfig.getConfig().robloxSupport) {
            Logger.warn("Roblox Players Plugin can't iniciate because 'roblox-support' is disable");
            return;
        }

        CommandStorage.register(new TestCommand(this), "test");
        CommandStorage.register(new ReloadCommand(this), "rmc");

        RobloxThread.iniciate(getConfig().getString("roblox-out-uri"), getConfig().getString("roblox-in-uri"));
    }

    @Override
    public void onDisable() {
        RobloxThread.stopThread();
    }
}