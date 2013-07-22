package com.censoredsoftware.Demigods.Engine.Runnable;

import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.Demigods.Engine.Object.Battle.Battle;
import com.censoredsoftware.Demigods.Engine.Utility.BattleUtility;

public class BattleRunnable extends BukkitRunnable
{
	@Override
	public void run()
	{
		for(Battle battle : Battle.getAll()) // TODO Something is buggy here.
		{
			if(battle.getMeta().getKills() > battle.getMaxKills() || battle.getStartTime() + battle.getDuration() <= System.currentTimeMillis() && battle.getMeta().getKills() > battle.getMinKills()) battle.end();
			else BattleUtility.battleBorder(battle);
		}
	}
}
