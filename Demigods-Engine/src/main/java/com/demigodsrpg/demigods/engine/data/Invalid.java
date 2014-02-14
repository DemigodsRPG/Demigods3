package com.demigodsrpg.demigods.engine.data;

import java.util.Map;

public class Invalid extends DataAccess<Void, Invalid>
{
	@Override
	protected Void getId()
	{
		throw new UnsupportedOperationException("Plugin tried accessing non-existent data type.");
	}

	@Override
	public Map<String, Object> serialize()
	{
		throw new UnsupportedOperationException("Plugin tried accessing non-existent data type.");
	}
}
