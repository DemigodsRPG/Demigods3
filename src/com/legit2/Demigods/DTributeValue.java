package com.legit2.Demigods;

import org.bukkit.inventory.ItemStack;

public class DTributeValue
{
	/*
	 *  getTributeValue : Returns an int of the value of the (ItemStack)item.
	 */
	public static int getTributeValue(ItemStack itemStack)
	{
		int val = 0;
		if(itemStack == null) return 0;
		switch(itemStack.getType())
		{
			case STONE: val+=itemStack.getAmount()*0.5; break;
			case COBBLESTONE: val+=itemStack.getAmount()*0.3; break;
			case DIRT: val+=itemStack.getAmount()*0.1; break;
			case LOG: val+=itemStack.getAmount()*1; break;
			case WOOD: val+=itemStack.getAmount()*0.23; break;
			case STICK: val+=itemStack.getAmount()*0.11; break;
			case GLASS: val+=itemStack.getAmount()*1.5; break;
			case LAPIS_BLOCK: val+=itemStack.getAmount()*85; break;
			case SANDSTONE: val+=itemStack.getAmount()*0.9; break;
			case GOLD_BLOCK: val+=itemStack.getAmount()*170; break;
			case IRON_BLOCK: val+=itemStack.getAmount()*120; break;
			case BRICK: val+=itemStack.getAmount()*10; break;
			case TNT: val+=itemStack.getAmount()*10; break;
			case MOSSY_COBBLESTONE: val+=itemStack.getAmount()*10; break;
			case OBSIDIAN: val+=itemStack.getAmount()*10; break;
			case DIAMOND_BLOCK: val+=itemStack.getAmount()*300; break;
			case CACTUS: val+=itemStack.getAmount()*1.7; break;
			case YELLOW_FLOWER: val+=itemStack.getAmount()*0.1; break;
			case SEEDS: val+=itemStack.getAmount()*0.3; break;
			case PUMPKIN: val+=itemStack.getAmount()*0.7; break;
			case CAKE: val+=itemStack.getAmount()*22; break;
			case APPLE: val+=itemStack.getAmount()*5; break;
			case COAL: val+=itemStack.getAmount()*2.5; break;
			case DIAMOND: val+=itemStack.getAmount()*30; break;
			case IRON_ORE: val+=itemStack.getAmount()*7; break;
			case GOLD_ORE: val+=itemStack.getAmount()*13; break;
			case IRON_INGOT: val+=itemStack.getAmount()*12; break;
			case GOLD_INGOT: val+=itemStack.getAmount()*18; break;
			case STRING: val+=itemStack.getAmount()*2.4; break;
			case WHEAT: val+=itemStack.getAmount()*0.6; break;
			case BREAD: val+=itemStack.getAmount()*2; break;
			case RAW_FISH: val+=itemStack.getAmount()*2.4; break;
			case PORK: val+=itemStack.getAmount()*2.4; break;
			case COOKED_FISH: val+=itemStack.getAmount()*3.4; break;
			case GRILLED_PORK: val+=itemStack.getAmount()*3.4; break;
			case GOLDEN_APPLE: val+=itemStack.getAmount()*190; break;
			case GOLD_RECORD: val+=itemStack.getAmount()*60; break;
			case GREEN_RECORD: val+=itemStack.getAmount()*60; break;
			case GLOWSTONE: val+=itemStack.getAmount()*1.7; break;
			case REDSTONE: val+=itemStack.getAmount()*3.3; break;
			case EGG: val+=itemStack.getAmount()*0.3; break;
			case SUGAR: val+=itemStack.getAmount()*1.2; break;
			case BONE: val+=itemStack.getAmount()*3; break;
			case ENDER_PEARL: val+=itemStack.getAmount()*1.7; break;
			case SULPHUR: val+=itemStack.getAmount()*1.2; break;
			case COCOA: val+=itemStack.getAmount()*0.6; break;
			case ROTTEN_FLESH: val+=itemStack.getAmount()*3; break;
			case RAW_CHICKEN: val+=itemStack.getAmount()*2; break;
			case COOKED_CHICKEN: val+=itemStack.getAmount()*2.6; break;
			case RAW_BEEF: val+=itemStack.getAmount()*2; break;
			case COOKED_BEEF: val+=itemStack.getAmount()*2.7; break;
			case MELON: val+=itemStack.getAmount()*0.8; break;
			case COOKIE: val+=itemStack.getAmount()*0.45; break;
			case VINE: val+=itemStack.getAmount()*1.2; break;
			case EMERALD: val+=itemStack.getAmount()*7; break;
			case EMERALD_BLOCK: val+=itemStack.getAmount()*70; break;
			case DRAGON_EGG: val+=itemStack.getAmount()*10000; break;
			
			default: val += itemStack.getAmount() * 0.1; break;
		}
		return val;
	}
}
