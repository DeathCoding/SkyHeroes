/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.manager;

import com.frax.SkyHeroes.listener.GameListener;
import com.frax.SkyHeroes.userdata.UserData;
import com.frax.SkyHeroes.utils.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {

    /**
     * some variables
     */
    ScoreboardUtil su;
    UserData data;
    private Config cfg;


    /**
     * constructor sets the player
     */
    public ScoreboardManager() {

    }

    /**
     * updates the scoreboard for this player
     */
    public void update(Player p) {
        System.out.println("1");
        su = new ScoreboardUtil(p,"§b§lSKY§6HEROES");
        data = new UserData();
        cfg = new Config();
        String kristalle = data.getLanguage(p) ? ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("CrystalDe")) : ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("CrystalEng"));

        String hero = data.getLanguage(p) ? ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("heroDe")) : ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("heroEng"));
        String villain = data.getLanguage(p) ? ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("villainDe")) : ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("villainEng"));

        int killstreak = GameListener.killstreak.containsKey(p) ? GameListener.killstreak.get(p) : 0;

        if(data.getCfg(p).contains(p.getUniqueId().toString() + ".nation")) {
            if (data.getNation(p).equals("Hero")) {
                su.addScore("§e" + hero, 13);
                su.addScore(" ", 12);
            } else {
                su.addScore("§e" + villain, 13);
                su.addScore(" ", 12);
                System.out.println(data.getNation(p));
            }
        }

        su.addScore("§6" + kristalle, 11);
        su.addScore("§f" + data.getCrystals(p), 10);
        su.addScore("   ", 9);
        su.addScore("§6Level", 8);
        su.addScore("§f" + (int) data.getLevel(p) + " ", 7);
        su.addScore("      ", 6);
        su.addScore("§6Killstreak", 5);
        su.addScore("§f" + killstreak + "      ", 4);
        su.addScore("  ", 3);
        su.addScore("§6TeamSpeak", 2);
        su.addScore("§fts.SkyHeroes.net", 1);

        sendBelowName(p);
    }

    private void sendBelowName(Player p) {
        Scoreboard board;
        Objective obj;

        board = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = board.registerNewObjective("level", "dummy");
        obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
        obj.setDisplayName("Level");

        for (Player online : Bukkit.getOnlinePlayers()) {
            Score score = obj.getScore(online.getName());
            score.setScore((int) data.getLevel(online));
        }

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.setScoreboard(board);
        }
    }
}
