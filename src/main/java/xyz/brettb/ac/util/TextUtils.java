package xyz.brettb.ac.util;

import org.bukkit.ChatColor;

public class TextUtils {

    public static String colorizeText(String text) {
        if (text == null) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
