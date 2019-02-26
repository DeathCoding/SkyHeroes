/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.listener;

import com.frax.SkyHeroes.SkyHeroes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.help.HelpTopic;

import java.util.ArrayList;
import java.util.List;

public class ServerListener implements Listener {

    // config variables
    public static int def;
    public static String message;
    public static int itemsOnGround;

    // the counters for the threads, till they will execute something
    int counterItems = 60;
    int counterMessage = def;

    // variables for threads
    Thread itemThread;
    Thread messageThread;

    //boolean for itemThread, checking if amount of items reached
    private boolean isAllowed = false;
    private static boolean running = true;

    @EventHandler
    public void onServerEnablePlugin(PluginEnableEvent e) {

        /**
         * Thread for deleting the items
         */
        itemThread = new Thread(() -> {

            while (running) {
                for (int i = counterItems; i > 0; i--) {
                    if (i == counterItems) {
                        SkyHeroes.cu.broadcast("All items will be reseted in " + counterItems + " seconds!");
                    }

                    if (i == 1) {
                        for (World w : Bukkit.getWorlds()) {
                            for (Entity e1 : w.getEntities()) {
                                if (e1 instanceof Item && e1.isOnGround()) {
                                    e1.remove();
                                    isAllowed = false;
                                }
                            }
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        /**
         * Thread for messages after a certain time
         */
        messageThread = new Thread(() -> {
           while (running) {
                for (int i = counterMessage; i > 0; i--) {
                    if (i == 1) {
                        SkyHeroes.cu.broadcast(message);
                        counterMessage = def;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
           }
        });

        // starting the threads
        itemThread.start();
        messageThread.start();
    }

    /**
     *
     * Event that casts if an unknown command was used
     * @param e
     */
    @EventHandler
    public void onUnknowCommand(PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        String msg = e.getMessage().split(" ")[0];
        HelpTopic topic = Bukkit.getServer().getHelpMap().getHelpTopic(msg);

        if (topic.equals(null)) {
            p.sendMessage("This command has no existents in this holy world!");
            e.setCancelled(true);
        }
    }

    public void stopRunning() {
        running = false;
        try {
            itemThread.join();
            messageThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Thread getItemThread() {
        return itemThread;
    }

    public Thread getMessageThread() {
        return messageThread;
    }
}
