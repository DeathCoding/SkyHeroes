/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return true;

        Player p = (Player) commandSender;
        if (strings.length == 0) {
            if (command.getName().equalsIgnoreCase("credits")) {

            } else if (command.getName().equalsIgnoreCase("social")) {

            }
        }

        return true;
    }

}
