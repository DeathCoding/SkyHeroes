/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.commands;

import com.frax.SkyHeroes.manager.ScoreboardManager;
import com.frax.SkyHeroes.userdata.UserData;
import com.frax.SkyHeroes.utils.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;


public class Toplist implements CommandExecutor {

    Stats st;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player)commandSender;

        st = new Stats();

        if(strings.length == 0) {
            if (command.getName().equalsIgnoreCase("top")) {
                HashMap<Integer, String> rang = st.getMostKills(10);
                player.sendMessage("§6§m●----------§r§6●§e SkyHeroes Top 10 §6§m●---------§r§6●");
                for (int i = 0; i < 10; i++) {
                    if (rang.get(i) == null || rang.get(i).equals(null)) {
                        player.sendMessage(ChatColor.AQUA + "" + (i + 1) + ". " + ChatColor.GOLD + "N/A");
                    } else {
                        String uuid = rang.get(i);
                        OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                        st.setPlayer(p);
                        player.sendMessage(ChatColor.AQUA + "" + (i + 1) + ". " + ChatColor.YELLOW + p.getName() + ChatColor.DARK_RED + " Kills: " + ChatColor.GOLD + st.getKills());
                    }
                }
                player.sendMessage("§6§m●-------------------------------------§r§6●");
            }
        }

        return true;
    }
}
