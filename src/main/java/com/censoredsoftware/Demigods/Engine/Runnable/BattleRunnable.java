package com.censoredsoftware.Demigods.Engine.Runnable;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.Demigods.Engine.Object.Battle.Battle;
import com.censoredsoftware.Demigods.Engine.Utility.SpigotUtility;

public class BattleRunnable extends BukkitRunnable
{
	private static boolean spigot = SpigotUtility.runningSpigot();

	@Override
	public void run()
	{
		// Battle onTick logic
		for(Battle battle : Battle.getAllActive())
		{
			if(battle.getMeta().getKills() > battle.getMaxKills() || battle.getStartTime() + battle.getDuration() <= System.currentTimeMillis() && battle.getMeta().getKills() > battle.getMinKills()) battle.end();
		}

		// Delete old battles
		int limit = (int) Math.ceil(Bukkit.getOnlinePlayers().length / 2.0);
		if(Battle.battleQueue.size() >= (limit < 3 ? 3 : limit))
		{
			Battle delete = Battle.battleQueue.poll();
			if(delete != null) delete.delete();
		}
	}
}
