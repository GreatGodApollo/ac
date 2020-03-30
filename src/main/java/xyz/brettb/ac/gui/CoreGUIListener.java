package xyz.brettb.ac.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.brettb.ac.items.ItemUtils;

public class CoreGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() == null || e.getInventory() == null) {
            return;
        }

        if (!(e.getWhoClicked() instanceof Player) || !(e.getInventory().getHolder() instanceof CoreGUI)) {
            return;
        }

        if (ItemUtils.isInvalid(e.getCurrentItem())) {
            return;
        }

        e.setCancelled(true);

        if (e.getRawSlot() >= e.getInventory().getSize()) {
            return;
        }

        ((CoreGUI) e.getInventory().getHolder()).onClick(e);
    }

}
