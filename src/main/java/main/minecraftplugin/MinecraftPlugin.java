package main.minecraftplugin;

import main.minecraftplugin.backpack.Backpack;
import main.minecraftplugin.backpack.BackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class MinecraftPlugin extends JavaPlugin {

    private static MinecraftPlugin instance;
    private BackpackManager backpackManager;
    private Config config;

    @Override
    public void onLoad() {
        instance = this;
        config = new Config();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager manager = Bukkit.getPluginManager();

        getCommand("backpack").setExecutor(new BackpackCommand());

        backpackManager = new BackpackManager();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        config.save();
        backpackManager.save();
    }

    public static MinecraftPlugin getInstance() {
        return instance;
    }

    public Config getConfiguration() {
        return config;
    }

    public BackpackManager getBackpackManager(){
        return backpackManager;
    }
}
