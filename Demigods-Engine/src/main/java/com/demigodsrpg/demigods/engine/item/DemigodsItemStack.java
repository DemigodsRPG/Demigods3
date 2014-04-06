package com.demigodsrpg.demigods.engine.item;

import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.data.IdType;
import com.demigodsrpg.demigods.engine.data.Register;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class DemigodsItemStack extends DataAccess<UUID, DemigodsItemStack>
{
	private UUID id;
	private ItemStack item;

	public DemigodsItemStack()
	{
	}

	@Register(idType = IdType.UUID)
	public DemigodsItemStack(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		if(conf.getValues(true) != null) item = ItemStack.deserialize(conf.getValues(true));
	}

	@Override
	public Map<String, Object> serialize()
	{
		return item.serialize();
	}

	public void generateId()
	{
		id = UUID.randomUUID();
	}

	public UUID getId()
	{
		return id;
	}

	public void setItem(ItemStack item)
	{
		this.item = item;
	}

	/**
	 * Returns the DItemStack as an actual, usable ItemStack.
	 *
	 * @return ItemStack
	 */
	public ItemStack getBukkitItem()
	{
		return item;
	}

	private static final DataAccess<UUID, DemigodsItemStack> DATA_ACCESS = new DemigodsItemStack();

	public static DemigodsItemStack get(UUID id)
	{
		return DATA_ACCESS.getDirect(id);
	}

	public static DemigodsItemStack create(ItemStack item)
	{
		DemigodsItemStack trackedItem = new DemigodsItemStack();
		trackedItem.generateId();
		trackedItem.setItem(item);
		trackedItem.save();
		return trackedItem;
	}
}
