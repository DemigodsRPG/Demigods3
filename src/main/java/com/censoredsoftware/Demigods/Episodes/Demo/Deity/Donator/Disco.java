package com.censoredsoftware.Demigods.Episodes.Demo.Deity.Donator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.Demigods.Engine.Object.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Object.Ability.AbilityInfo;
import com.censoredsoftware.Demigods.Engine.Object.Ability.Devotion;
import com.censoredsoftware.Demigods.Engine.Object.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Object.Deity.DeityInfo;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureInfo;
import com.censoredsoftware.Demigods.Engine.Utility.*;

public class Disco extends Deity
{
	private static String name = "Disco", alliance = "Donator";
	private static ChatColor color = ChatColor.STRIKETHROUGH;
	private static Set<Material> claimItems = new HashSet<Material>()
	{
		{
			add(Material.DIRT);
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
			add(new RainbowWalking());
		}
	};

	public Disco()
	{
		super(new DeityInfo(name, alliance, color, claimItems, lore, type), abilities);
	}
}

class RainbowWalking extends Ability
{
	private static String deity = "Disco", name = "Rainbow Walking", command = null, permission = "demigods.donator.disco";
	private static int cost = 170, delay = 1500, cooldownMin = 0, cooldownMax = 0;
	private static AbilityInfo info;
	private static List<String> details = new ArrayList<String>()
	{
		{
			add(ChatColor.GRAY + " " + UnicodeUtility.rightwardArrow() + " " + ChatColor.WHITE + "Constantly shit rainbows.");
		}
	};
	private static Devotion.Type type = Devotion.Type.SUPPORT;

	protected RainbowWalking()
	{
		super(info = new AbilityInfo(deity, name, command, permission, cost, delay, cooldownMin, cooldownMax, details, type), null, new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(Player online : Bukkit.getOnlinePlayers())
				{
					if(Deity.canUseDeitySilent(online, "Disco") && online.isSneaking()) doEffect(online, true);
					else doEffect(online, false);
				}
			}

			private void doEffect(Player player, boolean effect)
			{
				for(Entity entity : player.getNearbyEntities(20, 20, 20))
				{
					if(!(entity instanceof Player)) continue;
					Player viewing = (Player) entity;
					if(effect)
					{
						viewing.hidePlayer(player);
						rainbow(player, viewing);
					}
					else viewing.showPlayer(player);
				}
				if(effect)
				{
					rainbow(player, player);
					player.playSound(player.getLocation(), Sound.BURP, 1F, 0F);
				}
			}

			private void rainbow(Player disco, Player player)
			{
				if(!ZoneUtility.zoneNoBuild(disco, disco.getLocation()) && !StructureUtility.isInRadiusWithFlag(disco.getLocation(), StructureInfo.Flag.NO_PVP_ZONE) && !StructureUtility.isInRadiusWithFlag(disco.getLocation(), StructureInfo.Flag.NO_GRIEFING_ZONE))
				{
					player.sendBlockChange(disco.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(), Material.WOOL, (byte) MiscUtility.generateIntRange(0, 15));
					if(SpigotUtility.runningSpigot())
					{
						SpigotUtility.playParticle(disco.getLocation(), Effect.COLOURED_DUST, 1, 0, 1, 10F, 100, 10);
						DataUtility.saveTimed(player.getName(), "disco_invisible", true, 3);
					}
				}
			}
		});
	}
}
