package com.censoredsoftware.demigods.episodes.demo.ability.ultimate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.core.util.Randoms;
import com.censoredsoftware.core.util.Spigots;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.element.Ability;
import com.censoredsoftware.demigods.engine.element.Deity;
import com.censoredsoftware.demigods.engine.location.DLocation;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.util.Structures;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.Sets;

public class Discoball extends Ability
{
	private final static String deity = "DrD1sco", name = "Discoball of Doom", command = "discoball", permission = "demigods.insignian.disco";
	private final static int cost = 30, delay = 30, repeat = 4;
	private static Info info;
	private final static Devotion.Type type = Devotion.Type.ULTIMATE;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Spread the music while causing destruction.");
		}
	};
	private final static Set<FallingBlock> discoBalls = Sets.newHashSet();

	protected Discoball()
	{
		super(info = new Info(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(Demigods.isDisabledWorld(interactEvent.getPlayer().getWorld())) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Ability.Util.isLeftClick(interactEvent)) return;

				if(!Deity.Util.canUseDeitySilent(player, deity)) return;

				if(character.getMeta().isBound(name))
				{
					if(!DCharacter.Util.isCooledDown(character, name, true)) return;

					Util.discoBall(player);
				}
			}

			@EventHandler(priority = EventPriority.HIGHEST)
			public void onBlockChange(EntityChangeBlockEvent changeEvent)
			{
				if(Demigods.isDisabledWorld(changeEvent.getBlock().getWorld())) return;

				if(changeEvent.getEntityType() != EntityType.FALLING_BLOCK) return;
				changeEvent.getBlock().setType(Material.AIR);
				FallingBlock block = (FallingBlock) changeEvent.getEntity();
				if(discoBalls.contains(block))
				{
					discoBalls.remove(block);
					block.remove();
				}
			}
		}, new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(FallingBlock block : discoBalls)
				{
					if(block != null)
					{
						Location location = block.getLocation();
						if(Demigods.isDisabledWorld(location.getWorld())) return;
						Util.playRandomNote(location, 2F);
						Util.sparkleSparkle(location);
						Util.destoryNearby(location);
					}
				}
			}
		});
	}

	public static class RainbowWalking extends Ability
	{
		private final static String name = "Rainbow Walking", command = null;
		private final static int cost = 0, delay = 0, repeat = 5;
		private static Info info;
		private final static List<String> details = new ArrayList<String>(1)
		{
			{
				add("Spread the disco while sneaking.");
			}
		};
		private final static Devotion.Type type = Devotion.Type.STEALTH;

		public RainbowWalking(final String deity, String permission)
		{
			super(info = new Info(deity, name, command, permission, cost, delay, repeat, details, type), null, new BukkitRunnable()
			{
				@Override
				public void run()
				{
					for(Player online : Bukkit.getOnlinePlayers())
					{
						if(Demigods.isDisabledWorld(online.getWorld())) return;
						if(Deity.Util.canUseDeitySilent(online, "DrD1sco") && online.isSneaking() && !online.isFlying() && !Zones.zoneNoPVP(online.getLocation()) && !Structures.isTrespassingInNoGriefingZone(online)) doEffect(online, true);
						else if(Deity.Util.canUseDeitySilent(online, "DrD1sco")) doEffect(online, false);
					}
				}

				private void doEffect(Player player, boolean effect)
				{
					for(Entity entity : player.getNearbyEntities(30, 30, 30))
					{
						if(!(entity instanceof Player)) continue;
						Player viewing = (Player) entity;
						if(effect)
						{
							viewing.hidePlayer(player);
							Discoball.Util.rainbow(player, viewing);
						}
						else viewing.showPlayer(player);
					}
					if(effect)
					{
						Discoball.Util.rainbow(player, player);
						Discoball.Util.playRandomNote(player.getLocation(), 0.5F);
					}
				}
			});
		}
	}

	public static class Util
	{
		public static void discoBall(final Player player)
		{
			// Set variables
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

			if(!Ability.Util.doAbilityPreProcess(player, name, cost, info)) return;
			character.getMeta().subtractFavor(cost);
			DCharacter.Util.setCoolDown(character, name, System.currentTimeMillis() + delay);

			// Cooldown
			DCharacter.Util.setCoolDown(character, name, System.currentTimeMillis() + delay * 1000);

			balls(player);

			player.sendMessage(ChatColor.YELLOW + "Dance!");

			Bukkit.getScheduler().scheduleSyncDelayedTask(Demigods.plugin, new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.sendMessage(ChatColor.RED + "B" + ChatColor.GOLD + "o" + ChatColor.YELLOW + "o" + ChatColor.GREEN + "g" + ChatColor.AQUA + "i" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.DARK_PURPLE + " W" + ChatColor.BLUE + "o" + ChatColor.RED + "n" + ChatColor.GOLD + "d" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "r" + ChatColor.AQUA + "l" + ChatColor.LIGHT_PURPLE + "a" + ChatColor.DARK_PURPLE + "n" + ChatColor.BLUE + "d" + ChatColor.RED + "!");
				}
			}, 40);
		}

		public static void balls(Player player)
		{
			for(Location location : DLocation.Util.getCirclePoints(new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() + 30 < 256 ? player.getLocation().getBlockY() + 30 : 256, player.getLocation().getBlockZ()), 3.0, 50))
			{
				spawnBall(location);
			}

		}

		public static void spawnBall(Location location)
		{
			final FallingBlock discoBall = location.getWorld().spawnFallingBlock(location, Material.GLOWSTONE, (byte) 0);
			discoBalls.add(discoBall);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Demigods.plugin, new BukkitRunnable()
			{
				@Override
				public void run()
				{
					discoBalls.remove(discoBall);
					discoBall.remove();
				}
			}, 600);
		}

		public static void rainbow(Player disco, Player player)
		{
			player.sendBlockChange(disco.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(), Material.WOOL, (byte) Randoms.generateIntRange(0, 15));
			if(Demigods.isRunningSpigot()) Spigots.playParticle(disco.getLocation(), Effect.COLOURED_DUST, 1, 0, 1, 10F, 100, 30);
		}

		public static void playRandomNote(Location location, float volume)
		{
			location.getWorld().playSound(location, Sound.NOTE_BASS_GUITAR, volume, (float) ((double) Randoms.generateIntRange(5, 10) / 10.0));
		}

		public static void sparkleSparkle(Location location)
		{
			if(Demigods.isRunningSpigot()) Spigots.playParticle(location, Effect.CRIT, 1, 1, 1, 10F, 1000, 30);
		}

		public static void destoryNearby(Location location)
		{
			location.getWorld().createExplosion(location, 2F);
		}
	}
}
