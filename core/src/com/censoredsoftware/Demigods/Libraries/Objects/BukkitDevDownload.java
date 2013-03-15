/*
 * Originally made by github user @betterphp, adapted for API.  Used with permission.
 * https://github.com/betterphp/UpdateChecker/blob/master/uk/co/jacekk/bukkit/updatechecker/UpdateChecker.java
 */

package com.censoredsoftware.Demigods.Libraries.Objects;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.plugin.PluginDescriptionFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.censoredsoftware.Demigods.Demigods;

public class BukkitDevDownload
{
	private static final Demigods API = Demigods.INSTANCE;
	private URL filesFeed;
	private String version;
	private String link;
	private String jarLink;

	public BukkitDevDownload(String url)
	{
		try
		{
			this.filesFeed = new URL("http://dev.bukkit.org/server-mods/demigods/files.rss");
		}
		catch(Exception e)
		{
			API.misc.severe("Could not connect to BukkitDev.");
		}
	}

	public synchronized boolean updateNeeded()
	{
		try
		{
			InputStream input = this.filesFeed.openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);

			Node latestFile = document.getElementsByTagName("item").item(0);
			NodeList children = latestFile.getChildNodes();

			this.version = children.item(1).getTextContent().replaceAll("[a-zA-Z ]", "");
			try
			{
				this.link = children.item(3).getTextContent();
			}
			catch(Exception e)
			{
				API.misc.warning("Failed to find download page.");
			}
			input.close();

			try
			{
				input = (new URL(this.link)).openConnection().getInputStream();
			}
			catch(Exception e)
			{
				API.misc.warning("Failed to open connection with download page.");
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line;

			while((line = reader.readLine()) != null)
			{
				if(line.trim().startsWith("<li class=\"user-action user-action-download\">"))
				{
					this.jarLink = line.substring(line.indexOf("href=\"") + 6, line.lastIndexOf("\""));
					break;
				}
			}

			reader.close();
			input.close();

			PluginDescriptionFile pdf = API.getDescription();
			String currentVersion = pdf.getVersion();
			if(!currentVersion.equals(this.version)) return true;
		}
		catch(Exception e)
		{
			API.misc.warning("Failed to read download page.");
		}

		return false;
	}

	public String getVersion()
	{
		return this.version;
	}

	public String getLink()
	{
		return this.link;
	}

	public String getJarLink()
	{
		return this.jarLink;
	}
}
