package com.gmail.necnionch.myplugin.jeconcachename.bukkit;

import com.gmail.necnionch.myplugin.jeconcachename.common.BukkitConfigDriver;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NameCacheFile extends BukkitConfigDriver {
    public NameCacheFile(JavaPlugin plugin) {
        super(plugin, "cache.yml", "empty.yml");
    }

    private final Map<UUID, String> names = new HashMap<>();

    @Override
    public boolean onLoaded(FileConfiguration config) {
        names.clear();

        if (super.onLoaded(config)) {
            for (String uuid : config.getKeys(false)) {
                names.put(UUID.fromString(uuid), config.getString(uuid));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean save() {
        config = new YamlConfiguration();
        for (Map.Entry<UUID, String> e : names.entrySet()) {
            config.set(e.getKey().toString(), e.getValue());
        }
        return super.save();
    }

    public Map<UUID, String> getNames() {
        return names;
    }

}
