/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.quest;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestHandler {

    private File f;
    private FileConfiguration cfg;
    private String questType;

    public QuestHandler(String questType) {
        f = new File("plugins/SkyHeroes", questType);
        cfg = YamlConfiguration.loadConfiguration(f);
        this.questType = questType;
    }

    public String selectQuest() {
        List<String> quests = getQuests();
        Random r = new Random(quests.size());
        return quests.get(r.nextInt());
    }

    public List<Double> getPrice(String quest) {
        List<Double> price = new ArrayList<>();
        price.add(cfg.getDouble(quest + ".crystal"));
        price.add(cfg.getDouble(quest + ".exp"));
        return price;
    }

    public List<String> getObjective(String quest) {
        List<String> objective = new ArrayList<>();
        objective.add(cfg.getString(quest + ".mission"));
        objective.add(cfg.getString(quest + ".amount"));
        return objective;
    }

    public List<String> getQuests() {
        return cfg.getStringList("quests");
    }

    public File getF() {
        return f;
    }

    public void setF(File f) {
        this.f = f;
    }

    public FileConfiguration getCfg() {
        return cfg;
    }

    public void setCfg(FileConfiguration cfg) {
        this.cfg = cfg;
    }

    public String getQuestType() {
        return questType;
    }

    public void setQuestType(String questType) {
        this.questType = questType;
    }
}
