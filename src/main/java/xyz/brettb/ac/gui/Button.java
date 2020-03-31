package xyz.brettb.ac.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Button extends Label {

    @Getter
    @Setter
    private Consumer<CoreClickEvent> clickHandler;

    @Getter
    @Setter
    private boolean closingOnClick = false;

    public Button(ItemStack item, Dimension size, Dimension position, Consumer<CoreClickEvent> clickHandler, String name, String... lore) {
        super(item, size, position, name, lore);
        this.clickHandler = clickHandler;
    }

    public Button(ItemStack item, Dimension size, Dimension position, Consumer<CoreClickEvent> clickHandler) {
        super(item, size, position);
        this.clickHandler = clickHandler;
    }

    @Override
    public void onClick(CoreClickEvent e) {
        clickHandler.accept(e);
        e.setClosing(closingOnClick);
    }

}
