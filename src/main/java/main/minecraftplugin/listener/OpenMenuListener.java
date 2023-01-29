package main.minecraftplugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OpenMenuListener implements Listener {

    public Inventory inventory;



    @EventHandler
    public void openMenu(PlayerInteractEvent event){

        Player player = event.getPlayer();
        Action action = event.getAction();

        //System.out.println(event.getAction());

        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if(player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {


                this.inventory = Bukkit.createInventory(null, 27, "Lobby");

                ItemStack compass = new ItemStack(Material.COMPASS);

                ItemMeta meta_compass = compass.getItemMeta();

                meta_compass.setDisplayName("Lobby");

                compass.setItemMeta(meta_compass);

                inventory.setItem(11, compass);

                player.openInventory(inventory);

                //System.out.println("Wdjwdwdkwd");

            }
        }
    }
}
