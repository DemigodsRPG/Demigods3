package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import java.util.HashMap;
import java.util.Map;

import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Modules.Data.DataStubModule;

public class PlayerCharacterAbilities implements DataStubModule
{
	private Map<String, Object> abilityData;

	public PlayerCharacterAbilities(Map map)
	{
		setMap(map);
		save(this);
	}

	public PlayerCharacterAbilities(int id)
	{
		abilityData = new HashMap();
		saveData("ID", id);
	}

	static void save(PlayerCharacterAbilities abilities)
	{
		DemigodsData.characterAbilityData.saveData(abilities.getID(), abilities);
	}

	public boolean containsKey(String key)
	{
		return abilityData.get(key) != null && abilityData.containsKey(key);
	}

	public Object getData(String key)
	{
		return abilityData.get(key);
	}

	public void saveData(String key, Object data)
	{
		abilityData.put(key, data);
	}

	public void removeData(String key)
	{
		if(!containsKey(key)) return;
		abilityData.remove(key);
	}

	@Override
	public int getID()
	{
		return Integer.parseInt(getData("ID").toString());
	}

	@Override
	public Map getMap()
	{
		return abilityData;
	}

	@Override
	public void setMap(Map map)
	{
		abilityData = map;
	}

	public boolean isEnabledAbility(String ability)
	{
		return containsKey(ability) && Boolean.parseBoolean(getData(ability).toString());
	}

	public void toggleAbility(String ability, boolean option)
	{
		saveData(ability, option);
	}
}
