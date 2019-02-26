/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {

    private String VERSION = "";
    private String NMS_PREFIX = "net.minecraft.server.";
    private String CRAFT_PREFIX = "org.bukkit.craftbukkit.";

    String version;

    public ReflectionUtil() {
        String serverPackageName = Bukkit.getServer().getClass().getPackage().getName();
        version = serverPackageName.substring(serverPackageName.lastIndexOf(".") + 1);
    }

    public Class<?> getNMSClass(String name) {
        try {
            return Class.forName(NMS_PREFIX + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Class<?> getCraftClass(String name) {
        try {
            return Class.forName(CRAFT_PREFIX + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getEntityPlayer(Player p) {
        try {
            return getCraftClass("entity.CraftPlayer").getMethod("getHandle").invoke(p);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Class<?> getIchatBaseComponent() {
        return getNMSClass("IChatBaseComponent");
    }

    public Class<?> getClassNmsChatSerializer() {
        return getNMSClass("IChatBaseComponent$ChatSerializer");
    }

    public Class<?> getPacketPlayOutPlayerListHeaderFooter() {
        return getNMSClass("PacketPlayOutPlayerListHeaderFooter");
    }

    public Object getObjectNmsChatSerializer(String text) {
        try {
            Class<?> classSeri = getClassNmsChatSerializer();
            Method serialize = classSeri.getDeclaredMethod("a", new Class[] {String.class});
            return serialize.invoke(new Object[] { "{\"text\": \"" + text + "\"}" });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendPacket(Player p, Object packet) {
        try {
            Object nmsPlayer = getEntityPlayer(p);

            Field fieldCond = nmsPlayer.getClass().getField("playerConnection");
            fieldCond.setAccessible(true);

            Object nmsCon = fieldCond.get(nmsPlayer);

            Method sendPacket = nmsCon.getClass().getMethod("sendPacket", getNMSClass("Packet"));
            sendPacket.invoke(nmsCon, packet);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

}
