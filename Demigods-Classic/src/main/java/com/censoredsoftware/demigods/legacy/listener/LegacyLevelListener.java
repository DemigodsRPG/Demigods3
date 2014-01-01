package com.censoredsoftware.demigods.legacy.listener;

import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.util.Zones;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class LegacyLevelListener implements Listener
{
	private static final double MULTIPLIER = 1.0;
	private static final int LOSSLIMIT = 15000;

	@EventHandler(priority = EventPriority.HIGHEST)
	public void gainEXP(BlockBreakEvent event)
	{
		if(event.getPlayer() != null)
		{
			Player player = event.getPlayer();
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			if(event.isCancelled() || Zones.isNoDemigodsWorld(event.getBlock().getWorld()) || Zones.inNoBuildZone(player, event.getBlock().getLocation())) return;
			if(character == null) return;
			int value = 0;
			switch(event.getBlock().getType())
			{
				case DIAMOND_ORE:
					if(event.getExpToDrop() != 0) value = 100;
					break;
				case COAL_ORE:
					if(event.getExpToDrop() != 0) value = 3;
					break;
				case LAPIS_ORE:
					if(event.getExpToDrop() != 0) value = 30;
					break;
				case OBSIDIAN:
					value = 15;
					break;
				case REDSTONE_ORE:
					if(event.getExpToDrop() != 0) value = 5;
					break;
			}
			value *= MULTIPLIER;
			character.getMeta().addSkillPoints(value);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void gainEXP(EntityDamageByEntityEvent event)
	{
		if(event.isCancelled() || Zones.isNoDemigodsWorld(event.getEntity().getWorld())) return;
		if(event.getDamager() instanceof Player)
		{
			Player player = (Player) event.getDamager();
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			if(character == null) return;
			character.getMeta().addSkillPoints((int) (event.getDamage() * MULTIPLIER));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void deathPenalty(EntityDeathEvent event)
	{
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
		if(Zones.isNoDemigodsWorld(event.getEntity().getWorld()) || character == null) return;
		double reduced = 0.1;
		int before = character.getMeta().getSkillPoints();
		int reduceamt = (int) Math.round(before * reduced * MULTIPLIER * (character.getMinorDeities().size() + 1));
		if(reduceamt > LOSSLIMIT) reduceamt = LOSSLIMIT;
		character.getMeta().subtractSkillPoints(reduceamt);
		if(character.getMinorDeities().isEmpty()) player.sendMessage(ChatColor.DARK_RED + "You have failed in your service to " + character.getDeity().getName() + ".");
		else player.sendMessage(ChatColor.DARK_RED + "You have failed in your service to your deities.");
		player.sendMessage(ChatColor.DARK_RED + "Your Devotion has been reduced by " + (before - character.getMeta().getSkillPoints()) + ".");
	}
}
