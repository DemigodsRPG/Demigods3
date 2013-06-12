package com.censoredsoftware.Demigods.Engine.Object;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class DemigodsModelFactory
{
	public static DemigodsLocation createDemigodsLocation(String world, double X, double Y, double Z, float yaw, float pitch)
	{
		DemigodsLocation trackedLocation = new DemigodsLocation();
		trackedLocation.setWorld(world);
		trackedLocation.setX(X);
		trackedLocation.setY(Y);
		trackedLocation.setZ(Z);
		trackedLocation.setYaw(yaw);
		trackedLocation.setPitch(pitch);
		DemigodsLocation.save(trackedLocation);
		return trackedLocation;
	}

	public static DemigodsLocation createDemigodsLocation(Location location)
	{
		return createDemigodsLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	public static DemigodsPlayer createDemigodsPlayer(OfflinePlayer player)
	{
		DemigodsPlayer trackedPlayer = new DemigodsPlayer();
		trackedPlayer.setPlayer(player.getName());
		trackedPlayer.setLastLoginTime(player.getLastPlayed());
		DemigodsPlayer.save(trackedPlayer);
		return trackedPlayer;
	}

	public static DemigodsItemStack createDemigodsItemStack(ItemStack item)
	{
		DemigodsItemStack trackedItem = new DemigodsItemStack();
		trackedItem.setTypeId(item.getTypeId());
		trackedItem.setByteId(item.getData().getData());
		trackedItem.setAmount(item.getAmount());
		trackedItem.setDurability(item.getDurability());
		if(item.hasItemMeta())
		{
			if(item.getItemMeta().hasDisplayName()) trackedItem.setName(item.getItemMeta().getDisplayName());
			if(item.getItemMeta().hasLore()) trackedItem.setLore(item.getItemMeta().getLore());
		}
		trackedItem.setEnchantments(item);
		trackedItem.setBookMeta(item);
		DemigodsItemStack.save(trackedItem);
		return trackedItem;
	}

	public static DemigodsBlock createDemigodsBlock(Location location, String type, Material material, byte matByte)
	{
		DemigodsLocation trackedLocation = DemigodsModelFactory.createDemigodsLocation(location);

		DemigodsBlock trackedBlock = new DemigodsBlock();
		trackedBlock.setLocation(trackedLocation);
		trackedBlock.setPreviousMaterial(Material.getMaterial(location.getBlock().getTypeId()));
		trackedBlock.setPreviousMaterialByte(location.getBlock().getData());
		trackedBlock.setType(type);
		trackedBlock.setMaterial(material);
		trackedBlock.setMaterialByte(matByte);
		location.getBlock().setType(material);
		location.getBlock().setData(matByte);
		DemigodsBlock.save(trackedBlock);
		return trackedBlock;
	}

	public static DemigodsBlock createDemigodsBlock(Location location, String type, Material material)
	{
		return createDemigodsBlock(location, type, material, (byte) 0);
	}
}
