package com.censoredsoftware.Demigods.API;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.Libraries.BukkitDevDownload;

public class UpdateAPI
{
	private static Demigods API = Demigods.INSTANCE;
	private static BukkitDevDownload bukkitDev = new BukkitDevDownload("http://dev.bukkit.org/server-mods/demigods/files.rss");

	public boolean check()
	{
		return bukkitDev.updateNeeded();
	}

	public String getLatestVersion()
	{
		check();
		return bukkitDev.getVersion();
	}

	public boolean execute()
	{
		try
		{
			// Define variables
			byte[] buffer = new byte[1024];
			int read = 0;
			int bytesTransferred = 0;
			String downloadLink = bukkitDev.getJarLink();

			API.misc.info("Attempting to download latest version...");

			// Set latest build URL
			URL plugin = new URL(downloadLink);

			// Open connection to latest build and set user-agent for download, also determine file size
			URLConnection pluginCon = plugin.openConnection();
			pluginCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"); // FIXES 403 ERROR
			int contentLength = pluginCon.getContentLength();

			// Check for update directory
			File updateFolder = new File("plugins" + File.separator + Bukkit.getUpdateFolder());
			if(!updateFolder.exists()) updateFolder.mkdir();

			// Create new .jar file and add it to update directory
			File pluginUpdate = new File("plugins" + File.separator + Bukkit.getUpdateFolder() + File.separator + "Demigods.jar");
			API.misc.info("File will been written to: " + pluginUpdate.getCanonicalPath());

			InputStream is = pluginCon.getInputStream();
			OutputStream os = new FileOutputStream(pluginUpdate);

			while((read = is.read(buffer)) > 0)
			{
				os.write(buffer, 0, read);
				bytesTransferred += read;

				if(contentLength > 0)
				{
					// Determine percent of file and add it to variable
					int percentTransferred = (int) (((float) bytesTransferred / contentLength) * 100);

					if(percentTransferred != 100)
					{
						API.misc.info("Download progress: " + percentTransferred + "%");
					}
				}
			}

			is.close();
			os.flush();
			os.close();

			// Download complete!
			API.misc.info("Download complete!");
			API.misc.info("Update will complete on next server reload.");
			return true;
		}
		catch(MalformedURLException ex)
		{
			API.misc.warning("Error accessing URL: " + ex);
		}
		catch(FileNotFoundException ex)
		{
			API.misc.warning("Error accessing URL: " + ex);
		}
		catch(IOException ex)
		{
			API.misc.warning("Error downloading file: " + ex);
		}
		return false;
	}
}
