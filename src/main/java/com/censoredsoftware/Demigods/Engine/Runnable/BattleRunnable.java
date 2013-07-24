package com.censoredsoftware.Demigods.Engine.Runnable;

import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.Demigods.Engine.Object.Battle.Battle;

public class BattleRunnable extends BukkitRunnable
{
	@Override
	public void run()
	{
		// Battle onTick logic
		for(Battle battle : Battle.getAllActive())
			if(battle.getMeta().getKillCounter() > battle.getMaxKills() || battle.getStartTime() + battle.getDuration() <= System.currentTimeMillis() && battle.getMeta().getKillCounter() > battle.getMinKills()) battle.end();

		// Delete all inactive battles
		for(Battle remove : Battle.getAllInactive())
			if(remove.getDeleteTime() >= System.currentTimeMillis()) remove.delete();
	}
}
