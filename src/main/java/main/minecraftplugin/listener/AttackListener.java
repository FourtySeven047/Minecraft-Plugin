package main.minecraftplugin.listener;

import main.minecraftplugin.MinecraftPlugin;
import main.minecraftplugin.PVPArena.StartPVP;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackListener implements Listener {

    StartPVP startPvp = new StartPVP();

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

        if(event.getEntity() instanceof Player){
            Player entity = (Player) event.getEntity();
            if((entity.getHealth() - event.getFinalDamage()) <= 0){
                event.setCancelled(true);
                if(event.getDamager() instanceof Player){
                    MinecraftPlugin.getInstance().arenaData.addPoint(event.getDamager().getUniqueId());
                }
                startPvp.startArena(entity.getUniqueId());
            }
        }




        //Abfrage ob man in der Arena ist. Wenn ja füge Punkte hinzu
        /*
        if(MinecraftPlugin.getInstance().data.getArenaStatus(event.getEntity().getUniqueId()) == 1 && MinecraftPlugin.getInstance().data.getArenaStatus(event.getDamager().getUniqueId()) == 1){
            Player entity = (Player) event.getEntity();
            if((entity.getHealth() - event.getFinalDamage()) <= 0){
                event.setCancelled(true);

                MinecraftPlugin.getInstance().arenaData.addPoint(event.getDamager().getUniqueId());

                startPvp.startArena(entity.getUniqueId());
            }
        }

         */
    }
}
