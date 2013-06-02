package com.censoredsoftware.Demigods.Language;

import java.util.HashSet;
import java.util.Set;

public class English implements Translation
{
	@Override
	public int version()
	{
		return 1;
	}

	@Override
	public String translator()
	{
		return "Censored Software";
	}

	@Override
	public Set<Episode> episodes()
	{
		return new HashSet<Episode>()
		{
			{
				add(Episode.ENGINE);
				add(Episode.DEMO);
			}
		};
	}

	@Override
	public String getText(Episode episode, Text text)
	{
		// Default to English if no translation exists for this episode.
		if(!episodes().contains(episode)) return new English().getText(episode, text);

		// Translation
		if(text.equals(Text.TEXT)) return "This is a line of text.";

		// Throw NullPointerException if no translation exists.
		else throw new NullPointerException("No such translation.");
	}

}
