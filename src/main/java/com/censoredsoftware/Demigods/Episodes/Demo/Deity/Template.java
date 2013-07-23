package com.censoredsoftware.Demigods.Episodes.Demo.Deity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.censoredsoftware.Demigods.Engine.Object.Ability.Ability;
import com.censoredsoftware.Demigods.Engine.Object.Ability.AbilityInfo;
import com.censoredsoftware.Demigods.Engine.Object.Ability.Devotion;
import com.censoredsoftware.Demigods.Engine.Object.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Object.Deity.DeityInfo;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerWrapper;
import com.censoredsoftware.Demigods.Engine.Utility.UnicodeUtility;

public class Template extends Deity
{
	private static String name = "Template", alliance = "Test", permission = "demigods.test.test";
	private static ChatColor color = ChatColor.GRAY;
	private static Set<Material> claimItems = new HashSet<Material>()
	{
		{
			add(Material.DIRT);
		}
	};
	private static List<String> lore = new ArrayList<String>()
	{
		{
			add(" ");
			add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + color + name);
			add(ChatColor.RESET + "-----------------------------------------------------");
			add(ChatColor.YELLOW + " Claim Items:");
			for(Material item : claimItems)
			{
				add(ChatColor.GRAY + " " + UnicodeUtility.rightwardArrow() + " " + ChatColor.WHITE + item.name());
			}
			add(ChatColor.YELLOW + " Abilities:");
		}
	};
	private static Type type = Type.DEMO;
	private static Set<Ability> abilities = new HashSet<Ability>()
	{
		{
			add(new Test());
		}
	};

	public Template()
	{
		super(new DeityInfo(name, alliance, permission, color, claimItems, lore, type), abilities);
	}
}

class Test extends Ability
{
	private static String deity = "Template", name = "Test", command = "test", permission = "demigods.test.test";
	private static int cost = 170, delay = 1500, repeat = 0;
	private static AbilityInfo info;
	private static List<String> details = new ArrayList<String>()
	{
		{
			add("Blaze your target.");
		}
	};
	private static Devotion.Type type = Devotion.Type.SUPPORT;

	protected Test()
	{
		super(info = new AbilityInfo(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(!Ability.isLeftClick(interactEvent)) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				PlayerCharacter character = PlayerWrapper.getPlayer(player).getCurrent();

				if(!Deity.canUseDeitySilent(player, deity)) return;

				if(character.getMeta().isEnabledAbility(name) || player.getItemInHand() != null && character.getMeta().isBound(name) && character.getMeta().getBind(name).toItemStack().equals(player.getItemInHand()))
				{
					if(!PlayerCharacter.isCooledDown(character, name, false)) return;

					test(player);
				}
			}
		}, null);
	}

	// The actual ability command
	public static void test(Player player)
	{
		// Define variables
		PlayerCharacter character = PlayerWrapper.getPlayer(player).getCurrent();
		LivingEntity target = Ability.autoTarget(player);

		if(!Ability.doAbilityPreProcess(player, target, "test", cost, info)) return;
		PlayerCharacter.setCoolDown(character, name, System.currentTimeMillis() + delay);
		character.getMeta().subtractFavor(cost);

		if(!Ability.doTargeting(player, target.getLocation(), true)) return;

		if(target instanceof Player)
		{
			Player victim = (Player) target;
			victim.sendMessage("Test!");
			player.sendMessage("Tested " + victim.getName() + "!");
		}
	}
}
