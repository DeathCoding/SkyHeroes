/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.listener;

import com.frax.SkyHeroes.SkyHeroes;
import com.frax.SkyHeroes.manager.ScoreboardManager;
import com.frax.SkyHeroes.userdata.UserData;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LanguageListener implements Listener {

    private UserData data;
    private ScoreboardManager sm;

    public LanguageListener(ScoreboardManager sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onItemInteractEvent(PlayerInteractEvent e) {
        data = new UserData();
        Inventory inv;
        Player p = e.getPlayer();

        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            if (p.getItemInHand() == null || !(p.getItemInHand().hasItemMeta()) || !(p.getItemInHand().getItemMeta().hasDisplayName())) {
                return;
            }

            if (p.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Language")) {
                e.setCancelled(true);
                inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Please choose a Language");

                inv.setItem(3, german());
                inv.setItem(5, englisch());

                e.getPlayer().openInventory(inv);
            }
        } else {
            return;
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        data = new UserData();
        if (inv.getTitle().equals(ChatColor.GOLD + "Please choose a Language")) {
            e.setCancelled(true);

            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().hasItemMeta() == false || e.getCurrentItem().getItemMeta().hasDisplayName() == false) {
                return;
            }

            if (e.getCurrentItem().getType().equals(Material.GLOWSTONE_DUST)) {
                data.setLanguageType(true, (Player) e.getWhoClicked());
                sm.update((Player) e.getWhoClicked());
                SkyHeroes.cu.sendMessage((Player) e.getWhoClicked(), ChatColor.DARK_PURPLE + "Du hast die Deutsche Sprache aktiviert!");
                e.getWhoClicked().closeInventory();
            }

            if (e.getCurrentItem().getType().equals(Material.COAL)) {
                data.setLanguageType(false, (Player) e.getWhoClicked());
                sm.update((Player) e.getWhoClicked());
                SkyHeroes.cu.sendMessage((Player) e.getWhoClicked(), ChatColor.DARK_PURPLE + "You have activated the English language!");
                e.getWhoClicked().closeInventory();
            }
        }
    }

    private ItemStack englisch() {
        ItemStack i = new ItemStack(Material.COAL);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.DARK_BLUE + "English");
        i.setItemMeta(im);

        return i;
    }

    private ItemStack german() {
        ItemStack i = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "Deutsch");
        i.setItemMeta(im);

        return i;
    }

}
