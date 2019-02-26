/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.tutorial;

import com.frax.SkyHeroes.SkyHeroes;
import com.frax.SkyHeroes.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class TutorialThread extends Thread {

    private Player p;

    LocationUtil util;
    File f;
    FileConfiguration cfg;
    boolean running;

    /**
     *sets the player who needs to get teleportet
     * @param p
     */
    public TutorialThread(Player p) {
        this.p = p;
        util = new LocationUtil("tutorial");
        f = new File("plugins/SkyHeroes", "locs.yml");
        cfg = YamlConfiguration.loadConfiguration(f);
        running = true;
    }

    /**
     * teleports the player to the specific locations
     */
    @Override
    public void run() {
        int step = 1;

        while (running) {
            int time = cfg.getInt("tutorial" + "." + step + ".time");
            util.setPath("tutorial" + "." + step);

            Location loc = util.getLocation();

            if (loc != null) {
                p.teleport(loc);
                SkyHeroes.cu.sendMessage(p, "Tutorial point: " + step);
            } else {
                running = false;
                util.setPath("Spawn");
                p.teleport(util.getLocation());
                try {
                    join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            step++;
            try {
                Thread.sleep(time * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
