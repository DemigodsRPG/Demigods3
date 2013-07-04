package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterBetrayCharacterEvent;
import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterKillCharacterEvent;
import com.censoredsoftware.Demigods.Engine.Object.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Utility.TextUtility;

public class CharacterListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCharacterKillCharacter(CharacterKillCharacterEvent event)
	{
		PlayerCharacter attacker = event.getCharacter();
		PlayerCharacter killed = event.getKilled();
		String attackerAlliance = "Mortal";
		if(attacker != null) attackerAlliance = attacker.getAlliance();
		String killedAlliance = "Mortal";
		if(killed != null) killedAlliance = killed.getAlliance();

		attacker.addKill();

		if(killed == null && attacker == null) Demigods.message.broadcast(Demigods.text.getText(TextUtility.Text.MORTAL_SLAIN_1));
		else if(killed == null && attacker != null) Demigods.message.broadcast(Demigods.text.getText(TextUtility.Text.MORTAL_SLAIN_2).replace("{attacker}", ChatColor.YELLOW + attacker.getName() + ChatColor.GRAY).replace("{attackerAlliance}", attackerAlliance));
		else if(killed != null && attacker == null) Demigods.message.broadcast(ChatColor.GRAY + Demigods.text.getText(TextUtility.Text.DEMI_SLAIN_1).replace("{killed}", ChatColor.YELLOW + killed.getName() + ChatColor.GRAY).replace("{killedAlliance}", killedAlliance));
		else if(killed != null && attacker != null) Demigods.message.broadcast(ChatColor.GRAY + Demigods.text.getText(TextUtility.Text.DEMI_SLAIN_2).replace("{killed}", ChatColor.YELLOW + killed.getName() + ChatColor.GRAY).replace("{killedAlliance}", killedAlliance).replace("{attacker}", ChatColor.YELLOW + attacker.getName() + ChatColor.GRAY).replace("{attackerAlliance}", attackerAlliance));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCharacterBetrayCharacter(CharacterBetrayCharacterEvent event)
	{
		PlayerCharacter attacker = event.getCharacter();
		PlayerCharacter killed = event.getKilled();
		String alliance = event.getAlliance();

		// TODO: Punishments.

		if(!alliance.equals("Mortal")) Demigods.message.broadcast(ChatColor.GRAY + Demigods.text.getText(TextUtility.Text.DEMI_BETRAY).replace("{killed}", ChatColor.YELLOW + killed.getName() + ChatColor.GRAY).replace("{attacker}", ChatColor.YELLOW + attacker.getName() + ChatColor.GRAY).replace("{alliance}", alliance));
		else Demigods.message.broadcast(ChatColor.GRAY + Demigods.text.getText(TextUtility.Text.MORTAL_BETRAY));
	}
}
