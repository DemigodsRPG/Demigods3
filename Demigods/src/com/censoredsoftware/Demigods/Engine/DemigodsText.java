package com.censoredsoftware.Demigods.Engine;

import org.bukkit.ChatColor;

import com.censoredsoftware.Demigods.Engine.Language.Translation;

public class DemigodsText
{
	public static enum Text
	{
		PROTECTED_BLOCK, ADMIN_WAND_GENERATE_ALTAR, ADMIN_WAND_GENERATE_ALTAR_COMPLETE, ADMIN_WAND_REMOVE_ALTAR, ADMIN_WAND_REMOVE_ALTAR_COMPLETE, CREATE_SHRINE_1, CREATE_SHRINE_2, ADMIN_WAND_REMOVE_SHRINE, ADMIN_WAND_REMOVE_SHRINE_COMPLETE, NO_WARP_ALTAR, CHARACTER_CREATE_COMPLETE, KILLSTREAK, MORTAL_SLAIN_1, MORTAL_SLAIN_2, DEMI_SLAIN_1, DEMI_SLAIN_2;
	}

	public static class English implements Translation
	{
		@Override
		public int version()
		{
			return 1;
		}

		@Override
		public String translator()
		{
			return "Censored Software";
		}

		@Override
		public String getText(Enum text)
		{
			if(!(text instanceof Text)) throw new NullPointerException("No such translation.");
			Text get = (Text) text;
			switch(get)
			{
				case PROTECTED_BLOCK:
					return "That block is protected by a Deity!";
				case ADMIN_WAND_GENERATE_ALTAR:
					return "Generating new Altar...";
				case ADMIN_WAND_GENERATE_ALTAR_COMPLETE:
					return "Altar created!";
				case ADMIN_WAND_REMOVE_ALTAR:
					return "Right-click this Altar again to remove it.";
				case ADMIN_WAND_REMOVE_ALTAR_COMPLETE:
					return "Altar removed!";
				case CREATE_SHRINE_1:
					return "The {alliance} are pleased...";
				case CREATE_SHRINE_2:
					return "You have created a Shrine in the name of {deity}!";
				case ADMIN_WAND_REMOVE_SHRINE:
					return "Right-click this Shrine again to remove it.";
				case ADMIN_WAND_REMOVE_SHRINE_COMPLETE:
					return "Shrine removed!";
				case NO_WARP_ALTAR:
					return "You've never set a warp at this Altar.";
				case CHARACTER_CREATE_COMPLETE:
					return "You have been accepted into the lineage of {deity}!";
				case KILLSTREAK:
					return "{character} is on a killstreak of {kills} kills.";
				case MORTAL_SLAIN_1:
					return ChatColor.YELLOW + "A mortal" + ChatColor.GRAY + " was slain by " + ChatColor.YELLOW + "another mortal" + ChatColor.GRAY + ".";
				case MORTAL_SLAIN_2:
					return ChatColor.YELLOW + "A mortal" + ChatColor.GRAY + " was slain by {attacker} of the {attackerAlliance} alliance.";
				case DEMI_SLAIN_1:
					return "{killed} of the {killedAlliance} was slain by " + ChatColor.YELLOW + "a mortal" + ChatColor.GRAY + ".";
				case DEMI_SLAIN_2:
					return "{killed} of the {killedAlliance} was slain by {attacker} of the {attackerAlliance} alliance.";
			}
			throw new NullPointerException("No such translation.");
		}
	}
}
