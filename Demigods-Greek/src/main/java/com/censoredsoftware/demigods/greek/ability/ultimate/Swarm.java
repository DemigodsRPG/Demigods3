package com.censoredsoftware.demigods.greek.ability.ultimate;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.censoredsoftware.censoredlib.util.Spigots;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Swarm extends GreekAbility
{
	private static final String NAME = "Swarm", COMMAND = "swarm";
	private static final int COST = 3700, DELAY = 500, REPEAT = 0;
	private static final List<String> DETAILS = Lists.newArrayList("Swarm your enemies with powerful zombies.");
	private static final Skill.Type TYPE = Skill.Type.ULTIMATE;

	public Swarm(String deity)
	{
		super(NAME, COMMAND, deity, COST, DELAY, REPEAT, DETAILS, TYPE, null, new Predicate<Player>()
		{
			@Override
			public boolean apply(Player player)
			{
				// Define variables
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
				Set<LivingEntity> targets = Sets.newHashSet(); /* TODO: Ability.Util.preProcessAbility(player, player.getNearbyEntities(50, 50, 50), NAME); */

				if(targets == null || targets.isEmpty()) return false;

				player.sendMessage(ChatColor.DARK_GREEN + "Swarming " + (targets.size() > 10 ? 10 : targets.size()) + " targets.");

				int count = 0;
				for(LivingEntity entity : targets)
				{
					count++;
					if(count > 10) break;
					spawnZombie(character, entity);
				}

				return true;
			}
		}, null, null);
	}

	public static boolean spawnZombie(DCharacter character, LivingEntity target)
	{
		Location spawnLocation = character.getCurrentLocation().clone();
		if(Demigods.Util.isRunningSpigot()) Spigots.playParticle(spawnLocation, Effect.EXPLOSION_HUGE, 1, 1, 1, 1F, 5, 300);
		Zombie zombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
		zombie.addPotionEffects(Sets.newHashSet(new PotionEffect(PotionEffectType.SPEED, 999, 5, false), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999, 5, false), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999, 1, false), new PotionEffect(PotionEffectType.JUMP, 999, 5, false), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999, 2, false)));
		zombie.setCustomName(character.getName() + "'s Minion");
		zombie.setCustomNameVisible(true);
		zombie.setTarget(target);
		return true;
	}
}
