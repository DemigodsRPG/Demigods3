package com.censoredsoftware.Demigods.Engine.Runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerWrapper;

public class FavorRunnable extends BukkitRunnable
{
	private double multiplier;

	public FavorRunnable(double multiplier)
	{
		this.multiplier = multiplier;
	}

	@Override
	public void run()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			PlayerCharacter character = PlayerWrapper.getPlayer(player).getCurrent();
			if(character == null || !character.isImmortal()) continue;
			int regenRate = (int) Math.ceil(multiplier * character.getMeta().getAscensions());
			if(regenRate < 5) regenRate = 5;
			character.getMeta().addFavor(regenRate);
		}
	}
}
