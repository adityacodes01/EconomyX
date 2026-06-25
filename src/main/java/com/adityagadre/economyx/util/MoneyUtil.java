package com.adityagadre.economyx.util;

import com.adityagadre.economyx.registry.ModItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

/** Shared helpers for converting between dollar amounts and physical bill items. */
public final class MoneyUtil {

    private MoneyUtil() {}

    /** Bill item -> dollar value, ordered high to low for greedy change-making. */
    public static Map<Item, Long> billValues() {
        Map<Item, Long> map = new LinkedHashMap<>();
        map.put(ModItems.HUNDRED_DOLLAR_BILL.get(), 100L);
        map.put(ModItems.FIFTY_DOLLAR_BILL.get(), 50L);
        map.put(ModItems.TWENTY_DOLLAR_BILL.get(), 20L);
        map.put(ModItems.TEN_DOLLAR_BILL.get(), 10L);
        map.put(ModItems.FIVE_DOLLAR_BILL.get(), 5L);
        map.put(ModItems.ONE_DOLLAR_BILL.get(), 1L);
        return map;
    }

    /** Total dollar value of all bills currently in the player's inventory. */
    public static long totalBillValue(Player player) {
        long total = 0;
        Inventory inv = player.getInventory();
        Map<Item, Long> values = billValues();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            Long v = values.get(stack.getItem());
            if (v != null) total += v * stack.getCount();
        }
        return total;
    }

    /** Give the player {@code amount} dollars as the fewest bills possible. */
    public static void giveBills(Player player, long amount) {
        for (Map.Entry<Item, Long> entry : billValues().entrySet()) {
            long denom = entry.getValue();
            int count = (int) (amount / denom);
            amount %= denom;
            while (count > 0) {
                int give = Math.min(count, 64);
                ItemStack bills = new ItemStack(entry.getKey(), give);
                if (!player.getInventory().add(bills)) {
                    player.drop(bills, false);
                }
                count -= give;
            }
        }
    }

    /**
     * Remove bills worth at least {@code amount} from the player's inventory (greedy, high to low)
     * and return the total value actually removed (which may overshoot {@code amount}).
     */
    public static long removeBills(Player player, long amount) {
        long removed = 0;
        Inventory inv = player.getInventory();
        Map<Item, Long> values = billValues();
        for (Map.Entry<Item, Long> entry : values.entrySet()) {
            if (removed >= amount) break;
            Item bill = entry.getKey();
            long denom = entry.getValue();
            for (int i = 0; i < inv.getContainerSize() && removed < amount; i++) {
                ItemStack stack = inv.getItem(i);
                if (stack.getItem() != bill) continue;
                while (!stack.isEmpty() && removed < amount) {
                    stack.shrink(1);
                    removed += denom;
                }
            }
        }
        return removed;
    }

    /** The bill item for an exact denomination value (e.g. 20 -> $20 bill), or null if none. */
    public static Item billForValue(long value) {
        for (Map.Entry<Item, Long> entry : billValues().entrySet()) {
            if (entry.getValue() == value) return entry.getKey();
        }
        return null;
    }

    /** Give the player {@code count} of a specific item, splitting into 64-stacks. */
    public static void giveStacks(Player player, Item item, long count) {
        while (count > 0) {
            int give = (int) Math.min(count, 64);
            ItemStack stack = new ItemStack(item, give);
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
            count -= give;
        }
    }

    /** Dollar value of a single stack of bills (0 if it isn't a bill). */
    public static long valueOf(ItemStack stack) {
        Long v = billValues().get(stack.getItem());
        return v == null ? 0 : v * stack.getCount();
    }
}
