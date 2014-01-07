package com.censoredsoftware.demigods.classic;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.WildAmazing.marinating.Demigods.Deities.Deity;
import com.censoredsoftware.censoredlib.util.Randoms;
import com.censoredsoftware.demigods.engine.data.serializable.DCharacter;
import com.censoredsoftware.demigods.engine.data.serializable.DPlayer;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ClassicLoader
{
	private static final Logger log = Logger.getLogger("Minecraft");
	private static final String PATH = "plugins/Demigods/";
	private static HashMap<String, HashMap<String, Object>> SAVEDDATA = Maps.newHashMap();

	static
	{
		File f1 = new File(PATH + "Players/");
		if(f1.exists())
		{
			File[] list = f1.listFiles();
			if(list.length != 0) for(File element : list)
			{
				String load = element.getName();
				if(load.endsWith(".dem"))
				{
					load = load.substring(0, load.length() - 4);
					try
					{
						ObjectInputStream ois = new ObjectInputStream(new FileInputStream(element));
						Object result = ois.readObject();
						@SuppressWarnings("unchecked")
						HashMap<String, Object> cast = (HashMap<String, Object>) result;
						SAVEDDATA.put(load, cast);
						ois.close();
					}
					catch(Exception error)
					{
						log.severe("[Demigods] Could not load player " + load);
						error.printStackTrace();
						log.severe("[Demigods] End stack trace for " + load);
					}
				}
			}
		}
	}

	public static boolean hasPlayer(String p)
	{
		return SAVEDDATA.containsKey(p);
	}

	public static boolean hasData(String p, String id)
	{
		return hasPlayer(p) && SAVEDDATA.get(p).containsKey(id);
	}

	public static Object getData(String p, String id)
	{
		if(hasData(p, id)) return SAVEDDATA.get(p).get(id);
		return null;
	}

	public static HashMap<String, HashMap<String, Object>> getCompleteData()
	{
		return SAVEDDATA;
	}

	public static ArrayList<Deity> getDeities(String p) throws NullPointerException
	{
		try
		{
			return (ArrayList<Deity>) getData(p, "DEITIES");
		}
		catch(Throwable ignored)
		{}
		return null;
	}

	public static String getAllegiance(String p)
	{
		if(hasData(p, "ALLEGIANCE")) return((String) getData(p, "ALLEGIANCE"));
		return "Mortal";
	}

	public static int getDevotion(String p, String deityname)
	{
		if(hasData(p, deityname + "_dvt")) return (Integer) getData(p, deityname + "_dvt");
		return -1;
	}

	public static long getDevotion(String p)
	{
		try
		{
			int total = 0;
			for(Deity d : getDeities(p))
				total += getDevotion(p, d.getName());
			return total;
		}
		catch(Throwable ignored)
		{}
		return -1;
	}

	public static void convertLegacyData()
	{
		for(String legacyPlayer : ClassicLoader.getCompleteData().keySet())
			convertPlayer(legacyPlayer);
	}

	public static void convertPlayer(String player)
	{
		String alliance = getAllegiance(player);
		if(getDeities(player) == null || alliance.equalsIgnoreCase("giant")) return;

		List<Deity> pickOne = Lists.newArrayList();
		Set<String> deities = Sets.newHashSet();

		for(Deity legacy : getDeities(player))
		{
			if(legacy.getName().equals("Typhon")) continue;
			deities.add(legacy.getName());
			if(legacy.getDefaultAlliance().equalsIgnoreCase(alliance))
			{
				if(alliance.equalsIgnoreCase("titan") && (legacy.getName().equals("Cronus") || legacy.getName().equals("Prometheus") || legacy.getName().equals("Rhea"))) pickOne.add(legacy);
				else if(alliance.equalsIgnoreCase("god") && (legacy.getName().equals("Zeus") || legacy.getName().equals("Poseidon") || legacy.getName().equals("Hades"))) pickOne.add(legacy);
			}
		}

		try
		{
			String mainDeity = pickOne.get(pickOne.size() < 2 ? 0 : Randoms.generateIntRange(0, pickOne.size() - 1)).getName();
			deities.remove(mainDeity);

			DPlayer dPlayer = DPlayer.Util.getPlayer(player);
			try
			{
				DCharacter character = DCharacter.Util.create(dPlayer, mainDeity, player);
				character.setMinorDeities(deities);
				character.getMeta().setSkillPoints((int) getDevotion(player));
			}
			catch(Throwable ignored)
			{}
			Messages.warning("SUCCESS!");
		}
		catch(Throwable fixMe)
		{
			Messages.warning("ERR: " + deities.size());
		}
	}
}
