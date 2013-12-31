package com.censoredsoftware.demigods.engine.template;

import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TemplateAbility implements Ability
{
	private final static String name = "Test", command = "test";
	private final static int cost = 170, delay = 1500, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Test your target.");
	private String deity;
	private final static Skill.Type type = Skill.Type.OFFENSE;

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
				LivingEntity target = Abilities.autoTarget(player);

				if(!Abilities.preProcessAbility(player, target, cost)) return false;
				DCharacter.Util.setCoolDown(character, name, System.currentTimeMillis() + delay);
				character.getMeta().subtractFavor(cost);

				if(!Abilities.target(player, target.getLocation(), true)) return false;

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
