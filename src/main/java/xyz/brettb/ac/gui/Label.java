package xyz.brettb.ac.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.brettb.ac.items.CoreMaterial;
import xyz.brettb.ac.items.ItemBuilder;
import xyz.brettb.ac.util.ArrayUtils;
import xyz.brettb.ac.util.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Label extends CoreGUIComponent {

    public Label(ItemStack item, Dimension size, Dimension position, String name, String... lore) {
        super (item, size, position);
        ItemBuilder itemB = ItemBuilder.of(item);
        itemB.setDisplayName(TextUtils.colorizeText(name));
        itemB.setLore(ArrayUtils.colorize(Arrays.asList(lore)));
        setItem(itemB.build());
    }

    public Label(ItemStack item, Dimension size, Dimension position) {
        super (item, size, position);
    }

    public static Label background(int x, int y, int width, int height) {
        return new Label(CoreMaterial.BLACK_STAINED_GLASS_PANE.parseItem(), Dimension.of(width, height), Dimension.of(x, y), " ");
    }

    @Override
    public ItemStack simpleRender(Player player, int offsetX, int offsetY) {
        return getItem();
    }

    @Override
    public void onClick(CoreClickEvent e) {
        // IGNORE EVENT
    }

    private void addLore(String... lore) {
        ItemBuilder itemB = ItemBuilder.of(getItem());
        itemB.addLore(ArrayUtils.colorize(Arrays.asList(lore)));
        setItem(itemB.build());
    }

    private void setName(String name) {
        ItemBuilder itemB = ItemBuilder.of(getItem());
        itemB.setDisplayName(TextUtils.colorizeText(name));
    }

}
