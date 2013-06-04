package com.censoredsoftware.Demigods.Engine;

import java.util.HashSet;
import java.util.Set;

import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Language.Blacklist;

public class DemigodsBlacklist
{
	public static class EnglishCharacterNames implements Blacklist
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
		public Set<String> getBlackList()
		{
			return new HashSet<String>()
			{
				{
					// Manual Blacklist
					add("Fuck");
					add("Shit");
					add("Ass");
					add("Dick");
					add("Penis");
					add("Vagina");
					add("Cunt");
					add("Bitch");
					add("Nigger");
					add("God");
					add("Titan");
					add("Phil");
					add("Staff");
					add("Server");
					add("Console");

					// Deities
					for(Deity deity : Demigods.getLoadedDeities())
					{
						add(deity.getInfo().getName());
					}
				}
			};
		}
	}
}
