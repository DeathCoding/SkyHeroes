/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.nms;

import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class TabUtil {

    private Player p;
    private ReflectionUtil util;

    public TabUtil(Player p) {
        this.p = p;
        util = new ReflectionUtil();
    }

    public void sendTab(String header, String footer) {

        if (header == null) {
            header = "";
        }

        if (footer == null) {
            footer = "";
        }

        header = header.replaceAll("%player%", p.getDisplayName());
        footer = footer.replaceAll("%player%", p.getDisplayName());

        try {
            Object tabHeader = util.getIchatBaseComponent().getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + header + "\"}" });
            Object tabFooter = util.getIchatBaseComponent().getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + footer + "\"}" });

            Constructor<?> titleConstructer = util.getPacketPlayOutPlayerListHeaderFooter()
                    .getConstructor();
            Object packet = titleConstructer.newInstance();

            Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, tabFooter);

            Field field2 = packet.getClass().getDeclaredField("a");
            field2.setAccessible(true);
            field2.set(packet, tabHeader);

            util.sendPacket(p, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
