package com.censoredsoftware.demigods.ability.ultimate;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.util.Spigots;
import com.censoredsoftware.demigods.util.Zones;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Set;

public class Swarm implements Ability
{
	private final static String name = "Swarm", command = "swarm";
	private final static int cost = 3700, delay = 600, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Swarm you enemies with super powerful zombies.");
	private String deity, permission;

	public Swarm(String deity, String permission)
	{
		this.deity = deity;
		this.permission = permission;
	}

	static boolean swarm(Player player)
	{
		// Define variables
		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
		Set<LivingEntity> targets = Ability.Util.doAbilityPreProcess(player, player.getNearbyEntities(50, 50, 50), cost);

		if(targets == null || targets.isEmpty()) return false;

		player.sendMessage(ChatColor.DARK_GREEN + "Swarming " + (targets.size() > 10 ? 10 : targets.size()) + " targets.");

		int count = 0;
		for(LivingEntity entity : targets)
		{
			count++;
			if(count > 10) break;
			Util.spawnZombie(character, entity);
		}

		return true;
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
	public String getPermission()
	{
		return permission;
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
	public Material getWeapon()
	{
		return null;
	}

	@Override
	public boolean hasWeapon()
	{
		return getWeapon() != null;
	}

	@Override
	public Listener getListener()
	{
		return new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(Zones.inNoDemigodsZone(interactEvent.getPlayer().getLocation())) return;

				if(!Ability.Util.isLeftClick(interactEvent)) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Deity.Util.canUseDeitySilent(character, deity)) return;

				if(player.getItemInHand() != null && character.getMeta().checkBound(name, player.getItemInHand().getType()))
				{
					if(!DCharacter.Util.isCooledDown(character, name, true)) return;

					if(swarm(player))
					{
						int cooldownMultiplier = (int) (delay * ((double) character.getMeta().getAscensions() / 100));
						DCharacter.Util.setCoolDown(character, name, System.currentTimeMillis() + cooldownMultiplier * 1000);
					}
				}
			}
		};
	}

	@Override
	public BukkitRunnable getRunnable()
	{
		return null;
	}

	public static class Util
	{
		public static boolean spawnZombie(DCharacter character, LivingEntity target)
		{
			Location spawnLocation = character.getCurrentLocation().clone();
			if(Demigods.MiscUtil.isRunningSpigot()) Spigots.playParticle(spawnLocation, Effect.EXPLOSION_HUGE, 1, 1, 1, 1F, 5, 300);
			Zombie zombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
			zombie.addPotionEffects(Sets.newHashSet(new PotionEffect(PotionEffectType.SPEED, 999, 5, false), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999, 5, false), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999, 1, false), new PotionEffect(PotionEffectType.JUMP, 999, 5, false), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999, 2, false)));
			zombie.setCustomName(character.getName() + "'s Minion");
			zombie.setCustomNameVisible(true);
			zombie.setTarget(target);
			return true;
		}
	}
}
