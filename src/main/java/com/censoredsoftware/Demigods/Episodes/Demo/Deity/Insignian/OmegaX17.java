package com.censoredsoftware.Demigods.Episodes.Demo.Deity.Insignian;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.censoredsoftware.Demigods.Engine.Object.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Object.Ability.AbilityInfo;
import com.censoredsoftware.Demigods.Engine.Object.Ability.Devotion;
import com.censoredsoftware.Demigods.Engine.Object.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Object.Deity.DeityInfo;
import com.censoredsoftware.Demigods.Engine.Utility.UnicodeUtility;

public class OmegaX17 extends Deity
{
	private final static String name = "OmegaX17", alliance = "Insignian", permission = "demigods.insignian.omega";
	private final static ChatColor color = ChatColor.BLACK;
	private final static Set<Material> claimItems = new HashSet<Material>(1)
	{
		{
			add(Material.TNT);
		}
	};
	private final static List<String> lore = new ArrayList<String>()
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
	private final static Type type = Type.DEMO;
	private final static Set<Ability> abilities = new HashSet<Ability>(1)
	{
		{
			add(new NoSplosion());
		}
	};

	public OmegaX17()
	{
		super(new DeityInfo(name, alliance, permission, color, claimItems, lore, type), abilities);
	}
}

class NoSplosion extends Ability
{
	private final static String deity = "OmegaX17", name = "No Explosion Damage", command = null, permission = "demigods.insignian.omega";
	private final static int cost = 0, delay = 0, repeat = 0;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Take no damage from explosions.");
		}
	};
	private final static Devotion.Type type = Devotion.Type.PASSIVE;

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
