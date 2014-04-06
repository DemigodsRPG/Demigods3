package com.demigodsrpg.demigods.engine.deity;

import com.censoredsoftware.library.serializable.yaml.SimpleYamlFile;
import com.demigodsrpg.demigods.engine.Demigods;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Deity
{
	@Override String toString();

	String getName();

	Alliance getAlliance();

	String getPermission();

	PermissionDefault getPermissionDefault();

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

	SimpleYamlFile getConfig();

	enum Flag
	{
		PLAYABLE, NON_PLAYABLE, MAJOR_DEITY, MINOR_DEITY, NEUTRAL, DIFFICULT, ALTERNATE_ASCENSION_LEVELING, NO_SHRINE, NO_OBELISK, NO_BATTLE
	}

	enum Mood
	{
		ECSTATIC, PLEASED, INTERESTED, CALM /* (the default) */, SAD, DEFEATED, ANGRY, ENRAGED, CONFUSED
	}

	interface MoodPack
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

	interface MoodManager
	{
		void set(Mood mood, MoodPack moodPack);

		MoodPack get(Mood mood);
	}

	class Util
	{
		public static boolean canUseDeity(DemigodsCharacter character, String deity)
		{
			if(character == null) return false;
			if(!character.getBukkitOfflinePlayer().isOnline()) return canUseDeitySilent(character, deity);
			Player player = character.getBukkitOfflinePlayer().getPlayer();
			if(!player.hasPermission(Demigods.getMythos().getDeity(deity).getPermission()))
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

		public static boolean canUseDeitySilent(DemigodsCharacter character, String deity)
		{
			return !(character.getBukkitOfflinePlayer().isOnline() && !character.getBukkitOfflinePlayer().getPlayer().hasPermission(Demigods.getMythos().getDeity(deity).getPermission())) && character != null && character.isDeity(deity);
		}

		public static boolean canUseDeitySilent(Player player, String deityName)
		{
			String currentDeityName = DemigodsPlayer.of(player).getCurrentDeityName();
			return deityName.equalsIgnoreCase(currentDeityName);
		}
	}
}
