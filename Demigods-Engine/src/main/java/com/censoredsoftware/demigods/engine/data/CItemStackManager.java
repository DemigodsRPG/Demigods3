package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.censoredlib.data.inventory.CItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CItemStackManager
{
	public static void save(CItemStack itemStack)
	{
		Data.itemStacks.put(itemStack.getId(), itemStack);
	}

	public static void delete(UUID id)
	{
		Data.itemStacks.remove(id);
	}

	public static CItemStack load(UUID id)
	{
		return Data.itemStacks.get(id);
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
