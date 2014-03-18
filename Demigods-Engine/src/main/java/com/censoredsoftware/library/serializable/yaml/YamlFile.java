package com.censoredsoftware.library.serializable.yaml;

/**
 * Interface for methods that all of our YamlFile classes share.
 */
public interface YamlFile
{
	/**
	 * Method to find the directory this file is located in.
	 * 
	 * @return The directory path.
	 */
	String getDirectoryPath();

	/**
	 * Method to find the entire name of this file, including the extension.
	 * 
	 * @return The full file name.
	 */
	String getFullFileName();

	/**
	 * This method is intended to use the getCurrentFileData() method to create a cache, or add data to an object.
	 */
	void loadDataFromFile();

	/**
	 * This method returns the data from the file in certain way.
	 * 
	 * @param <V> Some value that the file returns.
	 * @return The file's data.
	 */
	<V> V getCurrentFileData();

	/**
	 * Save all relevant data to the file.
	 * 
	 * @return Save was successful.
	 */
	boolean saveDataToFile();
}
