/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.userdata;

import com.frax.SkyHeroes.SkyHeroes;
import com.frax.SkyHeroes.manager.Config;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.File;
import java.io.IOException;

import static com.frax.SkyHeroes.listener.GameListener.basisExp;

public class UserData {

    File f;
    FileConfiguration cfg;

    /**
     * sets the offlineplayer
     */
    public UserData() {
        f = new File("plugins/SkyHeroes", "UserData.yml");
        cfg = YamlConfiguration.loadConfiguration(f);
    }

    /**
     * adds crystals to a player
     * @param amount
     */
    public void addCrystals(double amount, OfflinePlayer p) {
        cfg.set(p.getUniqueId().toString() + ".crystals", getCrystals(p) + amount);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * removes crystals from a player
     * @param amount
     */
    public void removeCrystals(int amount, OfflinePlayer p) {
        cfg.set(p.getUniqueId().toString() + ".crystals", getCrystals(p) - amount);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * adds a level to a player
     */
    public void addLevel(OfflinePlayer p) {
        double level = cfg.getDouble(p.getUniqueId().toString() + ".lvl");
        level++;
        cfg.set(p.getUniqueId().toString() + ".lvl", level);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * adds exp to a player
     * @param amount
     */
    public void addExp(double amount, OfflinePlayer p) {
        cfg.set(p.getUniqueId().toString() + ".exp", getExp(p) + amount);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * resets the exp from a player
     */
    public void removeExp(double amount, OfflinePlayer p) {
        cfg.set(p.getUniqueId().toString() + ".exp", getExp(p) - amount);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates a new userdata entry for an unknown user
     */
    public void createAccount(OfflinePlayer p) {
        if(!cfg.contains(p.getUniqueId().toString() + ".lvl")) {
            cfg.set(p.getUniqueId().toString() + ".lvl", 1);
            cfg.set(p.getUniqueId().toString() + ".exp", 0);
            cfg.set(p.getUniqueId().toString() + ".language", false);
            cfg.set(p.getUniqueId().toString() + ".bounty", 0);
            cfg.set(p.getUniqueId().toString() + ".crystals", 0);
            cfg.set(p.getUniqueId().toString() + ".locked", 3);

            try {
                cfg.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * sets the nation of a player
     * @param nation
     */
    public void addNation(String nation, OfflinePlayer p) {
        cfg.set(p.getUniqueId().toString() + ".nation", nation);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //language type from Player
    public void setLanguageType(boolean type, OfflinePlayer p) {
        cfg.set(p.getUniqueId().toString() + ".language", type);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTutorialDone(OfflinePlayer p) {
        cfg.set(p.getUniqueId().toString() + ".tutorial", true);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * adds bounty to a player
     * @param value
     */
    public void addBounty(double value, OfflinePlayer p) {
        cfg.set(p.getUniqueId().toString() + ".bounty", getBounty(p) + value);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * resets the bounty
     */
    public void resetBounty(OfflinePlayer p) {
        cfg.set(p.getUniqueId().toString() + ".bounty", 0);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeLock(OfflinePlayer p) {
        cfg.set(p.getUniqueId().toString() + ".locked", getLocks(p) - 1);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLocks(OfflinePlayer p) {
        return cfg.getInt(p.getUniqueId().toString() + ".locked");
    }

    /**
     * gets the bounty
     * @return
     */
    public double getBounty(OfflinePlayer p) {
        return cfg.getDouble(p.getUniqueId().toString() + ".bounty");
    }

    /**
     * gets the crystals
     * @return
     */
    public int getCrystals(OfflinePlayer p) {
        return cfg.getInt(p.getUniqueId().toString() + ".crystals");
    }

    /**
     * gets the exp of a player
     * @return
     */
    public int getExp(OfflinePlayer p) {
        return cfg.getInt(p.getUniqueId().toString() + ".exp");
    }

    /**
     * gets the level
     * @return
     */
    public double getLevel(OfflinePlayer p) {
        return cfg.getDouble(p.getUniqueId().toString() + ".lvl");
    }

    /**
     * gets the language
     * @return
     */
    public boolean getLanguage(OfflinePlayer p) { return cfg.getBoolean(p.getUniqueId().toString() + ".language"); }

    /**
     * retuns the nation
     * @return
     */
    public String getNation(OfflinePlayer p) { return cfg.getString(p.getUniqueId().toString() + ".nation"); }

    /**
     * gets cfg
     * @return
     */
    public FileConfiguration getCfg(OfflinePlayer p) {
        return cfg;
    }

    /**
     * returns if the tutorial was done
     * @return
     */
    public boolean hasTutorialDone(OfflinePlayer p) { return cfg.getBoolean(p.getUniqueId().toString() + ".tutorial"); }
}
