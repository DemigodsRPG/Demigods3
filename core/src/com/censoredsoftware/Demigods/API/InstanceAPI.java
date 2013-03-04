package com.censoredsoftware.Demigods.API;

import com.censoredsoftware.Demigods.Demigods;

public class InstanceAPI
{
	final Demigods instance;

	public InstanceAPI(Demigods instance)
	{
		this.instance = instance;
	}

	public Demigods getInstance()
	{
		return this.instance;
	}
}
