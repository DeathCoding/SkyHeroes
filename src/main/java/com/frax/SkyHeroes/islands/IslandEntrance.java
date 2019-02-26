/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.islands;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class IslandEntrance implements Listener {

    Island is;
    IslandPlayers ip;

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        is = new Island();
        ip = new IslandPlayers(e.getPlayer());
        List<String> islands = is.getIslands();

        Player p = e.getPlayer();

        Location loc2 = e.getTo();

        for (int i = 0; i < islands.size(); i++) {
            if (is.isInIsland(loc2, islands.get(i))) {
                if (ip.hasIsland(islands.get(i))) {
                    return;
                } else {

                    Location min = is.getLocationMin(islands.get(i), p.getWorld());
                    Location max = is.getLocationMax(islands.get(i), p.getWorld());

                    double xMin = p.getLocation().getX() - min.getX();
                    double zMin = p.getLocation().getZ() - min.getZ();
                    double xMax = max.getX() - p.getLocation().getX();
                    double zMax = max.getZ() - p.getLocation().getZ();

                    Vector f = new Vector();

                    if (zMax < xMax && zMax < zMin && zMax < xMin) {
                        f.setZ(2);
                    } else if (xMax < zMax && xMax < xMin && xMax < zMin) {
                        f.setX(2);
                    } else if (zMin < zMax && zMin < xMax && zMin < xMin) {
                        f.setZ(-2);
                    } else if (xMin < xMax && xMin < zMax && xMin < zMin) {
                        f.setX(-2);
                    }

                    f.setY(0.85);
                    f.multiply(1.2);

                    p.spawnParticle(Particle.FLAME, p.getLocation(), 5);
                    p.setVelocity(f);

                }
            } else if (!is.isInIsland(loc2, islands.get(i))) {

            }
        }
    }

}
