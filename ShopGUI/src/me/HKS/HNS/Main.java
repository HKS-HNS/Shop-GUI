package me.HKS.HNS;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.HKS.HNS.Gui.Gui;
import net.milkbowl.vault.economy.Economy;

/***
 * 
 * Main Class
 * 
 * @author HKS-HNS
 * 
 */

public class Main extends JavaPlugin {
    public static Main Instance;
    private static Economy econ = null;

    @Override
    public void onEnable() {
    	// Tests if Vault exists
        if (!setupEconomy()) {
            Bukkit.getLogger().warning(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.getCommand("Shop").setExecutor((CommandExecutor) new Config());
        this.getServer().getPluginManager().registerEvents(new Gui(), (Plugin) this);
        Config.config();
    }

    // Returns the value if vault exists and creates the service provider for the Vault API
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider < Economy > rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}