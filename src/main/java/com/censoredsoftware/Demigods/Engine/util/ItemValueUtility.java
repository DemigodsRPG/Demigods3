package com.censoredsoftware.demigods.engine.util;

import org.bukkit.inventory.ItemStack;

public class ItemValueUtility
{
	/**
	 * Returns the value of the passed in <code>item</code>.
	 * 
	 * @param item the ItemStack to be checked.
	 * @return int the value of the ItemStack.
	 */
	public static int getTributeValue(ItemStack item)
	{
		// Return if null
		if(item == null) return 0;

		// Define variables
		double multiplier = 0;

		// Set the multiplier
		switch(item.getType())
		{
			case ENDER_PORTAL_FRAME:
				multiplier = 23.0;
				break;
			case CAULDRON_ITEM:
				multiplier = 84.0;
				break;
			case LAVA_BUCKET:
				multiplier = 36.5;
				break;
			case MILK_BUCKET:
				multiplier = 36.5;
				break;
			case WATER_BUCKET:
				multiplier = 36.5;
				break;
			case NETHER_WARTS:
				multiplier = 13.2;
				break;
			case NETHER_STAR:
				multiplier = 820.0;
				break;
			case BEACON:
				multiplier = 885.3;
				break;
			case SADDLE:
				multiplier = 5.3;
				break;
			case EYE_OF_ENDER:
				multiplier = 18.0;
				break;
			case STONE:
				multiplier = 0.5;
				break;
			case COBBLESTONE:
				multiplier = 0.3;
				break;
			case LOG:
				multiplier = item.getAmount();
				break;
			case WOOD:
				multiplier = 0.23;
				break;
			case STICK:
				multiplier = 0.11;
				break;
			case GLASS:
				multiplier = 1.5;
				break;
			case LAPIS_BLOCK:
				multiplier = 85.0;
				break;
			case SANDSTONE:
				multiplier = 0.9;
				break;
			case GOLD_BLOCK:
				multiplier = 170;
				break;
			case IRON_BLOCK:
				multiplier = 120;
				break;
			case BRICK:
				multiplier = 10;
				break;
			case TNT:
				multiplier = 10;
				break;
			case MOSSY_COBBLESTONE:
				multiplier = 10;
				break;
			case OBSIDIAN:
				multiplier = 10;
				break;
			case DIAMOND_BLOCK:
				multiplier = 300;
				break;
			case CACTUS:
				multiplier = 1.7;
				break;
			case YELLOW_FLOWER:
				multiplier = 0.1;
				break;
			case SEEDS:
				multiplier = 0.3;
				break;
			case PUMPKIN:
				multiplier = 0.7;
				break;
			case CAKE:
				multiplier = 22;
				break;
			case APPLE:
				multiplier = 5;
				break;
			case CARROT:
				multiplier = 1.7;
				break;
			case POTATO:
				multiplier = 1.7;
				break;
			case COAL:
				multiplier = 2.5;
				break;
			case DIAMOND:
				multiplier = 30;
				break;
			case IRON_ORE:
				multiplier = 7;
				break;
			case GOLD_ORE:
				multiplier = 13;
				break;
			case IRON_INGOT:
				multiplier = 12;
				break;
			case GOLD_INGOT:
				multiplier = 18;
				break;
			case STRING:
				multiplier = 2.4;
				break;
			case WHEAT:
				multiplier = 0.6;
				break;
			case BREAD:
				multiplier = 2;
				break;
			case RAW_FISH:
				multiplier = 2.4;
				break;
			case PORK:
				multiplier = 2.4;
				break;
			case COOKED_FISH:
				multiplier = 3.4;
				break;
			case GRILLED_PORK:
				multiplier = 3.4;
				break;
			case GOLDEN_APPLE:
				multiplier = 190;
				break;
			case GOLD_RECORD:
				multiplier = 60;
				break;
			case GREEN_RECORD:
				multiplier = 60;
				break;
			case GLOWSTONE:
				multiplier = 1.7;
				break;
			case REDSTONE:
				multiplier = 3.3;
				break;
			case REDSTONE_BLOCK:
				multiplier = 27.9;
				break;
			case EGG:
				multiplier = 0.3;
				break;
			case SUGAR:
				multiplier = 1.2;
				break;
			case BONE:
				multiplier = 3;
				break;
			case ENDER_PEARL:
				multiplier = 1.7;
				break;
			case SULPHUR:
				multiplier = 1.2;
				break;
			case COCOA:
				multiplier = 0.6;
				break;
			case ROTTEN_FLESH:
				multiplier = 3;
				break;
			case RAW_CHICKEN:
				multiplier = 2;
				break;
			case COOKED_CHICKEN:
				multiplier = 2.6;
				break;
			case RAW_BEEF:
				multiplier = 2;
				break;
			case COOKED_BEEF:
				multiplier = 2.7;
				break;
			case MELON:
				multiplier = 0.8;
				break;
			case COOKIE:
				multiplier = 0.45;
				break;
			case VINE:
				multiplier = 1.2;
				break;
			case EMERALD:
				multiplier = 7;
				break;
			case EMERALD_BLOCK:
				multiplier = 70;
				break;
			case DRAGON_EGG:
				multiplier = 10000;
				break;
			default:
				multiplier = 0.1;
				break;
		}

		// Multiply and return
		return (int) (Math.ceil(item.getAmount() * multiplier));
	}
}
