package main.minecraftplugin.listener;

import main.minecraftplugin.MinecraftPlugin;
import main.minecraftplugin.Money.ATMManagement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.UUID;

public class OpenMenuListener implements Listener {

    public Inventory inventory;

    ATMManagement atmManagement = new ATMManagement();


    /*
    @EventHandler
    public void onRightClickOnPlayer(PlayerInteractEntityEvent event){

        if(!(event.getRightClicked() instanceof Player)){
            return;
        }

        if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.PAPER || event.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS) {
            return;
        }

        Player player = (Player) event.getRightClicked();


        this.inventory = Bukkit.createInventory(null, 27, "Interaktionen");

        ItemStack giveMoney = new ItemStack(Material.SLIME_BLOCK);

        ItemMeta meta_slime = giveMoney.getItemMeta();

        meta_slime.setDisplayName("Geld geben");

        giveMoney.setItemMeta(meta_slime);

        inventory.setItem(11, giveMoney);

        event.getPlayer().openInventory(inventory);

        System.out.println(player.getUniqueId());

        System.out.println(event.getRightClicked().getUniqueId());
        //MinecraftPlugin.getInstance().clickData.playerToAddMoney.set(0, player.getUniqueId().toString());

    }

     */


    @EventHandler
    public void openMenu(PlayerInteractEvent event){

        Player player = event.getPlayer();
        Action action = event.getAction();

        //System.out.println(event.getAction());

        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if(player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {


                this.inventory = Bukkit.createInventory(null, 27, "Lobby");

                ItemStack compass = new ItemStack(Material.COMPASS);
                ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);


                ItemMeta meta_compass = compass.getItemMeta();
                ItemMeta meta_sword = sword.getItemMeta();

                meta_compass.setDisplayName("Lobby");
                meta_sword.setDisplayName("PVP Arena");

                compass.setItemMeta(meta_compass);
                sword.setItemMeta(meta_sword);

                inventory.setItem(11, compass);
                inventory.setItem(15, sword);

                player.openInventory(inventory);

                //System.out.println("Wdjwdwdkwd");

            }
        }

        if(action == Action.RIGHT_CLICK_BLOCK){
            //System.out.println("Rechtsklick");
            if(event.getClickedBlock().getType() == Material.TRIPWIRE_HOOK){
                //System.out.println("Nen Knecht hat nh Tripwire angeklickt");
                if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.PAPER){
                    //System.out.println("Und hat nen Papier in der Hand");
                    //Start Bankautomat
                    ItemStack card = new ItemStack(event.getPlayer().getInventory().getItemInMainHand());

                    ItemMeta meta = card.getItemMeta();

                    if (meta.getLore() == null) {
                        return;
                    }

                    List<String> metaLore = new ArrayList<>();
                    metaLore = meta.getLore();
                    String id = metaLore.get(0);

                    if(MinecraftPlugin.getInstance().moneyData.exists(id.substring(2))){
                        System.out.println("Player hat Konto!");

                        atmManagement.openATMMenue(player, id);

                        /*
                        this.inventory = Bukkit.createInventory(null, 27, "Bankautomat");

                        ItemStack addMoney = new ItemStack(Material.SLIME_BLOCK);
                        ItemStack withdrawMoney = new ItemStack(Material.REDSTONE_BLOCK);
                        ItemStack moneyStatus = new ItemStack(Material.PAPER);

                        ItemMeta meta_slime = addMoney.getItemMeta();
                        ItemMeta meta_redstone = withdrawMoney.getItemMeta();
                        ItemMeta meta_paper = moneyStatus.getItemMeta();

                         */


                    }
                    //System.out.println(id.substring(2));
                }
            }
        }
        /*
        Es gab hier irgendeine Fehlermeldung. Austesten!
        if(action == Action.RIGHT_CLICK_BLOCK && player.getInventory().getItemInMainHand().getType() == Material.PAPER){

            //SQL Databse Implementation. Check if Block Location is in Database.
            //ID ist dann ein hash aus irgendeiner uniqen Info wie die Location?

            System.out.println(event.getClickedBlock().getLocation().toString());

            ItemStack item = player.getInventory().getItemInMainHand();

            ItemMeta meta = item.getItemMeta();


        }

         */
    }
}
