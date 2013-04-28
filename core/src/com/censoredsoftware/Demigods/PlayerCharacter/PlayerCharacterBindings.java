package com.censoredsoftware.Demigods.PlayerCharacter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import com.censoredsoftware.Demigods.DemigodsData;
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

	public synchronized void setBound(String ability, Material material)
	{
		saveData(material.getId(), ability);

		// TODO None of the below should be in here, it should be in the place where the player/character can be grabbed.

		/**
		 * Player player = (Player) Bukkit.getOfflinePlayer(this.playerName);
		 * if(API.data.getCharData(this.charID, ability + "_bind") == null)
		 * {
		 * if(player.getItemInHand().getType() == Material.AIR)
		 * {
		 * player.sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
		 * }
		 * else
		 * {
		 * if(isBound(material))
		 * {
		 * player.sendMessage(ChatColor.YELLOW + "That item is already bound to a skill.");
		 * return false;
		 * }
		 * else if(material == Material.AIR)
		 * {
		 * player.sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
		 * return false;
		 * }
		 * else
		 * {
		 * if(API.data.hasCharData(this.charID, "bindings"))
		 * {
		 * ArrayList<Material> bindings = getBindings();
		 * if(!bindings.contains(material)) bindings.add(material);
		 * API.data.saveCharData(this.charID, "bindings", bindings);
		 * }
		 * else
		 * {
		 * ArrayList<Material> bindings = new ArrayList<Material>();
		 * bindings.add(material);
		 * API.data.saveCharData(this.charID, "bindings", bindings);
		 * }
		 * 
		 * 
		 * 
		 * API.data.saveCharData(this.charID, ability + "_bind", material);
		 * player.sendMessage(ChatColor.YELLOW + ability + " is now bound to: " + material.name().toUpperCase());
		 * return true;
		 * }
		 * }
		 * }
		 * else
		 * {
		 * removeBind(ability, ((Material) API.data.getCharData(this.charID, ability + "_bind")));
		 * player.sendMessage(ChatColor.YELLOW + ability + "'s bind has been removed.");
		 * }
		 * return false;
		 */
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
