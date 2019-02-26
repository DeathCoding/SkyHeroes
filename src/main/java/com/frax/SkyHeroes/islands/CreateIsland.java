/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.islands;

import com.frax.SkyHeroes.SkyHeroes;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CreateIsland implements CommandExecutor {

    private HashMap<String, Location> loc1 = new HashMap<>();
    private HashMap<String, Location> loc2 = new HashMap<>();

    private Island is;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player p = (Player)commandSender;
        is = new Island();

        if (p.hasPermission("SkyHeroes.createIsland")) {
            if (strings.length == 0) {
                if (command.getName().equalsIgnoreCase("island")) {
                    return true;
                }
            }

            if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("pos1")) {
                    loc1.put(p.getName(), p.getLocation());
                    p.sendMessage("Position 1 gesetzt");
                } else if (strings[0].equalsIgnoreCase("pos2")) {
                    loc2.put(p.getName(), p.getLocation());
                    p.sendMessage("Position 2 gesetzt");
                }
            }

            if (strings[0].equalsIgnoreCase("create")) {
                if (strings.length == 5) {
                    Location pos1 = loc1.get(p.getName());
                    Location pos2 = loc2.get(p.getName());
                    String islandName = strings[1];
                    int level = Integer.parseInt(strings[2]);
                    int price = Integer.parseInt(strings[3]);
                    int tier = Integer.parseInt(strings[4]);
                    if (pos1 == null || pos2 == null) {
                        p.sendMessage("setze zwei punkte");
                        return true;
                    }

                    is.createIsland(islandName, level, price, tier, pos1, pos2);
                    p.sendMessage("Island created");
                }
            }
        } else {
            SkyHeroes.cu.sendMessage(p, "You dont have enough permissions to use this command");
        }

        return true;
    }
}
