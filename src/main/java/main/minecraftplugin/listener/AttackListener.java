package main.minecraftplugin.listener;

import main.minecraftplugin.MinecraftPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackListener implements Listener {

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player){
              System.out.println("Player made damage");
              Entity player = event.getDamager();

              //System.out.println("Blablalba");
              //System.out.println(MinecraftPlugin.getInstance().getArenaStatusPoints(player.getUniqueId()));

            if(MinecraftPlugin.getInstance().getArenaStatusPoints(player.getUniqueId()) == 0){
                event.setCancelled(true);
            }
        }
    }
}
