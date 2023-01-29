package main.minecraftplugin.PVPArena;

import com.sun.tools.javac.jvm.Items;
import main.minecraftplugin.MinecraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class StartPVP {

    public void startArena(UUID uuid) {

        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        ItemStack golden_apple = new ItemStack(Material.GOLDEN_APPLE, 8);
        Player player = Bukkit.getPlayer(uuid);
        PlayerInventory inventory = player.getInventory();
        Location location = new Location(player.getWorld(), -206, 67,124);

        inventory.clear();
        inventory.setItem(8, compass);
        inventory.setItem(0, sword);
        inventory.setItem(1, golden_apple);

        inventory.setHelmet(helmet);

        player.setGameMode(GameMode.SURVIVAL);

        player.setFoodLevel(20);
        player.setHealth(20);

        player.teleport(location);
    }

    public void stopArenaOrOther(UUID uuid){

        MinecraftPlugin.getInstance().data.setArena(uuid, 0);

        Player player = Bukkit.getPlayer(uuid);
        PlayerInventory inventory = player.getInventory();
        Location location = new Location(player.getWorld(), -229, 67, 103);

        inventory.clear();
        ItemStack compass = new ItemStack(Material.COMPASS);
        inventory.setItem(8, compass);

        player.setGameMode(GameMode.CREATIVE);

        player.teleport(location);
    }
}
