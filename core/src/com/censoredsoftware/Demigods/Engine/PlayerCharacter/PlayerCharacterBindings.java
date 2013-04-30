package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Modules.Data.DataStubModule;

public class PlayerCharacterBindings implements DataStubModule
{
	private Map<Object, Object> bindingData;

	public PlayerCharacterBindings(Map map)
	{
		setMap(map);
		save(this);
	}

	public PlayerCharacterBindings(int id)
	{
		bindingData = new HashMap();
		saveData("ID", id);
	}

	static void save(PlayerCharacterBindings bindings)
	{
		DemigodsData.characterBindingData.saveData(bindings.getID(), bindings);
	}

	public boolean containsKey(Object key)
	{
		return bindingData.get(key) != null && bindingData.containsKey(key);
	}

	public Object getData(Object key)
	{
		return bindingData.get(key);
	}

	public void saveData(Object key, Object data)
	{
		bindingData.put(key, data);
	}

	public void removeData(Object key)
	{
		if(!containsKey(key)) return;
		bindingData.remove(key);
	}

	@Override
	public int getID()
	{
		return Integer.parseInt(getData("ID").toString());
	}

	@Override
	public Map getMap()
	{
		return bindingData;
	}

	@Override
	public void setMap(Map map)
	{
		bindingData = map;
	}

	public boolean isBound(Material material)
	{
		return getBindings() != null && getBindings().contains(material);
	}

	public Material getBind(String ability)
	{
		for(int type : getBindings())
		{
			if(getData(type).toString().equalsIgnoreCase(ability)) return Material.getMaterial(type);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getBindings()
	{
		List<Integer> bindings = new ArrayList<Integer>();
		for(Object bind : bindingData.keySet())
		{
			if(!DemigodsData.isInt(bind.toString())) continue;
			bindings.add(Integer.parseInt(bind.toString()));
		}
		return bindings;
	}

	public void setBound(String ability, Material material)
	{
		// TODO None of the below should be in here, it should be in the place where the player/character can be grabbed.
		Player player = CharacterAPI.getChar(getID()).getOwner().getPlayer();
		if(!containsKey(ability))
		{
			if(player.getItemInHand().getType() == Material.AIR)
			{
				player.sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
			}
			else
			{
				if(isBound(material))
				{
					player.sendMessage(ChatColor.YELLOW + "That item is already bound to a skill.");
					return;
				}
				else if(material == Material.AIR)
				{
					player.sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
					return;
				}
				else
				{
					saveData(material.getId(), ability);
					player.sendMessage(ChatColor.YELLOW + ability + " is now bound to: " + material.name().toUpperCase());
					return;
				}
			}
		}
		else
		{
			removeBind(ability);
			player.sendMessage(ChatColor.YELLOW + ability + "'s bind has been removed.");
		}
	}

	public void removeBind(Material material)
	{
		if(containsKey(material.getId())) removeData(material.getId());
	}

	public void removeBind(String ability)
	{
		if(getBind(ability) != null) removeBind(getBind(ability));
	}
}
