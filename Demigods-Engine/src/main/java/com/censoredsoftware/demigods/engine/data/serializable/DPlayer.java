package com.censoredsoftware.demigods.engine.data.serializable;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.censoredlib.data.location.Region;
import com.censoredsoftware.censoredlib.exception.MojangIdNotFoundException;
import com.censoredsoftware.censoredlib.helper.MojangIdGrabber;
import com.censoredsoftware.demigods.base.listener.ChatRecorder;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.Data;
import com.censoredsoftware.demigods.engine.language.English;
import com.censoredsoftware.demigods.engine.util.Configs;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class DPlayer implements ConfigurationSerializable
{
	private String mojangAccount;
	private String playerName;
	private String mortalName, mortalListName;
	private boolean canPvp;
	private long lastLoginTime, lastLogoutTime;
	private String currentDeityName;
	private int characterSlots;
	private UUID current;
	private UUID previous;
	private UUID mortalInventory, mortalEnderInventory;
	private ChatRecorder chatRecording;

	public DPlayer()
	{
		characterSlots = Configs.getSettingInt("character.default_character_slots");
	}

	public DPlayer(String mojangAccount, ConfigurationSection conf)
	{
		this.mojangAccount = mojangAccount;
		this.playerName = conf.getString("playerName");
		if(conf.isString("mortalName")) this.mortalName = conf.getString("mortalName");
		if(conf.isString("mortalListName")) this.mortalListName = conf.getString("mortalListName");
		if(conf.isBoolean("canPvp")) canPvp = conf.getBoolean("canPvp");
		if(conf.isLong("lastLoginTime")) lastLoginTime = conf.getLong("lastLoginTime");
		else lastLoginTime = -1;
		if(conf.isLong("lastLogoutTime")) lastLogoutTime = conf.getLong("lastLogoutTime");
		else lastLogoutTime = -1;
		if(conf.getString("currentDeityName") != null) currentDeityName = conf.getString("currentDeityName");
		if(conf.isInt("characterSlots")) characterSlots = conf.getInt("characterSlots");
		else characterSlots = Configs.getSettingInt("character.default_character_slots");
		if(conf.getString("current") != null) current = UUID.fromString(conf.getString("current"));
		if(conf.getString("previous") != null) previous = UUID.fromString(conf.getString("previous"));
		if(conf.getString("mortalInventory") != null) mortalInventory = UUID.fromString(conf.getString("mortalInventory"));
		if(conf.getString("mortalEnderInventory") != null) mortalEnderInventory = UUID.fromString(conf.getString("mortalEnderInventory"));
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<>();
		map.put("playerName", playerName);
		map.put("characterSlots", characterSlots);
		try
		{
			map.put("canPvp", canPvp);
			map.put("lastLoginTime", lastLoginTime);
			map.put("lastLogoutTime", lastLogoutTime);
		}
		catch(Exception ignored)
		{}
		if(mortalName != null) map.put("mortalName", mortalName);
		if(mortalListName != null) map.put("mortalListName", mortalListName);
		if(currentDeityName != null) map.put("currentDeityName", currentDeityName);
		if(current != null) map.put("current", current.toString());
		if(previous != null) map.put("previous", previous.toString());
		if(mortalInventory != null) map.put("mortalInventory", mortalInventory.toString());
		if(mortalEnderInventory != null) map.put("mortalEnderInventory", mortalEnderInventory.toString());
		return map;
	}

	public void setPlayerName(String player)
	{
		this.playerName = player;
	}

	void setMojangAccount(String account)
	{
		this.mojangAccount = account;
	}

	public void setMortalName(String name)
	{
		mortalName = name;
	}

	public String getMortalName()
	{
		return mortalName != null ? mortalName : playerName;
	}

	public void setMortalListName(String name)
	{
		mortalListName = name;
	}

	public String getMortalListName()
	{
		return mortalListName != null ? mortalListName : playerName;
	}

	public void resetCurrent()
	{
		this.current = null;
		this.currentDeityName = null;

		if(getOfflinePlayer().isOnline())
		{
			getOfflinePlayer().getPlayer().setDisplayName(getOfflinePlayer().getName());
			getOfflinePlayer().getPlayer().setPlayerListName(getOfflinePlayer().getName());
			getOfflinePlayer().getPlayer().setMaxHealth(20.0);
		}
	}

	public void setCanPvp(boolean pvp)
	{
		this.canPvp = pvp;
		Util.save(this);
	}

	public void updateCanPvp()
	{
		if(!getOfflinePlayer().isOnline()) return;

		// Define variables
		final Player player = getOfflinePlayer().getPlayer();
		final boolean inNoPvpZone = Zones.inNoPvpZone(player.getLocation());

		if(getCurrent() != null && Battle.Util.isInBattle(getCurrent())) return;

		if(!canPvp() && !inNoPvpZone)
		{
			setCanPvp(true);
			player.sendMessage(ChatColor.GRAY + English.UNSAFE_FROM_PVP.getLine());
		}
		else if(!inNoPvpZone)
		{
			setCanPvp(true);
			Data.TIMED.removeBool(player.getName() + "pvp_cooldown");
		}
		else if(canPvp() && !Data.TIMED.boolContainsKey(player.getName() + "pvp_cooldown"))
		{
			int delay = Configs.getSettingInt("zones.pvp_area_delay_time");
			Data.TIMED.setBool(player.getName() + "pvp_cooldown", true, delay, TimeUnit.SECONDS);

			Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.plugin(), new BukkitRunnable()
			{
				@Override
				public void run()
				{
					if(Zones.inNoPvpZone(player.getLocation()))
					{
						if(getCurrent() != null && Battle.Util.isInBattle(getCurrent())) return;
						setCanPvp(false);
						player.sendMessage(ChatColor.GRAY + English.SAFE_FROM_PVP.getLine());
					}
				}
			}, (delay * 20));
		}
	}

	public OfflinePlayer getOfflinePlayer()
	{
		return Bukkit.getOfflinePlayer(playerName);
	}

	public void setLastLoginTime(Long time)
	{
		this.lastLoginTime = time;
		Util.save(this);
	}

	public long getLastLoginTime()
	{
		return this.lastLoginTime;
	}

	public void setLastLogoutTime(long time)
	{
		this.lastLogoutTime = time;
		Util.save(this);
	}

	public long getLastLogoutTime()
	{
		return this.lastLogoutTime;
	}

	public void setCharacterSlots(int slots)
	{
		characterSlots = slots;
	}

	public void addCharacterSlot()
	{
		characterSlots += 1;
	}

	public void removeCharacterSlot()
	{
		characterSlots -= 1;
	}

	public int getCharacterSlots()
	{
		return characterSlots;
	}

	public void setToMortal()
	{
		Player player = getOfflinePlayer().getPlayer();
		saveCurrentCharacter();
		player.setMaxHealth(20.0);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setExp(0);
		player.setLevel(0);
		player.setGameMode(GameMode.SURVIVAL);
		for(PotionEffect potion : player.getActivePotionEffects())
			player.removePotionEffect(potion.getType());
		player.setDisplayName(getMortalName());
		player.setPlayerListName(getMortalListName());
		setMortalName(null);
		setMortalListName(null);
		applyMortalInventory();
		Demigods.BOARD.getTeam("Mortal").addPlayer(getOfflinePlayer());
	}

	public void saveMortalInventory(Player player)
	{
		// Player inventory
		DCharacter.Inventory mortalInventory = new DCharacter.Inventory();
		PlayerInventory inventory = player.getInventory();
		mortalInventory.generateId();
		if(inventory.getHelmet() != null) mortalInventory.setHelmet(inventory.getHelmet());
		if(inventory.getChestplate() != null) mortalInventory.setChestplate(inventory.getChestplate());
		if(inventory.getLeggings() != null) mortalInventory.setLeggings(inventory.getLeggings());
		if(inventory.getBoots() != null) mortalInventory.setBoots(inventory.getBoots());
		mortalInventory.setItems(inventory);
		DCharacter.Util.saveInventory(mortalInventory);
		this.mortalInventory = mortalInventory.getId();

		// Enderchest
		DCharacter.EnderInventory enderInventory = new DCharacter.EnderInventory();
		Inventory enderChest = player.getEnderChest();
		enderInventory.generateId();
		enderInventory.setItems(enderChest);
		DCharacter.Util.saveInventory(enderInventory);
		this.mortalEnderInventory = enderInventory.getId();

		Util.save(this);
	}

	public void saveCurrentCharacter()
	{
		// Update the current character
		final Player player = getOfflinePlayer().getPlayer();
		final DCharacter character = getCurrent();

		if(character != null)
		{
			// Set to inactive and update previous
			character.setActive(false);
			this.previous = character.getId();

			// Set the values
			character.setHealth(player.getHealth() >= character.getMaxHealth() ? character.getMaxHealth() : player.getHealth());
			character.setHunger(player.getFoodLevel());
			character.setLevel(player.getLevel());
			character.setExperience(player.getExp());
			character.setLocation(player.getLocation());
			Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.plugin(), new BukkitRunnable()
			{
				@Override
				public void run()
				{
					if(player.getBedSpawnLocation() != null) character.setBedSpawn(player.getBedSpawnLocation());
				}
			}, 1);
			character.setGameMode(player.getGameMode());
			character.setPotionEffects(player.getActivePotionEffects());
			character.saveInventory();

			// Disown pets
			DPet.Util.disownPets(character.getName());

			// Remove from their team
			Demigods.BOARD.getTeam(getCurrent().getAlliance().getName()).removePlayer(getOfflinePlayer());

			// Save it
			DCharacter.Util.save(character);
		}
	}

	public void switchCharacter(final DCharacter newChar)
	{
		final Player player = getOfflinePlayer().getPlayer();

		if(!newChar.getPlayerName().equals(this.playerName))
		{
			player.sendMessage(ChatColor.RED + "You can't do that.");
			return;
		}

		// Save the current character
		saveCurrentCharacter();

		// Set new character to active and other info
		this.current = newChar.getId();
		currentDeityName = newChar.getDeity().getName();

		// Apply the new character
		newChar.applyToPlayer(player);

		// Teleport them
		try
		{
			player.teleport(newChar.getLocation());
		}
		catch(Exception e)
		{
			Messages.warning("There was a problem while teleporting a player to their character.");
		}

		// Save instances
		Util.save(this);
		DCharacter.Util.save(newChar);
	}

	public boolean canPvp()
	{
		return this.canPvp;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public String getMojangAccount()
	{
		return mojangAccount;
	}

	public String getCurrentDeityName()
	{
		return currentDeityName;
	}

	public Region getRegion()
	{
		if(getOfflinePlayer().isOnline()) return Region.Util.getRegion(getOfflinePlayer().getPlayer().getLocation());
		return Region.Util.getRegion(getCurrent().getLocation());
	}

	public boolean hasCurrent()
	{
		return getCurrent() != null;
	}

	public DCharacter getCurrent()
	{
		if(this.current == null) return null;
		DCharacter character = DCharacter.Util.load(this.current);
		if(character != null && character.isUsable()) return character;
		return null;
	}

	public DCharacter getPrevious()
	{
		if(this.previous == null) return null;
		return DCharacter.Util.load(this.previous);
	}

	public Set<DCharacter> getCharacters()
	{
		return Sets.newHashSet(Collections2.filter(DCharacter.Util.loadAll(), new Predicate<DCharacter>()
		{
			@Override
			public boolean apply(DCharacter character)
			{
				return character != null && character.getMojangAccount().equals(mojangAccount) && character.isUsable();
			}
		}));
	}

	public Set<DCharacter> getUsableCharacters()
	{
		return Sets.filter(getCharacters(), new Predicate<DCharacter>()
		{
			@Override
			public boolean apply(DCharacter character)
			{
				return character.isUsable();
			}
		});
	}

	public DCharacter.Inventory getMortalInventory()
	{
		return DCharacter.Util.getInventory(mortalInventory);
	}

	public DCharacter.EnderInventory getMortalEnderInventory()
	{
		return DCharacter.Util.getEnderInventory(mortalEnderInventory);
	}

	public void applyMortalInventory()
	{
		if(getMortalInventory() == null) mortalInventory = DCharacter.Util.createEmptyInventory().getId();
		if(getMortalEnderInventory() == null) mortalEnderInventory = DCharacter.Util.createEmptyEnderInventory().getId();
		getMortalInventory().setToPlayer(getOfflinePlayer().getPlayer());
		getMortalEnderInventory().setToPlayer(getOfflinePlayer().getPlayer());
		mortalInventory = null;
		mortalEnderInventory = null;
	}

	public boolean canMakeCharacter()
	{
		return getUsableCharacters().size() < getCharacterSlots();
	}

	public boolean canUseCurrent()
	{
		if(getCurrent() == null || !getCurrent().isUsable())
		{
			getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED + "Your current character was unable to init!");
			getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED + "Please contact the server administrator immediately.");
			return false;
		}
		else return getOfflinePlayer().isOnline();
	}

	public void remove()
	{
		// First we need to kick the player if they're online
		if(getOfflinePlayer().isOnline()) getOfflinePlayer().getPlayer().kickPlayer(ChatColor.RED + "Your player save has been cleared.");

		// Remove characters
		for(DCharacter character : getCharacters())
			character.remove();

		// Now we clear the DPlayer save itself
		Util.delete(getMojangAccount());
	}

	/**
	 * Starts recording recording the <code>player</code>'s chat.
	 */
	public void startRecording()
	{
		chatRecording = ChatRecorder.Util.startRecording(getOfflinePlayer().getPlayer());
	}

	/**
	 * Stops recording and sends all messages that have been recorded thus far to the player.
	 * 
	 * @param display if true, the chat will be sent to the player
	 */
	public List<String> stopRecording(boolean display)
	{
		Player player = getOfflinePlayer().getPlayer();
		// Handle recorded chat
		if(chatRecording != null && chatRecording.isRecording())
		{
			// Send held back chat
			List<String> messages = chatRecording.stop();
			if(messages.size() > 0 && display)
			{
				player.sendMessage(" ");
				if(messages.size() == 1)
				{
					player.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + English.HELD_BACK_MESSAGE.getLine());
				}
				else
				{
					player.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + English.HELD_BACK_MESSAGES.getLine().replace("{size}", "" + messages.size()));
				}
				for(String message : messages)
					player.sendMessage(message);
			}

			return messages;
		}
		return null;
	}

	public static class Util
	{
		public static DPlayer create(final OfflinePlayer player)
		{
			DPlayer playerSave = new DPlayer();
			playerSave.setMojangAccount(MojangIdGrabber.getUUID(player));
			playerSave.setPlayerName(player.getName());
			playerSave.setLastLoginTime(player.getLastPlayed());
			playerSave.setCanPvp(true);
			Util.save(playerSave);
			return playerSave;
		}

		public static void save(DPlayer player)
		{
			Data.PLAYER.put(player.getMojangAccount(), player);
		}

		public static void delete(String mojangAccount)
		{
			Data.PLAYER.remove(mojangAccount);
		}

		public static DPlayer getPlayer(final OfflinePlayer player)
		{
			String id = MojangIdGrabber.getUUID(player);
			if(id == null) throw new MojangIdNotFoundException(player.getName());
			DPlayer found = getPlayer(id);
			if(found == null) return create(player);
			return found;
		}

		public static DPlayer getPlayerFromName(final String playerName)
		{
			try
			{
				return Iterables.find(Data.PLAYER.values(), new Predicate<DPlayer>()
				{
					@Override
					public boolean apply(DPlayer dPlayer)
					{
						return dPlayer.getPlayerName().equals(playerName);
					}
				});
			}
			catch(NoSuchElementException ignored)
			{}
			return null;
		}

		public static DPlayer getPlayer(String mojangAccount)
		{
			if(Data.PLAYER.containsKey(mojangAccount)) return Data.PLAYER.get(mojangAccount);
			return null;
		}

		/**
		 * Returns true if the <code>player</code> is currently immortal.
		 * 
		 * @param player the player to check.
		 * @return boolean
		 */
		public static boolean isImmortal(Player player)
		{
			DCharacter character = getPlayer(player).getCurrent();
			return character != null && character.isUsable() && character.isActive();
		}

		public static Collection<OfflinePlayer> getMortals()
		{
			return Collections2.transform(Collections2.filter(Data.PLAYER.values(), new Predicate<DPlayer>()
			{
				@Override
				public boolean apply(DPlayer player)
				{
					DCharacter character = player.getCurrent();
					return character == null || !character.isUsable() || !character.isActive();
				}
			}), new Function<DPlayer, OfflinePlayer>()
			{
				@Override
				public OfflinePlayer apply(DPlayer player)
				{
					return player.getOfflinePlayer();
				}
			});
		}

		public static Set<Player> getOnlineMortals()
		{
			return Sets.filter(Sets.newHashSet(Bukkit.getOnlinePlayers()), new Predicate<Player>()
			{
				@Override
				public boolean apply(Player player)
				{
					DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
					return character == null || !character.isUsable() || !character.isActive();
				}
			});
		}

		/**
		 * Returns true if <code>player</code> has a character with the name <code>charName</code>.
		 * 
		 * @param player the player to check.
		 * @param charName the charName to check with.
		 * @return boolean
		 */
		public static boolean hasCharName(Player player, String charName)
		{
			for(DCharacter character : getPlayer(player).getCharacters())
				if(character.getName().equalsIgnoreCase(charName)) return true;
			return false;
		}
	}
}
