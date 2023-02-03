package main.minecraftplugin.Money;

import main.minecraftplugin.MinecraftPlugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MoneyManagement implements CommandExecutor {

    private MinecraftPlugin plugin;
    public MoneyManagement(MinecraftPlugin plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)){return true;}

        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("createBankAccount")){
            createBankAccount(player);

            Inventory inventory = player.getInventory();

            ItemStack card = new ItemStack(Material.PAPER, 1);
            ItemMeta meta = card.getItemMeta();

            List<String> kontoId = new ArrayList<>();
            kontoId.add("§0" + player.getUniqueId().toString());
            meta.setLore(kontoId);

            meta.setDisplayName("Bankkarte von " + player.getName());

            card.setItemMeta(meta);

            inventory.addItem(card);
        }

        if(command.getName().equalsIgnoreCase("addMoney")){
            addMoney(player.getUniqueId().toString(), 100);
        }

        return true;
    }

    public void createTable(){
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS bankAccounts " +
                    "(OWNER VARCHAR(100),ID VARCHAR(100),PIN INT(100),BETRAG FLOAT(15,4),PRIMARY KEY (OWNER))");
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createBankAccount(Player player){
        try {
            UUID uuid = player.getUniqueId();

            if(!exists(uuid.toString())){
                int pin = 1234;
                PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO bankAccounts" +
                        " (OWNER,ID,PIN) VALUES (?,?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.setString(3, "1234");
                ps2.executeUpdate();

                return;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean exists(String id){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM bankAccounts WHERE ID=?");
            ps.setString(1, id);

            ResultSet result = ps.executeQuery();
            if(result.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void addMoney(String id, float money) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE bankAccounts SET BETRAG=? WHERE ID=?");
            ps.setFloat(1, getMoneyBetrag(id.substring(2)) + money);
            ps.setString(2, id.substring(2));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public float getMoneyBetrag(String id){

        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT BETRAG FROM bankAccounts WHERE ID=?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            float points = 0;
            if(rs.next()){
                points = rs.getFloat("BETRAG");
                return points;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*
    -------------------------------------------------------------------------------------------------------------
    WICHTIG! AB HIER BEGINNT DER BEREICH, IN WELCHEM DAS CASH, ALSO DAS BARGELD GEMANAGED WIRD. NICHT VERTAUSCHEN!
    -------------------------------------------------------------------------------------------------------------
     */

    public void createCashTable(){
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playercash " +
                    "(PLAYER VARCHAR(100),UUID VARCHAR(100),CASH FLOAT(15,4),PRIMARY KEY (PLAYER))");
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createCashAccount(Player player){
        try {
            UUID uuid = player.getUniqueId();

            if(!existsCash(uuid)){
                PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO playercash" +
                        " (PLAYER,UUID,CASH) VALUES (?,?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.setFloat(3, 450);
                ps2.executeUpdate();

                return;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean existsCash(UUID uuid){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM playercash WHERE UUID=?");
            ps.setString(1, uuid.toString());

            ResultSet result = ps.executeQuery();
            if(result.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void addCash(String uuid, float money) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE playercash SET CASH=? WHERE UUID=?");
            ps.setFloat(1, getCashBetrag(uuid) + money);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public float getCashBetrag(String uuid){

        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT CASH FROM playercash WHERE UUID=?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            float points = 0;
            if(rs.next()){
                points = rs.getFloat("CASH");
                return points;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
