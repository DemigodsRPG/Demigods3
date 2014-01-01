package com.censoredsoftware.demigods.greek.ability.ultimate;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.Battle;
import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class Storm extends GreekAbility
{
	private final static String name = "Storm", command = "storm";
	private final static int cost = 3700, delay = 5000, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Strike pure fear into the hearts of your enemies.");
	private final static Skill.Type type = Skill.Type.ULTIMATE;

	public Storm(String deity)
	{
		super(name, command, deity, cost, delay, repeat, details, type, null, new Predicate<Player>()
		{
			@Override
			public boolean apply(final Player player)
			{
				// Define variables
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				// Can they do it?
				if(!Ability.Util.preProcessAbility(player, cost)) return false;

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
					if((entity instanceof Player))
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
		// Handle weather effect
		if(player.getPlayerWeather() != null && !player.getPlayerWeather().equals(WeatherType.DOWNFALL))
		{
			// Save current weather
			final WeatherType currentWeather = player.getPlayerWeather();

			// Set the weather
			player.setPlayerWeather(WeatherType.DOWNFALL);

			// Create the runnable to switch back
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.plugin(), new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.setPlayerWeather(currentWeather);
				}
			}, ticks);
		}
	}

	public static void strikeLightning(DCharacter character, Location location)
	{

	}
}
