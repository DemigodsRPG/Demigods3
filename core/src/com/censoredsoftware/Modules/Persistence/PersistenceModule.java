package com.censoredsoftware.Modules.Persistence;

import java.util.List;

import com.censoredsoftware.Modules.Data.DataModule;
import com.censoredsoftware.Modules.Data.DataStubModule;

public interface PersistenceModule
{
	public boolean save(DataModule dataModule);

	public boolean save(DataStubModule stub);

	public boolean save(List stubs);

	public void load();
}
