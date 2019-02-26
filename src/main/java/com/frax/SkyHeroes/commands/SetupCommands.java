/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.commands;

import com.frax.SkyHeroes.SkyHeroes;
import com.frax.SkyHeroes.manager.Config;
import com.frax.SkyHeroes.manager.ScoreboardManager;
import com.frax.SkyHeroes.userdata.UserData;
import com.frax.SkyHeroes.utils.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class SetupCommands implements CommandExecutor {

    LocationUtil lu;
    private UserData data;
    private Config cfg;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player)commandSender;
        lu = new LocationUtil("Spawn");
        cfg = new Config();
        data = new UserData();

        if(strings.length == 0) {
            if(command.getName().equalsIgnoreCase("setup")) {
                if(player.hasPermission("SkyHeroes.setup")) {
                    SkyHeroes.cu.sendMessage(player, ChatColor.AQUA + "/setup spawn");
                    SkyHeroes.cu.sendMessage(player, ChatColor.AQUA + "/setup nation");
                } else {
                    SkyHeroes.cu.sendMessage(player, "Du hast leider nicht genügend rechte!");
                }
            }
        }

        if(strings.length == 1) {
            if(strings[0].equalsIgnoreCase("spawn")) {
                lu.setLocation(player.getLocation());
                SkyHeroes.cu.sendMessage(player, ChatColor.AQUA + "Spawn is setted!");
            }

            if(strings[0].equalsIgnoreCase("nation")) {
                Villager v = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
                v.setCustomName(ChatColor.GOLD + "Nation");
                v.setCustomNameVisible(true);
                player.sendMessage("Villager created!");
            }

            if(strings[0].equalsIgnoreCase("island")) {
                Villager v = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
                v.setCustomName(ChatColor.GOLD + "Shop");
                v.setCustomNameVisible(true);
                player.sendMessage("Villager created!");
            }

            if(strings[0].equalsIgnoreCase("quests")) {
                Villager v = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
                v.setCustomName(ChatColor.GOLD + "Quests");
                v.setCustomNameVisible(true);
                player.sendMessage("Villager created!");
            }
        }

        return true;
    }
}
