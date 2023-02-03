package main.minecraftplugin.Money;

import main.minecraftplugin.MinecraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ATMManagement {

    public void openATMMenue(Player player, String id){


        Inventory inventory = Bukkit.createInventory(null, 27, "Bankautomat");

        ItemStack addMoney = new ItemStack(Material.SLIME_BLOCK);
        ItemStack withdrawMoney = new ItemStack(Material.REDSTONE_BLOCK);
        ItemStack moneyStatus = new ItemStack(Material.PAPER);

        ItemMeta meta_slime = addMoney.getItemMeta();
        ItemMeta meta_redstone = withdrawMoney.getItemMeta();
        ItemMeta meta_paper = moneyStatus.getItemMeta();

        meta_slime.setDisplayName("Einzahlen");
        meta_redstone.setDisplayName("Auszahlen");
        meta_paper.setDisplayName("Kontostand: " + MinecraftPlugin.getInstance().moneyData.getMoneyBetrag(id.substring(2)));

        addMoney.setItemMeta(meta_slime);
        withdrawMoney.setItemMeta(meta_redstone);
        moneyStatus.setItemMeta(meta_paper);

        inventory.setItem(11, addMoney);
        inventory.setItem(15, withdrawMoney);
        inventory.setItem(8, moneyStatus);

        player.openInventory(inventory);
    }
}
