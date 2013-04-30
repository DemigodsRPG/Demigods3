package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Demo.Deities;
import com.censoredsoftware.Demigods.Engine.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

public class DeityAPI
{
	public static List<Deity> getLoadedDeities()
	{
		return new ArrayList<Deity>()
		{
			{
				for(Deities deity : Deities.values())
				{
					add(deity.getDeity());
				}
			};
		};
	}

	public static List<String> getLoadedDeityAlliances()
	{
		return new ArrayList<String>()
		{
			{
				for(Deity deity : getLoadedDeities())
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
				for(Deity deity : getLoadedDeities())
				{
					if(deity.getInfo().getAlliance().equalsIgnoreCase(alliance)) add(deity);
				}
			};
		};
	}

	public static Deity getDeity(String deity)
	{
		for(Deity loaded : getLoadedDeities())
		{
			if(loaded.getInfo().getName().equalsIgnoreCase(deity)) return loaded;
		}
		return null;
	}

	public static List<Ability> getLoadedAbilities()
	{
		return new ArrayList<Ability>()
		{
			{
				for(Deity deity : getLoadedDeities())
				{
					addAll(deity.getAbilities());
				}
			};
		};
	}

	public static boolean invokeAbilityCommand(Player player, String command, boolean bind)
	{
		for(Ability ability : getLoadedAbilities())
		{
			if(ability.getInfo().getType() == Ability.Type.PASSIVE) continue;
			if(ability.getInfo().getCommand().equalsIgnoreCase(command))
			{
				PlayerCharacter character = PlayerAPI.getCurrentChar(player);

				if(!Demigods.permission.hasPermissionOrOP(player, ability.getInfo().getPermission())) return true;

				if(!MiscAPI.canUseDeity(player, ability.getInfo().getDeity())) return true;

				if(bind)
				{
					// Bind item
					character.getBindings().setBound(ability.getInfo().getName(), player.getItemInHand().getType());
				}
				else
				{
					if(character.getAbilities().isEnabledAbility(ability.getInfo().getName()))
					{
						character.getAbilities().toggleAbility(ability.getInfo().getName(), false);
						player.sendMessage(ChatColor.YELLOW + ability.getInfo().getName() + " is no longer active.");
					}
					else
					{
						character.getAbilities().toggleAbility(ability.getInfo().getName(), true);
						player.sendMessage(ChatColor.YELLOW + ability.getInfo().getName() + " is now active.");
					}
				}
				return true;
			}
		}
		return false;
	}
}
