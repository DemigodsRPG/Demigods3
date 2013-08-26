package com.censoredsoftware.demigods.deity;

import com.censoredsoftware.demigods.Elements;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Deity
{
	public String getName();

	public Elements.ListedDeity getListedDeity();

	public String getAlliance();

	public String getPermission();

	public ChatColor getColor();

	public Map<Material, Integer> getClaimItems();

	public Map<Material, Integer> getForsakeItems();

	public List<String> getLore();

	public Set<Flag> getFlags();

	public Set<Ability> getAbilities();

	public enum Flag
	{
		PLAYABLE, NON_PLAYABLE, MAJOR_DEITY, MINOR_DEITY
	}

	@Override
	public String toString();

	public static class Util
	{
		public static Set<String> getLoadedDeityAlliances()
		{
			return Sets.newHashSet(Collections2.transform(Sets.newHashSet(Elements.Deities.values()), new Function<Elements.Deities, String>()
			{
				@Override
				public String apply(Elements.Deities d)
				{
					return d.getDeity().getAlliance();
				}
			}));
		}

		public static Collection<Deity> getAllDeitiesInAlliance(final String alliance)
		{
			return Collections2.filter(Collections2.transform(Sets.newHashSet(Elements.Deities.values()), new Function<Elements.Deities, Deity>()
			{
				@Override
				public Deity apply(Elements.Deities d)
				{
					return d.getDeity();
				}
			}), new Predicate<Deity>()
			{
				@Override
				public boolean apply(Deity d)
				{
					return d.getAlliance().equalsIgnoreCase(alliance);
				}
			});
		}

		public static Deity getDeity(String deity)
		{
			return Elements.Deities.get(deity);
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
