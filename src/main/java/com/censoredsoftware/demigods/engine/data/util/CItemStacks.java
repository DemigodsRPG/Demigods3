package com.censoredsoftware.demigods.engine.data.util;

import com.censoredsoftware.censoredlib.data.inventory.CItemStack;
import com.censoredsoftware.demigods.engine.data.DataManager;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CItemStacks {
    public static void save(CItemStack itemStack) {
        DataManager.itemStacks.put(itemStack.getId(), itemStack);
    }

    public static void delete(UUID id) {
        DataManager.itemStacks.remove(id);
    }

    public static CItemStack load(UUID id) {
        return DataManager.itemStacks.get(id);
    }

    public static CItemStack create(ItemStack item) {
        CItemStack trackedItem = new CItemStack();
        trackedItem.generateId();
        trackedItem.setItem(item);
        save(trackedItem);
        return trackedItem;
    }
}
