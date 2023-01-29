package main.minecraftplugin;

import main.minecraftplugin.PVPArena.StartPVP;
import main.minecraftplugin.SQL.MySQL;
import main.minecraftplugin.SQL.SQLGetter;
import main.minecraftplugin.backpack.Backpack;
import main.minecraftplugin.backpack.BackpackManager;
import main.minecraftplugin.listener.AttackListener;
import main.minecraftplugin.listener.ClickInMenuListener;
import main.minecraftplugin.listener.OpenMenuListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public final class MinecraftPlugin extends JavaPlugin implements Listener {

    // Man könnte möglicherweise alles in eine UTILS Datei verlagern wenn Zeit dafür ist!

    private static MinecraftPlugin instance;
    private BackpackManager backpackManager;
    private Config config;
    public MySQL SQL;
    public SQLGetter data;

    @Override
    public void onLoad() {
        instance = this;
        config = new Config();

    }

    @Override
    public void onEnable() {

        // TODO:
        //Implementieren eines Death Listeners, welcher einen, wenn man in der Arena ist, richtig teleportiert.
        //Vielleicht noch nen Scoreboard oder so
        //Ein Bus/Bahn System wäre auch gut im Portfolio
        //Implementiere PVP Arena, welche über den Kompass Erreichbar ist.
        //PVP nur in Arena. Attack Listener!
        // Implementiere Join Listener! Möglicherweise Willkommensnachricht, vielmehr jedoch Zum Spawn Teleportieren.



        //https://www.youtube.com/watch?v=AdFXiHC9ywM&t=1s
        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new OpenMenuListener(), this);
        manager.registerEvents(new AttackListener(), this);
        manager.registerEvents(new ClickInMenuListener(), this);
        manager.registerEvents(this, this);

        getCommand("backpack").setExecutor(new BackpackCommand());

        backpackManager = new BackpackManager();

        this.SQL = new MySQL();
        this.data = new SQLGetter(this);
        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().info("Database not connected!");
        }

        if(SQL.isConnected()){
            Bukkit.getLogger().info("Database is connected.");
            data.createTable();
            //this.getServer().getPluginManager().registerEvents(this, this);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        backpackManager.save();
        config.save();
        SQL.disconnect();
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

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.sendMessage("Willkommen du Knecht");
        data.createPlayer(player);
        data.setArena(player.getUniqueId(), 0);
    }

    public int getArenaStatusPoints(UUID uuid){
        return data.getArenaStatus(uuid);
    }

    //Das ist nicht optimal, es gibt aber irgendwie keine andere Möglichkeit. lol.
    //@EventHandler
    public void onClick(InventoryClickEvent event){

        Inventory inventory = event.getInventory();
        HumanEntity player = event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        if(inventory.getName() == "Lobby") {
            event.setCancelled(true);
            //System.out.println("In Lobby Menü");
            if(event.getSlot() == 11){
                Location location = new Location(player.getWorld(), -229, 67, 103);

                player.teleport(location);
            }
            if(event.getSlot() == 15){
                //Start PVP Arena. Kann man auch in SQL Datenbank abspeichern. Oder?
                //
                // startPVP.startArena(uuid);

                if(data.getArenaStatus(uuid) == 0){
                    //data.setArena(uuid, 1);
                    System.out.println("Arena Status auf 0. Starte Arena.");
                    data.setArena(uuid, 1);
                }
                else{
                    //data.setArena(uuid, 0);
                    System.out.println("Arena Status nicht auf 0. Beende Arena.");
                    data.setArena(uuid, 0);
                }

                //Weiterleitung in die Main Java Klasse?
                //int points = minecraftPlugin.getArenaStatusPoints(uuid);
                //System.out.println("Arena Status points: " + points)
            }

            System.out.println(event.getSlot());
        }
    }
}
