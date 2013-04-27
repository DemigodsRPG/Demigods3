package com.censoredsoftware.Demigods.Deity;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface Deity
{
	public List<Material> getClaimItems();

	public List<String> getInfo(Player player);

	public String loadDeity();

	public List<String> getCommands();

	public String getName();

	public String getAlliance();

	public ChatColor getColor();
}
