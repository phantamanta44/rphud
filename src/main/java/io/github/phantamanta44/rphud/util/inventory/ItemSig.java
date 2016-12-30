package io.github.phantamanta44.rphud.util.inventory;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSig {

    private final Item item;
    private final int meta;

    public ItemSig(int id) {
        this(id, -1);
    }

    public ItemSig(int id, int meta) {
        this(Item.getItemById(id), meta);
    }

    public ItemSig(Item item) {
        this(item, -1);
    }

    public ItemSig(Item item, int meta) {
        this.item = item;
        this.meta = meta;
    }

    public boolean matches(ItemStack is) {
        return is.getItem().equals(item) && (meta == -1 || is.getMetadata() == meta);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(item.getUnlocalizedName());
        if (meta != -1)
            sb.append(":").append(meta);
        return sb.toString();
    }

}
