/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes;

import com.frax.SkyHeroes.commands.*;
import com.frax.SkyHeroes.islands.CreateIsland;
import com.frax.SkyHeroes.islands.Island;
import com.frax.SkyHeroes.islands.IslandEntrance;
import com.frax.SkyHeroes.islands.IslandShopMerchant;
import com.frax.SkyHeroes.listener.LanguageListener;
import com.frax.SkyHeroes.listener.NationChooser;
import com.frax.SkyHeroes.listener.GameListener;
import com.frax.SkyHeroes.listener.ServerListener;
import com.frax.SkyHeroes.manager.Config;
import com.frax.SkyHeroes.manager.ScoreboardManager;
import com.frax.SkyHeroes.quest.QuestListener;
import com.frax.SkyHeroes.tutorial.Test;
import com.frax.SkyHeroes.tutorial.TutorialCommand;
import com.frax.SkyHeroes.utils.ChatUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyHeroes extends JavaPlugin {

    public static ChatUtil cu = new ChatUtil("");

    public Config cfg;
    private ServerListener serverListener;
    private ScoreboardManager sm;

    /**
     * when the server starts
     */
    public void onEnable() {
        cfg = new Config();
        sm = new ScoreboardManager();

        cfg.createDefaults();
        cfg.readData();

        registerCommands();
        registerListener();

    }

    /**
     * when the server stops or reloaded
     */
    public void onDisable() {
        serverListener.stopRunning();
    }

    /**
     * registers all commands
     */
    private void registerCommands() {
        getCommand("setup").setExecutor(new SetupCommands());
        getCommand("top").setExecutor(new Toplist());
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("test").setExecutor(new Test());
        getCommand("tutorial").setExecutor(new TutorialCommand());
        getCommand("island").setExecutor(new CreateIsland());
        getCommand("bounty").setExecutor(new BountyCommand());
        getCommand("credits").setExecutor(new InfoCommands());
        getCommand("social").setExecutor(new InfoCommands());
        getCommand("item").setExecutor(new ItemSetup());
    }

    /**
     * registers all listeners
     */
    private void registerListener() {
        serverListener = new ServerListener();
        getServer().getPluginManager().registerEvents(new GameListener(sm), this);
        getServer().getPluginManager().registerEvents(new NationChooser(sm), this);
        getServer().getPluginManager().registerEvents(new LanguageListener(sm), this);
        getServer().getPluginManager().registerEvents(new IslandEntrance(), this);
        getServer().getPluginManager().registerEvents(new IslandShopMerchant(sm), this);
        getServer().getPluginManager().registerEvents(new QuestListener(), this);
        getServer().getPluginManager().registerEvents(serverListener, this);
    }

    /**
     * gives an instance of the main class back
     * @return
     */
    public static SkyHeroes getMain() {
        return getPlugin(SkyHeroes.class);
    }

}
