package com.demigodsrpg.demigods.greek.ability.passive;

import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.ability.GreekAbility;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.List;

public class Carry extends GreekAbility.Passive
{
	private static final String NAME = "Carry";
	private static final int REPEAT = 20;
	private static final List<String> DETAILS = Lists.newArrayList("Hold a leash to carry others on your shoulders.");

	public Carry(final String deity, final boolean needsLead)
	{
		super(NAME, deity, REPEAT, DETAILS, new Listener()
		{
			@EventHandler(priority = EventPriority.MONITOR)
			private void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
			{
				if(Zones.inNoDemigodsZone(event.getPlayer().getLocation()) || !(event.getRightClicked() instanceof Player)) return;
				Player player = event.getPlayer();
				Player clicked = (Player) event.getRightClicked();

				if(Deity.Util.canUseDeitySilent(clicked, deity) && (!needsLead || clicked.getItemInHand().getType().equals(Material.LEASH)) && clicked.getPassenger() == null)
				{
					DemigodsCharacter character = DemigodsCharacter.of(player);
					DemigodsCharacter clickedChar = DemigodsCharacter.of(clicked);
					if(character == null || clickedChar == null || !character.alliedTo(clickedChar)) return;

					clicked.setPassenger(player);
				}
			}

			@EventHandler(priority = EventPriority.HIGHEST)
			private void onPlayerItemHeld(PlayerItemHeldEvent event)
			{
				Player player = event.getPlayer();
				if(Zones.inNoDemigodsZone(player.getLocation())) return;

				if(!Deity.Util.canUseDeitySilent(player, deity) || player.getPassenger() == null) return;

				if(needsLead && player.getInventory().getItem(event.getNewSlot()) != null && !player.getInventory().getItem(event.getNewSlot()).getType().equals(Material.LEASH)) player.getPassenger().leaveVehicle();
				else if(player.getInventory().getItem(event.getNewSlot()) != null) player.getPassenger().leaveVehicle();
			}
		}, null);
	}
}
