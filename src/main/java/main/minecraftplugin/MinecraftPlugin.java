package main.minecraftplugin;

import main.minecraftplugin.backpack.Backpack;
import main.minecraftplugin.backpack.BackpackManager;
import main.minecraftplugin.listener.ClickInMenuListener;
import main.minecraftplugin.listener.OpenMenuListener;
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
        //Ein Bus/Bahn System wäre auch gut im Portfolio
        //Implementiere PVP Arena, welche über den Kompass Erreichbar ist.
        //PVP nur in Arena. Attack Listener!
        // Implementiere Join Listener! Möglicherweise Willkommensnachricht, vielmehr jedoch Zum Spawn Teleportieren.

        //https://www.youtube.com/watch?v=AdFXiHC9ywM&t=1s
        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new OpenMenuListener(), this);
        manager.registerEvents(new ClickInMenuListener(), this);

        getCommand("backpack").setExecutor(new BackpackCommand());

        backpackManager = new BackpackManager();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        backpackManager.save();
        config.save();
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
