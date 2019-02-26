/*
 * Copyright (c)
 * Developed by Daniel Platkevicius
 */

package com.frax.SkyHeroes.listener;

import com.frax.SkyHeroes.SkyHeroes;
import com.frax.SkyHeroes.manager.Config;
import com.frax.SkyHeroes.manager.ScoreboardManager;
import com.frax.SkyHeroes.nms.ActionBarUtil;
import com.frax.SkyHeroes.nms.TabUtil;
import com.frax.SkyHeroes.nms.SenderThread;
import com.frax.SkyHeroes.quest.QuestHandler;
import com.frax.SkyHeroes.quest.QuestPlayer;
import com.frax.SkyHeroes.tutorial.TutorialThread;
import com.frax.SkyHeroes.userdata.UserData;
import com.frax.SkyHeroes.utils.LocationUtil;
import com.frax.SkyHeroes.utils.Stats;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.swing.plaf.synth.SynthRadioButtonMenuItemUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameListener implements Listener {

    ScoreboardManager sm;
    LocationUtil lu;
    Stats s;
    UserData data;
    Config cfg;
    ActionBarUtil abUtil;
    TabUtil tabUtil;
    Thread t;
    TutorialThread tutorial;
    QuestPlayer qp;
    QuestHandler qh;
    Thread fightThread;
    private SenderThread thread;
    List<Player> inFight = new ArrayList<>();
    public static HashMap<Player, Integer> killstreak = new HashMap<>();
    public static int basisExp;
    public static int basisCrystals;
    public static int fallDeath;
    public static String tabHeader;
    public static String tabFooter;
    public static int lvl1, lvl2, lvl3, money1, money2, money3;

    public GameListener(ScoreboardManager sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onPlayerFight(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof  Player)) return;

        Player damager = (Player) e.getDamager();
        Player target = (Player) e.getEntity();

        if (!inFight.contains(damager)) {
            inFight.add(damager);
        }

        if (!inFight.contains(target)) {
            inFight.add(target);

            SkyHeroes.cu.sendMessage(damager, "Your in a fight, if you log out all your items will get dropped");
            SkyHeroes.cu.sendMessage(target, "Your in a fight, if you log out all your items will get dropped");
        }

        fightThread = new Thread(() -> {
            boolean fight = true;
            while (fight) {
                for (int i = 30; i > 0; i--) {
                    if (i == 1) {
                        fight = false;
                        inFight.remove(damager);
                        inFight.remove(target);

                        try {
                            fightThread.join();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        fightThread.start();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (inFight.contains(e.getPlayer())) {
            e.getPlayer().setHealth(0.0D);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ItemStack i = new ItemStack(Material.BOOK);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatColor.RED + "Language");
        i.setItemMeta(im);

        sm = new ScoreboardManager();
        UserData data = new UserData();
        lu = new LocationUtil("Spawn");
        Player p = e.getPlayer();
        tabUtil = new TabUtil(p);
        abUtil = new ActionBarUtil();
        thread = new SenderThread(p, sm);
        s = new Stats(p);
        s.createAccount();
        data.createAccount(p);

        sm.update(e.getPlayer());
        tabUtil.sendTab(tabHeader, tabFooter);

        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta ims = is.getItemMeta();

        int locks = data.getLocks(p);

        if (locks == 3) {
            ims.setDisplayName(ChatColor.RED + "Locked");
            ims.setLore(Arrays.asList("Level: " + lvl1, "Crystals: " + money1));
            is.setItemMeta(ims);
        } else if (locks == 2) {
            ims.setDisplayName(ChatColor.YELLOW + "Locked");
            ims.setLore(Arrays.asList("Level: " + lvl2, "Crystals: " + money2));
            is.setItemMeta(ims);
        } else {
            ims.setDisplayName(ChatColor.GREEN + "Locked");
            ims.setLore(Arrays.asList("Level: " + lvl3, "Crystals: " + money3));
            is.setItemMeta(ims);
        }

        for (int j = 8; j < 36; j++) {
            if (locks == 3) {
                p.getInventory().setItem(j, is);
            } else if (locks == 2 && (j > 8 && j < 27) ) {
                p.getInventory().setItem(j, is);
            } else if (locks == 1 && (j > 8 && j < 18)) {
                p.getInventory().setItem(j, is);
            }
        }

        p.getInventory().setItem(8, i);

        if (data.hasTutorialDone(p)) {
            if (!lu.getFile().exists()) {
                System.out.println("Spawn has to be setted!");
                SkyHeroes.cu.sendMessage(p, ChatColor.GOLD + "Spawn is not setted! Report it to an admin.");
                return;
            } else {
                p.teleport(lu.getLocation());
            }
        } else {
            data.setTutorialDone(p);
            tutorial = new TutorialThread(p);
            tutorial.start();
        }

        t = new Thread(thread);
        t.start();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.getPlayer().getInventory().remove(Material.STAINED_GLASS_PANE);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        data = new UserData();
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().hasItemMeta() == false || e.getCurrentItem().getItemMeta().hasDisplayName() == false) {
            return;
        }

        if (e.getClickedInventory() instanceof PlayerInventory) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Locked")) {
                e.setCancelled(true);
                if (data.getLevel(p) >= lvl1 && data.getCrystals(p) >= money1) {
                    for (int i = 27; i < 36; i++) {
                        ItemStack is = new ItemStack(Material.AIR);
                        p.getInventory().setItem(i, is);
                        p.updateInventory();
                    }

                    data.removeCrystals(money1, p);
                    data.removeLock(p);

                    ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE);
                    ItemMeta ims = is.getItemMeta();
                    ims.setDisplayName(ChatColor.YELLOW + "Locked");
                    ims.setLore(Arrays.asList("Level: " + lvl2, "Crystals: " + money2));
                    is.setItemMeta(ims);

                    for (int i = 18; i < 27; i++) {
                        p.getInventory().setItem(i, is);
                        p.updateInventory();
                    }
                }
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Locked")) {
                e.setCancelled(true);
                if (data.getLevel(p) >= lvl2 && data.getCrystals(p) >= money2) {
                    for (int i = 18; i < 27; i++) {
                        ItemStack is = new ItemStack(Material.AIR);
                        p.getInventory().setItem(i, is);
                        p.updateInventory();
                    }

                    data.removeCrystals(money2, p);
                    data.removeLock(p);

                    ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE);
                    ItemMeta ims = is.getItemMeta();
                    ims.setDisplayName(ChatColor.GREEN + "Locked");
                    ims.setLore(Arrays.asList("Level: " + lvl3, "Crystals: " + money3));
                    is.setItemMeta(ims);

                    for (int i = 9; i < 18; i++) {
                        p.getInventory().setItem(i, is);
                        p.updateInventory();
                    }
                }
            } else {
                e.setCancelled(true);
                if (data.getLevel(p) >= lvl3 && data.getCrystals(p) >= money3) {
                    for (int i = 9; i < 18; i++) {
                        ItemStack is = new ItemStack(Material.AIR);
                        p.getInventory().setItem(i, is);
                        p.updateInventory();
                    }

                    data.removeCrystals(money3, p);
                    data.removeLock(p);
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        s = new Stats(p);
        if (e.getTo().getY() <= fallDeath && !p.isDead()) {
            p.setHealth(0.0D);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        lu = new LocationUtil("Spawn");

        e.setRespawnLocation(lu.getLocation());

        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta ims = is.getItemMeta();

        int locks = data.getLocks(p);

        if (locks == 3) {
            ims.setDisplayName(ChatColor.RED + "Locked");
            ims.setLore(Arrays.asList("Level: " + lvl1, "Crystals: " + money1));
            is.setItemMeta(ims);
        } else if (locks == 2) {
            ims.setDisplayName(ChatColor.YELLOW + "Locked");
            ims.setLore(Arrays.asList("Level: " + lvl2, "Crystals: " + money2));
            is.setItemMeta(ims);
        } else {
            ims.setDisplayName(ChatColor.GREEN + "Locked");
            ims.setLore(Arrays.asList("Level: " + lvl3, "Crystals: " + money3));
            is.setItemMeta(ims);
        }

        for (int j = 8; j < 36; j++) {
            if (locks == 3) {
                p.getInventory().setItem(j, is);
            } else if (locks == 2 && (j > 8 && j < 27) ) {
                p.getInventory().setItem(j, is);
            } else if (locks == 1 && (j > 8 && j < 18)) {
                p.getInventory().setItem(j, is);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
        Player p = e.getPlayer();

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (b.getType() == Material.ANVIL) {
                e.setCancelled(true);

                Inventory inv = Bukkit.createInventory(p, InventoryType.ANVIL);
                p.openInventory(inv);
            }
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();

        if (e.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            e.setCancelled(true);
            ArmorStand holder = (ArmorStand) e.getRightClicked();

            p.getInventory().addItem(holder.getChestplate());
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage("");
        Player killer = e.getEntity().getKiller();
        Player target = e.getEntity().getPlayer();
        qp = new QuestPlayer(killer);
        qh = new QuestHandler("side.yml");
        sm = new ScoreboardManager();
        cfg = new Config();
        abUtil = new ActionBarUtil();

        data = new UserData();

        double percent = (data.getLevel(target) / data.getLevel(killer));

        System.out.println(percent);

        String expGain = data.getLanguage(killer) ? ChatColor.translateAlternateColorCodes('&' , cfg.getCfg().getString("expGainDe")) : ChatColor.translateAlternateColorCodes('&' , cfg.getCfg().getString("expGainEng"));
        expGain = expGain.replace("%exp%", "" + Math.round(percent * basisExp));
        expGain = expGain.replace("%crystals%", "" + Math.round(percent * basisCrystals));

        String levelUp = data.getLanguage(killer) ? ChatColor.translateAlternateColorCodes('&' , cfg.getCfg().getString("levelUpDe")) : ChatColor.translateAlternateColorCodes('&' , cfg.getCfg().getString("levelUpEng"));

        String kill = data.getLanguage(killer) ? ChatColor.translateAlternateColorCodes('&' , cfg.getCfg().getString("killDe")) : ChatColor.translateAlternateColorCodes('&' , cfg.getCfg().getString("killEng"));

        String killed = data.getLanguage(target) ? ChatColor.translateAlternateColorCodes('&' , cfg.getCfg().getString("slainDe")) : ChatColor.translateAlternateColorCodes('&' , cfg.getCfg().getString("slainEng"));

        s = new Stats(killer);
        if (killer != null) {
            kill = kill.replace("%target%", target.getName());
            killed = killed.replace("%killer%", killer.getName());
            s.addKills();

            if (!killstreak.containsKey(killer)) {
                killstreak.put(killer, 1);
            } else {
                killstreak.put(killer, killstreak.get(killer) + 1);
                checkKillstreak(killer);
            }

            if (killstreak.containsKey(target))
                killstreak.remove(target);

            System.out.println("b" + percent * basisExp);
            System.out.println("b" + percent * basisCrystals);

            data.addExp(percent * basisExp, killer);
            data.addCrystals(basisCrystals * percent, killer);
            data.addCrystals(data.getBounty(target), killer);
            SkyHeroes.cu.sendMessage(killer, expGain);
            SkyHeroes.cu.sendMessage(killer, "You collected a bounty of " + data.getBounty(target) + " Crystal(s) from " + target.getName());
            data.resetBounty(target);

            for (int i = 0; i < qp.getDailys().size(); i++) {
                if (qh.getObjective(qp.getDailys().get(i)).get(0).contains("kill")) {
                    qp.addKills(qp.getDailys().get(i));
                }
            }

            while (data.getExp(killer) >= GameListener.basisExp * (data.getLevel(killer) + 1.5)) {
                data.removeExp(GameListener.basisExp * (data.getLevel(killer) + 1.5), killer);
                data.addLevel(killer);
                SkyHeroes.cu.sendMessage(killer,  levelUp + " " + (int) data.getLevel(killer));

               Firework f = killer.getWorld().spawn(killer.getLocation(), Firework.class);

                FireworkMeta fm = f.getFireworkMeta();

                fm.addEffect(FireworkEffect.builder()
                        .flicker(false)
                        .trail(true)
                        .with(FireworkEffect.Type.CREEPER)
                        .withColor(Color.GREEN)
                        .withFade(Color.BLUE)
                        .build());
                fm.setPower(1);
                f.setFireworkMeta(fm);
            }

            SkyHeroes.cu.sendMessage(killer, kill);
            SkyHeroes.cu.sendMessage(target, killed);

            sm.update(killer);

            s.setPlayer(target);
            s.addDeaths();

            sm.update(target);
        }
    }

    private void checkKillstreak(Player p) {
        if (killstreak.get(p) == 5) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, 2));
        } else if (killstreak.get(p) == 10) {
            SkyHeroes.cu.broadcast("10 killstreak");
        } else if (killstreak.get(p) == 15) {
            SkyHeroes.cu.broadcast("15 killstreak");
        }
    }

}
