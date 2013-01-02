package com.legit2.Demigods;

import java.io.IOException;
import java.util.ArrayList;

import com.legit2.Demigods.Metrics.Graph;

public class DMetrics
{
	static Demigods plugin;
	
	public DMetrics(Demigods d) 
	{
		plugin = d;
	}

	public static void allianceStatsPastWeek()
	{
		try
		{
		    Metrics metrics = new Metrics(plugin);
		
		    // New Graph
		    Graph graph = metrics.createGraph("Alliances for the Past Week");
		
		    // Gods
		    graph.addPlotter(new Metrics.Plotter("Gods")
		    {

		            @Override
		            public int getValue()
		            {
		            	int numGods;
		            	ArrayList<String> gods = new ArrayList<String>();
		            	
		            	if(DUtil.getImmortalList() == null) numGods = 0;
		            	else
		            	{
							for (String s : DUtil.getImmortalList())
							{
								if (DUtil.getAlliance(s) != null && DUtil.getAlliance(s).equalsIgnoreCase("god"))
								{
									if (DSave.hasData(s, "LASTLOGINTIME"))
									{
										if ((Long)DSave.getData(s, "LASTLOGINTIME") < System.currentTimeMillis()-604800000) continue;
									}
									gods.add(s);
								}
							}    
							numGods = gods.size();
		            	}
		            	return numGods; // Number of players who are in other alliances
		            }
		
		    });
		
		    // Titans
		    graph.addPlotter(new Metrics.Plotter("Titans")
		    {
		
		            @Override
		            public int getValue() {
		            	int numTitans;
		            	ArrayList<String> titans = new ArrayList<String>();
		            	
		            	if(DUtil.getImmortalList() == null) numTitans = 0;
		            	else
		            	{
							for (String s : DUtil.getImmortalList())
							{
								if (DUtil.getAlliance(s) != null && DUtil.getAlliance(s).equalsIgnoreCase("titan"))
								{
									if (DSave.hasData(s, "LASTLOGINTIME"))
									{
										if ((Long)DSave.getData(s, "LASTLOGINTIME") < System.currentTimeMillis()-604800000) continue;
									}
									titans.add(s);
								}
							}    
							numTitans = titans.size();
		            	}
		            	return numTitans; // Number of players who are in other alliances
		            }
		
		    });
		    
		    // Giants
		    graph.addPlotter(new Metrics.Plotter("Giants")
		    {
		
		            @Override
		            public int getValue() {
		            	int numGiants;
		            	ArrayList<String> giants = new ArrayList<String>();
		            	
		            	if(DUtil.getImmortalList() == null) numGiants = 0;
		            	else
		            	{
							for (String s : DUtil.getImmortalList())
							{
								if (DUtil.getAlliance(s) != null && DUtil.getAlliance(s).equalsIgnoreCase("giant"))
								{
									if (DSave.hasData(s, "LASTLOGINTIME"))
									{
										if ((Long)DSave.getData(s, "LASTLOGINTIME") < System.currentTimeMillis()-604800000) continue;
									}
									giants.add(s);
								}
							}  
							numGiants = giants.size();
		            	}
		            	return numGiants; // Number of players who are in other alliances
		            }
		
		    });
		    
		    // Other
		    graph.addPlotter(new Metrics.Plotter("Other")
		    {
		
		            @Override
		            public int getValue() {
		            	int numOthers;
		            	ArrayList<String> others = new ArrayList<String>();
		            	
		            	if(DUtil.getImmortalList() == null) numOthers = 0;
		            	else
		            	{
							for (String s : DUtil.getImmortalList())
							{
								if (DUtil.getAlliance(s) != null && !DUtil.getAlliance(s).equalsIgnoreCase("god") && (!DUtil.getAlliance(s).equalsIgnoreCase("titan")) && (!DUtil.getAlliance(s).equalsIgnoreCase("giant")))
								{
									if (DSave.hasData(s, "LASTLOGINTIME"))
									{
										if ((Long)DSave.getData(s, "LASTLOGINTIME") < System.currentTimeMillis()-604800000) continue;
									}
									others.add(s);
								}
							}    
							numOthers = others.size();
		            	}
		            	return numOthers; // Number of players who are in other alliances
		            }
		
		    });
		
		    metrics.start();
		}
		catch (IOException e)
		{
		    DUtil.severe(e.getMessage());
		}
	}
	
	public static void allianceStatsAllTime()
	{
		try
		{
		    Metrics metrics = new Metrics(plugin);
		
		    // New Graph
		    Graph graph = metrics.createGraph("Alliances for All Time");
		
		    // Gods
		    graph.addPlotter(new Metrics.Plotter("Gods")
		    {
		
		            @Override
		            public int getValue()
		            {
		            	int numGods;
		            	ArrayList<String> gods = new ArrayList<String>();
		            	
		            	if(DUtil.getImmortalList() == null) numGods = 0;
		            	else
		            	{
							for (String s : DUtil.getImmortalList())
							{
								if (DUtil.getAlliance(s) != null && DUtil.getAlliance(s).equalsIgnoreCase("god"))
								{
									gods.add(s);
								}
							}    
							numGods = gods.size();
		            	}
		            	return numGods; // Number of players who are in the God Alliance
		            }
		
		    });
		
		    // Titans
		    graph.addPlotter(new Metrics.Plotter("Titans")
		    {
		
		            @Override
		            public int getValue()
		            {
		            	int numTitans;
		            	ArrayList<String> titans = new ArrayList<String>();
		            	
		            	if(DUtil.getImmortalList() == null) numTitans = 0;
		            	else
		            	{
							for (String s : DUtil.getImmortalList())
							{
								if (DUtil.getAlliance(s) != null && DUtil.getAlliance(s).equalsIgnoreCase("titan"))
								{
									titans.add(s);
								}
							}
							numTitans = titans.size();
		            	}
		            	return numTitans; // Number of players who are in the Titan Alliance
		            }
		
		    });
		    
		    // Giants
		    graph.addPlotter(new Metrics.Plotter("Giants")
		    {
		
		            @Override
		            public int getValue()
		            {
		            	int numGiants;
		            	ArrayList<String> giants = new ArrayList<String>();
		            	
		            	if(DUtil.getImmortalList() == null) numGiants = 0;
		            	else
		            	{
							for (String s : DUtil.getImmortalList())
							{
								if (DUtil.getAlliance(s) != null && DUtil.getAlliance(s).equalsIgnoreCase("giant"))
								{
									giants.add(s);
								}
							}
							numGiants = giants.size();
		            	}
		            	return numGiants; // Number of players who are in the Giant Alliance
		            }
		
		    });
		    
		    // Other
		    graph.addPlotter(new Metrics.Plotter("Other")
		    {
		
		            @Override
		            public int getValue()
		            {
		            	int numOthers;
		            	ArrayList<String> others = new ArrayList<String>();
		            	
		            	if(DUtil.getImmortalList() == null) numOthers = 0;
		            	else
		            	{
							for (String s : DUtil.getImmortalList())
							{
								if (DUtil.getAlliance(s) != null && !DUtil.getAlliance(s).equalsIgnoreCase("god") && (!DUtil.getAlliance(s).equalsIgnoreCase("titan")) && (!DUtil.getAlliance(s).equalsIgnoreCase("giant")))
								{
									others.add(s);
								}
							}
							numOthers = others.size();
		            	}
		            	return numOthers; // Number of players who are in other alliances
		            }
		
		    });
		
		    metrics.start();
		}
		catch (IOException e)
		{
		    DUtil.severe(e.getMessage());
		}
	}
}