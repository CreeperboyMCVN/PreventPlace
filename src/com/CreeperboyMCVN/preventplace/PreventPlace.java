/* Decompiler 4ms, total 304ms, lines 56 */
package com.CreeperboyMCVN.preventplace;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class PreventPlace extends JavaPlugin implements Listener {
    
    Map<String, Boolean> isOpenInv = new HashMap<>();
    
   @Override
   public void onEnable() {
      this.getLogger().info("Plugin Prevent Place đang được bật");
      this.getServer().getPluginManager().registerEvents(this, this);
      this.getLogger().info("Đã register event");
      this.saveDefaultConfig();
      this.getLogger().info("Đã lưu dữ liệu");
      this.getLogger().info("Đã hoàn thành bật plugin");
      this.getLogger().info("********************************");
      this.getLogger().info("*         PREVENT PLACE        *");
      this.getLogger().info("*             v 1.1            *");
      this.getLogger().info("* Creator: CreeperboyMCVN      *");
      this.getLogger().info("*                              *");
      this.getLogger().info("********************************");
      if (getConfig().getInt("config-version") == 0) {
          List<String> blacklistedItem = new ArrayList<>();
          blacklistedItem.add("BARREL");
          blacklistedItem.add("CHEST");
          blacklistedItem.add("DISPENSER");
          blacklistedItem.add("DROPPER");
          blacklistedItem.add("CRAFTING");
          getConfig().set("BlacklistedInventory", blacklistedItem);
          getConfig().set("config-version", 1);
          getConfig().set("StoreErr", "&cBạn không thể đặt vật phẩm có tên tùy chỉnh vào đây!");
          saveConfig();
      this.getLogger().info("********************************************************");
      this.getLogger().info("*                      PREVENT PLACE                   *");
      this.getLogger().info("*                          v 1.1                       *");
      this.getLogger().info("* Creator: CreeperboyMCVN                              *");
      this.getLogger().info("*                                                      *");
      this.getLogger().info("* Update v 1.1:                                        *");
      this.getLogger().info("* - Thêm các mục 'config-version', 'BlacklistedInvento *");
      this.getLogger().info("*    ry', 'StoreErr' trong config.yml                  *");
      this.getLogger().info("* - Thêm tính năng cấm bỏ đồ có tên tùy chỉnh vào các  *");
      this.getLogger().info("*    kho đồ trong mục 'BlacklistedInventory'           *");
      this.getLogger().info("********************************************************");
      }
   }

   @Override
   public void onDisable() {
      this.getLogger().info("Đang tắt plugin Prevent Place");
      this.getLogger().info("Đang lưu dữ liệu");
      this.saveDefaultConfig();
      this.getLogger().info("Plugin đã tắt! Bye!!");
   }

   @Override
   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage((String)Objects.requireNonNull(this.getConfig().getString("ConsoleErr")));
         return true;
      } else {
         if (label.equalsIgnoreCase("preventplace")) {
            if (sender.hasPermission("preventplace.reload")) {
                this.reloadConfig();
                this.saveDefaultConfig();
                notify((Player) sender);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',(String)Objects.requireNonNull(this.getConfig().getString("Reload"))));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(this.getConfig().getString("NoPerm"))));
            }
         }

         return false;
      }
   }

   @EventHandler
   public void onPlace(BlockPlaceEvent event) {
      Player player = event.getPlayer();
      ItemStack check = event.getItemInHand();
      ItemMeta checkmeta = check.getItemMeta();

      assert checkmeta != null;
      
      if (!getConfig().getBoolean("EnablePreventPlace")) return;

      if (!checkmeta.hasDisplayName()) return;
      if (checkmeta.hasDisplayName()) {
         event.setCancelled(true);
         player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(this.getConfig().getString("PlaceError"))));
      }

   }
   
   @EventHandler
   public void onOpenInv(InventoryOpenEvent e) {
       Player p = (Player) e.getPlayer();
       isOpenInv.put(p.getName(), Boolean.FALSE);
       if (e.getInventory().getHolder() == null) return;
       isOpenInv.put(p.getName(), Boolean.TRUE);
   }
   
   @EventHandler
   public void onCloseInv(InventoryCloseEvent e) {
       Player p = (Player) e.getPlayer();
       isOpenInv.put(p.getName(), Boolean.FALSE);
   }
   
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onStoringItems(InventoryClickEvent e) {
       Player p = (Player) e.getWhoClicked();
       ClickType clickType = e.getClick();
       List<String> blItem = this.getConfig().getStringList("BlacklistedItem");
       ItemStack item = e.getCurrentItem();
       
       
       if (item == null) return;
       
       if (!getConfig().getBoolean("EnablePreventStore")) return;
       
       if (clickType.toString().equalsIgnoreCase("NUMBER_KEY")) {
           if (!getConfig().getBoolean("EnableNumberClickType")) {
                e.setCancelled(true);
                e.setResult(Result.DENY);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(this.getConfig().getString("NumberClickMessage"))));
            }
       }
       
       if (isOpenInv.get(p.getName())) {
           for (String s: blItem) {
               if (item.getType().toString().equalsIgnoreCase(s)) {
                   e.setCancelled(true);
                   e.setResult(Result.DENY);
                   p.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(this.getConfig().getString("StoreErr"))));
               }
           }
       }
   }
    
    
    public void notify(Player p) {
        
        if (!(p instanceof Player)) return;
        
        if (p.getPlayer().hasPermission("preventplace.adminnotify")) {
            if (getConfig().getBoolean("EnableNumberClickType")&&getConfig().getBoolean("EnablePreventStore")) {
                p.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(this.getConfig().getString("UseNumberClickTypeWarning"))));
            }
        }
    }
    
    public void onJoin(PlayerJoinEvent e) {
        notify(e.getPlayer());
    }
   
}