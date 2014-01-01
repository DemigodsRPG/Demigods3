package com.censoredsoftware.demigods.engine.command;

import com.censoredsoftware.censoredlib.helper.WrappedCommand;
import com.censoredsoftware.censoredlib.language.Symbol;
import com.censoredsoftware.censoredlib.util.Maps2;
import com.censoredsoftware.censoredlib.util.Strings;
import com.censoredsoftware.censoredlib.util.Times;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.Battle;
import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.TributeManager;
import com.censoredsoftware.demigods.engine.language.English;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GeneralCommands extends WrappedCommand
{
	public GeneralCommands()
	{
		super(DemigodsPlugin.plugin(), false);
	}

	@Override
	public Set<String> getCommands()
	{
		return Sets.newHashSet("check", "owner", "binds", "leaderboard", "alliance", "values", "names");
	}

	@Override
	public boolean processCommand(CommandSender sender, Command command, String[] args)
	{
		if("check".equalsIgnoreCase(command.getName())) return check(sender);
		if("owner".equalsIgnoreCase(command.getName())) return owner(sender, args);
		if("alliance".equalsIgnoreCase(command.getName())) return alliance(sender, args);
		if("binds".equalsIgnoreCase(command.getName())) return binds(sender);
		if("leaderboard".equalsIgnoreCase(command.getName())) return leaderboard(sender);
		if("tributes".equalsIgnoreCase(command.getName())) return tributes(sender);
		return "names".equalsIgnoreCase(command.getName()) && names(sender);
	}

	private boolean check(CommandSender sender)
	{
		Player player = (Player) sender;
		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

		if(character == null)
		{
			player.sendMessage(ChatColor.RED + "You are nothing but a mortal. You have no worthy statistics.");
			return true;
		}

		// Define variables
		int kills = character.getKillCount();
		int deaths = character.getDeathCount();
		String charName = character.getName();
		String deity = character.getDeity().getName();
		Alliance alliance = character.getAlliance();

		int favor = character.getMeta().getFavor();
		int maxFavor = character.getMeta().getMaxFavor();
		int ascensions = character.getMeta().getAscensions();
		int skillPoints = character.getMeta().getSkillPoints();
		ChatColor deityColor = character.getDeity().getColor();
		ChatColor favorColor = Strings.getColor(character.getMeta().getFavor(), character.getMeta().getMaxFavor());

		// Set player status
		String status = ChatColor.YELLOW + "Ready.";
		if(!character.canPvp()) status = ChatColor.DARK_AQUA + "Safe.";
		else if(Battle.Util.isInBattle(character)) status = ChatColor.GOLD + "In battle.";

		// Send the user their info via chat
		Messages.tagged(sender, "Player Check");

		sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Character: " + deityColor + charName);
		sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Deity: " + deityColor + deity + ChatColor.WHITE + " of the " + ChatColor.GOLD + alliance.getName() + "s");
		sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Favor: " + favorColor + favor + ChatColor.GRAY + " (of " + ChatColor.GREEN + maxFavor + ChatColor.GRAY + ")");
		sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Ascensions: " + ChatColor.GREEN + ascensions);
		sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Available Skill Points: " + ChatColor.GREEN + skillPoints);
		sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Kills: " + ChatColor.GREEN + kills + ChatColor.WHITE + " / Deaths: " + ChatColor.RED + deaths + ChatColor.GRAY + " (Ratio: " + (deaths == 0 ? kills : (Math.round(kills / deaths * 100.0) / 100.0)) + ")");
		sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.RESET + "Status: " + status);

		return true;
	}

	private boolean owner(CommandSender sender, String[] args)
	{
		// Check permissions
		if(!sender.hasPermission("demigods.basic")) return Messages.noPermission(sender);

		Player player = (Player) sender;
		if(args.length < 1)
		{
			player.sendMessage(ChatColor.RED + "You must select a character.");
			player.sendMessage(ChatColor.RED + "/owner <character>");
			return true;
		}
		DCharacter charToCheck = DCharacter.Util.getCharacterByName(args[0]);
		if(charToCheck == null) player.sendMessage(ChatColor.RED + "That character doesn't exist.");
		else player.sendMessage(charToCheck.getDeity().getColor() + charToCheck.getName() + ChatColor.YELLOW + " belongs to " + charToCheck.getOfflinePlayer().getName() + ".");
		return true;
	}

	private boolean alliance(CommandSender sender, String[] args)
	{
		// Check permissions
		if(!sender.hasPermission("demigods.basic")) return Messages.noPermission(sender);

		Player player = (Player) sender;

		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
		if(character == null)
		{
			player.sendMessage(English.DISABLED_MORTAL.getLine());
			return true;
		}

		if(args.length < 1)
		{
			character.chatWithAlliance("...");
			return true;
		}
		else character.chatWithAlliance(Joiner.on(" ").join(args));
		return true;
	}

	private boolean binds(CommandSender sender)
	{
		// Check permissions
		if(!sender.hasPermission("demigods.basic")) return Messages.noPermission(sender);

		// Define variables
		Player player = (Player) sender;
		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

		if(character != null && !character.getMeta().getBinds().isEmpty())
		{
			Messages.tagged(player, "Currently Bound Abilities");
			player.sendMessage(" ");

			// Get the binds and display info
			for(Map.Entry<String, Object> entry : character.getMeta().getBinds().entrySet())
			{
				player.sendMessage(ChatColor.GRAY + "  " + Symbol.RIGHTWARD_ARROW_HOLLOW + ChatColor.YELLOW + " " + StringUtils.capitalize(entry.getKey().toLowerCase()) + ChatColor.GRAY + " is bound to " + (Strings.beginsWithVowel(entry.getValue().toString()) ? "an " : "a ") + ChatColor.ITALIC + Strings.beautify(entry.getValue().toString()).toLowerCase() + ChatColor.GRAY + ". " + (DCharacter.Util.isCooledDown(character, entry.getKey()) ? "(" + ChatColor.GREEN + "ready" + ChatColor.GRAY + ")" : "(" + ChatColor.AQUA + "cooling down... " + Times.getTimeTagged(DCharacter.Util.getCooldown(character, entry.getKey()), true) + ChatColor.GRAY + ")"));
			}

			player.sendMessage(" ");
		}
		else player.sendMessage(ChatColor.RED + "You currently have no ability binds.");

		return true;
	}

	private boolean leaderboard(CommandSender sender)
	{
		// Define variables
		List<DCharacter> characters = Lists.newArrayList(DCharacter.Util.getAllUsable());
		Map<UUID, Double> scores = Maps.newLinkedHashMap();
		for(DCharacter character : characters)
		{
			double score = character.getKillCount() - character.getDeathCount();
			if(score > 0) scores.put(character.getId(), score);
		}

		// Sort rankings
		scores = Maps2.sortByValue(scores, false);

		// Print info
		Messages.tagged(sender, "Leaderboard");
		sender.sendMessage(" ");
		sender.sendMessage(ChatColor.GRAY + "    Rankings are determined by kills and deaths.");
		sender.sendMessage(" ");

		int length = characters.size() > 15 ? 16 : characters.size() + 1;
		List<Map.Entry<UUID, Double>> list = Lists.newArrayList(scores.entrySet());
		int count = 0;

		for(int i = list.size() - 1; i >= 0; i--)
		{
			count++;
			Map.Entry<UUID, Double> entry = list.get(i);

			if(count >= length) break;
			DCharacter character = DCharacter.Util.load(entry.getKey());
			sender.sendMessage(ChatColor.GRAY + "    " + ChatColor.RESET + count + ". " + character.getDeity().getColor() + character.getName() + ChatColor.RESET + ChatColor.GRAY + " (" + character.getPlayerName() + ") " + ChatColor.RESET + "Kills: " + ChatColor.GREEN + character.getKillCount() + ChatColor.WHITE + " / Deaths: " + ChatColor.RED + character.getDeathCount());
		}

		sender.sendMessage(" ");
		return true;
	}

	private boolean tributes(CommandSender sender)
	{
		// Define variables
		Player player = (Player) sender;
		int count = 0;

		if(TributeManager.getTributeValuesMap().isEmpty())
		{
			sender.sendMessage(ChatColor.RED + "There are currently no tributes on record.");
			return true;
		}

		// Send header
		Messages.tagged(sender, "Current High Value Tributes");
		sender.sendMessage(" ");

		for(Map.Entry<Material, Integer> entry : Maps2.sortByValue(TributeManager.getTributeValuesMap(), true).entrySet())
		{
			// Handle count
			if(count >= 10) break;
			count++;

			// Display value
			sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.YELLOW + Strings.beautify(entry.getKey().name()) + ChatColor.GRAY + " (currently worth " + ChatColor.GREEN + entry.getValue() + ChatColor.GRAY + " per item)");
		}

		sender.sendMessage(" ");
		sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Values are constantly changing based on how players");
		sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "tribute, so check back often!");

		if(!Material.AIR.equals(player.getItemInHand().getType()))
		{
			sender.sendMessage(" ");
			sender.sendMessage(ChatColor.GRAY + "The " + (player.getItemInHand().getAmount() == 1 ? "item in your hand is" : "items in your hand are") + " worth " + ChatColor.GREEN + TributeManager.getValue(player.getItemInHand()) + ChatColor.GRAY + " in total.");
		}

		return true;
	}

	private boolean names(CommandSender sender)
	{
		// Print info
		Messages.tagged(sender, "Online Players");
		sender.sendMessage(" ");
		sender.sendMessage(ChatColor.GRAY + "    " + ChatColor.UNDERLINE + "Immortals:");
		sender.sendMessage(" ");

		// Characters
		for(DCharacter character : DCharacter.Util.getOnlineCharacters())
			sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + character.getDeity().getColor() + character.getName() + ChatColor.GRAY + " is owned by " + ChatColor.WHITE + character.getPlayerName() + ChatColor.GRAY + ".");

		sender.sendMessage(" ");

		Set<Player> mortals = DPlayer.Util.getOnlineMortals();

		if(mortals.isEmpty()) return true;

		sender.sendMessage(ChatColor.GRAY + "    " + ChatColor.UNDERLINE + "Mortals:");
		sender.sendMessage(" ");

		// Mortals
		for(Player mortal : mortals)
			sender.sendMessage(ChatColor.GRAY + " " + Symbol.RIGHTWARD_ARROW + " " + ChatColor.WHITE + mortal.getDisplayName() + ".");

		sender.sendMessage(" ");

		return true;
	}
}
