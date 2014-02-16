package com.demigodsrpg.demigods.engine.entity.player;

import com.censoredsoftware.library.exception.MojangIdNotFoundException;
import com.censoredsoftware.library.helper.MojangIdGrabber;
import com.demigodsrpg.demigods.base.listener.ChatRecorder;
import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.battle.Battle;
import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.data.IdType;
import com.demigodsrpg.demigods.engine.data.Register;
import com.demigodsrpg.demigods.engine.data.TimedServerData;
import com.demigodsrpg.demigods.engine.entity.DemigodsTameable;
import com.demigodsrpg.demigods.engine.inventory.DemigodsEnderInventory;
import com.demigodsrpg.demigods.engine.inventory.DemigodsPlayerInventory;
import com.demigodsrpg.demigods.engine.language.English;
import com.demigodsrpg.demigods.engine.location.DemigodsRegion;
import com.demigodsrpg.demigods.engine.util.Configs;
import com.demigodsrpg.demigods.engine.util.Messages;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class DemigodsPlayer extends DataAccess<String, DemigodsPlayer>
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

	public DemigodsPlayer()
	{
		characterSlots = Configs.getSettingInt("character.default_character_slots");
	}

	@Register(idType = IdType.STRING)
	public DemigodsPlayer(String mojangAccount, ConfigurationSection conf)
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

		if(getBukkitOfflinePlayer().isOnline())
		{
			getBukkitOfflinePlayer().getPlayer().setDisplayName(getBukkitOfflinePlayer().getName());
			getBukkitOfflinePlayer().getPlayer().setPlayerListName(getBukkitOfflinePlayer().getName());
			getBukkitOfflinePlayer().getPlayer().setMaxHealth(20.0);
		}
	}

	public void setCanPvp(boolean pvp)
	{
		this.canPvp = pvp;
		save();
	}

	public void updateCanPvp()
	{
		if(!getBukkitOfflinePlayer().isOnline()) return;

		// Define variables
		final Player player = getBukkitOfflinePlayer().getPlayer();
		final boolean inNoPvpZone = Zones.inNoPvpZone(player.getLocation());

		if(getCharacter() != null && Battle.isInBattle(getCharacter())) return;

		if(!canPvp() && !inNoPvpZone)
		{
			setCanPvp(true);
			player.sendMessage(ChatColor.GRAY + English.UNSAFE_FROM_PVP.getLine());
		}
		else if(!inNoPvpZone)
		{
			setCanPvp(true);
			TimedServerData.remove(player.getName(), "pvp_cooldown");
		}
		else if(canPvp() && !TimedServerData.exists(player.getName(), "pvp_cooldown"))
		{
			int delay = Configs.getSettingInt("zones.pvp_area_delay_time");
			TimedServerData.saveTimed(player.getName(), "pvp_cooldown", true, delay, TimeUnit.SECONDS);

			Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new BukkitRunnable()
			{
				@Override
				public void run()
				{
					if(Zones.inNoPvpZone(player.getLocation()))
					{
						if(getCharacter() != null && Battle.isInBattle(getCharacter())) return;
						setCanPvp(false);
						player.sendMessage(ChatColor.GRAY + English.SAFE_FROM_PVP.getLine());
					}
				}
			}, (delay * 20));
		}
	}

	public OfflinePlayer getBukkitOfflinePlayer()
	{
		return Bukkit.getOfflinePlayer(playerName);
	}

	public void setLastLoginTime(Long time)
	{
		this.lastLoginTime = time;
		save();
	}

	public long getLastLoginTime()
	{
		return this.lastLoginTime;
	}

	public void setLastLogoutTime(long time)
	{
		this.lastLogoutTime = time;
		save();
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
		Player player = getBukkitOfflinePlayer().getPlayer();
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
	}

	public void saveMortalInventory(Player player)
	{
		// Player inventory
		DemigodsPlayerInventory mortalInventory = new DemigodsPlayerInventory();
		PlayerInventory inventory = player.getInventory();
		mortalInventory.generateId();
		if(inventory.getHelmet() != null) mortalInventory.setHelmet(inventory.getHelmet());
		if(inventory.getChestplate() != null) mortalInventory.setChestplate(inventory.getChestplate());
		if(inventory.getLeggings() != null) mortalInventory.setLeggings(inventory.getLeggings());
		if(inventory.getBoots() != null) mortalInventory.setBoots(inventory.getBoots());
		mortalInventory.setItems(inventory);
		mortalInventory.save();
		this.mortalInventory = mortalInventory.getId();

		// Enderchest
		DemigodsEnderInventory enderInventory = new DemigodsEnderInventory();
		Inventory enderChest = player.getEnderChest();
		enderInventory.generateId();
		enderInventory.setItems(enderChest);
		enderInventory.save();
		this.mortalEnderInventory = enderInventory.getId();

		save();
	}

	public void saveCurrentCharacter()
	{
		// Update the current character
		final Player player = getBukkitOfflinePlayer().getPlayer();
		final DemigodsCharacter character = getCharacter();

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
			Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new BukkitRunnable()
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
			DemigodsTameable.disownPets(character.getName());

			// Save it
			character.save();
		}
	}

	public void switchCharacter(final DemigodsCharacter newChar)
	{
		final Player player = getBukkitOfflinePlayer().getPlayer();

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
		save();
		newChar.save();
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

	public DemigodsRegion getRegion()
	{
		if(getBukkitOfflinePlayer().isOnline()) return DemigodsRegion.at(getBukkitOfflinePlayer().getPlayer().getLocation());
		return DemigodsRegion.at(getCharacter().getLocation());
	}

	public boolean isACharacter()
	{
		return getCharacter() != null;
	}

	public DemigodsCharacter getCharacter()
	{
		if(current == null) return null;
		DemigodsCharacter character = DemigodsCharacter.get(current);
		if(character != null && character.isUsable()) return character;
		return null;
	}

	public DemigodsCharacter getPreviousCharacter()
	{
		if(previous == null) return null;
		return DemigodsCharacter.get(previous);
	}

	public Set<DemigodsCharacter> getCharacters()
	{
		return Sets.newHashSet(Collections2.filter(DemigodsCharacter.all(), new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return character != null && character.getMojangAccount().equals(mojangAccount) && character.isUsable();
			}
		}));
	}

	public Set<DemigodsCharacter> getUsableCharacters()
	{
		return Sets.filter(getCharacters(), new Predicate<DemigodsCharacter>()
		{
			@Override
			public boolean apply(DemigodsCharacter character)
			{
				return character.isUsable();
			}
		});
	}

	public DemigodsPlayerInventory getMortalInventory()
	{
		return DemigodsPlayerInventory.get(mortalInventory);
	}

	public DemigodsEnderInventory getMortalEnderInventory()
	{
		return DemigodsEnderInventory.get(mortalEnderInventory);
	}

	public void applyMortalInventory()
	{
		if(getMortalInventory() == null) mortalInventory = DemigodsPlayerInventory.createEmpty().getId();
		if(getMortalEnderInventory() == null) mortalEnderInventory = DemigodsEnderInventory.createEmpty().getId();
		getMortalInventory().setToPlayer(getBukkitOfflinePlayer().getPlayer());
		getMortalEnderInventory().setToPlayer(getBukkitOfflinePlayer().getPlayer());
		mortalInventory = null;
		mortalEnderInventory = null;
	}

	public boolean canMakeCharacter()
	{
		return getUsableCharacters().size() < getCharacterSlots();
	}

	public boolean canUseCurrent()
	{
		if(getCharacter() == null || !getCharacter().isUsable())
		{
			getBukkitOfflinePlayer().getPlayer().sendMessage(ChatColor.RED + "Your current character was unable to init!");
			getBukkitOfflinePlayer().getPlayer().sendMessage(ChatColor.RED + "Please contact the server administrator immediately.");
			return false;
		}
		else return getBukkitOfflinePlayer().isOnline();
	}

	public void remove()
	{
		// First we need to kick the player if they're online
		if(getBukkitOfflinePlayer().isOnline()) getBukkitOfflinePlayer().getPlayer().kickPlayer(ChatColor.RED + "Your player save has been cleared.");

		// Remove characters
		for(DemigodsCharacter character : getCharacters())
			character.remove();

		// Now we clear the DemigodsPlayer save itself
		super.remove();
	}

	@Override
	protected String getId()
	{
		return getMojangAccount();
	}

	/**
	 * Starts recording recording the <code>player</code>'s chat.
	 */
	public void startRecording()
	{
		chatRecording = ChatRecorder.Util.startRecording(getBukkitOfflinePlayer().getPlayer());
	}

	/**
	 * Stops recording and sends all messages that have been recorded thus far to the player.
	 * 
	 * @param display if true, the chat will be sent to the player
	 */
	public List<String> stopRecording(boolean display)
	{
		Player player = getBukkitOfflinePlayer().getPlayer();
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

	public static DemigodsPlayer create(final OfflinePlayer player)
	{
		DemigodsPlayer playerSave = new DemigodsPlayer();
		playerSave.setMojangAccount(MojangIdGrabber.getUUID(player));
		playerSave.setPlayerName(player.getName());
		playerSave.setLastLoginTime(player.getLastPlayed());
		playerSave.setCanPvp(true);
		playerSave.save();

		// Log the creation
		Messages.info(English.LOG_PLAYER_CREATED.getLine().replace("{player}", player.getName()).replace("{id}", MojangIdGrabber.getUUID(player)));

		return playerSave;
	}

	public static DemigodsPlayer of(final OfflinePlayer player)
	{
		String id = MojangIdGrabber.getUUID(player);
		if(id == null) throw new MojangIdNotFoundException(player.getName());
		DemigodsPlayer found = get(id);
		if(found == null) return create(player);
		return found;
	}

	public static DemigodsPlayer getFromName(final String playerName)
	{
		try
		{
			return Iterables.find(all(), new Predicate<DemigodsPlayer>()
			{
				@Override
				public boolean apply(DemigodsPlayer demigodsPlayer)
				{
					return demigodsPlayer.getPlayerName().equals(playerName);
				}
			});
		}
		catch(NoSuchElementException ignored)
		{}
		return null;
	}

	private static final DataAccess<String, DemigodsPlayer> DATA_ACCESS = new DemigodsPlayer();

	public static DemigodsPlayer get(String mojangAccount)
	{
		return DATA_ACCESS.getDirect(mojangAccount);
	}

	public static Collection<DemigodsPlayer> all()
	{
		return DATA_ACCESS.allDirect();
	}

	/**
	 * Returns true if the <code>player</code> is currently immortal.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean isImmortal(Player player)
	{
		DemigodsCharacter character = of(player).getCharacter();
		return character != null && character.isUsable() && character.isActive();
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
		for(DemigodsCharacter character : of(player).getCharacters())
			if(character.getName().equalsIgnoreCase(charName)) return true;
		return false;
	}
}
