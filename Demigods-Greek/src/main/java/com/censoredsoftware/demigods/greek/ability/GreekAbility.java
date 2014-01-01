package com.censoredsoftware.demigods.greek.ability;

import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.google.common.base.Predicate;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.material.MaterialData;

import java.util.List;

public class GreekAbility implements Ability
{
	private final String name, command, deity;
	private final int cost, delay, repeat;
	private final List<String> details;
	private final Skill.Type type;
	private final MaterialData weapon;
	private final Predicate<Player> action;
	private final Listener listener;
	private final Runnable runnable;

	public GreekAbility(String name, String command, String deity, int cost, int delay, int repeat, List<String> details, Skill.Type type, MaterialData weapon, Predicate<Player> action, Listener listener, Runnable runnable)
	{
		this.name = name;
		this.command = command;
		this.deity = deity;
		this.cost = cost;
		this.delay = delay;
		this.repeat = repeat;
		this.details = details;
		this.type = type;
		this.weapon = weapon;
		this.action = action;
		this.listener = listener;
		this.runnable = runnable;
	}

	@Override
	public String getDeity()
	{
		return deity;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getCommand()
	{
		return command;
	}

	@Override
	public int getCost()
	{
		return cost;
	}

	@Override
	public int getDelay()
	{
		return delay;
	}

	@Override
	public int getRepeat()
	{
		return repeat;
	}

	@Override
	public List<String> getDetails()
	{
		return details;
	}

	@Override
	public Skill.Type getType()
	{
		return type;
	}

	@Override
	public MaterialData getWeapon()
	{
		return weapon;
	}

	@Override
	public boolean hasWeapon()
	{
		return getWeapon() != null;
	}

	@Override
	public Predicate<Player> getActionPredicate()
	{
		return action;
	}

	@Override
	public Listener getListener()
	{
		return listener;
	}

	@Override
	public Runnable getRunnable()
	{
		return runnable;
	}

	public static class Passive implements Ability
	{
		private final String name, deity;
		private final int repeat;
		private final List<String> details;
		private final Listener listener;
		private final Runnable runnable;

		public Passive(String name, String deity, int repeat, List<String> details, Listener listener, Runnable runnable)
		{
			this.name = name;
			this.deity = deity;
			this.repeat = repeat;
			this.details = details;
			this.listener = listener;
			this.runnable = runnable;
		}

		@Override
		public String getCommand()
		{
			return null;
		}

		@Override
		public int getCost()
		{
			return 0;
		}

		@Override
		public int getDelay()
		{
			return 0;
		}

		@Override
		public Skill.Type getType()
		{
			return Skill.Type.PASSIVE;
		}

		@Override
		public MaterialData getWeapon()
		{
			return null;
		}

		@Override
		public boolean hasWeapon()
		{
			return getWeapon() != null;
		}

		@Override
		public Predicate<Player> getActionPredicate()
		{
			return null;
		}

		@Override
		public String getDeity()
		{
			return deity;
		}

		@Override
		public String getName()
		{
			return name;
		}

		@Override
		public int getRepeat()
		{
			return repeat;
		}

		@Override
		public List<String> getDetails()
		{
			return details;
		}

		@Override
		public Listener getListener()
		{
			return listener;
		}

		@Override
		public Runnable getRunnable()
		{
			return runnable;
		}
	}
}