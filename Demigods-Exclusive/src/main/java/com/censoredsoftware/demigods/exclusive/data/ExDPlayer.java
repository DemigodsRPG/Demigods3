package com.censoredsoftware.demigods.exclusive.data;

import com.censoredsoftware.censoredlib.exception.MojangIdNotFoundException;
import com.censoredsoftware.censoredlib.helper.MojangIdGrabber;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Data;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class ExDPlayer implements ConfigurationSerializable
{
	private String mojangAccount;

	public ExDPlayer()
	{}

	public ExDPlayer(String mojangAccount, ConfigurationSection conf)
	{
		this.mojangAccount = mojangAccount;
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<>();
		// TODO
		return map;
	}

	void setMojangAccount(String account)
	{
		this.mojangAccount = account;
	}

	public DPlayer getDPlayer()
	{
		return Data.PLAYER.get(mojangAccount);
	}

	public void remove()
	{
		DPlayer dPlayer = getDPlayer();

		// First we need to kick the player if they're online
		if(dPlayer.getOfflinePlayer().isOnline()) dPlayer.getOfflinePlayer().getPlayer().kickPlayer(ChatColor.RED + "Your player save has been cleared.");

		// Now we clear the DPlayer save itself
		Util.delete(mojangAccount);
	}

	public static class Util
	{
		private Util()
		{}

		public static ExDPlayer create(final OfflinePlayer player)
		{
			ExDPlayer playerSave = new ExDPlayer();
			playerSave.setMojangAccount(MojangIdGrabber.getUUID(player));
			Util.save(playerSave);
			return playerSave;
		}

		public static void save(ExDPlayer player)
		{
			ExData.EXCLUSIVE_PLAYER.put(player.mojangAccount, player);
		}

		public static void delete(String mojangAccount)
		{
			ExData.EXCLUSIVE_PLAYER.remove(mojangAccount);
		}

		public static ExDPlayer getPlayer(String mojangAccount)
		{
			if(ExData.EXCLUSIVE_PLAYER.containsKey(mojangAccount)) return ExData.EXCLUSIVE_PLAYER.get(mojangAccount);
			return null;
		}

		public static ExDPlayer getPlayer(final OfflinePlayer player)
		{
			String id = MojangIdGrabber.getUUID(player);
			if(id == null) throw new MojangIdNotFoundException(player.getName());
			ExDPlayer found = getPlayer(id);
			if(found == null) return create(player);
			return found;
		}
	}
}