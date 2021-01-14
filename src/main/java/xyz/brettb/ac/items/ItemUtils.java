package xyz.brettb.ac.items;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    public static boolean isInvalid(ItemStack item) {
        return item == null || item.getType() == XMaterial.AIR.parseMaterial();
    }

}
