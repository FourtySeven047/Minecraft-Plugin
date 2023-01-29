package main.minecraftplugin.listener;

import main.minecraftplugin.MinecraftPlugin;
import main.minecraftplugin.PVPArena.StartPVP;
import main.minecraftplugin.SQL.MySQL;
import main.minecraftplugin.SQL.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import sun.awt.OSInfo;
import java.lang.String;
import java.util.UUID;


public class ClickInMenuListener implements Listener {


    StartPVP startPVP = new StartPVP();
    //public SQLGetter data;

    //public SQLGetter data;
    //MinecraftPlugin minecraftPlugin = new MinecraftPlugin();

    @EventHandler
    public void onClick(InventoryClickEvent event){

        Inventory inventory = event.getInventory();
        HumanEntity player = event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        if(inventory.getName() == "Lobby") {
            event.setCancelled(true);
            //System.out.println("In Lobby Menü");
            if(event.getSlot() == 11){
                //Location location = new Location(player.getWorld(), -229, 67, 103);
                //player.teleport(location);

                startPVP.stopArenaOrOther(uuid);
            }
            if(event.getSlot() == 15){

                if(MinecraftPlugin.getInstance().getArenaStatusPoints(uuid) == 0){

                    System.out.println("Arena Status auf 0. Starte Arena.");
                    MinecraftPlugin.getInstance().data.setArena(uuid, 1);
                    startPVP.startArena(uuid);

                }
                else{

                    System.out.println("Arena Status nicht auf 0. Beende Arena.");
                    startPVP.stopArenaOrOther(uuid);
                    //MinecraftPlugin.getInstance().data.setArena(uuid, 0);
                }
            }
            System.out.println(event.getSlot());
        }
    }
}
