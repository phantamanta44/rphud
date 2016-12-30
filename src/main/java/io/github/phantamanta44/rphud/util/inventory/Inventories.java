package io.github.phantamanta44.rphud.util.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Inventories {

    public static Stream<ItemStack> stream(IInventory inv) {
        return IntStream.range(0, inv.getSizeInventory())
                .mapToObj(inv::getStackInSlot)
                .filter(Objects::nonNull);
    }

}
