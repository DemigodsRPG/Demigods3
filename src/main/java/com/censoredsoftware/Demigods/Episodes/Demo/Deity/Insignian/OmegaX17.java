package com.censoredsoftware.Demigods.Episodes.Demo.Deity.Insignian;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Object.Ability.AbilityInfo;
import com.censoredsoftware.Demigods.Engine.Object.Ability.Devotion;
import com.censoredsoftware.Demigods.Engine.Object.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Object.Deity.DeityInfo;
import com.censoredsoftware.Demigods.Engine.Utility.MiscUtility;
import com.censoredsoftware.Demigods.Engine.Utility.StructureUtility;
import com.censoredsoftware.Demigods.Engine.Utility.UnicodeUtility;
import com.censoredsoftware.Demigods.Engine.Utility.ZoneUtility;
import com.google.common.collect.Maps;

public class OmegaX17 extends Deity
{
	private static String name = "OmegaX17", alliance = "Insignian", permission = "demigods.insignian.omega";
	private static ChatColor color = ChatColor.BLACK;
	private static Set<Material> claimItems = new HashSet<Material>()
	{
		{
			add(Material.TNT);
		}
	};
	private static List<String> lore = new ArrayList<String>()
	{
		{
			add(" ");
			add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + color + name);
			add(ChatColor.RESET + "-----------------------------------------------------");
			add(ChatColor.YELLOW + " Claim Items:");
			for(Material item : claimItems)
			{
				add(ChatColor.GRAY + " " + UnicodeUtility.rightwardArrow() + " " + ChatColor.WHITE + item.name());
			}
			add(ChatColor.YELLOW + " Abilities:");
		}
	};
	private static Type type = Type.DEMO;
	private static Set<Ability> abilities = new HashSet<Ability>()
	{
		{
			add(new SplosionWalking());
			add(new NoSplosion());
			add(new Equalizer());
		}
	};

	public OmegaX17()
	{
		super(new DeityInfo(name, alliance, permission, color, claimItems, lore, type), abilities);
	}
}

class SplosionWalking extends Ability
{
	private static String deity = "OmegaX17", name = "Explosion Walking", command = null, permission = "demigods.insignian.omega";
	private static int cost = 0, delay = 0, repeat = 20;
	private static AbilityInfo info;
	private static List<String> details = new ArrayList<String>()
	{
		{
			add(ChatColor.GRAY + " " + UnicodeUtility.rightwardArrow() + " " + ChatColor.WHITE + "The end of all things.");
		}
	};
	private static Devotion.Type type = Devotion.Type.SUPPORT;

	protected SplosionWalking()
	{
		super(info = new AbilityInfo(deity, name, command, permission, cost, delay, repeat, details, type), null, new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(Player online : Bukkit.getOnlinePlayers())
				{
					if(Deity.canUseDeitySilent(online, "OmegaX17") && online.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid() && !online.isFlying() && !ZoneUtility.zoneNoPVP(online.getLocation()) && !StructureUtility.isTrespassingInNoGriefingZone(online)) doIt(online);
				}
			}

			public void doIt(Player player)
			{
				player.getWorld().createExplosion(player.getLocation(), 1F, false);
			}
		});
	}
}

class NoSplosion extends Ability
{
	private static String deity = "OmegaX17", name = "No Explosion Damage", command = null, permission = "demigods.insignian.omega";
	private static int cost = 0, delay = 0, repeat = 0;
	private static List<String> details = new ArrayList<String>()
	{
		{
			add(ChatColor.GRAY + " " + UnicodeUtility.rightwardArrow() + " " + ChatColor.WHITE + "Take no damage from explosions.");
		}
	};
	private static Devotion.Type type = Devotion.Type.PASSIVE;

	protected NoSplosion()
	{
		super(new AbilityInfo(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.MONITOR)
			public void onEntityDamange(EntityDamageEvent damageEvent)
			{
				if(damageEvent.getEntity() instanceof Player)
				{
					Player player = (Player) damageEvent.getEntity();
					if(!Deity.canUseDeitySilent(player, deity)) return;

					// If the player receives falling damage, cancel it
					if(damageEvent.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || damageEvent.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) damageEvent.setCancelled(true);
				}
			}
		}, null);
	}
}

class Equalizer extends Ability
{
	private static String deity = "OmegaX17", name = "Omega Equalizer", command = null, permission = "demigods.insignian.omega";
	private static int cost = 0, delay = 0, repeat = 600;
	private static AbilityInfo info;
	private static List<String> details = new ArrayList<String>()
	{
		{
			add(ChatColor.GRAY + " " + UnicodeUtility.rightwardArrow() + " " + ChatColor.WHITE + "Prevent Omega from being too OP.");
		}
	};
	private static Devotion.Type type = Devotion.Type.PASSIVE;

	private static Map<Player, String> equalizing = Maps.newHashMap();

	protected Equalizer()
	{
		super(info = new AbilityInfo(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onAsyncPlayerChat(AsyncPlayerChatEvent chatEvent)
			{
				Player player = chatEvent.getPlayer();
				if(equalizing.containsKey(player) && chatEvent.getMessage().equals(equalizing.get(player)))
				{
					chatEvent.setCancelled(true);
					equalizing.remove(player);
					player.sendMessage(ChatColor.YELLOW + "Hooray!  You may now continue being OP.");
				}
			}

			@EventHandler(priority = EventPriority.HIGHEST)
			public void onPlayerMove(PlayerMoveEvent moveEvent)
			{
				Player player = moveEvent.getPlayer();
				if(equalizing.containsKey(player)) moveEvent.setCancelled(true);
			}
		}, new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(Player online : Bukkit.getOnlinePlayers())
				{
					if(Deity.canUseDeitySilent(online, "OmegaX17") && !tooSlow(online) && MiscUtility.generateIntRange(0, 19) == 1) equalize(online);
				}
			}

			public boolean tooSlow(Player player)
			{
				if(equalizing.containsKey(player))
				{
					player.sendMessage(ChatColor.YELLOW + "Too slow, try again:");
					equalize(player);
					return true;
				}
				return false;
			}

			public void equalize(Player player)
			{
				player.sendMessage(ChatColor.DARK_RED + Demigods.message.chatTitle("Equalizer"));
				equalizing.put(player, MiscUtility.generateString(19));
				player.sendMessage(ChatColor.YELLOW + "  Here is your code (you have 10 seconds to say it in chat): ");
				player.sendMessage(ChatColor.GRAY + "  " + equalizing.get(player));
			}
		});
	}
}
