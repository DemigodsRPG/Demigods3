package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.censoredlib.data.inventory.CItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CItemStackManager
{
	public static void save(CItemStack itemStack)
	{
		DataManager.itemStacks.put(itemStack.getId(), itemStack);
	}

	public static void delete(UUID id)
	{
		DataManager.itemStacks.remove(id);
	}

	public static CItemStack load(UUID id)
	{
		return DataManager.itemStacks.get(id);
	}

	public static CItemStack create(ItemStack item)
	{
		CItemStack trackedItem = new CItemStack();
		trackedItem.generateId();
		trackedItem.setItem(item);
		save(trackedItem);
		return trackedItem;
	}
}
