package xyz.brettb.ac.items;

import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    public static boolean isInvalid(ItemStack item) {
        return item == null || item.getType() == CoreMaterial.AIR.parseMaterial();
    }

}
