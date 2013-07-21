package com.censoredsoftware.Demigods.Episodes.Demo.Deity.Insignian;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.Demigods.Engine.Object.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Object.Ability.AbilityInfo;
import com.censoredsoftware.Demigods.Engine.Object.Ability.Devotion;
import com.censoredsoftware.Demigods.Engine.Object.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Object.Deity.DeityInfo;
import com.censoredsoftware.Demigods.Engine.Utility.StructureUtility;
import com.censoredsoftware.Demigods.Engine.Utility.UnicodeUtility;
import com.censoredsoftware.Demigods.Engine.Utility.ZoneUtility;

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
	private static Devotion.Type type = Devotion.Type.STEALTH;

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
				player.getWorld().createExplosion(player.getLocation(), 400F, false);
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
