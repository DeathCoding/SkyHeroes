/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.islands;

import com.frax.SkyHeroes.manager.ScoreboardManager;
import com.frax.SkyHeroes.userdata.UserData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class IslandShopMerchant implements Listener {

    Island is;
    IslandPlayers ip;
    UserData data;
    ScoreboardManager sm;
    public static int invSize;

    public IslandShopMerchant(ScoreboardManager sm) {
        is = new Island();
        this.sm = sm;
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        List<String> island = is.getIslands();
        ip = new IslandPlayers(p);
        List<String> islands = new ArrayList<>();

        Inventory inv = Bukkit.createInventory(null, invSize, ChatColor.GOLD + "Shop");

        if (e.getRightClicked().getCustomName().equalsIgnoreCase(ChatColor.GOLD + "Shop")) {
            e.setCancelled(true);
            for (int i = 0; i < island.size(); i++) {
                ItemStack icon;
                if (is.getTier(island.get(i)) == 1) {
                    if (ip.hasIsland(island.get(i))) {
                        icon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
                        ItemMeta iconMeta = icon.getItemMeta();
                        iconMeta.setDisplayName(ChatColor.GREEN + island.get(i));
                        iconMeta.setLore(is.getLore(island.get(i)));
                        icon.setItemMeta(iconMeta);
                        inv.setItem(is.getSlot(island.get(i)), icon);
                    } else {
                        icon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
                        ItemMeta iconMeta = icon.getItemMeta();
                        iconMeta.setDisplayName(ChatColor.GOLD + island.get(i));
                        iconMeta.setLore(is.getLore(island.get(i)));
                        icon.setItemMeta(iconMeta);
                        inv.setItem(is.getSlot(island.get(i)), icon);
                    }
                } else if (is.getTier(island.get(i)) >= 2) {
                    if (ip.hasIsland(island.get(i))) {
                        icon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
                        ItemMeta iconMeta = icon.getItemMeta();
                        iconMeta.setDisplayName(ChatColor.GREEN + island.get(i));
                        iconMeta.setLore(is.getLore(island.get(i)));
                        icon.setItemMeta(iconMeta);
                        inv.setItem(is.getSlot(island.get(i)), icon);
                    } else if (is.hasConnected(island.get(i), p)) {
                        icon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
                        ItemMeta iconMeta = icon.getItemMeta();
                        iconMeta.setDisplayName(ChatColor.GOLD + island.get(i));
                        iconMeta.setLore(is.getLore(island.get(i)));
                        icon.setItemMeta(iconMeta);
                        inv.setItem(is.getSlot(island.get(i)), icon);
                    } else {
                        icon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                        ItemMeta iconMeta = icon.getItemMeta();
                        iconMeta.setDisplayName(ChatColor.RED + island.get(i));
                        iconMeta.setLore(is.getLore(island.get(i)));
                        icon.setItemMeta(iconMeta);
                        inv.setItem(is.getSlot(island.get(i)), icon);
                    }
                }
            }
            p.openInventory(inv);
        }
    }

    @EventHandler
    public void onInteractInventory(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        List<String> island = is.getIslands();

        ip = new IslandPlayers(p);
        data = new UserData();

        if (e.getInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "Shop")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().hasItemMeta() == false || e.getCurrentItem().getItemMeta().hasDisplayName() == false) {
                return;
            }

            for (int i = 0; i < island.size(); i++) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + island.get(i)) && !e.getCurrentItem().equals(null)) {
                    if (data.getLevel(p) >= is.getLevel(island.get(i))) {
                        if (data.getCrystals(p) >= is.getPrice(island.get(i))) {
                            data.removeCrystals(is.getPrice(island.get(i)), p);
                            ip.buyIsland(island.get(i));
                            sm.update(p);
                            p.closeInventory();
                        }
                    }
                }
            }
        }
    }
}
