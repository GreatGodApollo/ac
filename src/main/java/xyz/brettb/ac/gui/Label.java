package xyz.brettb.ac.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.brettb.ac.items.CoreMaterial;
import xyz.brettb.ac.util.ArrayUtils;
import xyz.brettb.ac.util.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Label extends CoreGUIComponent {

    @Getter
    @Setter
    private String name;

    @Getter
    private List<String> lore;

    public Label(ItemStack item, Dimension size, Dimension position, String name, String... lore) {
        super (item, size, position);
        this.name = name;
        this.lore = Arrays.asList(lore);
    }

    public static Label background(int x, int y, int width, int height) {
        return new Label(CoreMaterial.BLACK_STAINED_GLASS_PANE.parseItem(), Dimension.of(width, height), Dimension.of(x, y), " ");
    }

    @Override
    public ItemStack simpleRender(Player player, int offsetX, int offsetY) {
        ItemStack newItem = getItem().clone();
        ItemMeta meta = newItem.getItemMeta();
        meta.setDisplayName(TextUtils.colorizeText("&r" + name));
        meta.setLore(ArrayUtils.colorize(lore));
        newItem.setItemMeta(meta);
        return newItem;
    }

    @Override
    public void onClick(CoreClickEvent e) {
        // IGNORE EVENT
    }

    private void addLore(String... lore) {
        ArrayList<String> newLore = new ArrayList<>();
        newLore.addAll(getLore());
        newLore.addAll(Arrays.asList(lore));
        this.lore = Collections.unmodifiableList(newLore);
    }

}
