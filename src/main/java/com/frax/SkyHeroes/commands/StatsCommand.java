/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.commands;

import com.frax.SkyHeroes.manager.Config;
import com.frax.SkyHeroes.userdata.UserData;
import com.frax.SkyHeroes.utils.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    Stats st;
    UserData data;
    Config cfg;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player)commandSender;
        st = new Stats(player);
        data = new UserData();
        cfg = new Config();
        String hero = data.getLanguage(player) ? ChatColor.translateAlternateColorCodes('&' , cfg.getCfg().getString("heroDe")) : ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("heroEng"));
        String villain = data.getLanguage(player) ? ChatColor.translateAlternateColorCodes('&' , cfg.getCfg().getString("villainDe")) : ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("villainEng"));
        if(strings.length == 0) {
            if (command.getName().equalsIgnoreCase("stats")) {
                player.sendMessage("§6§m●----------§r§6●§e SkyHeroes-Stats §6§m●----------§r§6●");
                player.sendMessage("                " + ChatColor.GOLD + player.getName());
                if (data.getNation(player) == "Hero") {
                    player.sendMessage(ChatColor.DARK_PURPLE + "Nation: " + ChatColor.YELLOW + hero);
                } else {
                    player.sendMessage(ChatColor.DARK_PURPLE + "Nation: " + ChatColor.YELLOW + villain);
                }

                player.sendMessage(" ");
                player.sendMessage(ChatColor.DARK_PURPLE + "Kills: " + ChatColor.YELLOW + st.getKills());
                player.sendMessage(ChatColor.DARK_PURPLE + "Deaths: " + ChatColor.YELLOW + st.getDeaths());
                player.sendMessage(ChatColor.DARK_PURPLE + "KD: " + ChatColor.YELLOW + st.getKD());
                player.sendMessage(" ");
                player.sendMessage(ChatColor.DARK_PURPLE + "Level: " + ChatColor.YELLOW + (int) data.getLevel(player));
                player.sendMessage("§6§m●-------------------------------------§r§6●");
            }
        }

        if (strings.length == 1) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(strings[0]);

            st.setPlayer(p);

            player.sendMessage("§6§m●----------§r§6●§e SkyHeroes-Stats §6§m●----------§r§6●");
            player.sendMessage("                " + ChatColor.GOLD + p.getName());
            if (data.getNation(player) == "Hero") {
                player.sendMessage(ChatColor.DARK_PURPLE + "Nation: " + ChatColor.YELLOW + hero);
            } else {
                player.sendMessage(ChatColor.DARK_PURPLE + "Nation: " + ChatColor.YELLOW + villain);
            }

            player.sendMessage(" ");
            player.sendMessage(ChatColor.DARK_PURPLE + "Kills: " + ChatColor.YELLOW + st.getKills());
            player.sendMessage(ChatColor.DARK_PURPLE + "Deaths: " + ChatColor.YELLOW + st.getDeaths());
            player.sendMessage(ChatColor.DARK_PURPLE + "KD: " + ChatColor.YELLOW + st.getKD());
            player.sendMessage(" ");
            player.sendMessage(ChatColor.DARK_PURPLE + "Level: " + ChatColor.YELLOW + (int) data.getLevel(p));
            player.sendMessage("§6§m●-------------------------------------§r§6●");
        }
        return true;
    }
}
