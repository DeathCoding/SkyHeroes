/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.manager;

import com.frax.SkyHeroes.SkyHeroes;
import com.frax.SkyHeroes.islands.IslandShopMerchant;
import com.frax.SkyHeroes.listener.GameListener;
import com.frax.SkyHeroes.listener.ServerListener;
import com.frax.SkyHeroes.nms.SenderThread;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    /**
     * some variables
     */
    File f;
    FileConfiguration cfg;

    /**
     * constructor for this class, sets the file and cfg
     */
    public Config() {
        f = new File("plugins/SkyHeroes", "config.yml");
        cfg = YamlConfiguration.loadConfiguration(f);
    }

    /**
     * creates new entries in the config
     */
    public void createDefaults() {
        cfg.options().copyDefaults(true);

        cfg.addDefault("prefix", "[SkyHeroes] ");
        cfg.addDefault("fallKill", -10);
        cfg.addDefault("basisExp", 1000);
        cfg.addDefault("basisCrystals", 100);
        cfg .addDefault("IslandInvSize", 45);

        cfg.addDefault("level1Lock", 10);
        cfg.addDefault("level2Lock", 20);
        cfg.addDefault("level3Lock", 30);

        cfg.addDefault("money1Lock", 1500);
        cfg.addDefault("money2Lock", 3000);
        cfg.addDefault("money3Lock", 4500);

        cfg.addDefault("tabHeader", "&cHeader");
        cfg.addDefault("tabFooter", "&cFooter");

        cfg.addDefault("heroMessageEng", "You are now an Hero!");
        cfg.addDefault("heroMessageDe", "Du bist nun ein Held!");

        cfg.addDefault("villainMessageEng", "You are now a villain!");
        cfg.addDefault("villainMessageDe", "Du bist nun ein Schurke!");

        cfg.addDefault("heroEng", "Hero");
        cfg.addDefault("heroDe", "Held");

        cfg.addDefault("villainEng", "Villain");
        cfg.addDefault("villainDe", "Schurke");

        cfg.addDefault("CrystalEng", "Crystals");
        cfg.addDefault("CrystalDe", "Kristalle");

        cfg.addDefault("killEng", "You have slain the player %target%!");
        cfg.addDefault("killDe", "Du hast den Spieler %target% getoetet!");

        cfg.addDefault("slainEng", "You got killed by %killer%!");
        cfg.addDefault("slainDe", "Du wurdest von %killer% getoetet!");

        cfg.addDefault("expGainEng", "You got %exp% exp and %crystals% crystals!");
        cfg.addDefault("expGainDe", "Du hast %exp% Erfahrungspunkte und %crystals% Kristalle erhalten!");

        cfg.addDefault("levelUpEng", "You leveled up!");
        cfg.addDefault("levelUpDe", "Du hast ein neues level!");

        cfg.addDefault("amountItemsOnGround", 60);
        cfg.addDefault("messageBroadcast", "hallo was geht lol");
        cfg.addDefault("messageCounter", 60);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sets the variables
     */
    public void readData() {
        SkyHeroes.cu.setPrefix(ChatColor.translateAlternateColorCodes('&', cfg.getString("prefix")));
        GameListener.fallDeath = cfg.getInt("fallKill");
        GameListener.basisExp = cfg.getInt("basisExp");
        GameListener.basisCrystals = cfg.getInt("basisCrystals");
        GameListener.lvl1 = cfg.getInt("level1Lock");
        GameListener.lvl2 = cfg.getInt("level2Lock");
        GameListener.lvl3 = cfg.getInt("level3Lock");
        GameListener.money1 = cfg.getInt("money1Lock");
        GameListener.money2 = cfg.getInt("money2Lock");
        GameListener.money3 = cfg.getInt("money3Lock");
        SenderThread.basisExp = cfg.getInt("basisExp");
        GameListener.tabHeader = ChatColor.translateAlternateColorCodes('&', cfg.getString("tabHeader"));
        GameListener.tabFooter = ChatColor.translateAlternateColorCodes('&', cfg.getString("tabFooter"));
        IslandShopMerchant.invSize = cfg.getInt("IslandInvSize");
        ServerListener.itemsOnGround = cfg.getInt("amountItemsOnGround");
        ServerListener.message = ChatColor.translateAlternateColorCodes('&', cfg.getString("messageBroadcast"));
        ServerListener.def = cfg.getInt("messageCounter");
    }

    /**
     *
     * @return cfg
     */
    public FileConfiguration getCfg() {
        return cfg;
    }
}
