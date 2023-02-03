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
import org.bukkit.GameMode;
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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
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
    public ClickInMenuListener clickData;
    StartPVP startPVP = new StartPVP();

    @Override
    public void onLoad() {
        instance = this;
        config = new Config();

    }

    @Override
    public void onEnable() {

        // TODO:
        //Implementieren eines Death Listeners, welcher einen, wenn man in der Arena ist, richtig teleportiert. Gemacht!
        //Vielleicht noch nen Scoreboard oder so. Ja, aber dafuer brauche ich viel Geduld.
        //Ein Bus/Bahn System wäre auch gut im Portfolio.
        //Geld System mit Bankautomaten und Bargeld und so. Geschafft!
        //Scoreboard oder Command to get wie viele Kills man hat in der Arena
        //Vielleicht ein Trading System, mit welchem man items mit spielern sicher handeln kann.
        //Rang System, welcher jedem Spieler einen  Rang zuweist. THIS IS NEXT!

        // FEATURES:
        //
        //Lobby Kompass, mit welchem man sich zum Spawn Point, oder in die Kampfarena teleportieren kann.
        //Geld System, mit welchem man Geld von seinem Konto einzahlen und abheben kann. über eine Bankkarte. Von dieser aus kann ich auch Geld von anderen Spielern abheben. Alle Daten diesbezüglich werden in einer SQL Datenbank geschrieben
        //PIN System ist noch nicht implementiert. Man erstellt sich über /createbankaccount ein Konto. Passiert noch nicht automatisch. Bargeld "Account" wird jedoch beim ersten Joinen hinzugefügt.
        // Außerdem besteht die Möglichkeit, anderen Spielern sein Bargeld zu geben.
        //Es gibt eine Kampfarena, bei welcher man sich mit anderen Spielern in Sachen PVP messen kann. Die Anzahl der Kills, welche man bereits errungen hat, ist ueber /kills einsehbar. Daten werden in einer SQL Datenbank gespeichert.
        //Zudem gibt es ein Backpack System. Der Backpack eines Spielers ist über /backpack oder kürzer /bp zu erreichen. Die Daten aller Spieler Backpacks werden in einem Textdokument im Base64 Format gespeichert. Nicht in einer MySQL Datenbank.
        //Zudem gibt es einige Listener, welche zur Verwirklichung aller oben genannten Projekte verwendet bzw. geraucht wurden.


        //https://www.youtube.com/watch?v=AdFXiHC9ywM&t=1s
        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new OpenMenuListener(), this);
        manager.registerEvents(new AttackListener(), this);
        manager.registerEvents(new ClickInMenuListener(), this);
        manager.registerEvents(this, this);
        getCommand("points").setExecutor(this);
        getCommand("createBankAccount").setExecutor(new MoneyManagement(this));
        getCommand("addMoney").setExecutor(new MoneyManagement(this));

        getCommand("backpack").setExecutor(new BackpackCommand());

        backpackManager = new BackpackManager();

        this.SQL = new MySQL();
        this.data = new SQLGetter(this);
        this.arenaData = new ArenaPointsSQL(this);
        this.moneyData = new MoneyManagement(this);
        this.clickData = new ClickInMenuListener();
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
            moneyData.createCashTable();
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
        moneyData.createCashAccount(event.getPlayer());
        //startPVP.stopArenaOrOther(player.getUniqueId());
        //On Leave muss ich dann noch machen, oder?

        MinecraftPlugin.getInstance().data.setArena(player.getUniqueId(), 0);

        Location location = new Location(player.getWorld(), -229, 67, 103);

        player.setGameMode(GameMode.CREATIVE);
        player.teleport(location);

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
