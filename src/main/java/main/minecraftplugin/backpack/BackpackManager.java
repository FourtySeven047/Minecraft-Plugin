package main.minecraftplugin.backpack;

import main.minecraftplugin.Config;
import main.minecraftplugin.MinecraftPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class BackpackManager {

    private final Map<UUID, Backpack> map;

    public BackpackManager(){
        map = new HashMap<>();

        load();
    }

    public Backpack getBackpack(UUID uuid){

        if(map.containsKey(uuid)){
            return map.get(uuid);
        }

        Backpack backpack = new Backpack(uuid);

        map.put(uuid, backpack);

        return backpack;
    }

    public void setBackpack(UUID uuid, Backpack backpack){
        map.put(uuid, backpack);
    }
    private void load(){
        Config config = MinecraftPlugin.getInstance().getConfiguration();

        List<String> uuids = config.getConfig().getStringList("backpacks");

        uuids.forEach(s -> {
            UUID  uuid = UUID.fromString(s);

            String base64 = config.getConfig().getString("backpack." + s);

            try {
                map.put(uuid, new Backpack(uuid, base64));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void save(){
        Config config = MinecraftPlugin.getInstance().getConfiguration();

        List<String> uuids = new ArrayList<>();

        for (UUID uuid : map.keySet()) {
            uuids.add(uuid.toString());
        }

        config.getConfig().set("backpacks", uuids);
        map.forEach((uuid, backpack) -> {
            config.getConfig().set("backpack." + uuid.toString(), backpack.toBase64());
        });
    }

}
