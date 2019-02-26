/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class ActionBarUtil {

    private Class<?> playOutChat, chatComponent, chatMessageTypeClass, textClass;
    private ReflectionUtil util;

    public ActionBarUtil() {
        util = new ReflectionUtil();
        playOutChat = util.getNMSClass("PacketPlayOutChat");
        chatComponent = util.getNMSClass("IChatBaseComponent");
        chatMessageTypeClass = util.getNMSClass("ChatMessageType");
        textClass = util.getNMSClass("ChatComponentText");
    }

    public void send(Player p, String message) {
        try {
            Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
            Object chatMessageType = null;
            for (Object obj : chatMessageTypes) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMessageType = obj;
                }
            }

            Object o = textClass.getConstructor(new Class<?>[] {String.class}).newInstance(message);
            Object ppoc = playOutChat.getConstructor(new Class<?>[]{chatComponent, chatMessageTypeClass}).newInstance(o, chatMessageType);

            util.sendPacket(p, ppoc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAll(String message) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            send(all, message);
        }
    }



}
