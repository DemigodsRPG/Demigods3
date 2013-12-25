package com.censoredsoftware.demigods.engine.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class Translation
{
	/**
	 * Gets a specific line of <code>LANGUAGE</code>.
	 * 
	 * @param text The line we want.
	 * @return The String value of the Text.
	 */
	public String getText(Text text)
	{
		return text.english();
	}

	/**
	 * Gets a List for the given <code>LANGUAGE</code>.
	 * 
	 * @param text The block of LANGUAGE we want.
	 * @return The ArrayList for the block of LANGUAGE.
	 */
	public List<String> getTextBlock(final Text text)
	{
		return text.englishBlock();
	}

	public Set<String> getBlackList()
	{
		Set<String> set = Sets.newHashSet();

		// Manual Blacklist
		set.add("Pussy");
		set.add("Cock");
		set.add("Fuck");
		set.add("Shit");
		set.add("Ass");
		set.add("Dick");
		set.add("Penis");
		set.add("Vagina");
		set.add("Cunt");
		set.add("Bitch");
		set.add("Nigger");
		set.add("Phil");
		set.add("Staff");
		set.add("Server");
		set.add("Console");
		set.add("Disowned");

		// Deities
		for(Deity deity : Demigods.mythos().getDeities())
		{
			set.add(deity.getName());
			set.add(deity.getAlliance().getName());
		}

		return set;
	}

	public static enum Text
	{
		NOTIFICATION_SKILL_POINTS_RECEIVED(new ArrayList<String>()
		{
			{
				add(ChatColor.GREEN + "You have earned {skillpoints} skill points for your recent battle!");
				add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Find an Altar to distribute them.");
			}
		}), NOTIFICATIONS_PRAYER_FOOTER(new ArrayList<String>()
		{
			{
				add(ChatColor.GRAY + "  Type " + ChatColor.RED + "clear" + ChatColor.GRAY + " to remove all notifications or type " + ChatColor.YELLOW + "menu");
				add(ChatColor.GRAY + "  to return to the main menu.");
			}
		}), NOTIFICATION_RECEIVED(new ArrayList<String>()
		{
			{
				add(ChatColor.GREEN + "You have a new notification!");
				add(ChatColor.GRAY + "Find an Altar to view your notifications.");
			}
		}), PRAYER_ENDED(new ArrayList<String>()
		{
			{
				add(ChatColor.AQUA + "You are no longer praying.");
				add(ChatColor.GRAY + "Your chat has been re-enabled.");
			}
		}), PRAYER_INTRO(new ArrayList<String>()
		{
			{
				add(ChatColor.GRAY + " While praying you are unable chat with players.");
				add(ChatColor.GRAY + " You can return to the main menu at anytime by typing " + ChatColor.YELLOW + "menu" + ChatColor.GRAY + ".");
				add(ChatColor.GRAY + " Walk away to stop praying.");
			}
		}), PVP_NO_PRAYER(new ArrayList<String>()
		{
			{
				add(ChatColor.GRAY + "You cannot pray when PvP is still possible.");
				add(ChatColor.GRAY + "Wait a few moments and then try again when it's safe.");
			}
		}), NOTIFICATION_SHRINE_CREATED(new ArrayList<String>()
		{
			{
				add(ChatColor.GRAY + "" + ChatColor.ITALIC + "The {alliance} are pleased...");
				add(ChatColor.GREEN + "You have created a Shrine in the name of {deity}!");
			}
		}), NOTIFICATION_ERROR_SKILL_DOESNT_EXIST(ChatColor.RED + "That skill doesn't exist."), NOTIFICATION_ERROR_UNOBTAINED_SKILL(ChatColor.RED + "You don't have that skill."), RUNNING_DG_VERSION(ChatColor.GRAY + "This server is running Demigods " + ChatColor.GRAY + "version:" + ChatColor.YELLOW + " {version}"), DG_FOR_MORE_INFORMATION(ChatColor.GRAY + "Type " + ChatColor.GREEN + "/dg" + ChatColor.GRAY + " for more information."), DISCONNECT_DURING_BATTLE(ChatColor.YELLOW + "{name} has left the battle."), DISCONNECT_GENERIC(ChatColor.YELLOW + "{name} has either quit or crashed."), DISCONNECT_SPAM(ChatColor.YELLOW + "{name} has disconnected due to spamming."), DISCONNECT_EOS(ChatColor.YELLOW + "{name} has lost connection."), DISCONNECT_OVERFLOW(ChatColor.YELLOW + "{name} has disconnected due to overload."), DISCONNECT_QUITTING(ChatColor.YELLOW + "{name} has quit."), DISCONNECT_TIMEOUT(ChatColor.YELLOW + "{name} has timed out."), WELCOME_BACK_IN_BATTLE(ChatColor.YELLOW + "Welcome back! You are currently in a battle."), WELCOME_BACK_BATTLE_LOST(ChatColor.RED + "You left the game during a battle and have lost."), FIND_ALTAR_TO_VIEW_NOTIFICATIONS(ChatColor.GRAY + "Find an Altar to view your notifications."), UNREAD_NOTIFICATION(ChatColor.GREEN + "You have an unread notification!"), UNREAD_NOTIFICATIONS(ChatColor.GREEN + "You have {size} unread notifications!"), MUST_BE_ALLIED_TO_TRIBUTE(ChatColor.YELLOW + "You must be allied to {deity} in order to tribute here."), CHEST_TRIBUTE_TO("Tribute to {deity}"), INSUFFICIENT_TRIBUTES(ChatColor.RED + "Your tributes were insufficient for {deity}'s blessings."), BLESSED_WITH_FAVOR(ChatColor.GRAY + "You have been blessed with " + ChatColor.GREEN + "{favor}" + ChatColor.GRAY + " favor."), DEITY_PLEASED(ChatColor.YELLOW + "{deity} is pleased!"), FAVOR_CAP_INCREASED(ChatColor.GRAY + "Your favor cap has increased to" + ChatColor.GREEN + " {cap}" + ChatColor.GRAY + "!"), EXTERNAL_SHRINE_TRIBUTE(ChatColor.YELLOW + "Someone just tributed at your Shrine!"), NOTIFICATION_ERROR_WARPING(ChatColor.RED + "Could not teleport to that warp, does it exist?"), NOTIFICATION_ERROR_DELETING_WARP(ChatColor.RED + "Warp could not be deleted, does it exist?"), NOTIFICATION_ERROR_INVITING(ChatColor.RED + "Could not send invite, is that a real person?"), NOTIFICATION_ERROR_CREATING_WARP(ChatColor.RED + "Your warp could not be created, please choose a new name."), NOTIFICATION_ERROR_MISC(ChatColor.RED + "There was an error while performing your request."), DIRECTIONS_MAIN_MENU_PRAYER(ChatColor.GRAY + "Type " + ChatColor.YELLOW + "menu" + ChatColor.GRAY + " to return the to main menu."), NOTIFICATION_SKILL_POINTS_ASSIGNED(ChatColor.GREEN + "Skill points assigned!"), ERROR_NOT_ENOUGH_SKILL_POINTS(ChatColor.RED + "You do not have enough skill points for that."), ERROR_MATERIAL_BOUND("That item has already been bound to an ability!"), DISABLED_MORTAL("Mortals cannot do that!"), HELD_BACK_MESSAGE("1 message was held back:"), HELD_BACK_MESSAGES("{size} messages were held back:"), KNELT_FOR_PRAYER("{player} has knelt to begin prayer."), UNSAFE_FROM_PVP("You can now PvP!"), SAFE_FROM_PVP("You are now safe from PvP!"), ERROR_BIND_WEAPON_REQUIRED("A {weapon} is required to bind {ability}!"), DATA_RESET_KICK("All Demigods data has been reset, you may now rejoin!"), ADMIN_CLEAR_DATA_STARTING("Clearing all Demigods data..."), ADMIN_CLEAR_DATA_FINISHED("All Demigods data cleared successfully!"), SUCCESS_ABILITY_BOUND("{ability} has been bound to {material}!"), SUCCESS_ABILITY_UNBOUND("{ability} has been unbound."), ERROR_EMPTY_SLOT("You cannot bind to an empty slot!"), NOTIFICATION_WARP_CREATED(ChatColor.GREEN + "Warp created!"), NOTIFICATION_WARP_DELETED(ChatColor.RED + "Warp deleted!"), NOTIFICATION_INVITE_SENT(ChatColor.GREEN + "Invite sent!"), ERROR_NAME_LENGTH("Your name should be between 2 and 12 characters."), ERROR_CHAR_EXISTS("A character with that name already exists."), ERROR_ALPHA_NUMERIC("Only alpha-numeric characters are allowed."), ERROR_MAX_CAPS("Please use no more than {maxCaps} capital letters."), NOTIFICATION_OBELISK_CREATED("You created an Obelisk!"), ALTAR_SPAWNED_NEAR("An Altar has spawned near you..."), PROTECTED_BLOCK("That block is protected by a Deity!"), ADMIN_WAND_GENERATE_ALTAR("Generating new Altar..."), ADMIN_WAND_GENERATE_ALTAR_COMPLETE("Altar created!"), ADMIN_WAND_REMOVE_ALTAR("Right-click this Altar again to remove it."), ADMIN_WAND_REMOVE_ALTAR_COMPLETE("Altar removed!"), ADMIN_WAND_REMOVE_SHRINE("Right-click this Shrine again to remove it."), ADMIN_WAND_REMOVE_SHRINE_COMPLETE("Shrine removed!"), ADMIN_WAND_REMOVE_OBELISK("Right-click this Obelisk again to remove it."), ADMIN_WAND_REMOVE_OBELISK_COMPLETE("Obelisk removed!"), NO_WARP_ALTAR("You've never set a warp at this Altar."), CHARACTER_CREATE_COMPLETE("{deity} has accepted you!"), KILLSTREAK("{character} is on a killstreak of {kills} kills."), MORTAL_SLAIN_1(ChatColor.YELLOW + "A mortal" + ChatColor.GRAY + " was slain by " + ChatColor.YELLOW + "another mortal" + ChatColor.GRAY + "."), MORTAL_SLAIN_2(ChatColor.YELLOW + "A mortal" + ChatColor.GRAY + " was slain by {attacker} of the {attackerAlliance} alliance."), DEMI_SLAIN_1("{killed} of the {killedAlliance} was slain by " + ChatColor.YELLOW + "a mortal" + ChatColor.GRAY + "."), DEMI_SLAIN_2("{killed} of the {killedAlliance} was slain by {attacker} of the {attackerAlliance} alliance."), DEMI_BETRAY("{killed} was betrayed by {attacker} of the {alliance} alliance."), MORTAL_BETRAY("A mortal was killed by another worthless mortal."), COMMAND_BLOCKED_BATTLE("That command is blocked during a battle."), NO_PVP_ZONE("No-PVP in this zone."), WEAKER_THAN_YOU("One weaker than you has been slain by your hand."), YOU_FAILED_DEITY("You have failed {deity}!"), RELOAD_COMPLETE("Reload complete!");

		private String english;
		private List<String> englishBlock;

		private Text(String english)
		{
			this.english = english;
			this.englishBlock = Lists.newArrayList();
		}

		private Text(List<String> englishBlock)
		{
			this.english = "";
			this.englishBlock = englishBlock;
		}

		public String english()
		{
			return english;
		}

		public List<String> englishBlock()
		{
			return englishBlock;
		}
	}
}
