package com.censoredsoftware.demigods.ability;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import com.censoredsoftware.core.bukkit.ConfigFile;
import com.censoredsoftware.core.util.Randoms;
import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.Elements;
import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.language.Translation;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DItemStack;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.player.Pet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public interface Ability
{
	public static final int TARGET_OFFSET = 5;

	public String getDeity();

	public String getName();

	public String getCommand();

	public String getPermission();

	public int getCost();

	public int getDelay();

	public int getRepeat();

	public List<String> getDetails();

	public Devotion.Type getType();

	public Material getWeapon();

	public boolean hasWeapon();

	public Listener getListener();

	public BukkitRunnable getRunnable();

	public static class Bind implements ConfigurationSerializable
	{
		private UUID id;
		private String identifier;
		private String ability;
		private Integer slot;
		private UUID item;

		public Bind()
		{}

		public Bind(UUID id, ConfigurationSection conf)
		{
			this.id = id;
			identifier = conf.getString("identifier");
			ability = conf.getString("ability");
			slot = conf.getInt("slot");
			item = UUID.fromString(conf.getString("item"));
		}

		@Override
		public Map<String, Object> serialize()
		{
			return new HashMap<String, Object>()
			{
				{
					put("identifier", identifier);
					put("ability", ability);
					put("slot", slot);
					put("item", item.toString());
				}
			};
		}

		public void generateId()
		{
			id = UUID.randomUUID();
		}

		void setIdentifier(String identifier)
		{
			this.identifier = identifier;
		}

		void setAbility(String ability)
		{
			this.ability = ability;
		}

		void setSlot(Integer slot)
		{
			this.slot = slot;
		}

		public void setItem(ItemStack item)
		{
			this.item = DItemStack.Util.create(item).getId();
			Util.save(this);
		}

		public UUID getId()
		{
			return this.id;
		}

		public ItemStack getRawItem()
		{
			return new ItemStack(DItemStack.Util.load(this.item).toItemStack().getType());
		}

		public ItemStack getItem()
		{
			return DItemStack.Util.load(this.item).toItemStack();
		}

		public String getAbility()
		{
			return this.ability;
		}

		public String getIdentifier()
		{
			return this.identifier;
		}

		public int getSlot()
		{
			return this.slot;
		}

		public static class File extends ConfigFile
		{
			private static String SAVE_PATH;
			private static final String SAVE_FILE = "binds.yml";

			public File()
			{
				super(Demigods.plugin);
				SAVE_PATH = Demigods.plugin.getDataFolder() + "/data/";
			}

			@Override
			public Map<UUID, Bind> loadFromFile()
			{
				final FileConfiguration data = getData(SAVE_PATH, SAVE_FILE);
				return new HashMap<UUID, Bind>()
				{
					{
						for(String stringId : data.getKeys(false))
							put(UUID.fromString(stringId), new Bind(UUID.fromString(stringId), data.getConfigurationSection(stringId)));
					}
				};
			}

			@Override
			public boolean saveToFile()
			{
				FileConfiguration saveFile = getData(SAVE_PATH, SAVE_FILE);
				Map<UUID, Bind> currentFile = loadFromFile();

				for(UUID id : DataManager.binds.keySet())
					if(!currentFile.keySet().contains(id) || !currentFile.get(id).equals(DataManager.binds.get(id))) saveFile.createSection(id.toString(), Util.loadBind(id).serialize());

				for(UUID id : currentFile.keySet())
					if(!DataManager.binds.keySet().contains(id)) saveFile.set(id.toString(), null);

				return saveFile(SAVE_PATH, SAVE_FILE, saveFile);
			}
		}
	}

	public static class Devotion implements ConfigurationSerializable
	{
		private UUID id;
		private String type;
		private Integer exp;
		private Integer level;

		public enum Type
		{
			OFFENSE, DEFENSE, STEALTH, SUPPORT, PASSIVE, ULTIMATE
		}

		public Devotion()
		{}

		public Devotion(UUID id, ConfigurationSection conf)
		{
			this.id = id;
			type = conf.getString("type");
			exp = conf.getInt("exp");
			level = conf.getInt("level");
		}

		@Override
		public Map<String, Object> serialize()
		{
			return new HashMap<String, Object>()
			{
				{
					put("type", type);
					put("exp", exp);
					put("level", level);
				}
			};
		}

		public void generateId()
		{
			id = UUID.randomUUID();
		}

		void setType(Type type)
		{
			this.type = type.toString();
		}

		void setExp(Integer exp)
		{
			this.exp = exp;
		}

		void setLevel(Integer level)
		{
			this.level = level;
		}

		public UUID getId()
		{
			return id;
		}

		public Type getType()
		{
			return Type.valueOf(this.type);
		}

		public Integer getLevel()
		{
			return this.level;
		}

		public Integer getExp()
		{
			return this.exp;
		}

		public Integer getExpGoal()
		{
			return (int) Math.ceil(500 * Math.pow(this.level + 1, 2.02)); // TODO: Will need to be tweaked and will possibly be different for each Type.
		}

		@Override
		public Object clone() throws CloneNotSupportedException
		{
			throw new CloneNotSupportedException();
		}

		public static class File extends ConfigFile
		{
			private static String SAVE_PATH;
			private static final String SAVE_FILE = "devotion.yml";

			public File()
			{
				super(Demigods.plugin);
				SAVE_PATH = Demigods.plugin.getDataFolder() + "/data/";
			}

			@Override
			public Map<UUID, Devotion> loadFromFile()
			{
				final FileConfiguration data = getData(SAVE_PATH, SAVE_FILE);
				return new HashMap<UUID, Devotion>()
				{
					{
						for(String stringId : data.getKeys(false))
							put(UUID.fromString(stringId), new Devotion(UUID.fromString(stringId), data.getConfigurationSection(stringId)));
					}
				};
			}

			@Override
			public boolean saveToFile()
			{
				FileConfiguration saveFile = getData(SAVE_PATH, SAVE_FILE);
				Map<UUID, Devotion> currentFile = loadFromFile();

				for(UUID id : DataManager.devotion.keySet())
					if(!currentFile.keySet().contains(id) || !currentFile.get(id).equals(DataManager.devotion.get(id))) saveFile.createSection(id.toString(), Util.loadDevotion(id).serialize());

				for(UUID id : currentFile.keySet())
					if(!DataManager.devotion.keySet().contains(id)) saveFile.set(id.toString(), null);

				return saveFile(SAVE_PATH, SAVE_FILE, saveFile);
			}
		}
	}

	public static class Util
	{
		public static void deleteBind(UUID id)
		{
			DataManager.binds.remove(id);
		}

		public static Bind createBind(String ability, int slot)
		{
			Bind bind = new Bind();
			bind.generateId();
			bind.setIdentifier(Randoms.generateString(6));
			bind.setAbility(ability);
			bind.setSlot(slot);
			save(bind);
			return bind;
		}

		public static Bind createBind(String ability, int slot, ItemStack item)
		{
			Bind bind = new Bind();
			bind.generateId();
			bind.setIdentifier(Randoms.generateString(6));
			bind.setAbility(ability);
			bind.setSlot(slot);
			bind.setItem(item);
			save(bind);
			return bind;
		}

		public static Devotion createDevotion(Devotion.Type type)
		{
			Devotion devotion = new Devotion();
			devotion.generateId();
			devotion.setType(type);
			devotion.setLevel(Demigods.config.getSettingInt("character.defaults." + type.name().toLowerCase()));
			save(devotion);
			return devotion;
		}

		public static void save(Bind bind)
		{
			DataManager.binds.put(bind.getId(), bind);
		}

		public static void save(Devotion devotion)
		{
			DataManager.devotion.put(devotion.getId(), devotion);
		}

		public static Bind loadBind(UUID id)
		{
			return DataManager.binds.get(id);
		}

		public static Devotion loadDevotion(UUID id)
		{
			return DataManager.devotion.get(id);
		}

		private static boolean doAbilityPreProcess(Player player, int cost)
		{
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

			if(!Battle.Util.canTarget(character))
			{
				player.sendMessage(ChatColor.YELLOW + "You can't do that from a no-PVP zone.");
				return false;
			}
			else if(character.getMeta().getFavor() < cost)
			{
				player.sendMessage(ChatColor.YELLOW + "You do not have enough favor.");
				return false;
			}
			else return true;
		}

		/**
		 * Returns true if the ability for <code>player</code>, called <code>name</code>,
		 * with a cost of <code>cost</code>, that is Type <code>type</code>, has
		 * passed all pre-process tests.
		 * 
		 * @param player the player doing the ability
		 * @param cost the cost (in favor) of the ability
		 * @return true/false depending on if all pre-process tests have passed
		 */
		public static boolean doAbilityPreProcess(Player player, int cost, Devotion.Type type)
		{
			// DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			return doAbilityPreProcess(player, cost); // TODO callAbilityEvent(name, character, cost, info);
		}

		/**
		 * Returns true if the ability for <code>player</code>, called <code>name</code>,
		 * with a cost of <code>cost</code>, that is Type <code>type</code>, that
		 * is doTargeting the LivingEntity <code>target</code>, has passed all pre-process tests.
		 * 
		 * @param player the Player doing the ability
		 * @param target the LivingEntity being targeted
		 * @param cost the cost (in favor) of the ability
		 * @return true/false depending on if all pre-process tests have passed
		 */
		public static boolean doAbilityPreProcess(Player player, LivingEntity target, int cost, Devotion.Type type)
		{
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

			if(doAbilityPreProcess(player, cost)) // TODO callAbilityEvent(name, character, cost, info))
			{
				if(target == null)
				{
					player.sendMessage(ChatColor.YELLOW + "No target found.");
					return false;
				}
				else if(Battle.Util.canParticipate(target) && !Battle.Util.canTarget(Battle.Util.defineParticipant(target)))
				{
					player.sendMessage(ChatColor.YELLOW + "Target is in a no-PVP zone.");
					return false;
				}
				else if(target instanceof Player)
				{
					DCharacter attacked = DPlayer.Util.getPlayer(((Player) target)).getCurrent();
					if(attacked != null && DCharacter.Util.areAllied(character, attacked)) return false;
				}
				else if(target instanceof Tameable)
				{
					Pet attacked = Pet.Util.getTameable(target);
					if(attacked != null && DCharacter.Util.areAllied(character, attacked.getOwner())) return false;
				}
				// TODO Bukkit.getServer().getPluginManager().callEvent(new AbilityTargetEvent(character, target, info));
				return true;
			}
			return false;
		}

		/**
		 * Returns true if the callAbilityEvent <code>callAbilityEvent</code> is caused by a left click.
		 * 
		 * @param event the interact callAbilityEvent
		 * @return true/false depending on if the callAbilityEvent is caused by a left click or not
		 */
		public static boolean isLeftClick(PlayerInteractEvent event)
		{
			Action action = event.getAction();
			return action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
		}

		/**
		 * Returns the LivingEntity that <code>player</code> is doTargeting.
		 * 
		 * @param player the interact callAbilityEvent
		 * @return the targeted LivingEntity
		 */
		public static LivingEntity autoTarget(Player player)
		{
			// TODO: Test this for lag.

			// Define variables
			int range = Demigods.config.getSettingInt("caps.target_range") > 140 ? 140 : Demigods.config.getSettingInt("caps.target_range");
			int correction = 3;
			Location target = player.getTargetBlock(null, range).getLocation();
			BlockIterator iterator = new BlockIterator(player, range);
			Set<LivingEntity> entities = Sets.newHashSet();
			List<LivingEntity> targets = Lists.newArrayList();

			// Save the nearby living entities
			for(Entity entity : player.getNearbyEntities(range, range, range))
			{
				if(entity instanceof LivingEntity) entities.add((LivingEntity) entity);
			}

			// Iterate through the blocks and find the target
			while(iterator.hasNext())
			{
				Block block = iterator.next();

				for(LivingEntity entity : entities)
				{
					if(entity.getLocation().distance(block.getLocation()) <= correction)
					{
						if(entity instanceof Tameable && ((Tameable) entity).isTamed() && Pet.Util.getTameable(entity) != null)
						{
							Pet wrapper = Pet.Util.getTameable(entity);
							if(DCharacter.Util.areAllied(DPlayer.Util.getPlayer(player).getCurrent(), wrapper.getOwner())) continue;
						}
						else
						{
							targets.add(entity);
						}
					}
				}
			}

			// Attempt to return the closest entity to the cursor
			for(LivingEntity entity : targets)
			{
				if(entity.getLocation().distance(target) <= correction) return entity;
			}

			// If it failed to do that then just return the first entity
			try
			{
				return targets.get(0);
			}
			catch(Exception ignored)
			{}

			return null;
		}

		public static Location directTarget(Player player)
		{
			return player.getTargetBlock(null, Demigods.config.getSettingInt("caps.target_range")).getLocation();
		}

		/**
		 * Returns true if the <code>player</code> ability hits <code>target</code>.
		 * 
		 * @param player the player using the ability
		 * @param target the targeted LivingEntity
		 * @return true/false depending on if the ability hits or misses
		 */
		public static boolean doTargeting(Player player, Location target, boolean notify)
		{
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			Location toHit = adjustedAimLocation(character, target);
			if(isHit(target, toHit)) return true;
			if(notify) player.sendMessage(ChatColor.RED + "Missed..."); // TODO Better message.
			return false;
		}

		/**
		 * Returns the location that <code>character</code> is actually aiming
		 * at when doTargeting <code>target</code>.
		 * 
		 * @param character the character triggering the ability callAbilityEvent
		 * @param target the location the character is doTargeting at
		 * @return the aimed at location
		 */
		public static Location adjustedAimLocation(DCharacter character, Location target)
		{
			// TODO: This needs major work.

			int ascensions = character.getMeta().getAscensions();
			if(ascensions < 3) ascensions = 3;

			int offset = (int) (TARGET_OFFSET + character.getOfflinePlayer().getPlayer().getLocation().distance(target));
			int adjustedOffset = offset / ascensions;
			if(adjustedOffset < 1) adjustedOffset = 1;
			Random random = new Random();
			World world = target.getWorld();

			int randomInt = random.nextInt(adjustedOffset);
			int sampleSpace = random.nextInt(3);

			double X = target.getX();
			double Z = target.getZ();
			double Y = target.getY();

			if(sampleSpace == 0)
			{
				X += randomInt;
				Z += randomInt;
			}
			else if(sampleSpace == 1)
			{
				X -= randomInt;
				Z -= randomInt;
			}
			else if(sampleSpace == 2)
			{
				X -= randomInt;
				Z += randomInt;
			}
			else if(sampleSpace == 3)
			{
				X += randomInt;
				Z -= randomInt;
			}

			return new Location(world, X, Y, Z);
		}

		/**
		 * Returns true if <code>target</code> is hit at <code>hit</code>.
		 * 
		 * @param target the LivingEntity being targeted
		 * @param hit the location actually hit
		 * @return true/false if <code>target</code> is hit
		 */
		public static boolean isHit(Location target, Location hit)
		{
			return hit.distance(target) <= 2;
		}

		public static List<Ability> getLoadedAbilities()
		{
			return new ArrayList<Ability>()
			{
				{
					for(Elements.ListedDeity deity : Elements.Deities.values())
					{
						addAll(deity.getDeity().getAbilities());
					}
				}
			};
		}

		public static boolean invokeAbilityCommand(Player player, String command)
		{
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			for(final Ability ability : character.getDeity().getAbilities())
			{
				if(ability.getType().equals(Devotion.Type.PASSIVE)) continue;

				if(ability.getCommand() != null && ability.getCommand().equalsIgnoreCase(command))
				{
					// Ensure that the deity can be used, permission allows it, etc
					if(!Deity.Util.canUseDeity(player, ability.getDeity())) return true;
					if(!player.hasPermission(ability.getPermission())) return true;

					// Handle enabling the command
					String abilityName = ability.getName();

					if(!character.getMeta().isBound(ability.getName()))
					{
						if(!ability.hasWeapon() && player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR))
						{
							// Slot must be empty
							player.sendMessage(ChatColor.RED + Demigods.language.getText(Translation.Text.ERROR_BIND_TO_SLOT));
							return true;
						}
						else if(ability.hasWeapon())
						{
							// Weapon required
							player.sendMessage(ChatColor.RED + Demigods.language.getText(Translation.Text.ERROR_BIND_WEAPON_REQUIRED).replace("{weapon}", ability.getWeapon().name().toLowerCase().replace("_", " ")).replace("{ability}", abilityName.toLowerCase()));
							return true;
						}

						// Create the bind
						final Bind bind = createBind(ability.getName(), player.getInventory().getHeldItemSlot());

						// Handle the item
						ItemStack item = ability.hasWeapon() ? player.getItemInHand() : new ItemStack(Material.STICK);
						ItemMeta itemMeta = item.getItemMeta();
						itemMeta.setDisplayName(ChatColor.RESET + abilityName);

						itemMeta.setLore(new ArrayList<String>()
						{
							{
								add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Consumes " + ability.getCost() + " favor per use.");
								add("");
								for(String detail : ability.getDetails())
								{
									add(ChatColor.AQUA + detail);
								}
								add("");
								add(ChatColor.BLACK + "" + ChatColor.STRIKETHROUGH + "Identifier: " + ChatColor.MAGIC + bind.getIdentifier());
							}
						});

						// Set the item meta
						item.setItemMeta(itemMeta);

						// Set the bind item
						bind.setItem(item);

						// Save the bind and give the item
						player.getInventory().setItemInHand(item);
						character.getMeta().addBind(bind);

						// Let them know
						player.sendMessage(ChatColor.GREEN + Demigods.language.getText(Translation.Text.SUCCESS_ABILITY_BOUND).replace("{ability}", StringUtils.capitalize(abilityName)).replace("{slot}", "" + (player.getInventory().getHeldItemSlot() + 1)));

						return true;
					}
					else
					{
						// Get the bind for info
						Bind bind = character.getMeta().getBind(abilityName);

						// Remove the bind and item
						character.getMeta().removeBind(bind);
						player.getInventory().setItem(bind.getSlot(), new ItemStack(Material.AIR));

						// Let them know
						player.sendMessage(ChatColor.GREEN + Demigods.language.getText(Translation.Text.SUCCESS_ABILITY_UNBOUND).replace("{ability}", StringUtils.capitalize(abilityName)));

						return true;
					}
				}
			}
			return false;
		}

		public static void dealDamage(LivingEntity source, LivingEntity target, double amount, EntityDamageEvent.DamageCause cause)
		{
			if(source instanceof Player)
			{
				DPlayer owner = DPlayer.Util.getPlayer(((Player) source));
				if(owner != null)
				{
					if(target instanceof Player)
					{
						DCharacter targetChar = DPlayer.Util.getPlayer(((Player) target)).getCurrent();
						if(targetChar != null && DCharacter.Util.areAllied(owner.getCurrent(), targetChar)) return;
					}
					else if(target instanceof Tameable && ((Tameable) target).isTamed())
					{
						Pet wrapper = Pet.Util.getTameable(target);
						if(wrapper != null && DCharacter.Util.areAllied(owner.getCurrent(), wrapper.getOwner())) return;
					}
				}
			}
			EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(source, target, cause, amount);
			target.setLastDamageCause(event);
			if(amount >= 1 && !event.isCancelled()) target.damage(amount);
		}
	}
}
