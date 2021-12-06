/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.CreeperboyMCVN.preventplace.Listener;

import com.CreeperboyMCVN.preventplace.PreventPlace;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author wndows
 */
public class InventoriesEvent implements Listener {
    
    private PreventPlace plugin;
    
    public InventoriesEvent(PreventPlace pl) {
        this.plugin = pl;
    }
    
    @EventHandler
    public void onStore(InventoryClickEvent e) {
        if (!plugin.getConfig().getBoolean("EnablePreventStore")) {
            return;
        }
        
        InventoryView iv = e.getView();
        String invTitle = iv.getTitle();
        Bukkit.getConsoleSender().sendMessage("InventoryView Debugger: "+iv.getTitle());
        ItemStack item = e.getCurrentItem();
        List<String> bypassName = plugin.getConfig().getStringList("BypassInventoryName");
        
        if (item==null) return;
        
        for (String s:bypassName) {
            if (invTitle.contains(s)) return;
        }
        
        if (plugin.getConfig().getBoolean("EnableBlacklistItem")) {
            List<String> blItems = plugin.getConfig().getStringList("BlacklistedItem");
            
            for (String s:blItems) {                
                if (item.getType().toString().equalsIgnoreCase(s)) {
                    Player p = (Player) e.getWhoClicked();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("StoreErr")));
                    e.setCancelled(true);
                    return;
                }
            }
        }
        
        if (plugin.getConfig().getBoolean("EnableStoreCustomName")) {
            ItemMeta im = item.getItemMeta();
            if (im==null) return;
            if (im.hasDisplayName()) {
                Player p = (Player) e.getWhoClicked();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("StoreErr")));
                e.setCancelled(true);
            }
        }
    }
    
    
    
}
