/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.islands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Island {

    private File f;
    private FileConfiguration cfg;
    private double minX, minY, minZ;
    private double maxX, maxY, maxZ;

    public Island() {
        f = new File("plugins/SkyHeroes", "islands.yml");
        cfg = YamlConfiguration.loadConfiguration(f);
    }

    public void createIsland(String islandName, int level, int price, int tier, Location loc, Location loc2) {
        this.minX = Math.min(loc.getBlockX(), loc2.getBlockX());
        this.minY = Math.min(loc.getBlockY(), loc2.getBlockY());
        this.minZ = Math.min(loc.getBlockZ(), loc2.getBlockZ());

        this.maxX = Math.max(loc.getBlockX(), loc2.getBlockX());
        this.maxY = Math.max(loc.getBlockY(), loc2.getBlockY());
        this.maxZ = Math.max(loc.getBlockZ(), loc2.getBlockZ());

        if (!cfg.getStringList("islands").contains(islandName)) {
            List<String> islands = cfg.getStringList("islands");
            islands.add(islandName);
            cfg.set("islands", islands);
        }

        cfg.set(islandName + ".minX", minX);
        cfg.set(islandName + ".minY", minY);
        cfg.set(islandName + ".minZ", minZ);

        cfg.set(islandName + ".maxX", maxX);
        cfg.set(islandName + ".maxY", maxY);
        cfg.set(islandName + ".maxZ", maxZ);

        cfg.set(islandName + ".name", islandName);
        cfg.set(islandName + ".level", level);
        cfg.set(islandName + ".price", price);
        cfg.set(islandName + ".tier", tier);
        cfg.set(islandName + ".slot", 1);

        List<String> connected = new ArrayList<>();
        connected.add("change this!");

        if (tier > 1) {
            cfg.set(islandName + ".connected", connected);
        }

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isInIsland(Location loc, String name) {
        double minX = cfg.getDouble(name + ".minX"), minY = cfg.getDouble(name + ".minY"), minZ = cfg.getDouble(name + ".minZ");
        double maxX = cfg.getDouble(name + ".maxX"), maxY = cfg.getDouble(name + ".maxY"), maxZ = cfg.getDouble(name + ".maxZ");

        if (loc.getBlockX() >= minX && loc.getBlockX() <= maxX
                && loc.getBlockY() >= minY && loc.getBlockY() <= maxY
                && loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ) {
            return true;
        }
        return false;
    }

    public List<String> getLore(String name) {
        List<String> description = new ArrayList<>();
        description.add("Level: " + cfg.getInt(name + ".level"));
        description.add("Price: " + cfg.getInt(name + ".price") + " Crystal(s)");
        description.add("Tier: " + cfg.getInt(name + ".tier"));

        return description;
    }

    public List<String> getConnectedIslands(String name) { return cfg.getStringList(name + ".connected"); }

    public List<String> getIslands() {
        return cfg.getStringList("islands");
    }

    public int getTier(String name) { return cfg.getInt(name + ".tier"); }

    public int getLevel(String name) {
        return cfg.getInt(name + ".level");
    }

    public int getPrice(String name) {
        return cfg.getInt(name + ".price");
    }

    public Location getLocationMax(String island, World world) {
        int maxX = cfg.getInt(island + ".maxX");
        int maxY = cfg.getInt(island + ".maxY");
        int maxZ = cfg.getInt(island + ".maxZ");

        return new Location(world, maxX, maxY, maxZ);
    }

    public Location getLocationMin(String island, World world) {
        int minX = cfg.getInt(island + ".minX");
        int minY = cfg.getInt(island + ".minY");
        int minZ = cfg.getInt(island + ".minZ");

        return new Location(world, minX, minY, minZ);
    }

    public boolean hasConnected(String name, Player p) {
        List<String> connected = getCon(name);
        IslandPlayers ip = new IslandPlayers(p);
        int j = 0;
        for (int i = 0; i < connected.size(); i++) {
            if (ip.hasIsland(connected.get(i))) {
              j++;
            }
        }
        if (j == connected.size()) {
            return true;
        }

        return false;
    }

    public int getSlot(String island) {
        return cfg.getInt(island + ".slot");
    }

    public List<String> getCon(String islandName) {
        return cfg.getStringList(islandName + ".connected");
    }
}
