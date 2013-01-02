package  com.legit2.Demigods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class DFlatFile
{
	private static String PATH;
	private static HashMap<String, HashMap<String, Object>> SAVEDDATA;
	
	/*
	 * HASHMAP OF PLAYER'S NAMES
	 * CONTAINS EACH PLAYER'S SAVED INFORMATION (IDENTIFIED BY STRINGS)
	 * BE VERY CAREFUL NOT TO SAVE THINGS THAT CAN'T BE WRITTEN
	 */
	
	public DFlatFile(String path) {
		PATH = path;
		SAVEDDATA = new HashMap<String, HashMap<String, Object>>();
		File f1 = new File(PATH + "Players/");
		if (!f1.exists()) {
			DUtil.info("Creating a new player save.");
			f1.mkdirs();
		}
		File[] list = f1.listFiles();
		for (File element : list) {
			String load = element.getName();
			if (load.endsWith(".player")) {
				load = load.substring(0, load.length() - 7);
				try {
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(element));
					Object result = ois.readObject();
					@SuppressWarnings("unchecked")
					HashMap<String, Object> cast = (HashMap<String, Object>) result;
					SAVEDDATA.put(load, cast);
					ois.close();
				} catch (Exception error) {
					DUtil.severe("Could not load player " + load);
					error.printStackTrace();
					DUtil.severe("End stack trace for " + load);
				}
			}
		}
	}

	/*
	 * Add a player's information to be saved, under the player's name.
	 */
	public static boolean addPlayer(Player p) {
		return addPlayer(p.getName()); // always use getName();
	}

	public static boolean addPlayer(String p) {
		if (hasPlayer(p))
			return false;
		SAVEDDATA.put(p, new HashMap<String, Object>());
		return true;
	}

	/*
	 * Get all of a player's data.
	 */
	public static HashMap<String, Object> getAllData(Player p) {
		return getAllData(p.getName());
	}

	public static HashMap<String, Object> getAllData(String p) {
		if (hasPlayer(p))
			return SAVEDDATA.get(p);
		return null;
	}

	public static HashMap<String, HashMap<String, Object>> getCompleteData() {
		return SAVEDDATA;
	}

	/*
	 * Get a specific piece of saved data, by id.
	 */
	public static Object getData(Player p, String id) {
		return getData(p.getName(), id);
	}

	public static Object getData(String p, String id) {
		if (hasData(p, id))
			return SAVEDDATA.get(p).get(id);
		return null;
	}

	public static String getPlayerSavePath() {
		return PATH + "Players/";
	}

	/*
	 * Check if the player has data saved under a certain id.
	 */
	public static boolean hasData(Player p, String id) {
		return hasData(p.getName(), id);
	}

	public static boolean hasData(String p, String id) {
		if (hasPlayer(p)) {
			if (SAVEDDATA.get(p).containsKey(id))
				return true;
		}
		return false;
	}

	/*
	 * Check if the player has saved information.
	 */
	public static boolean hasPlayer(Player p) {
		return hasPlayer(p.getName());
	}

	public static boolean hasPlayer(String p) {
		return SAVEDDATA.containsKey(p);
	}

	public static void overwrite(HashMap<String, HashMap<String, Object>> save) {
		SAVEDDATA = save;
	}

	/*
	 * Remove a specific piece of data by id
	 */
	public static Object removeData(Player p, String id) {
		return removeData(p.getName(), id);
	}

	public static Object removeData(String p, String id) {
		Object o = null;
		if (hasData(p, id)) {
			o = SAVEDDATA.get(p).remove(id);
		}
		return o;
	}

	public static void removeItem(String path) {
		(new File(path)).delete();
	}

	public static void removePlayer(Player p) {
		removePlayer(p.getName());
	}

	public static void removePlayer(String p) {
		if (SAVEDDATA.containsKey(p)) {
			SAVEDDATA.remove(p);
		}
	}

	/*
	 * Saves itself, but must be loaded elsewhere (main plugin).
	 */
	public static void save(String path) throws FileNotFoundException, IOException
	{
		(new File(path + "Players/")).mkdirs();
		
		for (String name : SAVEDDATA.keySet())
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path + "/Players/" + name + ".player"));
			
			oos.writeObject(SAVEDDATA.get(name));
			oos.flush();
			oos.close();
		}
	}

	/*
	 * Save data under a certain id.
	 */
	public static boolean saveData(Player p, String id, Object save) {
		return saveData(p.getName(), id, save);
	}

	public static boolean saveData(String p, String id, Object save) {
		if (!hasPlayer(p))
			return false;
		if (SAVEDDATA.get(p).containsKey(id))
			SAVEDDATA.get(p).remove(id); // remove if already there, to
											// overwrite
		SAVEDDATA.get(p).put(id, save);
		return true;
	}

	public static void saveItem(String path, Object item) {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(item);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

