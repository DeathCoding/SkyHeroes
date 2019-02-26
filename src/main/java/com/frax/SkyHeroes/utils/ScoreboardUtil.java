/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.utils;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ScoreboardUtil {

    private ScoreboardObjective obj;
    private Scoreboard board;
    private Player p;

    /**
     * constructor sets the name an DisplaySlot
     * @param name
     */
    public ScoreboardUtil(Player p, String name) {
        board = new Scoreboard();
        obj = board.registerObjective(name, IScoreboardCriteria.b);
        this.p = p;

        PacketPlayOutScoreboardObjective createPacket = new PacketPlayOutScoreboardObjective(obj, 0);
        PacketPlayOutScoreboardObjective removePacket = new PacketPlayOutScoreboardObjective(obj, 1);
        PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(1, obj);
        sendPacket(removePacket);
        sendPacket(createPacket);
        sendPacket(display);
        obj.setDisplayName(name);
    }

    /**
     * adds a new score
     * @param name
     * @param score
     * @return
     */
    public void addScore(String name, int score) {
        ScoreboardScore line = new ScoreboardScore(board, obj, name);
        line.setScore(score);
        PacketPlayOutScoreboardScore pPOSS = new PacketPlayOutScoreboardScore(line);
        sendPacket(pPOSS);
    }

    public void sendPacket(Packet<?> packet) {
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }

    public ScoreboardObjective getObj() {
        return obj;
    }

    public void setObj(ScoreboardObjective obj) {
        this.obj = obj;
    }

    public Scoreboard getBoard() {
        return board;
    }

    public void setBoard(Scoreboard board) {
        this.board = board;
    }
}
