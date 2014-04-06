package com.censoredsoftware.library.helper;

public abstract class CensoredCentralizedClass
{
	protected abstract int loadWorlds();

	protected abstract void loadListeners();

	protected abstract void loadCommands();

	protected abstract void loadPermissions(boolean load);
}
