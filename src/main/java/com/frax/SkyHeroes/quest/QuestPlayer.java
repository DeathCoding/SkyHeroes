/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.quest;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestPlayer {

    private Player p;
    private File f;
    private QuestHandler qh;
    private FileConfiguration cfg;

    public QuestPlayer(Player p) {
        this.p = p;
        qh = new QuestHandler("side.yml");
        f = new File("plugins/SkyHeroes", "QuestPlayer.yml");
        cfg = YamlConfiguration.loadConfiguration(f);
    }

    public void generateDailys() {
        if (hasGenerated()) {
            return;
        } else {
            List<String> objectives;
            List<String> quests = new ArrayList<>();
            List<String> allQuests = qh.getQuests();
            Random r = new Random();

            for (int i = 0; i < 5; i++) {
                 int j = r.nextInt(allQuests.size());
                 //if (!quests.contains(allQuests.get(j))) {
                     quests.add(allQuests.get(j));
                     objectives = qh.getObjective(quests.get(i));

                     if (objectives.get(0).contains("kill")) {
                         cfg.set(p.getUniqueId().toString() + "." + quests.get(i) + ".killsNeed", objectives.get(1));
                         cfg.set(p.getUniqueId().toString() + "." + quests.get(i) + ".killsHave", 0);
                         cfg.set(p.getUniqueId().toString() + "." + quests.get(i) + ".activated", false);
                         cfg.set(p.getUniqueId().toString() + "." + quests.get(i) + ".finished", false);
                     } else {
                         cfg.set(p.getUniqueId().toString() + "." + quests.get(i) + ".activated", false);
                         cfg.set(p.getUniqueId().toString() + "." + quests.get(i) + ".finished", false);
                     }
                //}
            }

            cfg.set(p.getUniqueId().toString() + ".generated", true);
            cfg.set(p.getUniqueId().toString() + ".quests", quests);

            try {
                cfg.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getKills(String quest) {
        return cfg.getInt(p.getUniqueId().toString() + "." + quest + ".killsHave");
    }

    public List<String> getDailys() {
        return cfg.getStringList(p.getUniqueId().toString() + ".quests");
    }

    public void setFinished(String quest) {
        cfg.set(p.getUniqueId().toString() + "." + quest + ".finished", true);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void activateQuest(String quest) {
        cfg.set(p.getUniqueId().toString() + "." + quest + ".activated", true);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasActivated(String quest) {
        return cfg.getBoolean(p.getUniqueId().toString() + "." + quest + ".activated");
    }

    public boolean hasFinished(String quest) {
        return cfg.getBoolean(p.getUniqueId().toString() + "." + quest + ".finished");
    }

    public void addKills(String quest) {
        cfg.set(p.getUniqueId().toString() + "." + quest + ".killHave", getKills(quest) + 1);
    }

    public void resetQuest() {
        LocalTime hour = ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.HOURS);
        List<String> quests = getDailys();
        if (hour.getHour() == 0 && hour.getMinute() >= 0 && hour.getMinute() <= 59) {
            for (int i = 0; i < quests.size(); i++) {
                cfg.set(p.getUniqueId().toString() + "." + quests.get(i), null);
            }

            cfg.set(p.getUniqueId().toString() + ".sidequests", null);
            cfg.set(p.getUniqueId().toString() + ".generated", false);
            try {
                cfg.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasGenerated() {
        return cfg.getBoolean(p.getUniqueId().toString() + ".generated");
    }

    public String getNeedKills(String s) {
        return cfg.getString(p.getUniqueId().toString() + "." + s + ".killsNeed");
    }
}
