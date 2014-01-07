package com.censoredsoftware.demigods.engine.data.wrap;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.censoredlib.data.inventory.CItemStack;
import com.censoredsoftware.demigods.engine.data.Data;

public class CItemStackManager
{
	public static void save(CItemStack itemStack)
	{
		Data.ITEM_STACK.put(itemStack.getId(), itemStack);
	}

	public static void delete(UUID id)
	{
		Data.ITEM_STACK.remove(id);
	}

	public static CItemStack load(UUID id)
	{
		return Data.ITEM_STACK.get(id);
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
