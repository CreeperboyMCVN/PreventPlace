/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.CreeperboyMCVN.preventplace.Listener;

import com.CreeperboyMCVN.preventplace.PreventPlace;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author wndows
 */
public class InventoriesEvent implements Listener {
    
    private PreventPlace plugin;
    private Map<String, Boolean> isOpenInv = new HashMap<>();
    
    public InventoriesEvent(PreventPlace pl) {
        this.plugin = pl;
    }
    
    @EventHandler
   public void onOpenInv(InventoryOpenEvent e) {
       Player p = (Player) e.getPlayer();
       isOpenInv.put(p.getName(), Boolean.TRUE);
   }
   
   @EventHandler
   public void onCloseInv(InventoryCloseEvent e) {
       Player p = (Player) e.getPlayer();
       isOpenInv.put(p.getName(), Boolean.FALSE);
   }
   
   @EventHandler(priority = EventPriority.LOW)
   public void onStoringItems(InventoryClickEvent e) {
       
       Player p = (Player) e.getWhoClicked();
       List<String> whitelistShop = plugin.getConfig().getStringList("WhitelistStoreName");
       
       if (p.hasPermission("preventplace.bypass")) return;
       if (!isOpenInv.get(p.getName())) return;
       if (e.getInventory().getHolder() == null) return;
       
       ItemStack item = e.getCurrentItem();
       
       if (item == null) return;
       if (!plugin.getConfig().getBoolean("EnablePreventStore")) return;
       
       if (plugin.getConfig().getBoolean("EnableBlacklistItem")) {
           List<String> blacklist = plugin.getConfig().getStringList("BlacklistedItem");
           for (String s: blacklist) {
               if (item.getType().toString().equalsIgnoreCase(s)) {
                   p.sendMessage(ChatColor.translateAlternateColorCodes('&', (String) Objects.requireNonNull(plugin.getConfig().getString("StoreErr"))));
                   e.setCancelled(true);
                   e.setResult(Event.Result.DENY);
               }
           }
       }
       
       if (plugin.getConfig().getBoolean("EnableStoreCustomName")) {
           ItemMeta itemMeta = item.getItemMeta();
           if (itemMeta == null) return;
           if (itemMeta.hasDisplayName()) {
               p.sendMessage(ChatColor.translateAlternateColorCodes('&', (String) Objects.requireNonNull(plugin.getConfig().getString("StoreErr"))));
               e.setCancelled(true);
               e.setResult(Event.Result.DENY);
           }
       }
   }
}
