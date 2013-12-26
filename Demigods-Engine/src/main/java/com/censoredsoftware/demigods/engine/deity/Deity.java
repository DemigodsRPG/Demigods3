package com.censoredsoftware.demigods.engine.deity;

import com.censoredsoftware.censoredlib.helper.ConfigFile2;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.ability.Ability;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Deity
{
	@Override
	public String toString();

	public String getName();

	public Alliance getAlliance();

	public String getPermission();

	public ChatColor getColor();

	public MaterialData getMaterialData();

	public Sound getSound();

	public Map<Material, Integer> getClaimItems();

	public Map<Material, Integer> getForsakeItems();

	public String getShortDescription();

	public List<String> getLore();

	public Set<Flag> getFlags();

	public List<Ability> getAbilities();

	public int getAccuracy();

	public int getFavorRegen();

	public int getMaxFavor();

	public double getMaxHealth();

	public int getFavorBank();

	public void updateMood();

	public ConfigFile2 getConfig();

	public enum Flag
	{
		PLAYABLE, NON_PLAYABLE, MAJOR_DEITY, MINOR_DEITY, NEUTRAL, DIFFICULT, ALTERNATE_ASCENSION_LEVELING, NO_SHRINE, NO_OBELISK, NO_BATTLE
	}

	public enum Mood
	{
		ECSTATIC, PLEASED, INTERESTED, CALM /* (the default) */, SAD, DEFEATED, ANGRY, ENRAGED, CONFUSED
	}

	public interface MoodPack
	{
		Alliance getAlliance();

		public MaterialData getMaterialData();

		Sound getSound();

		public Map<Material, Integer> getClaimItems();

		public Map<Material, Integer> getForsakeItems();

		public Set<Flag> getFlags();

		public List<Ability> getAbilities();

		public int getAccuracy();

		public int getFavorRegen();
	}

	public interface MoodManager
	{
		public void set(Mood mood, MoodPack moodPack);

		public MoodPack get(Mood mood);
	}

	public static class Util
	{
		public static Deity getDeity(final String deityName)
		{
			try
			{
				return Iterables.find(Demigods.mythos().getDeities(), new Predicate<Deity>()
				{
					@Override
					public boolean apply(Deity deity)
					{
						return deity.getName().equalsIgnoreCase(deityName);
					}
				});
			}
			catch(Exception ignored)
			{}
			return null;
		}

		public static boolean canUseDeity(DCharacter character, String deity)
		{
			if(character == null) return false;
			if(!character.getOfflinePlayer().isOnline()) return canUseDeitySilent(character, deity);
			Player player = character.getOfflinePlayer().getPlayer();
			if(!character.isDeity(deity))
			{
				player.sendMessage(ChatColor.RED + "You haven't claimed " + deity + "! You can't do that!");
				return false;
			}
			return true;
		}

		public static boolean canUseDeitySilent(DCharacter character, String deity)
		{
			return character != null && character.isDeity(deity);
		}

		public static boolean canUseDeitySilent(Player player, String deityName)
		{
			String currentDeityName = DPlayer.Util.getPlayer(player).getCurrentDeityName();
			return currentDeityName != null && currentDeityName.equalsIgnoreCase(deityName);
		}
	}
}
