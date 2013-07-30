package com.censoredsoftware.camcorder;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CamcorderPlayback extends BukkitRunnable
{
	static Player playback;

	protected static Location start;
	protected static String name;
	protected static Long startPlaybackTime;
	protected static Long endTime;
	protected static Integer count;

	public CamcorderPlayback(CamcorderPlugin plugin)
	{
		count = 0;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 1);
	}

	@Override
	public void run()
	{
		if(playback == null) return;
		if(System.currentTimeMillis() >= startPlaybackTime + endTime)
		{
			count = 0;
			playback.sendMessage(ChatColor.RED + "Done.");
			playback = null;
			return;
		}
		count++;
		try
		{
			Location result = getLocation(playback, count);
			if(result == null) return;
			playback.teleport(result);
		}
		catch(Exception ignored)
		{}
	}

	private static Location getLocation(Player player, long time)
	{
		String prefix = player.getName() + "." + name + "." + time + ".";
		double X = CamcorderPlugin.plugin.getConfig().getDouble(prefix + "X");
		double Y = CamcorderPlugin.plugin.getConfig().getDouble(prefix + "Y");
		double Z = CamcorderPlugin.plugin.getConfig().getDouble(prefix + "Z");
		float pitch = CamcorderPlugin.plugin.getConfig().getInt(prefix + "pitch");
		float yaw = CamcorderPlugin.plugin.getConfig().getInt(prefix + "yaw");
		Location result = new Location(player.getWorld(), X, Y, Z, yaw, pitch);
		if(result.distance(start) >= 1) return result;
		return null;
	}
}
