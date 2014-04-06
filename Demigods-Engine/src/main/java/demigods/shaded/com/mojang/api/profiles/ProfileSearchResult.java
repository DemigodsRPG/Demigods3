package demigods.shaded.com.mojang.api.profiles;

public class ProfileSearchResult
{
	private demigods.shaded.com.mojang.api.profiles.Profile[] profiles;
	private int size;

	public demigods.shaded.com.mojang.api.profiles.Profile[] getProfiles()
	{
		return profiles;
	}

	public void setProfiles(demigods.shaded.com.mojang.api.profiles.Profile[] profiles)
	{
		this.profiles = profiles;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}
}
