package com.censoredsoftware.Demigods.Engine;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.DemigodsPlugin;
import com.censoredsoftware.Demigods.Engine.Miscellaneous.TimedData;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedPlayer;
import com.censoredsoftware.Demigods.Engine.Utility.AdminUtility;

public class DemigodsScheduler
{
	public static void startThreads(DemigodsPlugin instance)
	{
		// Start favor runnable
		int rate = Demigods.config.getSettingInt("regeneration.favor") * 20;
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, new FavorRunnable(Demigods.config.getSettingDouble("multipliers.favor")), 20, rate);
		AdminUtility.sendDebug("Favor regeneration runnable enabled...");

		// Start battle runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, new BattleRunnable(), 20, 20);
		AdminUtility.sendDebug("Battle tracking runnable enabled...");

		// Start timed data runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, new TimedDataRunnable(), 20, 20);
		AdminUtility.sendDebug("Timed data runnable enabled...");
	}

	public static void stopThreads(DemigodsPlugin instance)
	{
		instance.getServer().getScheduler().cancelTasks(instance);
	}
}

class FavorRunnable implements Runnable
{
	private double multiplier;

	FavorRunnable(double multiplier)
	{
		this.multiplier = multiplier;
	}

	@Override
	public void run()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			PlayerCharacter character = TrackedPlayer.getTracked(player).getCurrent();
			if(character == null || !character.isImmortal()) continue;
			int regenRate = (int) Math.ceil(multiplier * character.getMeta().getAscensions());
			if(regenRate < 5) regenRate = 5;
			character.getMeta().addFavor(regenRate);
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}

class TimedDataRunnable implements Runnable
{
	@Override
	public void run()
	{
		for(TimedData data : TimedData.getAll())
		{
			if(data.getExpiration() <= System.currentTimeMillis()) data.delete();
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}

class BattleRunnable implements Runnable
{
	@Override
	public void run()
	{
		// TODO
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
