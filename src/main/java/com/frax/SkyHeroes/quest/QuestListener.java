/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.quest;

import com.frax.SkyHeroes.SkyHeroes;
import com.frax.SkyHeroes.listener.GameListener;
import com.frax.SkyHeroes.userdata.UserData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.frax.SkyHeroes.listener.GameListener.basisExp;

public class QuestListener implements Listener {

    private QuestPlayer qp;
    private List<String> quests;
    private QuestHandler qh;
    private UserData data;

    @EventHandler
    public void onInventoryIntecract(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        qh = new QuestHandler("side.yml");
        Player p = (Player) e.getWhoClicked();
        qp = new QuestPlayer(p);
        data = new UserData();
        List<String> quest = qp.getDailys();

        if (inv.getTitle().equals(ChatColor.GOLD + "Quests")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().hasItemMeta() == false || e.getCurrentItem().getItemMeta().hasDisplayName() == false) {
                return;
            }

            for (int i = 0; i < quest.size(); i++) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + quest.get(i))) {
                    p.sendMessage("you have activated the quest");
                    qp.activateQuest(quest.get(i));
                    ItemStack is = e.getCurrentItem();
                    is.setDurability((short) 1);

                    e.getInventory().setItem(e.getSlot(), is);
                    p.updateInventory();
                }

                if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + quest.get(i))) {
                    if (qh.getObjective(quest.get(i)).get(0).contains("kill")) {
                        if (qp.getKills(quest.get(i)) == Integer.parseInt(qp.getNeedKills(quest.get(i)))) {
                            qp.setFinished(quest.get(i));
                            data.addCrystals(qh.getPrice(quest.get(i)).get(0), p);
                            data.addExp(qh.getPrice(quest.get(i)).get(1), p);

                            while (data.getExp(p) >= GameListener.basisExp * (data.getLevel(p) + 1.5)) {
                                data.removeExp(GameListener.basisExp * (data.getLevel(p) + 1.5), p);
                                data.addLevel(p);
                                SkyHeroes.cu.sendMessage(p,  " " + (int) data.getLevel(p));
                            }

                            p.sendMessage("you have finished the quest");

                            ItemStack is = e.getCurrentItem();
                            is.setDurability((short) 13);

                            e.getInventory().setItem(e.getSlot(), is);

                            p.updateInventory();
                        }
                    } else if (qh.getObjective(quest.get(i)).get(0).contains("object")) {
                        if (p.getInventory().contains(Material.getMaterial(qh.getObjective(quest.get(i)).get(1)))) {
                            p.getInventory().remove(Material.getMaterial(qh.getObjective(quest.get(i)).get(1)));
                            qp.setFinished(quest.get(i));
                            p.sendMessage("you have finished the quest");

                            data.addCrystals(qh.getPrice(quest.get(i)).get(0), p);
                            data.addExp(qh.getPrice(quest.get(i)).get(1), p);

                            while (data.getExp(p) >= GameListener.basisExp * (data.getLevel(p) + 1.5)) {
                                data.removeExp(GameListener.basisExp * (data.getLevel(p) + 1.5), p);
                                data.addLevel(p);
                                SkyHeroes.cu.sendMessage(p,  " " + (int) data.getLevel(p));
                            }

                            ItemStack is = e.getCurrentItem();
                            is.setDurability((short) 13);

                            e.getInventory().setItem(e.getSlot(), is);
                            p.updateInventory();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        Inventory inv;
        qp = new QuestPlayer(e.getPlayer());
        qp.generateDailys();
        quests = qp.getDailys();
        qh = new QuestHandler("side.yml");

        if (e.getRightClicked().getCustomName().equalsIgnoreCase(ChatColor.GOLD + "Quests")) {
            e.setCancelled(true);

            inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Quests");

            for (int i = 0; i < quests.size(); i++) {
                if (qp.hasFinished(quests.get(i))) {
                    ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(ChatColor.GREEN + quests.get(i));
                    im.setLore(Arrays.asList("Mission: " + qh.getObjective(quests.get(i)).get(0),
                            "Objective: " + qh.getObjective(quests.get(i)).get(1),
                            "Crystals: " + qh.getPrice(quests.get(i)).get(0) + "",
                            "Exp: " + qh.getPrice(quests.get(i)).get(1) + ""));
                    is.setItemMeta(im);
                    inv.setItem(i * 2, is);
                } else if (qp.hasActivated(quests.get(i))) {
                    ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(ChatColor.GOLD + quests.get(i));
                    im.setLore(Arrays.asList("Mission: " + qh.getObjective(quests.get(i)).get(0),
                            "Objective: " + qh.getObjective(quests.get(i)).get(1),
                            "Crystals: " + qh.getPrice(quests.get(i)).get(0) + "",
                            "Exp: " + qh.getPrice(quests.get(i)).get(1) + ""));
                    is.setItemMeta(im);
                    inv.setItem(i * 2, is);
                } else if (!qp.hasActivated(quests.get(i)) && !qp.hasFinished(quests.get(i))) {
                    ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(ChatColor.RED + quests.get(i));
                    im.setLore(Arrays.asList("Mission: " + qh.getObjective(quests.get(i)).get(0),
                            "Objective: " + qh.getObjective(quests.get(i)).get(1),
                            "Crystals: " + qh.getPrice(quests.get(i)).get(0) + "",
                            "Exp: " + qh.getPrice(quests.get(i)).get(1) + ""));
                    is.setItemMeta(im);
                    inv.setItem(i * 2, is);
                }
            }

            e.getPlayer().openInventory(inv);
        }
    }
}
