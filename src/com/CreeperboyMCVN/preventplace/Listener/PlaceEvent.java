/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.CreeperboyMCVN.preventplace.Listener;

import com.CreeperboyMCVN.preventplace.PreventPlace;
import java.util.List;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author wndows
 */
public class PlaceEvent implements Listener {
    
    private PreventPlace plugin;
    
    public PlaceEvent(PreventPlace pl) {
        this.plugin = pl;
    }
    
   @EventHandler
   public void onPlace(BlockPlaceEvent event) {
      Player player = event.getPlayer();
      ItemStack check = event.getItemInHand();
      ItemMeta checkmeta = check.getItemMeta();

      assert checkmeta != null;
      
      if (!plugin.getConfig().getBoolean("EnablePreventPlace")) return;
      if (player.hasPermission("preventplace.bypass")) return;
      List<String> itemList = plugin.getConfig().getStringList("PlaceWhitelistItem");
      for (String s: itemList) {
          if (check.getType().toString().equalsIgnoreCase(s)) return;
      }

      if (!checkmeta.hasDisplayName()) return;
      if (checkmeta.hasDisplayName()) {
         event.setCancelled(true);
         player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(plugin.getConfig().getString("PlaceError"))));
      }

   }
}
