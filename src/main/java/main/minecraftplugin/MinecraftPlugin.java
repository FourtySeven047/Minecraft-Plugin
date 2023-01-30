package main.minecraftplugin;

import main.minecraftplugin.Money.MoneyManagement;
import main.minecraftplugin.PVPArena.StartPVP;
import main.minecraftplugin.SQL.ArenaPointsSQL;
import main.minecraftplugin.SQL.MySQL;
import main.minecraftplugin.SQL.SQLGetter;
import main.minecraftplugin.backpack.BackpackManager;
import main.minecraftplugin.listener.AttackListener;
import main.minecraftplugin.listener.ClickInMenuListener;
import main.minecraftplugin.listener.OpenMenuListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.UUID;

public final class MinecraftPlugin extends JavaPlugin implements CommandExecutor, Listener{

    // Man könnte möglicherweise alles in eine UTILS Datei verlagern wenn Zeit dafür ist!

    private static MinecraftPlugin instance;
    private BackpackManager backpackManager;
    private Config config;
    public MySQL SQL;
    public SQLGetter data;
    public ArenaPointsSQL arenaData;
    public MoneyManagement moneyData;
    StartPVP startPVP = new StartPVP();

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
        //Geld System mit Bankautomaten und Bargeld und so
        //Scoreboard oder Command to get wie viele Kills man hat in der Arena


        //https://www.youtube.com/watch?v=AdFXiHC9ywM&t=1s
        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new OpenMenuListener(), this);
        manager.registerEvents(new AttackListener(), this);
        manager.registerEvents(new ClickInMenuListener(), this);
        manager.registerEvents(this, this);
        getCommand("points").setExecutor(this);
        getCommand("createBankAccount").setExecutor(new MoneyManagement(this));

        getCommand("backpack").setExecutor(new BackpackCommand());

        backpackManager = new BackpackManager();

        this.SQL = new MySQL();
        this.data = new SQLGetter(this);
        this.arenaData = new ArenaPointsSQL(this);
        this.moneyData = new MoneyManagement(this);
        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().info("Database not connected!");
        }

        if(SQL.isConnected()){
            Bukkit.getLogger().info("Database is connected.");
            data.createTable();
            arenaData.createTable();
            moneyData.createTable();
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
        arenaData.createPlayer(player);
        startPVP.stopArenaOrOther(player.getUniqueId());
    }

    public int getArenaStatusPoints(UUID uuid){
        return data.getArenaStatus(uuid);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)) {return true;}
        Player player = (Player) sender;
        int points = arenaData.getArenaPoints(player.getUniqueId());
        player.sendMessage("Du hast im Moment " + points + " Kills in der Arena!");

        return true;
    }

}
