package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.censoredlib.util.Threads;
import com.censoredsoftware.censoredlib.util.Times;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.DemigodsServer;
import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.conversation.Administration;
import com.censoredsoftware.demigods.engine.data.wrap.NotificationManager;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsCharacter;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsPlayer;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.engine.mythos.StructureType;
import com.censoredsoftware.demigods.engine.util.Configs;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("deprecation")
public class TaskManager
{
	private static final boolean SAVE_ALERT = Configs.getSettingBoolean("saving.console_alert");
	private static final BukkitRunnable SYNC, ASYNC, SAVE, FAVOR;

	static
	{
		SYNC = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				// Update online players
				for(Player player : Bukkit.getOnlinePlayers())
				{
					if(Zones.inNoDemigodsZone(player.getLocation())) continue;
					DemigodsPlayer.of(player).updateCanPvp();
				}

				// Update Battles
				Battle.Util.updateBattles();

				// Update Battle Particles
				Battle.Util.updateBattleParticles();
			}
		};
		ASYNC = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				// Update Timed Data
				DemigodsServer.TIMED.clearExpired();

				// Update Notifications
				NotificationManager.updateNotifications();
			}
		};
		SAVE = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				// Save time for reference after saving
				long time = System.currentTimeMillis();

				// Save data
				Demigods.getDataManager().save();

				// Send the save message to the console
				if(SAVE_ALERT) Messages.info(Bukkit.getOnlinePlayers().length + " of " + DemigodsServer.getAllPlayers().size() + " total players saved in " + Times.getSeconds(time) + " seconds.");
			}
		};
		FAVOR = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				// Update Favor
				DemigodsCharacter.updateFavor();

				// Update Sanctity
				StructureType.Util.updateSanctity();
			}
		};
	}

	public static void startThreads()
	{
		// Start sync demigods runnable
		Bukkit.getScheduler().scheduleSyncRepeatingTask(DemigodsPlugin.getInst(), SYNC, 20, 20);
		Administration.Util.sendDebug("Main Demigods SYNC runnable enabled...");

		// Start async demigods runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(DemigodsPlugin.getInst(), ASYNC, 20, 20);
		Administration.Util.sendDebug("Main Demigods ASYNC runnable enabled...");

		// Start favor runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(DemigodsPlugin.getInst(), FAVOR, 20, (Configs.getSettingInt("regeneration_rates.favor") * 20));
		Administration.Util.sendDebug("Favor regeneration runnable enabled...");

		// Start saving runnable TODO Should we move this?
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(DemigodsPlugin.getInst(), SAVE, 20, (Configs.getSettingInt("saving.freq") * 20));

		// Enable Deity runnables
		for(Deity deity : Demigods.getMythos().getDeities())
			for(Ability ability : deity.getAbilities())
				if(ability.getRunnable() != null) Bukkit.getScheduler().scheduleSyncRepeatingTask(DemigodsPlugin.getInst(), ability.getRunnable(), ability.getDelay(), ability.getRepeat());

		// Triggers
		Threads.registerTriggers(DemigodsPlugin.getInst(), Demigods.getMythos().getTriggers());
	}

	public static void stopThreads()
	{
		DemigodsPlugin.getInst().getServer().getScheduler().cancelTasks(DemigodsPlugin.getInst());
		Threads.stopHooker(DemigodsPlugin.getInst());
	}
}
