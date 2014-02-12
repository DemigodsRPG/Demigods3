package com.demigodsrpg.demigods.engine.tribute;

import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class TributeData extends DataAccess<UUID, TributeData>
{
	private UUID id;
	private String category;
	private Material material;
	private int amount;

	private TributeData(Object ignored)
	{}

	public TributeData()
	{
		id = UUID.randomUUID();
	}

	public TributeData(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		category = conf.getString("category");
		material = Material.valueOf(conf.getString("material"));
		amount = Integer.parseInt(conf.getString("amount"));
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<>();
		map.put("category", category);
		map.put("material", material.name());
		map.put("amount", amount);
		return map;
	}

	public UUID getId()
	{
		return this.id;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public String getCategory()
	{
		return this.category;
	}

	public Material getMaterial()
	{
		return material;
	}

	public int getAmount()
	{
		return amount;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.id, this.category, this.material, this.amount);
	}

	@Override
	public boolean equals(Object other)
	{
		return other instanceof TributeData && Objects.equal(this.id, ((TributeData) other).getId());
	}

	private static final DataAccess<UUID, TributeData> DATA_ACCESS = new TributeData(null);

	public static TributeData get(UUID id)
	{
		return DATA_ACCESS.getDirect(id);
	}

	public static Collection<TributeData> all()
	{
		return DATA_ACCESS.getAll();
	}

	public static void save(String category, Material material, int amount)
	{
		// Remove the data if it exists already
		remove(category, material);

		// Create and save the tribute data
		TributeData tributeData = new TributeData();
		tributeData.setCategory(category);
		tributeData.setMaterial(material);
		tributeData.setAmount(amount);
		tributeData.save();
	}

	public static void remove(String category, Material material)
	{
		if(find(category, material) != null) find(category, material).remove();
	}

	public static TributeData find(String category, Material material)
	{
		if(findByCategory(category) == null) return null;

		for(TributeData data : findByCategory(category))
			if(data.getMaterial().name().equalsIgnoreCase(material.name())) return data;

		return null;
	}

	public static Set<TributeData> findByCategory(final String category)
	{
		return Sets.newHashSet(Collections2.filter(all(), new Predicate<TributeData>()
		{
			@Override
			public boolean apply(TributeData tributeData)
			{
				return tributeData.getCategory().equalsIgnoreCase(category);
			}
		}));
	}
}
