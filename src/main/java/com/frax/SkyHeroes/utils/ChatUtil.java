/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatUtil {

    String prefix;

    /**
     * constructor, which sets the prefix
     * @param prefix
     */
    public ChatUtil(String prefix) {
        this.prefix = prefix;
    }

    /**
     * send a message to a specific player
     * @param p
     * @param message
     */
    public void sendMessage(Player p, String message) {
        p.sendMessage(prefix + " " + message);
    }

    /**
     * makes a broadcast to all player
     * @param message
     */
    public void broadcast(String message) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.sendMessage(prefix + " " + message);
        }
    }

    /**
     * setter for the prefix
     * @param prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}
