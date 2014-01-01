package com.censoredsoftware.demigods.engine.template;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class TemplateAbility implements Ability
{
	private static final String NAME = "Test", COMMAND = "test";
	private static final int COST = 170, DELAY = 1, REPEAT = 0;
	private static final List<String> DETAILS = Lists.newArrayList("Test your target.");
	private static final Skill.Type TYPE = Skill.Type.OFFENSE;
	private final String deity;

	public TemplateAbility(String deity)
	{
		this.deity = deity;
	}

	@Override
	public String getDeity()
	{
		return deity;
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getCommand()
	{
		return COMMAND;
	}

	@Override
	public int getCost()
	{
		return COST;
	}

	@Override
	public int getDelay()
	{
		return DELAY;
	}

	@Override
	public int getRepeat()
	{
		return REPEAT;
	}

	@Override
	public List<String> getDetails()
	{
		return DETAILS;
	}

	@Override
	public Skill.Type getType()
	{
		return TYPE;
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
		return new Predicate<Player>()
		{
			@Override
			public boolean apply(Player player)
			{
				// Define variables
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
				LivingEntity target = Ability.Util.autoTarget(player);

				if(!Ability.Util.target(player, target.getLocation(), true)) return false;

				if(target instanceof Player)
				{
					Player victim = (Player) target;
					victim.sendMessage("Test!");
					player.sendMessage("Tested " + victim.getName() + "!");
				}

				return true;
			}
		};
	}

	@Override
	public Listener getListener()
	{
		return null;
	}

	@Override
	public BukkitRunnable getRunnable()
	{
		return null;
	}
}
