package com.censoredsoftware.demigods.engine.language;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.element.Deity;

import java.util.HashSet;
import java.util.Set;

// TODO Replace with YAML.

public class BlacklistManager
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
					add("Phil");
					add("Staff");
					add("Server");
					add("Console");
					add("Disowned");

					// Deities
					for(Deity deity : Demigods.getLoadedDeities())
					{
						add(deity.getInfo().getName());
						add(deity.getInfo().getAlliance());
					}
				}
			};
		}
	}
}
