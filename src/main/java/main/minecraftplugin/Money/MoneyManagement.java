package main.minecraftplugin.Money;

import main.minecraftplugin.MinecraftPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        }

        return true;
    }

    public void createTable(){
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS bankAccounts " +
                    "(OWNER VARCHAR(100),ID VARCHAR(100),PIN INT(100),BETRAG INT(100),PRIMARY KEY (OWNER))");
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createBankAccount(Player player){
        try {
            UUID uuid = player.getUniqueId();

            if(!exists(uuid)){
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

    public boolean exists(UUID uuid){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM bankAccounts WHERE ID=?");
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
}
