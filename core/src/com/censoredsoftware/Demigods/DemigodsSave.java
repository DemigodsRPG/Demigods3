package com.censoredsoftware.Demigods;

import java.io.File;

import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Modules.DataPersistence.YAMLPersistenceModule;
import com.censoredsoftware.Objects.Character.PlayerCharacter;

public class DemigodsSave
{
	private Plugin plugin;
	boolean yaml;

	public DemigodsSave(Plugin instance, boolean yaml)
	{
		this.plugin = instance;
		this.yaml = yaml;
	}

	public void save(PlayerCharacter character)
	{
		if(yaml)
		{
			String charIDString = String.valueOf(character.getID());

			// Main Data
			YAMLPersistenceModule mainYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator, charIDString);

			// Inventory TODO Make into one file somehow?
			YAMLPersistenceModule invYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "main");
			YAMLPersistenceModule invHelmetYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "helmet");
			YAMLPersistenceModule invHelmetEnchYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "helmet_ench");
			YAMLPersistenceModule invChestPlateYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "chest_plate");
			YAMLPersistenceModule invChestPlateEnchYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "chest_plate_ench");
			YAMLPersistenceModule invLeggingsYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "leggings");
			YAMLPersistenceModule invLeggingsEnchYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "leggings_ench");
			YAMLPersistenceModule invBootsYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "boots");
			YAMLPersistenceModule invBootsEnchYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "boots_ench");

			// Location
			YAMLPersistenceModule locYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator, charIDString + "_loc");

			// Class Related
			YAMLPersistenceModule classYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator, charIDString + "_class");
			YAMLPersistenceModule abilYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator, charIDString + "_abil");
			YAMLPersistenceModule bindYAML = new YAMLPersistenceModule(false, plugin, "character" + File.separator, charIDString + "_bind");

			// Save the Data
			mainYAML.save(character.grabPlayerCharacterData());
			invYAML.save(character.getInventory().grabItems());
			invHelmetYAML.save(character.getInventory().getHelmet().grabSpecialItemStackData());
			invHelmetEnchYAML.save(character.getInventory().getHelmet().grabEnchantmentsData());
			invChestPlateYAML.save(character.getInventory().getChestPlate().grabSpecialItemStackData());
			invChestPlateEnchYAML.save(character.getInventory().getChestPlate().grabEnchantmentsData());
			invLeggingsYAML.save(character.getInventory().getLeggings().grabSpecialItemStackData());
			invLeggingsEnchYAML.save(character.getInventory().getLeggings().grabEnchantmentsData());
			invBootsYAML.save(character.getInventory().getBoots().grabSpecialItemStackData());
			invBootsEnchYAML.save(character.getInventory().getBoots().grabEnchantmentsData());
			locYAML.save(character.getLocation().grabSpecialLocationData());
			classYAML.save(character.getCharacterClass().grabPlayerCharacterClassData());
			abilYAML.save(character.getCharacterClass().grabPlayerCharacterAbilityData());
			bindYAML.save(character.getCharacterClass().grabPlayerCharacterBindingData());

			// Example of a PlayerCharacter with ID 77777:
			// DIR: plugins/Demigods/data/character/
			// 77777.yml <- Main Data
			// 77777_loc.yml <- Location Data
			// 77777_class.yml <- Class Data
			// 77777_abil.yml <- Ability Data
			// 77777_bind.yml <- Bindings Data
			// 77777/inventory/
			// main.yml <- Main Inventory Data
			// helmet.yml <- Helmet
			// helmet_ench.yml <- Helmet Enchantments
			// chest_plate.yml <- Chest Plate
			// chest_plate_ench.yml <- Chest Plate Enchantments
			// leggings.yml <- Leggings
			// leggings_ench.yml <- Leggings Enchantments
			// boots.yml <- Boots
			// boots_ench.yml <- Boots Enchantments
		}
	}

	public void load(int charID)
	{
		if(yaml)
		{
			String charIDString = String.valueOf(charID);

			// Main Data
			YAMLPersistenceModule mainYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator, charIDString);

			// Inventory TODO Make into one file somehow?
			YAMLPersistenceModule invYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "main");
			YAMLPersistenceModule invHelmetYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "helmet");
			YAMLPersistenceModule invHelmetEnchYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "helmet_ench");
			YAMLPersistenceModule invChestPlateYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "chest_plate");
			YAMLPersistenceModule invChestPlateEnchYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "chest_plate_ench");
			YAMLPersistenceModule invLeggingsYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "leggings");
			YAMLPersistenceModule invLeggingsEnchYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "leggings_ench");
			YAMLPersistenceModule invBootsYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "boots");
			YAMLPersistenceModule invBootsEnchYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator + charIDString + File.separator + "inventory" + File.separator, "boots_ench");

			// Location
			YAMLPersistenceModule locYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator, charIDString + "_loc");

			// Class Related
			YAMLPersistenceModule classYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator, charIDString + "_class");
			YAMLPersistenceModule abilYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator, charIDString + "_abil");
			YAMLPersistenceModule bindYAML = new YAMLPersistenceModule(true, plugin, "character" + File.separator, charIDString + "_bind");
		}
	}
}
