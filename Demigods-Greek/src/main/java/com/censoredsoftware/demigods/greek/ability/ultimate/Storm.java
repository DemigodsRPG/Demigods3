package com.censoredsoftware.demigods.greek.ability.ultimate;

import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.Battle;
import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Storm extends GreekAbility
{
	private static final String NAME = "Storm", COMMAND = "storm";
	private static final int COST = 3700, DELAY = 300, REPEAT = 0;
	private static final List<String> DETAILS = Lists.newArrayList("Strike fear into the hearts of your enemies.");
	private static final Skill.Type TYPE = Skill.Type.ULTIMATE;

	public Storm(String deity)
	{
		super(NAME, COMMAND, deity, COST, DELAY, REPEAT, DETAILS, TYPE, null, new Predicate<Player>()
		{
			@Override
			public boolean apply(final Player player)
			{
				// Define variables
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				// Define variables
				final int ultimateSkillLevel = character.getMeta().getSkill(Skill.Type.ULTIMATE).getLevel();
				final int damage = (int) Math.ceil(8 * (int) Math.pow(ultimateSkillLevel, 0.5));
				final int radius = (int) Math.log10(10 * ultimateSkillLevel) * 25;

				// Make it stormy for the caster
				setWeather(player, 100);

				// Strike targets
				for(final Entity entity : player.getNearbyEntities(radius, radius, radius))
				{
					// Validate them first
					if(!(entity instanceof LivingEntity)) continue;
					if(entity instanceof Player)
					{
						DCharacter opponent = DPlayer.Util.getPlayer((Player) entity).getCurrent();
						if(opponent != null && DCharacter.Util.areAllied(character, opponent)) continue;
					}
					if(Battle.Util.canParticipate(entity) && !Battle.Util.canTarget(Battle.Util.defineParticipant(entity))) continue;

					// Make it stormy for players
					if(entity instanceof Player) setWeather((Player) entity, 100);

					// Strike them with a small delay
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.plugin(), new BukkitRunnable()
					{
						@Override
						public void run()
						{
							for(int i = 0; i <= 3; i++)
							{
								player.getWorld().strikeLightningEffect(entity.getLocation());
								Ability.Util.dealDamage(player, (LivingEntity) entity, damage, EntityDamageEvent.DamageCause.LIGHTNING);
							}
						}
					}, 15);
				}

				return true;
			}
		}, null, null);
	}

	public static void setWeather(final Player player, long ticks)
	{
		// Set the weather
		player.setPlayerWeather(WeatherType.DOWNFALL);

		// Create the runnable to switch back
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.plugin(), new BukkitRunnable()
		{
			@Override
			public void run()
			{
				player.resetPlayerWeather();
			}
		}, ticks);
	}
}
