package xyz.brettb.ac.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CoreGUIExitListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer() == null || e.getInventory() == null) {
            return;
        }
        if (!(e.getPlayer() instanceof Player) || !(e.getInventory().getHolder() instanceof CoreGUI)) {
            return;
        }

        ((CoreGUI) e.getInventory().getHolder()).onExit(e);
    }
}
