/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.commands;

import com.frax.SkyHeroes.SkyHeroes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemSetup implements CommandExecutor {

    ArmorStand holder;
    ItemStack i;
    Item item;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return true;

        Player p = (Player)commandSender;
        if (strings.length == 0) {
            if (command.getName().equalsIgnoreCase("item")) {
                if (p.hasPermission("SkyHeroes.item")) {
                    SkyHeroes.cu.sendMessage(p, "use /item <id> <name>");
                } else {
                    //TODO: MESSAGE
                }
            }
        }

        if (strings.length > 0) {
            int id = Integer.parseInt(strings[0]);
            String name = "";
            for (int i = 1; i < strings.length; i++) {
                name = name + strings[i] + " ";
            }

            Location loc = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY() - 0.3, p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());

            holder = p.getWorld().spawn(loc, ArmorStand.class);

            i = new ItemStack(Material.getMaterial(id));
            item = p.getWorld().dropItem(p.getLocation(), i);

            holder.setCustomName(ChatColor.AQUA + name + ChatColor.GRAY + " (Right click)");
            holder.setCustomNameVisible(true);
            holder.setGravity(false);
            holder.setVisible(false);
            item.setPickupDelay(Integer.MAX_VALUE);
            holder.setPassenger(item);
            holder.setChestplate(i);
        }

        return true;
    }

}
