/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.tutorial;

import com.frax.SkyHeroes.SkyHeroes;
import com.frax.SkyHeroes.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class Test implements CommandExecutor {

    LocationUtil util;
    File f;
    FileConfiguration cfg;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player p = (Player) commandSender;
        util = new LocationUtil("tutorial");
        f = new File("plugins/SkyHeroes", "locs.yml");
        cfg = YamlConfiguration.loadConfiguration(f);

        if (strings.length == 0) {
            if (command.getName().equalsIgnoreCase("test")) {

                Thread t = new Thread(new Runnable() {
                    boolean running = true;

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
                            }

                            step++;

                            try {
                                Thread.sleep(time * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                t.start();

            }
        }

        return true;
    }

}
