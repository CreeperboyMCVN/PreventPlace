package com.CreeperboyMCVN.preventplace;


import com.CreeperboyMCVN.preventplace.Listener.InventoriesEvent;
import com.CreeperboyMCVN.preventplace.Listener.PlaceEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PreventPlace extends JavaPlugin {
    
    Map<String, Boolean> isOpenInv = new HashMap<>();
    
   @Override
   public void onEnable() {
      this.getLogger().info("Plugin Prevent Place đang được bật");
      this.getServer().getPluginManager().registerEvents(new InventoriesEvent(this), this);
      this.getServer().getPluginManager().registerEvents(new PlaceEvent(this), this);
      this.getLogger().info("Đã register event");
      this.saveDefaultConfig();
      Updater up = new Updater(this);
      up.check();
      this.getLogger().info("Đã lưu dữ liệu");
      this.getLogger().info("Đã hoàn thành bật plugin");
      this.getLogger().info("********************************");
      this.getLogger().info("*         PREVENT PLACE        *");
      this.getLogger().info("*             v 1.3            *");
      this.getLogger().info("* Creator: CreeperboyMCVN      *");
      this.getLogger().info("*                              *");
      this.getLogger().info("********************************");
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
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',(String)Objects.requireNonNull(this.getConfig().getString("Reload"))));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(this.getConfig().getString("NoPerm"))));
            }
         }

         return false;
      }
   }

   
}