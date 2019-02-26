/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Stats {

    private OfflinePlayer p;
    private File f;
    private FileConfiguration cfg;

    /**
     * constructor sets the offlineplayer
     * @param p
     */
    public Stats(OfflinePlayer p) {
        this.p = p;
        f = new File("plugins/SkyHeroes", "stats.yml");
        cfg = YamlConfiguration.loadConfiguration(f);
    }

    /**
     * constructor
     */
    public Stats() {
        f = new File("plugins/SkyHeroes", "stats.yml");
        cfg = YamlConfiguration.loadConfiguration(f);
    }

    /**
     * returns the kills from a player
     * @return
     */
    public Integer getKills() {
        return cfg.getInt(p.getUniqueId().toString() + ".kills");
    }

    /**
     * returns the deaths from a player
     * @return
     */
    public Integer getDeaths() {
        return cfg.getInt(p.getUniqueId().toString() + ".deaths");
    }

    /**
     * retuns the kd of a player
     * @return
     */
    public double getKD() {
        if (!(getKills() == 0) && !(getDeaths() == 0)) {
            double kd = (double) getKills() / (double) getDeaths();

            double kdr = Math.round(kd * Math.pow(10, 2)) / Math.pow(10, 2);

            return kdr;
        }

        return 0;
    }

    /**
     * add a kill to a player
     */
    public void addKills() {
        int kills = cfg.getInt(p.getUniqueId().toString() + ".kills");
        kills++;

        cfg.set(p.getUniqueId().toString() + ".kills", kills);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * add a death to a player
     */
    public void addDeaths() {
        int deaths = cfg.getInt(p.getUniqueId().toString() + ".deaths");
        deaths++;

        cfg.set(p.getUniqueId().toString() + ".deaths", deaths);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates an account for an unknown user
     */
    public void createAccount() {
        if (!cfg.getStringList("Players").contains(p.getUniqueId().toString())) {
            List<String> players = cfg.getStringList("Players");
            players.add(p.getUniqueId().toString());
            cfg.set("Players", players);
            cfg.set(p.getUniqueId().toString() + ".kills", 0);
            cfg.set(p.getUniqueId().toString() + ".deaths", 0);
        }
        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets the most kills ordered by places
     * @param places
     * @return
     */
    public HashMap<Integer, String> getMostKills(int places) {
        List<String> players = cfg.getStringList("Players");
        List<Integer> kills = new ArrayList<>();
        HashMap<Integer, String> rang = new HashMap<>();

        for (int i = 0; i < players.size(); i++) {
            kills.add(cfg.getInt(players.get(i) + ".kills"));
        }

        Collections.sort(kills);
        Collections.reverse(kills);

        int sizeE = kills.size() < places ? kills.size() : places;

        ListIterator<String> iter = players.listIterator();

        for (int i = 0; i < sizeE; i++) {
            while (iter.hasNext()) {
                String user = iter.next();
                if (cfg.getInt(user + ".kills") == kills.get(i)) {
                    rang.put(i, user);
                    if (rang.get(i).equals(null)) {
                        rang.remove(i);
                    }
                    iter.remove();
                    break;
                }
            }

            while (iter.hasPrevious()) {
                iter.previous();
            }

        }

        return rang;
    }

    /**
     * sets the player
     * @param p
     */
    public void setPlayer(OfflinePlayer p) {
        this.p = p;
    }

    /**
     * gets the player
     * @return
     */
    public OfflinePlayer getPlayer() {
        return p;
    }

}
