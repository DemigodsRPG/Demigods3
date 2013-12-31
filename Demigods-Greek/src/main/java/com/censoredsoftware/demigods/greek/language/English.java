package com.censoredsoftware.demigods.greek.language;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public enum English
{
	NOTIFICATION_SHRINE_CREATED(new ArrayList<String>()
	{
		{
			add(ChatColor.GRAY + "" + ChatColor.ITALIC + "The {alliance} are pleased...");
			add(ChatColor.GREEN + "You have created a Shrine in the name of {deity}!");
		}
	}), NOTIFICATION_OBELISK_CREATED("You created an Obelisk!"), ALTAR_SPAWNED_NEAR("An Altar has spawned near you..."), ADMIN_WAND_GENERATE_ALTAR("Generating new Altar..."), ADMIN_WAND_GENERATE_ALTAR_COMPLETE("Altar created!"), ADMIN_WAND_REMOVE_ALTAR("Right-click this Altar again to remove it."), ADMIN_WAND_REMOVE_ALTAR_COMPLETE("Altar removed!"), ADMIN_WAND_REMOVE_SHRINE("Right-click this Shrine again to remove it."), ADMIN_WAND_REMOVE_SHRINE_COMPLETE("Shrine removed!"), ADMIN_WAND_REMOVE_OBELISK("Right-click this Obelisk again to remove it."), ADMIN_WAND_REMOVE_OBELISK_COMPLETE("Obelisk removed!");

	private String english;
	private List<String> englishBlock;

	private English(String english)
	{
		this.english = english;
		this.englishBlock = Lists.newArrayList();
	}

	private English(List<String> englishBlock)
	{
		this.english = "";
		this.englishBlock = englishBlock;
	}

	@Override
	public String toString()
	{
		return english;
	}

	public String getLine()
	{
		return english;
	}

	public List<String> getLines()
	{
		return englishBlock;
	}
}
