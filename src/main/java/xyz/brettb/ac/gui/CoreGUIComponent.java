package xyz.brettb.ac.gui;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.brettb.ac.items.CoreMaterial;

public abstract class CoreGUIComponent {

    private static int counter;

    @Getter
    private final int ID = counter++;

    @Getter
    @Setter
    private ItemStack item;

    @Getter
    @Setter
    private Dimension size;

    @Getter
    @Setter
    private Dimension position;

    protected CoreGUIComponent(@NonNull ItemStack item, @NonNull Dimension size, @NonNull Dimension position) {
        if (item.getType() == CoreMaterial.AIR.parseMaterial()) {
            throw new IllegalArgumentException("Item cannot be AIR!");
        }
        this.item = item;
        this.size = size;
        this.position = position;
    }

    public void render(Player player, CoreGUI gui, Dimension offset) {
        for (int x = 0; x < size.getX(); x++) {
            for (int y = 0; y < size.getY(); y++) {
                gui.setItem(offset.getX() + x, offset.getY() + y, simpleRender(player, x, y));
            }
        }
    }

    public void render(Player player, CoreGUI gui) {
        render(player, gui, Dimension.square(0));
    }

    ItemStack simpleRender(Player player, int offsetX, int offsetY) {
        return item;
    }

    public abstract void onClick(CoreClickEvent e);
}
