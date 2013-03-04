package com.legit2.Demigods.API;

import com.legit2.Demigods.Demigods;

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
