/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.nms;

import com.frax.SkyHeroes.manager.ScoreboardManager;
import com.frax.SkyHeroes.nms.ActionBarUtil;
import com.frax.SkyHeroes.userdata.UserData;
import org.bukkit.entity.Player;

public class SenderThread implements Runnable {

    private Player p;
    private UserData data;
    private ActionBarUtil abUtil;
    private ScoreboardManager sm;
    public static boolean running;
    public static double basisExp;

    public SenderThread(Player p, ScoreboardManager sm) {
        this.p = p;
        abUtil = new ActionBarUtil();
        running = true;
        this.sm = sm;
    }

    @Override
    public void run() {
        while (running) {
            updateActionBar(p);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateActionBar(Player p) {
        data = new UserData();
        double percent = Math.round((data.getExp(p) * 100) / (basisExp * (data.getLevel(p) + 1.5)));
        if (percent < 10) {
            abUtil.send(p, "§fLvl. " + (int) data.getLevel(p) + " §7┃┃┃┃┃┃┃┃┃┃ §7" + percent + "%");
        } else if (percent < 20) {
            abUtil.send(p, "§fLvl. " + (int) data.getLevel(p) + " §4┃§7┃┃┃┃┃┃┃┃┃ §4" + percent + "%");
        } else if (percent < 30) {
            abUtil.send(p, "§fLvl. " + (int) data.getLevel(p) + " §4┃┃§7┃┃┃┃┃┃┃┃ §4" + percent + "%");
        } else if (percent < 40) {
            abUtil.send(p, "§fLvl. " + (int) data.getLevel(p) + " §6┃┃┃§7┃┃┃┃┃┃┃ §6" + percent + "%");
        } else if (percent < 50) {
            abUtil.send(p, "§fLvl. " + (int) data.getLevel(p) + " §6┃┃┃┃§7┃┃┃┃┃┃ §6" + percent + "%");
        } else if (percent < 60) {
            abUtil.send(p, "§fLvl. " + (int) data.getLevel(p) + " §e┃┃┃┃┃§7┃┃┃┃┃ §e" + percent + "%");
        } else if (percent < 70) {
            abUtil.send(p, "§fLvl. " + (int) data.getLevel(p) + " §e┃┃┃┃┃┃§7┃┃┃┃ §e" + percent + "%");
        } else if (percent < 80) {
            abUtil.send(p, "§fLvl. " + (int) data.getLevel(p) + " §a┃┃┃┃┃┃┃§7┃┃┃ §a" + percent + "%");
        } else if (percent < 90) {
            abUtil.send(p, "§fLvl. " + (int) data.getLevel(p) + " §a┃┃┃┃┃┃┃┃§7┃┃ §a" + percent + "%");
        } else if (percent < 100) {
            abUtil.send(p, "§fLvl. " + (int) data.getLevel(p) + " §2┃┃┃┃┃┃┃┃┃§7┃ §2" + percent + "%");
        } else if (percent == 100) {
            abUtil.send(p, "§fLvl. " + (int) data.getLevel(p) + " §2┃┃┃┃┃┃┃┃┃┃ §2" + percent + "%");
        }
    }

    public boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        SenderThread.running = running;
    }
}
