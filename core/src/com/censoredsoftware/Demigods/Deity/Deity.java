package com.censoredsoftware.Demigods.Deity;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface Deity
{
	public ArrayList<Material> getClaimItems();

	public ArrayList<String> getInfo(Player player);

	public String loadDeity(Plugin plugin);

	public ArrayList<String> getCommands();

	public String getName();

	public String getAlliance();

	public ChatColor getColor();
}
