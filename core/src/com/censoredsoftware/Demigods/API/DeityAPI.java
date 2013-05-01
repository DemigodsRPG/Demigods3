package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;
import java.util.List;

import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Demigods;

public class DeityAPI
{
	public static List<String> getLoadedDeityAlliances()
	{
		return new ArrayList<String>()
		{
			{
				for(Deity deity : Demigods.getLoadedDeities())
				{
					if(!contains(deity.getInfo().getAlliance())) add(deity.getInfo().getAlliance());
				}
			};
		};
	}

	public static List<Deity> getAllDeitiesInAlliance(final String alliance)
	{
		return new ArrayList<Deity>()
		{
			{
				for(Deity deity : Demigods.getLoadedDeities())
				{
					if(deity.getInfo().getAlliance().equalsIgnoreCase(alliance)) add(deity);
				}
			};
		};
	}

	public static Deity getDeity(String deity)
	{
		for(Deity loaded : Demigods.getLoadedDeities())
		{
			if(loaded.getInfo().getName().equalsIgnoreCase(deity)) return loaded;
		}
		return null;
	}
}
