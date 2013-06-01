package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.inventory.ItemStack;

public class ValueUtility
{
	/**
	 * Returns the value of the passed in <code>item</code>.
	 * 
	 * @param item the ItemStack to be checked.
	 * @return int the value of the ItemStack.
	 */
	public static int getTributeValue(ItemStack item)
	{
		int val = 0;
		if(item == null) return 0;
		switch(item.getType())
		{
			case STONE:
				val += item.getAmount() * 0.5;
				break;
			case COBBLESTONE:
				val += item.getAmount() * 0.3;
				break;
			case DIRT:
				val += item.getAmount() * 0.1;
				break;
			case LOG:
				val += item.getAmount();
				break;
			case WOOD:
				val += item.getAmount() * 0.23;
				break;
			case STICK:
				val += item.getAmount() * 0.11;
				break;
			case GLASS:
				val += item.getAmount() * 1.5;
				break;
			case LAPIS_BLOCK:
				val += item.getAmount() * 85;
				break;
			case SANDSTONE:
				val += item.getAmount() * 0.9;
				break;
			case GOLD_BLOCK:
				val += item.getAmount() * 170;
				break;
			case IRON_BLOCK:
				val += item.getAmount() * 120;
				break;
			case BRICK:
				val += item.getAmount() * 10;
				break;
			case TNT:
				val += item.getAmount() * 10;
				break;
			case MOSSY_COBBLESTONE:
				val += item.getAmount() * 10;
				break;
			case OBSIDIAN:
				val += item.getAmount() * 10;
				break;
			case DIAMOND_BLOCK:
				val += item.getAmount() * 300;
				break;
			case CACTUS:
				val += item.getAmount() * 1.7;
				break;
			case YELLOW_FLOWER:
				val += item.getAmount() * 0.1;
				break;
			case SEEDS:
				val += item.getAmount() * 0.3;
				break;
			case PUMPKIN:
				val += item.getAmount() * 0.7;
				break;
			case CAKE:
				val += item.getAmount() * 22;
				break;
			case APPLE:
				val += item.getAmount() * 5;
				break;
			case COAL:
				val += item.getAmount() * 2.5;
				break;
			case DIAMOND:
				val += item.getAmount() * 30;
				break;
			case IRON_ORE:
				val += item.getAmount() * 7;
				break;
			case GOLD_ORE:
				val += item.getAmount() * 13;
				break;
			case IRON_INGOT:
				val += item.getAmount() * 12;
				break;
			case GOLD_INGOT:
				val += item.getAmount() * 18;
				break;
			case STRING:
				val += item.getAmount() * 2.4;
				break;
			case WHEAT:
				val += item.getAmount() * 0.6;
				break;
			case BREAD:
				val += item.getAmount() * 2;
				break;
			case RAW_FISH:
				val += item.getAmount() * 2.4;
				break;
			case PORK:
				val += item.getAmount() * 2.4;
				break;
			case COOKED_FISH:
				val += item.getAmount() * 3.4;
				break;
			case GRILLED_PORK:
				val += item.getAmount() * 3.4;
				break;
			case GOLDEN_APPLE:
				val += item.getAmount() * 190;
				break;
			case GOLD_RECORD:
				val += item.getAmount() * 60;
				break;
			case GREEN_RECORD:
				val += item.getAmount() * 60;
				break;
			case GLOWSTONE:
				val += item.getAmount() * 1.7;
				break;
			case REDSTONE:
				val += item.getAmount() * 3.3;
				break;
			case EGG:
				val += item.getAmount() * 0.3;
				break;
			case SUGAR:
				val += item.getAmount() * 1.2;
				break;
			case BONE:
				val += item.getAmount() * 3;
				break;
			case ENDER_PEARL:
				val += item.getAmount() * 1.7;
				break;
			case SULPHUR:
				val += item.getAmount() * 1.2;
				break;
			case COCOA:
				val += item.getAmount() * 0.6;
				break;
			case ROTTEN_FLESH:
				val += item.getAmount() * 3;
				break;
			case RAW_CHICKEN:
				val += item.getAmount() * 2;
				break;
			case COOKED_CHICKEN:
				val += item.getAmount() * 2.6;
				break;
			case RAW_BEEF:
				val += item.getAmount() * 2;
				break;
			case COOKED_BEEF:
				val += item.getAmount() * 2.7;
				break;
			case MELON:
				val += item.getAmount() * 0.8;
				break;
			case COOKIE:
				val += item.getAmount() * 0.45;
				break;
			case VINE:
				val += item.getAmount() * 1.2;
				break;
			case EMERALD:
				val += item.getAmount() * 7;
				break;
			case EMERALD_BLOCK:
				val += item.getAmount() * 70;
				break;
			case DRAGON_EGG:
				val += item.getAmount() * 10000;
				break;
			default:
				val += item.getAmount() * 0.1;
				break;
		}
		return val;
	}
}
