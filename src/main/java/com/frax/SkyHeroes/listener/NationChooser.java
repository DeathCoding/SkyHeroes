/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.listener;

import com.frax.SkyHeroes.SkyHeroes;
import com.frax.SkyHeroes.manager.Config;
import com.frax.SkyHeroes.manager.ScoreboardManager;
import com.frax.SkyHeroes.userdata.UserData;
import com.frax.SkyHeroes.utils.LocationUtil;
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

public class NationChooser implements Listener {

    private Inventory inv;
    private UserData data;
    private LocationUtil util;
    private Config cfg;
    private ScoreboardManager sm;

    public NationChooser(ScoreboardManager sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onClick(PlayerInteractEntityEvent e) {
        cfg = new Config();
        data = new UserData();

        if (e.getRightClicked().getCustomName().equalsIgnoreCase(ChatColor.GOLD + "Nation")) {
            e.setCancelled(true);
            inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Nation");

            inv.setItem(3, hero(e.getPlayer()));
            inv.setItem(5, villain(e.getPlayer()));

            e.getPlayer().openInventory(inv);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        data = new UserData();
        util = new LocationUtil("Spawn");
        cfg = new Config();

        String heroMessage = data.getLanguage(p) ? ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("heroMessageDe")) : ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("heroMessageEng"));
        String villainMessage = data.getLanguage(p) ? ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("villainMessageDe")) : ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("villainMessageEng"));

        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().hasItemMeta() == false || e.getCurrentItem().getItemMeta().hasDisplayName() == false) {
            return;
        }

        if (inv.getTitle().equals(ChatColor.GOLD + "Nation")) {
            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.GLOWSTONE_DUST) {
                data.addNation("Hero", p);
                p.teleport(util.getLocation());
                SkyHeroes.cu.sendMessage(p, heroMessage);
                sm.update(p);
            }

            if (e.getCurrentItem().getType() == Material.COAL) {
                data.addNation("Villain", p);
                p.teleport(util.getLocation());
                SkyHeroes.cu.sendMessage(p, villainMessage);
                sm.update(p);
            }

        }
    }

    private ItemStack hero(Player p) {
        String hero = data.getLanguage(p) ? ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("heroDe")) : ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("heroEng"));

        ItemStack i = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(hero);
        i.setItemMeta(im);

        return i;
    }

    private ItemStack villain(Player p) {
        String villain = data.getLanguage(p) ? ChatColor.translateAlternateColorCodes('&' , cfg.getCfg().getString("villainDe")) : ChatColor.translateAlternateColorCodes('&' ,cfg.getCfg().getString("villainEng"));
        ItemStack i = new ItemStack(Material.COAL);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(villain);
        i.setItemMeta(im);

        return i;
    }

}
