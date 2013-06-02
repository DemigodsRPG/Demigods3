package com.censoredsoftware.Demigods.Engine;

import com.censoredsoftware.Demigods.Engine.Language.Translation;

public class DemigodsText
{
	public static enum Text
	{
		PROTECTED_BLOCK, ADMIN_WAND_GENERATE_ALTAR, ADMIN_WAND_GENERATE_ALTAR_COMPLETE, ADMIN_WAND_REMOVE_ALTAR, ADMIN_WAND_REMOVE_ALTAR_COMPLETE, CREATE_SHRINE_1, CREATE_SHRINE_2, ADMIN_WAND_REMOVE_SHRINE, ADMIN_WAND_REMOVE_SHRINE_COMPLETE, NO_WARP_ALTAR;
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
			}
			throw new NullPointerException("No such translation.");
		}
	}
}
