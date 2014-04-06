package demigods.shaded.com.mojang.api.profiles;

public interface ProfileRepository
{
	public demigods.shaded.com.mojang.api.profiles.Profile[] findProfilesByCriteria(ProfileCriteria... criteria);
}
