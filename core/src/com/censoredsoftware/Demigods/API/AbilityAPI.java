/*
	Copyright (c) 2013 The Demigods Team
	
	Demigods License v1
	
	This plugin is provided "as is" and without any warranty.  Any express or
	implied warranties, including, but not limited to, the implied warranties
	of merchantability and fitness for a particular purpose are disclaimed.
	In no event shall the authors be liable to any party for any direct,
	indirect, incidental, special, exemplary, or consequential damages arising
	in any way out of the use or misuse of this plugin.
	
	Definitions
	
	 1. This Plugin is defined as all of the files within any archive
	    file or any group of files released in conjunction by the Demigods Team,
	    the Demigods Team, or a derived or modified work based on such files.
	
	 2. A Modification, or a Mod, is defined as this Plugin or a derivative of
	    it with one or more Modification applied to it, or as any program that
	    depends on this Plugin.
	
	 3. Distribution is defined as allowing one or more other people to in
	    any way download or receive a copy of this Plugin, a Modified
	    Plugin, or a derivative of this Plugin.
	
	 4. The Software is defined as an installed copy of this Plugin, a
	    Modified Plugin, or a derivative of this Plugin.
	
	 5. The Demigods Team is defined as Alex Bennett and Alexander Chauncey
	    of http://www.censoredsoftware.com/.
	
	Agreement
	
	 1. Permission is hereby granted to use, copy, modify and/or
	    distribute this Plugin, provided that:
	
	    a. All copyright notices within source files and as generated by
	       the Software as output are retained, unchanged.
	
	    b. Any Distribution of this Plugin, whether as a Modified Plugin
	       or not, includes this license and is released under the terms
	       of this Agreement. This clause is not dependant upon any
	       measure of changes made to this Plugin.
	
	    c. This Plugin, Modified Plugins, and derivative works may not
	       be sold or released under any paid license without explicit 
	       permission from the Demigods Team. Copying fees for the 
	       transport of this Plugin, support fees for installation or
	       other services, and hosting fees for hosting the Software may,
	       however, be imposed.
	
	    d. Any Distribution of this Plugin, whether as a Modified
	       Plugin or not, requires express written consent from the
	       Demigods Team.
	
	 2. You may make Modifications to this Plugin or a derivative of it,
	    and distribute your Modifications in a form that is separate from
	    the Plugin. The following restrictions apply to this type of
	    Modification:
	
	    a. A Modification must not alter or remove any copyright notices
	       in the Software or Plugin, generated or otherwise.
	
	    b. When a Modification to the Plugin is released, a
	       non-exclusive royalty-free right is granted to the Demigods Team
	       to distribute the Modification in future versions of the
	       Plugin provided such versions remain available under the
	       terms of this Agreement in addition to any other license(s) of
	       the initial developer.
	
	    c. Any Distribution of a Modified Plugin or derivative requires
	       express written consent from the Demigods Team.
	
	 3. Permission is hereby also granted to distribute programs which
	    depend on this Plugin, provided that you do not distribute any
	    Modified Plugin without express written consent.
	
	 4. The Demigods Team reserves the right to change the terms of this
	    Agreement at any time, although those changes are not retroactive
	    to past releases, unless redefining the Demigods Team. Failure to
	    receive notification of a change does not make those changes invalid.
	    A current copy of this Agreement can be found included with the Plugin.
	
	 5. This Agreement will terminate automatically if you fail to comply
	    with the limitations described herein. Upon termination, you must
	    destroy all copies of this Plugin, the Software, and any
	    derivatives within 48 hours.
 */

package com.censoredsoftware.Demigods.API;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BlockIterator;

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.Events.Ability.AbilityEvent;
import com.censoredsoftware.Demigods.Events.Ability.AbilityEvent.AbilityType;
import com.censoredsoftware.Demigods.Events.Ability.AbilityTargetEvent;
import com.censoredsoftware.Demigods.Objects.PlayerCharacter;

/**
 * API for Deity Ability related methods, used within or in relation to ability methods themselves.
 */
public class AbilityAPI
{
	private static final Demigods API = Demigods.INSTANCE;
	private static final int TARGETOFFSET = 5;

	/**
	 * Returns true if the ability for <code>player</code>, called <code>name</code>,
	 * with a cost of <code>cost</code>, that is AbilityType <code>type</code>, has
	 * passed all pre-process tests.
	 * 
	 * @param player the player doing the ability
	 * @param name the name of the ability
	 * @param cost the cost (in favor) of the ability
	 * @param type the AbilityType of the ability
	 * @return true/false depending on if all pre-process tests have passed
	 */
	public boolean doAbilityPreProcess(Player player, String name, int cost, AbilityType type)
	{
		PlayerCharacter character = API.player.getCurrentChar(player);

		return doAbilityPreProcess(player, cost) && event(name, character, cost, type);
	}

	/**
	 * Returns true if the ability for <code>player</code>, called <code>name</code>,
	 * with a cost of <code>cost</code>, that is AbilityType <code>type</code>, that
	 * is targeting the LivingEntity <code>target</code>, has passed all pre-process tests.
	 * 
	 * @param player the Player doing the ability
	 * @param target the LivingEntity being targeted
	 * @param name the name of the ability
	 * @param cost the cost (in favor) of the ability
	 * @param type the AbilityType of the ability
	 * @return true/false depending on if all pre-process tests have passed
	 */
	public boolean doAbilityPreProcess(Player player, LivingEntity target, String name, int cost, AbilityType type)
	{
		PlayerCharacter character = API.player.getCurrentChar(player);

		if(doAbilityPreProcess(player, cost) && event(name, character, cost, type))
		{
			if(!(target instanceof LivingEntity))
			{
				player.sendMessage(ChatColor.YELLOW + "No target found.");
				return false;
			}
			else if(!API.zone.canTarget(target))
			{
				player.sendMessage(ChatColor.YELLOW + "Target is in a no-PVP zone.");
				return false;
			}
			else if(target instanceof Player)
			{
				if(API.player.areAllied(player, (Player) target)) return false;
			}
			API.misc.callEvent(new AbilityTargetEvent(character, target));
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the event <code>event</code> is caused by a click.
	 * 
	 * @param event the interact event
	 * @return true/false depending on if the event is caused by a click or not
	 */
	public boolean isClick(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		return action != Action.PHYSICAL;
	}

	/**
	 * Returns the LivingEntity that <code>player</code> is targeting.
	 * 
	 * @param player the interact event
	 * @return the targeted LivingEntity
	 */
	public LivingEntity autoTarget(Player player)
	{
		BlockIterator iterator = new BlockIterator(player.getWorld(), player.getLocation().toVector(), player.getEyeLocation().getDirection(), 0, 100);

		while(iterator.hasNext())
		{
			Block item = iterator.next();
			for(Entity entity : player.getNearbyEntities(100, 100, 100))
			{
				if(entity instanceof LivingEntity)
				{
					int acc = 2;
					for(int x = -acc; x < acc; x++)
					{
						for(int z = -acc; z < acc; z++)
						{
							for(int y = -acc; y < acc; y++)
							{
								if(entity.getLocation().getBlock().getRelative(x, y, z).equals(item)) return (LivingEntity) entity;
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns true if the <code>player</code> ability hits <code>target</code>.
	 * 
	 * @param player the player using the ability
	 * @param target the targeted LivingEntity
	 * @return true/false depending on if the ability hits or misses
	 */
	public boolean targeting(Player player, LivingEntity target)
	{
		PlayerCharacter character = API.player.getCurrentChar(player);
		Location toHit = aimLocation(character, target.getLocation());
		if(isHit(target, toHit)) return true;
		player.sendMessage(ChatColor.RED + "Missed..."); // TODO Better message.
		return false;
	}

	/**
	 * Returns true if the ability event for <code>character</code>, called <code>name</code>,
	 * with a cost of <code>cost</code>, that is AbilityType <code>type</code>, has passed
	 * all pre-process tests.
	 * 
	 * @param character the character triggering the ability event
	 * @param name the name of the ability
	 * @param cost the cost (in favor) of the ability
	 * @param type the AbilityType of the ability
	 * @return true/false if the event isn't cancelled or not
	 */
	public boolean event(String name, PlayerCharacter character, int cost, AbilityType type)
	{
		AbilityEvent event = new AbilityEvent(name, character, cost, type);
		API.misc.callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Returns the location that <code>character</code> is actually aiming
	 * at when targeting <code>target</code>.
	 * 
	 * @param character the character triggering the ability event
	 * @param target the location the character is targeting at
	 * @return the aimed at location
	 */
	public Location aimLocation(PlayerCharacter character, Location target)
	{
		int ascensions = character.getAscensions();

		int offset = (int) (TARGETOFFSET + character.getOwner().getPlayer().getLocation().distance(target));
		int adjustedOffset = offset / ascensions;
		if(adjustedOffset < 1) adjustedOffset = 1;
		Random random = new Random();
		World world = target.getWorld();

		int randomInt = random.nextInt(adjustedOffset);

		int sampleSpace = random.nextInt(3);

		double X = target.getX();
		double Z = target.getZ();
		double Y = target.getY();

		if(sampleSpace == 0)
		{
			X += randomInt;
			Z += randomInt;
		}
		else if(sampleSpace == 1)
		{
			X -= randomInt;
			Z -= randomInt;
		}
		else if(sampleSpace == 2)
		{
			X -= randomInt;
			Z += randomInt;
		}
		else if(sampleSpace == 3)
		{
			X += randomInt;
			Z -= randomInt;
		}

		return new Location(world, X, Y, Z);
	}

	/**
	 * Returns true if <code>target</code> is hit at <code>hit</code>.
	 * 
	 * @param target the LivingEntity being targeted
	 * @param hit the location actually hit
	 * @return true/false if <code>target</code> is hit
	 */
	public boolean isHit(LivingEntity target, Location hit)
	{
		Location shouldHit = target.getLocation();
		return hit.distance(shouldHit) <= 2;
	}

	private boolean doAbilityPreProcess(Player player, int cost)
	{
		PlayerCharacter character = API.player.getCurrentChar(player);

		if(!API.zone.canTarget(player))
		{
			player.sendMessage(ChatColor.YELLOW + "You can't do that from a no-PVP zone.");
			return false;
		}
		else if(character.getFavor() < cost)
		{
			player.sendMessage(ChatColor.YELLOW + "You do not have enough favor.");
			return false;
		}
		else return true;
	}
}
