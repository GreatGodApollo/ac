package xyz.brettb.ac.items;

import lombok.NonNull;
import me.ialistannen.mininbt.ItemNBTUtil;
import me.ialistannen.mininbt.NBTWrappers;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ItemBuilder {
    private ItemStack item;

    private ItemBuilder(ItemStack itemStack) {
        item = itemStack;
    }

    public static ItemBuilder of(@NonNull Material mat) {
        return of(new ItemStack(mat));
    }

    public static ItemBuilder of(@NonNull CoreMaterial mat) {
        if (mat.parseItem(true) == null) {
            return of(new ItemStack(Material.STONE));
        }
        return of(new ItemStack(Objects.requireNonNull(mat.parseItem(true))));
    }

    public static ItemBuilder of(@NonNull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public ItemBuilder mutateMeta(Consumer<ItemMeta> mutator) {
        ItemMeta im = item.getItemMeta();
        mutator.accept(im);
        item.setItemMeta(im);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        return mutateMeta(im -> im.setDisplayName(name));
    }

    public ItemBuilder setDurability(short durability) {
        item.setDurability(durability);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        return mutateMeta(im -> im.setLore(Arrays.asList(lore)));
    }

    public ItemBuilder addEnchantment(Enchantment enchant, int level, boolean ignoreRestrictions) {
        return mutateMeta(im -> im.addEnchant(enchant, level, ignoreRestrictions));
    }

    public ItemBuilder addEnchantments(Map<Enchantment,Integer> enchants, boolean ignoreRestrictions) {
        enchants.forEach((enchant, level) -> addEnchantment(enchant, level, ignoreRestrictions));
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags) {
        return mutateMeta(im -> im.addItemFlags(flags));
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        return mutateMeta(im -> im.setUnbreakable(unbreakable));
    }

    public ItemBuilder setNBTTag(String key, String value) {
        NBTWrappers.NBTTagCompound nbt = ItemNBTUtil.getTag(item);
        nbt.setString(key, value);
        item = ItemNBTUtil.setNBTTag(nbt, item);
        return this;
    }

    public ItemStack build() {
        return item;
    }

}
