package com.censoredsoftware.Demigods.Engine.Object.Battle;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Object.Mob.TameableWrapper;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Utility.MiscUtility;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Model
public class BattleMeta
{
	@Id
	private Long Id;
	@CollectionSet(of = PlayerCharacter.class)
	private Set<PlayerCharacter> involvedPlayers;
	@CollectionSet(of = TameableWrapper.class)
	private Set<TameableWrapper> involvedTameable;
	@Attribute
	private int killCounter;
	@CollectionMap(key = PlayerCharacter.class, value = Integer.class)
	private Map<PlayerCharacter, Integer> kills;
	@CollectionMap(key = PlayerCharacter.class, value = Integer.class)
	private Map<PlayerCharacter, Integer> deaths;
	@Reference
	@Indexed
	private PlayerCharacter startedBy;

	public static BattleMeta create(BattleParticipant participant)
	{
		PlayerCharacter character = participant.getRelatedCharacter();

		BattleMeta meta = new BattleMeta();
		meta.initialize();
		meta.setStarter(character);
		BattleMeta.save(meta);
		return meta;
	}

	void setStarter(PlayerCharacter character)
	{
		this.startedBy = character;
		addParticipant(character);
	}

	void initialize()
	{
		this.kills = Maps.newHashMap();
		this.deaths = Maps.newHashMap();
		this.involvedPlayers = Sets.newHashSet();
		this.involvedTameable = Sets.newHashSet();
		this.killCounter = 0;
	}

	public void addParticipant(BattleParticipant participant)
	{
		if(participant instanceof PlayerCharacter) this.involvedPlayers.add((PlayerCharacter) participant);
		else this.involvedTameable.add((TameableWrapper) participant);
		save(this);
	}

	public void addKill(BattleParticipant participant)
	{
		this.killCounter += 1;
		PlayerCharacter character = participant.getRelatedCharacter();
		if(this.kills.containsKey(character)) this.kills.put(character, this.kills.get(character) + 1);
		else this.kills.put(character, 1);
		save(this);
	}

	public void addDeath(BattleParticipant participant)
	{
		PlayerCharacter character = participant.getRelatedCharacter();
		if(this.deaths.containsKey(character)) this.deaths.put(character, this.deaths.get(character) + 1);
		else this.deaths.put(character, 1);
		save(this);
	}

	public PlayerCharacter getStarter()
	{
		return this.startedBy;
	}

	public Set<BattleParticipant> getParticipants()
	{
		return new HashSet<BattleParticipant>()
		{
			{
				for(PlayerCharacter character : involvedPlayers)
					add(character);
				for(TameableWrapper tamable : involvedTameable)
					add(tamable);
			}
		};
	}

	public void printBattleOutcome()
	{
		String winningAlliance = "";
		String mostDeathsAlliance = "";

		Map<String, Integer> allianceKills = Maps.newHashMap();
		Map<String, Integer> allianceDeaths = Maps.newHashMap();

		for(String alliance : Deity.getLoadedDeityAlliances())
		{
			allianceKills.put(alliance, 0);
			allianceDeaths.put(alliance, 0);
		}

		// Get Kill Data
		for(Map.Entry<PlayerCharacter, Integer> entry : getDeaths())
		{
			PlayerCharacter murderer = entry.getKey();
			allianceKills.put(murderer.getAlliance(), allianceKills.get(murderer.getAlliance()) + entry.getValue());
		}

		for(Map.Entry<String, Integer> entry : Lists.reverse(Lists.newArrayList(MiscUtility.sortByValue(allianceKills).entrySet())))
		{
			winningAlliance = "The " + entry.getKey() + "  alliance wins with " + entry.getValue() + " total kills!";
			break;
		}

		// Get Death Data
		for(Map.Entry<PlayerCharacter, Integer> entry : getDeaths())
		{
			PlayerCharacter victim = entry.getKey();
			allianceDeaths.put(victim.getAlliance(), allianceKills.get(victim.getAlliance()) + entry.getValue());
		}

		for(Map.Entry<String, Integer> entry : Lists.reverse(Lists.newArrayList(MiscUtility.sortByValue(allianceDeaths).entrySet())))
		{
			mostDeathsAlliance = "The " + entry.getKey() + " alliance had the most deaths (" + entry.getValue() + ") this battle.";
			break;
		}

		// Print the data
		Demigods.message.broadcast(ChatColor.YELLOW + "A battle has ended: STATS -------------");
		Demigods.message.broadcast(ChatColor.YELLOW + winningAlliance);
		Demigods.message.broadcast(ChatColor.YELLOW + mostDeathsAlliance);
		Demigods.message.broadcast(ChatColor.YELLOW + getKills().get(1).getKey().getName() + " had the most kills: " + getKills().get(1).getValue());
		Demigods.message.broadcast(ChatColor.YELLOW + getDeaths().get(1).getKey().getName() + " had the most deaths: " + getDeaths().get(1).getValue());
	}

	public List<Map.Entry<PlayerCharacter, Integer>> getKills()
	{
		return Lists.reverse(Lists.newArrayList(MiscUtility.sortByValue(this.kills).entrySet()));
	}

	public List<Map.Entry<PlayerCharacter, Integer>> getDeaths()
	{
		return Lists.reverse(Lists.newArrayList(MiscUtility.sortByValue(this.deaths).entrySet()));
	}

	public int getKillCounter()
	{
		return this.killCounter;
	}

	public long getId()
	{
		return this.Id;
	}

	public static BattleMeta load(Long id)
	{
		return JOhm.get(BattleMeta.class, id);
	}

	public static Set<BattleMeta> loadAll()
	{
		return JOhm.getAll(BattleMeta.class);
	}

	public static void save(BattleMeta meta)
	{
		JOhm.save(meta);
	}

	public void delete()
	{
		JOhm.delete(BattleMeta.class, getId());
	}
}
