package com.censoredsoftware.demigods.engine.mythos;

import com.censoredsoftware.censoredlib.helper.ConfigFile2;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.google.common.collect.ImmutableSet;
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
	String toString();

	String getName();

	Alliance getAlliance();

	String getPermission();

	ChatColor getColor();

	MaterialData getMaterialData();

	Sound getSound();

	Map<Material, Integer> getClaimItems();

	Map<Material, Integer> getForsakeItems();

	String getShortDescription();

	List<String> getLore();

	Set<Flag> getFlags();

	List<Ability> getAbilities();

	int getAccuracy();

	int getFavorRegen();

	int getMaxFavor();

	double getMaxHealth();

	int getFavorBank();

	void updateMood();

	ConfigFile2 getConfig();

	public enum Flag
	{
		PLAYABLE, NON_PLAYABLE, MAJOR_DEITY, MINOR_DEITY, NEUTRAL, DIFFICULT, ALTERNATE_ASCENSION_LEVELING, NO_SHRINE, NO_OBELISK, NO_BATTLE
	}

	public enum Mood
	{
		ECSTATIC, PLEASED, INTERESTED, CALM /* (the default) */, SAD, DEFEATED, ANGRY, ENRAGED, CONFUSED
	}

	public interface Pantheon
	{
		public ImmutableSet<? extends Deity> deities();
	}

	public interface MoodPack
	{
		MaterialData getMaterialData();

		Sound getSound();

		Map<Material, Integer> getClaimItems();

		Map<Material, Integer> getForsakeItems();

		Set<Flag> getFlags();

		List<Ability> getAbilities();

		int getAccuracy();

		int getFavorRegen();
	}

	public interface MoodManager
	{
		void set(Mood mood, MoodPack moodPack);

		MoodPack get(Mood mood);
	}

	public static class Util
	{
		public static boolean canUseDeity(DCharacter character, String deity)
		{
			if(character == null) return false;
			if(!character.getOfflinePlayer().isOnline()) return canUseDeitySilent(character, deity);
			Player player = character.getOfflinePlayer().getPlayer();
			if(!player.hasPermission(Demigods.mythos().getDeity(deity).getPermission()))
			{
				player.sendMessage(ChatColor.RED + "You don't have permission to use " + deity + "!");
				return false;
			}
			if(!character.isDeity(deity))
			{
				player.sendMessage(ChatColor.RED + "You haven't claimed " + deity + "! You can't do that!");
				return false;
			}
			return true;
		}

		public static boolean canUseDeitySilent(DCharacter character, String deity)
		{
			return !(character.getOfflinePlayer().isOnline() && !character.getOfflinePlayer().getPlayer().hasPermission(Demigods.mythos().getDeity(deity).getPermission())) && character != null && character.isDeity(deity);
		}

		public static boolean canUseDeitySilent(Player player, String deityName)
		{
			String currentDeityName = DPlayer.Util.getPlayer(player).getCurrentDeityName();
			return deityName.equalsIgnoreCase(currentDeityName);
		}
	}
}