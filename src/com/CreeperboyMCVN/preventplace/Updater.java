/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.CreeperboyMCVN.preventplace;

import java.io.File;

/**
 *
 * @author wndows
 */
public class Updater {
    private PreventPlace plugin;
    
    public Updater (PreventPlace pl) {
        this.plugin = pl;
    }
    
    public void check() {
        int version = plugin.getConfig().getInt("config-version", 0);
        if ((version == 0)||(version < 2)) {
            File f = new File(plugin.getDataFolder() + File.separator, "config.yml");
            if (!f.exists()) {
                plugin.getLogger().info("Đang tạo tệp config.yml mới...");
                plugin.saveResource("config.yml", true);
                plugin.getLogger().info("Hoàn thành!");
            } else {
                plugin.getLogger().info("Tệp config.yml của bạn đã cũ! Đang tạo cái mới...");
                f.delete();
                plugin.saveResource("config.yml", true);
                plugin.getLogger().info("Hoàn thành!");
            }
        } else {
            plugin.getLogger().info("Tệp config.yml của bạn hiện là bản mới nhất! Việc thay thế không cần thiết.");
        }
    }
}
