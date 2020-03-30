package xyz.brettb.ac.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class CoreClickEvent {

    @Getter
    private Player player;

    @Getter
    private CoreGUI gui;

    @Getter
    private ItemStack item;

    @Getter
    private ClickType type;

    @Getter
    private int offsetX;

    @Getter
    private int offsetY;

    @Getter
    @Setter
    private boolean cancelled = true;

    @Getter
    @Setter
    private boolean closing = false;

    CoreClickEvent(Player player, CoreGUI gui, ItemStack item, ClickType type, Dimension offset) {
        this.player = player;
        this.gui = gui;
        this.item = item;
        this.type = type;
        this.offsetX = offset.getX();
        this.offsetY = offset.getY();
    }


}
