package com.censoredsoftware.demigods.exclusive.command;

import com.censoredsoftware.censoredlib.helper.CommandManager;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.Data;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WorldCommands extends CommandManager
{
	public static final WorldCommands INST = new WorldCommands();

	public static WorldCommands inst()
	{
		return INST;
	}

	private WorldCommands()
	{}

	private static final Sub createWorld, importWorld, editWorld, disableWorld, teleport;

	static
	{
		createWorld = new Sub()
		{
			@Override
			public String getName()
			{
				return "create";
			}

			@Override
			public String getParentCommand()
			{
				return "world";
			}

			@Override
			public int getMinArgs()
			{
				return 1;
			}

			@Override
			public boolean execute(CommandSender sender, String label, String[] args)
			{
				switch(args.length)
				{
					case 3:
					{
						try
						{
							String worldName = args[1];
							World.Environment environment = World.Environment.valueOf(args[2]);
							Util.enableDemigods(worldName);
							World created = Util.createWorld(worldName, environment);
							Util.addWorldToData(created);
						}
						catch(Exception errored)
						{
							sender.sendMessage(ChatColor.RED + "Something went wrong. :C");
						}
						break;
					}
					case 4:
					{
						try
						{
							String worldName = args[1];
							WorldType type = WorldType.valueOf(args[2]);
							World.Environment environment = World.Environment.valueOf(args[3]);
							Util.enableDemigods(worldName);
							World created = Util.createWorld(worldName, type, environment);
							Util.addWorldToData(created);
						}
						catch(Exception errored)
						{
							sender.sendMessage(ChatColor.RED + "Something went wrong. :C");
						}
						break;
					}
					default:
					{
						sender.sendMessage(ChatColor.YELLOW + "/world create <name> <environment>");
						sender.sendMessage(ChatColor.YELLOW + "/world create <name> <type> <environment>");
						break;
					}
				}
				return true;
			}
		};
		importWorld = new Sub()
		{
			@Override
			public String getName()
			{
				return "import";
			}

			@Override
			public String getParentCommand()
			{
				return "world";
			}

			@Override
			public int getMinArgs()
			{
				return 2;
			}

			@Override
			public boolean execute(CommandSender sender, String label, String[] args)
			{
				Util.loadWorld(args[2]);
				sender.sendMessage("ok go check if it loaded");
				// TODO
				return false;
			}
		};
		editWorld = new Sub()
		{
			@Override
			public String getName()
			{
				return "edit";
			}

			@Override
			public String getParentCommand()
			{
				return "world";
			}

			@Override
			public int getMinArgs()
			{
				return 2;
			}

			@Override
			public boolean execute(CommandSender sender, String label, String[] args)
			{
				// TODO
				return false;
			}
		};
		disableWorld = new Sub()
		{
			@Override
			public String getName()
			{
				return "disable";
			}

			@Override
			public String getParentCommand()
			{
				return "world";
			}

			@Override
			public int getMinArgs()
			{
				return 2;
			}

			@Override
			public boolean execute(CommandSender sender, String label, String[] args)
			{
				return false;
			}
		};
		// TODO should this be here?
		teleport = new Sub()
		{
			@Override
			public Setting allowSender(CommandSender sender)
			{
				return sender instanceof ConsoleCommandSender ? Setting.SKIP : Setting.ALLOW;
			}

			@Override
			public String getName()
			{
				return "tp";
			}

			@Override
			public String getParentCommand()
			{
				return "world";
			}

			@Override
			public int getMinArgs()
			{
				return 2;
			}

			@Override
			public boolean execute(CommandSender sender, String label, String[] args)
			{
				World guess = Bukkit.getWorld(args[1]);
				if(Bukkit.getWorld(args[1]) != null)
				{
					Player player = (Player) sender;
					player.teleport(guess.getSpawnLocation());
					sender.sendMessage(ChatColor.YELLOW + "Teleport complete.");
				}
				else sender.sendMessage(ChatColor.RED + "That is not a valid world name.");
				return true;
			}
		};
	}

	@Override
	public ImmutableSet<String> getCommandNames()
	{
		return ImmutableSet.of("world");
	}

	@Override
	public ImmutableList<Sub> getSubCommands()
	{
		return ImmutableList.of(createWorld, importWorld, editWorld, disableWorld, teleport);
	}

	@Override
	public boolean always(CommandSender sender, Command command, String label, String[] args)
	{
        if(sender instanceof Player && !sender.hasPermission("exclusive.world"))
        {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
            return false;
        }
        if(args.length < 1) sender.sendMessage(ChatColor.RED + "Not enough args."); // TODO MENU (for loop of commands maybe)
		return true;
	}

    public static class Util
    {
        private Util()
        {}

        // -- PLUGIN LOAD -- //

        public static void loadHandledWorlds()
        {
            for(String world : getHandledWorlds())
            {
                if(Bukkit.getWorld(world) != null) continue;
                loadWorld(world);
            }
        }

        // -- ENABLE DEMIGODS -- //

        public static void enableDemigods(String worldName)
        {
            List<String> enabled = DemigodsPlugin.plugin().getConfig().getStringList("restrictions.enabled_worlds");
            if(!enabled.contains(worldName))
            {
                enabled.add(worldName);
                DemigodsPlugin.plugin().saveConfig();
            }
        }

        // -- CREATE WORLD -- //

        public static World createWorld(String worldName, World.Environment environment)
        {
            return WorldCreator.name(worldName).type(WorldType.NORMAL).environment(environment).createWorld();
        }

        public static World createWorld(String worldName, World.Environment environment, long seed)
        {
			return WorldCreator.name(worldName).type(WorldType.NORMAL).environment(environment).seed(seed).createWorld();
        }

		public static World createWorld(String worldName, WorldType worldType, World.Environment environment)
		{
			return WorldCreator.name(worldName).type(worldType).environment(environment).createWorld();
        }

		public static World createWorld(String worldName, WorldType worldType, World.Environment environment, long seed)
		{
			return WorldCreator.name(worldName).type(worldType).environment(environment).seed(seed).createWorld();
		}

		private static void addWorldToData(World world)
		{
			ImmutableList.Builder<String> builder = ImmutableList.builder();
       builder.addAll(getHandledWorlds());
			builder.add(world.getName());
			setHandledWorlds(builder.build());
			setHandledWorldOptions(world);
		}

		private static void removeWorldFromData(String worldName)
		{
			List<String> list = Lists.newArrayList(getHandledWorlds());
			list.remove(worldName);
			setHandledWorlds(ImmutableList.copyOf(list));
		}

		public static ImmutableList<String> getHandledWorlds()
		{
			ImmutableList<String> list = Data.SERVER.getStringList("worlds");
			if(list != null) return list;
			return ImmutableList.of();
		}

		public static WorldCreator getHandledWorldOptions(String worldName)
		{
			ImmutableMap<String, String> worldOptions = Data.SERVER.getStringMap("world:" + worldName);
			try
			{
				return WorldCreator.name(worldName).type(WorldType.valueOf(worldOptions.get("type"))).environment(World.Environment.valueOf(worldOptions.get("env"))).seed(Long.parseLong(worldOptions.get("seed")));
			}
			catch(Exception errored)
			{
				Messages.logException(errored);
			}
			return WorldCreator.name(worldName);
		}

		private static void setHandledWorlds(ImmutableList<String> worlds)
		{
			Data.SERVER.setStringList("worlds", worlds);
		}

		public static void setHandledWorldOptions(World world)
		{
			ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
			builder.put("type", world.getWorldType().name());
			builder.put("env", world.getEnvironment().name());
			builder.put("seed", String.valueOf(world.getSeed()));
			Data.SERVER.setStringMap("world:" + world.getName(), builder.build());
		}

		// -- LOAD WORLD -- //

		public static World loadWorld(String worldName)
		{
			return WorldCreator.name(worldName).createWorld();
		}

		public static World loadWorld(WorldCreator worldCreator)
		{
			return worldCreator.createWorld();
		}

		// -- DISABLE WORLD -- //

		public static void disableWorld(String worldName)
		{
			Bukkit.unloadWorld(worldName, true);
		}
	}
}