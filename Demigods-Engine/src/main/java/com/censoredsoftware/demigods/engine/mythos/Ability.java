package com.censoredsoftware.demigods.engine.mythos;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.material.MaterialData;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface Ability
{
	String getDeity();

	String getName();

	String getCommand();

	int getCost();

	int getDelay();

	int getRepeat();

	List<String> getDetails();

	Skill.Type getType();

	MaterialData getWeapon();

	boolean hasWeapon();

	Predicate<Player> getActionPredicate();

	Listener getListener();

	Runnable getRunnable();

	public static class Util
	{
		public static Ability getAbility(final String deityName, final String commandName)
		{
			try
			{
				return Iterables.find(getLoadedAbilities(), new Predicate<Ability>()
				{
					@Override
					public boolean apply(Ability ability)
					{
						return commandName.equalsIgnoreCase(ability.getCommand()) && deityName.equalsIgnoreCase(ability.getDeity());
					}
				});
			}
			catch(Exception ignored)
			{
				// ignored
			}

			return null;
		}

		public static Collection<Ability> getLoadedAbilities()
		{
			Set<Ability> abilities = Sets.newHashSet();

			for(Deity deity : Demigods.mythos().getDeities())
			{
				abilities.addAll(deity.getAbilities());
			}

			return abilities;
		}
	}
}
