/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.islands;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class IslandPlayers {

    Player p;
    File f;
    FileConfiguration cfg;

    public IslandPlayers(Player p) {
        this.p = p;
        f = new File("plugins/SkyHeroes", "IslandPlayers.yml");
        cfg = YamlConfiguration.loadConfiguration(f);
    }

    public void buyIsland(String island) {
        List<String> islands = cfg.getStringList(p.getUniqueId().toString() + ".islands");
        islands.add(island);
        cfg.set(p.getUniqueId().toString() + ".islands", islands);

        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasIsland(String island) {
        List<String> islands = cfg.getStringList(p.getUniqueId().toString() + ".islands");
        if (islands.contains(island)) {
            return true;
        }
        return false;
    }

}
