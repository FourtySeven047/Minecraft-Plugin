package main.minecraftplugin.SQL;

import main.minecraftplugin.MinecraftPlugin;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ArenaPointsSQL {

    private MinecraftPlugin plugin;
    public ArenaPointsSQL(MinecraftPlugin plugin){
        this.plugin = plugin;
    }

    public void createTable(){
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS arenaPoints " +
                    "(NAME VARCHAR(100),UUID VARCHAR(100),POINTS INT(100),PRIMARY KEY (NAME))");
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createPlayer(Player player){
        try {
            UUID uuid = player.getUniqueId();

            if(!exists(uuid)){
                PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO arenaPoints" +
                        " (NAME,UUID) VALUES (?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.executeUpdate();

                return;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean exists(UUID uuid){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM arenaPoints WHERE UUID=?");
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

    public void addPoint(UUID uuid){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE arenaPoints SET POINTS=? WHERE UUID=?");
            ps.setInt(1, getArenaPoints(uuid) + 1);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int getArenaPoints(UUID uuid){

        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT POINTS FROM arenaPoints WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int points = 0;
            if(rs.next()){
                points = rs.getInt("POINTS");
                return points;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
