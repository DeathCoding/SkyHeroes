/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LocationUtil {

    private String path;
    private File f;
    private FileConfiguration cfg;

    /**
     * sets the path
     * @param path
     */
    public LocationUtil(String path) {
        this.path = path;
        f = new File("plugins/SkyHeroes", "locs.yml");
        cfg = YamlConfiguration.loadConfiguration(f);
    }

    /**
     * saves a new location
     * @param loc
     */
    public void setLocation(Location loc) {
        cfg.set(path + ".world", loc.getWorld().getName());
        cfg.set(path + ".x", loc.getX());
        cfg.set(path + ".y", loc.getY());
        cfg.set(path + ".z", loc.getZ());
        cfg.set(path + ".pitch", loc.getPitch());
        cfg.set(path + ".yaw", loc.getYaw());

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sets a new location with a time
     * @param loc
     * @param time
     */
    public void setLocation(Location loc, int time) {
        cfg.set(path + ".world", loc.getWorld().getName());
        cfg.set(path + ".x", loc.getX());
        cfg.set(path + ".y", loc.getY());
        cfg.set(path + ".z", loc.getZ());
        cfg.set(path + ".pitch", loc.getPitch());
        cfg.set(path + ".yaw", loc.getYaw());
        cfg.set(path + ".time", time);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns a location
     * @return
     */
    public Location getLocation() {
        Location loc = null;

        String world = cfg.getString(path + ".world");
        double x = cfg.getDouble(path + ".x");
        double y = cfg.getDouble(path + ".y");
        double z = cfg.getDouble(path + ".z");
        double pitch = cfg.getDouble(path + ".pitch");
        double yaw = cfg.getDouble(path + ".yaw");

        if (world != null) {
            loc = new Location(Bukkit.getWorld(world), x, y, z);

            loc.setPitch((float) pitch);
            loc.setYaw((float) yaw);

            return loc;
        }

        return null;
    }

    /**
     * sets the path
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * returns the file
     * @return
     */
    public File getFile() { return f; }

    /**
     * returns the cfg
      * @return
     */
    public FileConfiguration getCfg() {
        return cfg;
    }
}
