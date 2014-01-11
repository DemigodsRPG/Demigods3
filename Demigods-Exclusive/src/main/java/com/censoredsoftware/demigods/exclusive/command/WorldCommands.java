package com.censoredsoftware.demigods.exclusive.command;

import com.censoredsoftware.censoredlib.helper.CommandManager;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.Data;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.ImmutableList;
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

	private static final Sub createWorld, importWorld, editWorld, disableWorld, teleport, list;

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
							final String worldName = args[1];
							final World.Environment environment = World.Environment.valueOf(args[2]);

							Util.enableDemigods(worldName);
							Util.createWorld(worldName, environment);
							Util.addWorldToData(worldName);
						}
						catch(Exception errored)
						{
                            errored.printStackTrace();
							sender.sendMessage(ChatColor.RED + "Something went wrong. :C");
						}
						break;
					}
					case 4:
					{
						try
						{
							final String worldName = args[1];
							final WorldType type = WorldType.valueOf(args[2]);
							final World.Environment environment = World.Environment.valueOf(args[3]);

							Util.enableDemigods(worldName);
							Util.createWorld(worldName, type, environment);
							Util.addWorldToData(worldName);
						}
						catch(Exception errored)
						{
            errored.printStackTrace();
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
        list = new Sub()
        {
            @Override
            public String getName()
            {
                return "ls";
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
				String working = "Worlds: ";
				for(World world : Bukkit.getWorlds())
					working += world.getName() + ", ";
				working = working.substring(0, working.length() - 2) + ".";
				String[] message = sender instanceof Player ? Messages.wrapInGame(working) : Messages.wrapConsole(working);
				for(String line : message)
					sender.sendMessage(line);
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
		return ImmutableList.of(createWorld, importWorld, editWorld, disableWorld, teleport, list);
	}

	@Override
	public boolean always(CommandSender sender, Command command, String label, String[] args)
	{
        if(sender instanceof Player && !sender.hasPermission("exclusive.world"))
        {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
			return false;
		}
		if(args.length < 1) sender.sendMessage(ChatColor.RED + "Not enough args."); // TODO MENU (for loop of sub-commands maybe)
		return true;
	}

    public static class Util
    {
        private Util()
        {}

        // -- PLUGIN LOAD -- //

        public static void loadHandledWorlds()
        {
            if(noWorlds()) return;
            Messages.info("Loading worlds...");
			for(String world : getHandledWorlds())
			{
				loadWorld(world);
				Messages.info(world + " loaded.");
     }
        }

        // -- ENABLE DEMIGODS -- //

        public static void enableDemigods(String worldName)
        {
			List<String> enabled = DemigodsPlugin.plugin().getConfig().getStringList("restrictions.enabled_worlds");
			if(!enabled.contains(worldName))
			{
      enabled.add(worldName);
                DemigodsPlugin.plugin().getConfig().set("restrictions.enabled_worlds", enabled);
				DemigodsPlugin.plugin().saveConfig();
      }
            Zones.enableWorld(worldName);
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

		private static void addWorldToData(String worldName)
		{
            List<String> list = Lists.newArrayList();
            if(!noWorlds()) list.addAll(getHandledWorlds());
            list.add(worldName);
			setHandledWorlds(list);
		}

		private static void removeWorldFromData(String worldName)
		{
			List<String> list = Lists.newArrayList();
            if(!noWorlds()) list.addAll(getHandledWorlds());
			list.remove(worldName);
			setHandledWorlds(list);
		}

		public static List<String> getHandledWorlds()
		{
			List<String> list = Data.SERVER.getStringList("worlds");
			if(list != null) return list;
			return Lists.newArrayList();
		}

		private static boolean noWorlds()
		{
			return getHandledWorlds().isEmpty();
		}

		private static void setHandledWorlds(List<String> worlds)
		{
			Data.SERVER.setStringList("worlds", worlds);
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
			List<String> enabled = DemigodsPlugin.plugin().getConfig().getStringList("restrictions.enabled_worlds");
			if(enabled.contains(worldName))
            {
                enabled.remove(worldName);
                DemigodsPlugin.plugin().getConfig().set("restrictions.enabled_worlds", enabled);
                DemigodsPlugin.plugin().saveConfig();
            }
            Zones.disableWorld(worldName);
		}
	}
}