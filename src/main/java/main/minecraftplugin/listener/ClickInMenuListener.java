package main.minecraftplugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import sun.awt.OSInfo;
import java.lang.String;


public class ClickInMenuListener implements Listener {


    @EventHandler
    public void onClick(InventoryClickEvent event){

        Inventory inventory = event.getInventory();

        HumanEntity player = event.getWhoClicked();

        if(inventory.getName() == "Lobby") {
            event.setCancelled(true);
            //System.out.println("In Lobby Menü");
            if(event.getSlot() == 11){
                Location location = new Location(player.getWorld(), -229, 67, 103);

                player.teleport(location);
            }

            System.out.println(event.getSlot());

        }
    }
}
