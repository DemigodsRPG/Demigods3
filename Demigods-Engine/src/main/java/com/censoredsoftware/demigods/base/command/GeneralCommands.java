package com.censoredsoftware.demigods.base.command;

import com.censoredsoftware.censoredlib.helper.WrappedCommand;
import com.censoredsoftware.censoredlib.language.Symbol;
import com.censoredsoftware.censoredlib.util.Maps2;
import com.censoredsoftware.censoredlib.util.Strings;
import com.censoredsoftware.censoredlib.util.Times;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.*;
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
	public Set<String> getCommandNames()
	{
		return Sets.newHashSet("check", "owner", "binds", "leaderboard", "alliance", "tributes", "names", "skills");
	}

	@Override
	public boolean processCommand(CommandSender sender, Command command, String[] args)
	{
		// Check permission
		if(!sender.hasPermission("demigods.basic")) return Messages.noPermission(sender);

		// Define player and (try to define) character
		Player player = (Player) sender;
		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

		// Basic commands
		if("owner".equalsIgnoreCase(command.getName())) return owner(sender, args);
		if("leaderboard".equalsIgnoreCase(command.getName())) return leaderboard(sender);
		if("tributes".equalsIgnoreCase(command.getName())) return tributes(sender);
		if("names".equalsIgnoreCase(command.getName())) return names(sender);

		// Character/Deity specific commands
		if(character == null)
		{
			// They have no character, reject them
			player.sendMessage(ChatColor.RED + "You cannot use that commands, mortal.");
			return true;
		}
		else
		{
			// Accepted
			if("check".equalsIgnoreCase(command.getName())) return check(player, character);
			if("alliance".equalsIgnoreCase(command.getName())) return alliance(player, character, args);
			if("binds".equalsIgnoreCase(command.getName())) return binds(player, character);
			if("skills".equalsIgnoreCase(command.getName())) return skills(player, character);
		}

		// FIXME: This will only work for actual players. Console will always give an error.

		return false;
	}

	private boolean check(Player player, DCharacter character)
	{
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

		// Send the user their info
		Messages.tagged(player, "Player Check");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GRAY + "  " + Symbol.RIGHTWARD_ARROW_HOLLOW + " " + ChatColor.RESET + "Character: " + deityColor + charName);
		player.sendMessage(ChatColor.GRAY + "  " + Symbol.RIGHTWARD_ARROW_HOLLOW + " " + ChatColor.RESET + "Deity: " + deityColor + deity + ChatColor.WHITE + " of the " + ChatColor.GOLD + alliance.getName() + "s");
		player.sendMessage(ChatColor.GRAY + "  " + Symbol.RIGHTWARD_ARROW_HOLLOW + " " + ChatColor.RESET + "Favor: " + favorColor + favor + ChatColor.GRAY + " (of " + ChatColor.GREEN + maxFavor + ChatColor.GRAY + ")");
		player.sendMessage(ChatColor.GRAY + "  " + Symbol.RIGHTWARD_ARROW_HOLLOW + " " + ChatColor.RESET + "Ascensions: " + ChatColor.GREEN + ascensions);
		player.sendMessage(ChatColor.GRAY + "  " + Symbol.RIGHTWARD_ARROW_HOLLOW + " " + ChatColor.RESET + "Available Skill Points: " + ChatColor.GREEN + skillPoints);
		player.sendMessage(ChatColor.GRAY + "  " + Symbol.RIGHTWARD_ARROW_HOLLOW + " " + ChatColor.RESET + "Kills: " + ChatColor.GREEN + kills + ChatColor.WHITE + " / Deaths: " + ChatColor.RED + deaths + ChatColor.GRAY + " (Ratio: " + (deaths == 0 ? kills : (Math.round(kills / deaths * 100.0) / 100.0)) + ")");
		player.sendMessage(ChatColor.GRAY + "  " + Symbol.RIGHTWARD_ARROW_HOLLOW + " " + ChatColor.RESET + "Status: " + (character.canPvp() ? (Battle.Util.isInBattle(character) ? ChatColor.RED + "Battling" : ChatColor.YELLOW + "Ready") : ChatColor.GREEN + "Safe"));
		player.sendMessage(" ");

		return true;
	}

	private boolean skills(Player player, DCharacter character)
	{
		// Tag it
		Messages.tagged(player, "Skills");

		// Send skill info
		player.sendMessage(" ");
		for(Skill skill : character.getMeta().getLevelableSkills())
		{
			player.sendMessage(ChatColor.GRAY + "  " + Symbol.RIGHTWARD_ARROW_HOLLOW + (skill.hasMetCap() ? ChatColor.GRAY + "" + ChatColor.ITALIC : ChatColor.AQUA) + " " + skill.getType().getName() + ChatColor.RESET + ChatColor.GRAY + " (Level " + ChatColor.GREEN + skill.getLevel() + ChatColor.GRAY + ") " + (skill.hasMetCap() ? ChatColor.GOLD + "(Level Cap Met)" : ChatColor.GRAY + "(" + ChatColor.YELLOW + skill.getRequiredPoints() + ChatColor.GRAY + " skill points from level " + ChatColor.YELLOW + (skill.getLevel() + 1) + ChatColor.GRAY + ")"));
		}
		player.sendMessage(" ");

		return true;
	}

	private boolean owner(CommandSender sender, String[] args)
	{
		// Enough arguments?
		if(args.length < 1)
		{
			sender.sendMessage(ChatColor.RED + "You must select a character.");
			sender.sendMessage(ChatColor.RED + "/owner <character>");
			return true;
		}

		// Find the character
		DCharacter checked = DCharacter.Util.getCharacterByName(args[0]);

		// Send the message
		if(checked == null)
		{
			sender.sendMessage(ChatColor.RED + "That character doesn't exist.");
		}
		else
		{
			sender.sendMessage(checked.getDeity().getColor() + checked.getName() + ChatColor.YELLOW + " belongs to " + checked.getOfflinePlayer().getName() + ".");
		}

		return true;
	}

	private boolean alliance(Player player, DCharacter character, String[] args)
	{
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
		else
		{
			character.chatWithAlliance(Joiner.on(" ").join(args));
		}

		return true;
	}

	private boolean binds(Player player, DCharacter character)
	{
		if(!character.getMeta().getBinds().isEmpty())
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
