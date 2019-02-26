/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.tutorial;

import com.frax.SkyHeroes.SkyHeroes;
import com.frax.SkyHeroes.utils.LocationUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class TutorialCommand implements CommandExecutor {

    LocationUtil util;
    File f;
    FileConfiguration cfg;

    /**
     * command for setting the locations in the config
     * @param commandSender
     * @param command
     * @param s
     * @param strings
     * @return
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player p = (Player)commandSender;
        f = new File("plugins/SkyHeroes", "locs.yml");
        cfg = YamlConfiguration.loadConfiguration(f);

        if (p.hasPermission("admin.tutorial")) {
            if (strings.length == 0) {
                if (command.getName().equalsIgnoreCase("tutorial")) {
                    SkyHeroes.cu.sendMessage(p, "benutze /tutorial <anzahl> <zeit>");
                }
            }

            if (strings.length == 2) {
                try {
                    int numb = Integer.parseInt(strings[0]);
                    int time = Integer.parseInt(strings[1]);
                    util = new LocationUtil("tutorial" + "." + numb);
                    util.setLocation(p.getLocation(), time);

                } catch (NumberFormatException e) {
                    SkyHeroes.cu.sendMessage(p, "nur zahlen bitte!");
                }
            }
        } else {
            SkyHeroes.cu.sendMessage(p, "You dont have the permissions to use this command!");
        }

        return true;
    }

}
