package com.censoredsoftware.Demigods.API;

// TODO Move all these somewhere.

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacter;

public class MiscAPI
{
	public static void customDamage(LivingEntity source, LivingEntity target, int amount, EntityDamageEvent.DamageCause cause)
	{
		if(target instanceof Player)
		{
			if(source instanceof Player)
			{
				target.setLastDamageCause(new EntityDamageByEntityEvent(source, target, cause, amount));
			}
			else target.damage(amount);
		}
		else target.damage(amount);
	}

	public static boolean canUseDeity(Player player, String deity)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		// Check the player for DEITYNAME
		if(character != null && !character.isDeity(deity))
		{
			player.sendMessage(ChatColor.RED + "You haven't claimed " + deity + "! You can't do that!");
			return false;
		}
		else if(character == null || !character.isImmortal())
		{
			player.sendMessage(ChatColor.RED + "You can't do that, mortal!");
			return false;
		}
		return true;
	}

	public static boolean canUseDeitySilent(Player player, String deity)
	{
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		if(character == null) return false;

		// Check the player for DEITYNAME
		return character.isDeity(deity) && character.isImmortal();
	}

	/**
	 * Generates a random location with the center being <code>reference</code>.
	 * Must be at least <code>min</code> blocks from the center and no more than <code>max</code> blocks away.
	 * 
	 * @param reference the location used as the center for reference.
	 * @param min the minimum number of blocks away.
	 * @param max the maximum number of blocks away.
	 * @return the random location generated.
	 */
	public static Location randomLocation(Location reference, int min, int max)
	{
		Location location = reference.clone();

		double randX = DemigodsData.generateIntRange(min, max);
		double randZ = DemigodsData.generateIntRange(min, max);
		location.add(randX, 0, randZ);
		double highestY = location.clone().getWorld().getHighestBlockYAt(location);
		location.setY(highestY);

		Demigods.message.broadcast("Y: " + highestY); // TODO Why is this broadcasting?

		return location;
	}

	/**
	 * Returns a random location within the <code>chunk</code> passed in.
	 * 
	 * @param chunk the chunk that we will obtain the location from.
	 * @return the random location generated.
	 */
	public static Location randomChunkLocation(Chunk chunk)
	{
		Location reference = chunk.getBlock(DemigodsData.generateIntRange(1, 16), 64, DemigodsData.generateIntRange(1, 16)).getLocation();
		double locX = reference.getX();
		double locY = chunk.getWorld().getHighestBlockYAt(reference);
		double locZ = reference.getZ();
		return new Location(chunk.getWorld(), locX, locY, locZ);
	}
}
