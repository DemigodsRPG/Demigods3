package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.censoredlib.data.player.Pet;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;

import java.util.Collection;
import java.util.UUID;

public class DPet extends Pet implements Participant
{
	public DPet()
	{
		super();
	}

	public DPet(UUID id, ConfigurationSection conf)
	{
		super(id, conf);
	}

	protected void save()
	{
		Util.save(this);
	}

	public void delete()
	{
		Data.pets.remove(getId());
	}

	public void setOwner(DCharacter owner)
	{
		setOwnerId(owner.getPlayerName(), owner.getId());
	}

	public DCharacter getOwner()
	{
		DCharacter owner = DCharacter.Util.load(super.getOwnerId());
		if(owner == null)
		{
			disownPet();
			delete();
			return null;
		}
		else if(!owner.isUsable()) return null;
		return owner;
	}

	public Deity getDeity()
	{
		if(getOwner() == null)
		{
			disownPet();
			delete();
			return null;
		}
		else if(!getOwner().isUsable()) return null;
		return getOwner().getDeity();
	}

	@Override
	public DCharacter getRelatedCharacter()
	{
		return getOwner();
	}

	public static class Util
	{
		public static DPet load(UUID id)
		{
			return Data.pets.get(id);
		}

		public static void save(DPet pet)
		{
			Data.pets.put(pet.getId(), pet);
		}

		public static DPet create(Tameable tameable, DCharacter owner)
		{
			if(owner == null) throw new IllegalArgumentException("Owner cannot be null.");
			if(!(tameable instanceof LivingEntity)) throw new IllegalArgumentException("Pet must be alive.");
			DPet wrapper = new DPet();
			wrapper.generateId();
			wrapper.setTamable((LivingEntity) tameable);
			wrapper.setOwner(owner);
			save(wrapper);
			return wrapper;
		}

		public static Collection<DPet> findByType(final EntityType type)
		{
			return Collections2.filter(Data.pets.values(), new Predicate<DPet>()
			{
				@Override
				public boolean apply(DPet pet)
				{
					return pet.getEntityType().equals(type.name());
				}
			});
		}

		public static Collection<DPet> findByTamer(final String animalTamer)
		{
			return Collections2.filter(Data.pets.values(), new Predicate<DPet>()
			{
				@Override
				public boolean apply(DPet pet)
				{
					return pet.getAnimalTamer().equals(animalTamer);
				}
			});
		}

		public static Collection<DPet> findByUUID(final UUID uniqueId)
		{
			return Collections2.filter(Data.pets.values(), new Predicate<DPet>()
			{
				@Override
				public boolean apply(DPet pet)
				{
					return pet.getEntityUUID().equals(uniqueId);
				}
			});
		}

		public static Collection<DPet> findByOwner(final UUID ownerId)
		{
			return Collections2.filter(Data.pets.values(), new Predicate<DPet>()
			{
				@Override
				public boolean apply(DPet pet)
				{
					return pet.getOwnerId().equals(ownerId);
				}
			});
		}

		public static DPet getPet(LivingEntity tameable)
		{
			if(!(tameable instanceof Tameable)) throw new IllegalArgumentException("LivingEntity not tamable.");
			return Iterables.getFirst(findByUUID(tameable.getUniqueId()), null);
		}

		public static void disownPets(String animalTamer)
		{
			for(DPet wrapper : findByTamer(animalTamer))
			{
				if(wrapper.getEntity() == null) continue;
				((Tameable) wrapper.getEntity()).setOwner(new AnimalTamer()
				{
					@Override
					public String getName()
					{
						return "Disowned";
					}
				});
			}
		}

		public static void reownPets(AnimalTamer tamer, DCharacter character)
		{
			for(DPet wrapper : findByTamer(character.getName()))
				if(wrapper.getEntity() != null) ((Tameable) wrapper.getEntity()).setOwner(tamer);
		}
	}
}
