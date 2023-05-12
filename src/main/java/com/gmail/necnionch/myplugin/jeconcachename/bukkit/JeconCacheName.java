package com.gmail.necnionch.myplugin.jeconcachename.bukkit;

import jp.jyn.jecon.lib.jbukkitlib.uuid.UUIDRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class JeconCacheName extends JavaPlugin implements Listener {
    private final NameCacheFile cacheFile = new NameCacheFile(this);

    @Override
    public void onEnable() {
        cacheFile.load();

        UUIDRegistry registry = UUIDRegistry.getSharedCacheRegistry(this);

        Map<UUID, Optional<String>> uuidToNameCache;
        try {
            Field field = registry.getClass().getDeclaredField("uuidToNameCache");
            field.setAccessible(true);
            //noinspection unchecked
            uuidToNameCache = (Map<UUID, Optional<String>>) field.get(registry);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            getLogger().info("Failed to get 'updateCache(UUID, String)' method.");
            setEnabled(false);
            return;
        }

        getServer().getPluginManager().registerEvents(this, this);


        cacheFile.getNames().putAll(getServer().getOnlinePlayers().stream()
                .collect(Collectors.toMap(Player::getUniqueId, Player::getName)));
        cacheFile.save();


        if (!cacheFile.getNames().isEmpty()) {
            for (Map.Entry<UUID, String> e : cacheFile.getNames().entrySet()) {
                uuidToNameCache.put(e.getKey(), Optional.ofNullable(e.getValue()));
            }
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        cacheFile.getNames().put(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        cacheFile.save();
    }

}
