package xyz.brettb.ac.gui;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.brettb.ac.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class CoreGUI implements InventoryHolder {
    private List<CoreGUIComponent> components = new ArrayList<>();

    @Getter(AccessLevel.PROTECTED)
    private JavaPlugin plugin;

    @Getter
    private Player player;

    @Getter
    private Inventory inventory;

    private Dimension size;

    protected CoreGUI(@NonNull JavaPlugin pl, Player player, String title, @NonNull int rows) {
        if (rows < 1 || rows > 6) {
            throw new IllegalArgumentException("Rows must be between 1 and 6!");
        }

        this.player = player;
        inventory = Bukkit.createInventory(this, rows * 9, TextUtils.colorizeText(title));
        this.size = Dimension.of(9, rows * 9);
        this.plugin = pl;
    }

    public void render() {
        inventory.setContents(new ItemStack[0]);

        components.forEach(comp -> {
            Dimension pos = comp.getPosition();
            if (!pos.add(comp.getSize()).fitsInside(size)) {
                plugin.getLogger().warning("Invalid component at " + pos.toString());
                return;
            }
            comp.render(player, this, pos);
        });

        if (player.getOpenInventory().getTopInventory().getHolder() != this) {
            player.closeInventory();
            player.openInventory(getInventory());
        }
    }

    protected CoreGUI setItem(int x, int y, ItemStack item) {
        Dimension pos = Dimension.of(x, y);
        if (!pos.fitsInside(size)) {
            throw new IllegalArgumentException("Can't set item at position " + pos.toString() + ": position exceeds GUI bounds!");
        }
        inventory.setItem(x + y * 9, item);
        return this;
    }

    public void onClick(InventoryClickEvent e) {
        int slot = e.getRawSlot();

        Dimension pos = Dimension.of(slot % 9, slot / 9);
        components.stream()
                .filter(comp -> pos.getX() >= comp.getPosition().getX() && pos.getX() < comp.getPosition().getX() + comp.getSize().getX()
                    && pos.getY() >= comp.getPosition().getY() && pos.getY() < comp.getPosition().getY() + comp.getSize().getY())
                .reduce((a, b) -> b)
                .ifPresent(comp -> {
                    CoreClickEvent ce = new CoreClickEvent((Player) e.getWhoClicked(), this, e.getCurrentItem(), e.getClick(), pos.subtract(comp.getPosition()));
                    comp.onClick(ce);
                    e.setCancelled(ce.isCancelled());
                    if (ce.isClosing() && player.getOpenInventory().getTopInventory().getHolder() == this) {
                        player.closeInventory();
                    }
                });
    }

    protected CoreGUI addComponent(CoreGUIComponent comp) {
        if (!comp.getSize().add(comp.getPosition()).fitsInside(size)) {
            return this;
        }
        components.add(comp);
        return this;
    }

    protected CoreGUI setComponents(List<CoreGUIComponent> components) {
        this.components = new ArrayList<>(components);
        return this;
    }

}
