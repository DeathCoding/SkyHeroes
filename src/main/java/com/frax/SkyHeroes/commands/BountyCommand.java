/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.commands;

import com.frax.SkyHeroes.SkyHeroes;
import com.frax.SkyHeroes.userdata.UserData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyCommand implements CommandExecutor {

    UserData data;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return true;

        Player p = (Player)commandSender;
        if(strings.length == 0) {
            if (command.getName().equalsIgnoreCase("bounty")) {
                SkyHeroes.cu.sendMessage(p, "to give a player an bounty use /bounty <player> <value>");
                return true;
            }
        } else if(strings.length == 2) {
            OfflinePlayer op = Bukkit.getPlayer(strings[0]);
            double value = Double.parseDouble(strings[1]);
            data = new UserData();

            if (value > data.getCrystals(op)) {
                SkyHeroes.cu.sendMessage(p, "you dont have enough money to give him that bounty!");
            }else if (value <= 0) {
                SkyHeroes.cu.sendMessage(p,"you cant give negative bountys or the bounty 0");
            } else {
                data.addBounty(value, op);
                SkyHeroes.cu.sendMessage(p, "you gave the player " + op.getName() + " an bounty of " + value + " Crystal(s).");
                SkyHeroes.cu.broadcast("The player " + op.getName() + " has now a bounty of " + data.getBounty(op) + " Crystal(s)");
            }
        }

        return true;
    }
}
