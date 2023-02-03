package main.minecraftplugin.listener;

import main.minecraftplugin.MinecraftPlugin;
import main.minecraftplugin.Money.ATMManagement;
import main.minecraftplugin.PVPArena.StartPVP;
import main.minecraftplugin.SQL.MySQL;
import main.minecraftplugin.SQL.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import sun.awt.OSInfo;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarException;


public class ClickInMenuListener implements Listener {


    StartPVP startPVP = new StartPVP();
    public boolean getChatData = false;
    public float einzahlZahl = 0;
    public List<UUID> playerArray  = new ArrayList<>();
    public List<String> inaccuratePlayerArray  = new ArrayList<>();
    public List<String> idArray  = new ArrayList<>();
    public List<String> playerToAddMoney  = new ArrayList<>();
    public ATMManagement atmManagement = new ATMManagement();
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

        if(inventory.getName() == "Bankautomat"){
            event.setCancelled(true);

            if(event.getSlot() == 11){
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                ItemMeta itemMeta = itemStack.getItemMeta();


                List<String> metaLore = new ArrayList<>();
                metaLore = itemMeta.getLore();
                String id = metaLore.get(0);

                playerArray.clear();
                inaccuratePlayerArray.clear();
                idArray.clear();

                getChatData = true;
                player.sendMessage("&aGebe ein, wie viel Geld du einzahlen möchtest: ");
                player.closeInventory();
                playerArray.add(player.getUniqueId());
                inaccuratePlayerArray.add(id);
                inaccuratePlayerArray.add("add");
                idArray.add(id);
            }

            if(event.getSlot() == 15){
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                ItemMeta itemMeta = itemStack.getItemMeta();


                List<String> metaLore = new ArrayList<>();
                metaLore = itemMeta.getLore();
                String id = metaLore.get(0);

                getChatData = true;

                playerArray.clear();
                inaccuratePlayerArray.clear();
                idArray.clear();

                player.sendMessage("&aGebe ein, wie viel Geld du abheben möchtest: ");
                player.closeInventory();
                playerArray.add(player.getUniqueId());
                inaccuratePlayerArray.add(id);
                inaccuratePlayerArray.add("subtract");
                idArray.add(id);

            }
        }

        if(inventory.getName() == "Interaktionen"){
            event.setCancelled(true);
            if(event.getSlot() == 11){


                getChatData = true;

                playerArray.clear();
                inaccuratePlayerArray.clear();
                idArray.clear();

                inaccuratePlayerArray.add("filler");
                inaccuratePlayerArray.add("topplayer");

                player.sendMessage("&aGebe ein, wie viel Geld du dem Spieler geben moechtest: ");
                player.closeInventory();
                playerArray.add(uuid);
            }
        }
    }
    @EventHandler
    public void onChat(PlayerChatEvent event){
        System.out.println("Player Message: " + event.getMessage());

        if(playerArray.contains(event.getPlayer().getUniqueId())){

            if(inaccuratePlayerArray.get(1) == "add"){

                if(Float.parseFloat(event.getMessage()) > 0){
                    event.setCancelled(true);
                    if(Float.parseFloat(event.getMessage()) > MinecraftPlugin.getInstance().moneyData.getCashBetrag(event.getPlayer().getUniqueId().toString()))
                    {
                        event.getPlayer().sendMessage("Du hast dafuer zu wenig Bargeld bei dir!");
                        playerArray.clear();
                        inaccuratePlayerArray.clear();
                        idArray.clear();
                        return;
                    }
                    //Muss nochmal getestet werden.
                    float addValue = Float.parseFloat(event.getMessage());
                    MinecraftPlugin.getInstance().moneyData.addMoney(inaccuratePlayerArray.get(0), Float.parseFloat(event.getMessage()));
                    MinecraftPlugin.getInstance().moneyData.addCash(event.getPlayer().getUniqueId().toString(), addValue *= -1);
                    atmManagement.openATMMenue(event.getPlayer(), inaccuratePlayerArray.get(0));
                    playerArray.clear();
                    inaccuratePlayerArray.clear();
                    idArray.clear();
                }
                else{
                    event.getPlayer().sendMessage("Du musst eine gueltige Zahl angeben!");
                    event.setCancelled(true);

                    playerArray.clear();
                    inaccuratePlayerArray.clear();
                    idArray.clear();

                    return;
                }

            }

            if(inaccuratePlayerArray.get(1) == "subtract"){

                if(Float.parseFloat(event.getMessage()) > 0){
                    event.setCancelled(true);
                    float subtractValue = Float.parseFloat(event.getMessage());
                    if(subtractValue > MinecraftPlugin.getInstance().moneyData.getMoneyBetrag(playerArray.get(0).toString())){
                        event.getPlayer().sendMessage("Du hast dafuer nicht genuegend Geld auf deinem Konto!");
                        playerArray.clear();
                        inaccuratePlayerArray.clear();
                        idArray.clear();
                        return;
                    }
                    MinecraftPlugin.getInstance().moneyData.addCash(event.getPlayer().getUniqueId().toString(), subtractValue);
                    MinecraftPlugin.getInstance().moneyData.addMoney(inaccuratePlayerArray.get(0).toString(), subtractValue *= -1);
                    atmManagement.openATMMenue(event.getPlayer(), inaccuratePlayerArray.get(0));
                    playerArray.clear();
                    inaccuratePlayerArray.clear();
                    idArray.clear();
                }
                else{
                    event.getPlayer().sendMessage("Du musst eine gueltige Zahl angeben!");
                    event.setCancelled(true);
                    return;
                }
            }

            if(inaccuratePlayerArray.get(1) == "topplayer"){
                System.out.println("Hallo");

                if(playerToAddMoney.isEmpty()){
                    System.out.println("Player to Add Money is Empty. Returning...");
                    return;
                }

                if(Float.parseFloat(event.getMessage()) > 0){
                    event.setCancelled(true);
                    if(Float.parseFloat(event.getMessage()) > MinecraftPlugin.getInstance().moneyData.getCashBetrag(event.getPlayer().getUniqueId().toString()))
                    {
                        event.getPlayer().sendMessage("Du hast dafuer zu wenig Bargeld bei dir!");
                        playerArray.clear();
                        inaccuratePlayerArray.clear();
                        idArray.clear();
                        playerToAddMoney.clear();
                        return;
                    }
                    //Muss nochmal getestet werden.
                    float addValue = Float.parseFloat(event.getMessage());
                    MinecraftPlugin.getInstance().moneyData.addCash(event.getPlayer().getUniqueId().toString(), addValue *= -1);
                    MinecraftPlugin.getInstance().moneyData.addCash(playerToAddMoney.get(0), Float.parseFloat(event.getMessage()));
                    //MinecraftPlugin.getInstance().moneyData.addCash(event.getPlayer().getUniqueId().toString(), addValue *= -1);
                    playerArray.clear();
                    inaccuratePlayerArray.clear();
                    idArray.clear();
                    playerToAddMoney.clear();
                    System.out.println("Added Cash to Player");
                }
                else{
                    event.getPlayer().sendMessage("Du musst eine gueltige Zahl angeben!");
                    event.setCancelled(true);

                    playerArray.clear();
                    inaccuratePlayerArray.clear();
                    idArray.clear();
                    playerToAddMoney.clear();

                    return;
                }
            }
        }
    }

    @EventHandler
    public void onRightClickOnPlayer(PlayerInteractEntityEvent event){

        if(!(event.getRightClicked() instanceof Player)){
            return;
        }

        if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.PAPER || event.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS) {
            return;
        }

        Player player = (Player) event.getRightClicked();


        Inventory inventory = Bukkit.createInventory(null, 27, "Interaktionen");

        ItemStack giveMoney = new ItemStack(Material.SLIME_BLOCK);

        ItemMeta meta_slime = giveMoney.getItemMeta();

        meta_slime.setDisplayName("Geld geben");

        giveMoney.setItemMeta(meta_slime);

        inventory.setItem(11, giveMoney);

        event.getPlayer().openInventory(inventory);

        //System.out.println(player.getUniqueId());

        //System.out.println(event.getRightClicked().getUniqueId());
        //MinecraftPlugin.getInstance().clickData.playerToAddMoney.set(0, player.getUniqueId().toString());

        playerToAddMoney.add(player.getUniqueId().toString());
        playerToAddMoney.add(player.getUniqueId().toString());

    }



}
