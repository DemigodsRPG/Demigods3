package com.censoredsoftware.core.bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

import com.google.common.collect.Sets;

public class ColoredStringBuilder
{
	/**
	 * Data
	 */
	private Set<ChatColor> currentFormatting;
	private StringBuilder builder;
	private static final Set<Character> FORMATTING_CHARS = Sets.newHashSet('k', 'l', 'm', 'n', 'o', 'r');

	/**
	 * Constructor
	 */
	public ColoredStringBuilder()
	{
		currentFormatting = Sets.newHashSet();
		builder = new StringBuilder("");
	}

	/**
	 * Colors
	 */
	public ColoredStringBuilder black(String string)
	{
		append(string, ChatColor.BLACK);
		return this;
	}

	public ColoredStringBuilder darkBlue(String string)
	{
		append(string, ChatColor.DARK_BLUE);
		return this;
	}

	public ColoredStringBuilder darkGreen(String string)
	{
		append(string, ChatColor.DARK_GREEN);
		return this;
	}

	public ColoredStringBuilder darkAqua(String string)
	{
		append(string, ChatColor.DARK_AQUA);
		return this;
	}

	public ColoredStringBuilder darkRed(String string)
	{
		append(string, ChatColor.DARK_RED);
		return this;
	}

	public ColoredStringBuilder purple(String string)
	{
		append(string, ChatColor.DARK_PURPLE);
		return this;
	}

	public ColoredStringBuilder gold(String string)
	{
		append(string, ChatColor.GOLD);
		return this;
	}

	public ColoredStringBuilder gray(String string)
	{
		append(string, ChatColor.GRAY);
		return this;
	}

	public ColoredStringBuilder darkGray(String string)
	{
		append(string, ChatColor.DARK_GRAY);
		return this;
	}

	public ColoredStringBuilder blue(String string)
	{
		append(string, ChatColor.BLUE);
		return this;
	}

	public ColoredStringBuilder green(String string)
	{
		append(string, ChatColor.GREEN);
		return this;
	}

	public ColoredStringBuilder aqua(String string)
	{
		append(string, ChatColor.AQUA);
		return this;
	}

	public ColoredStringBuilder red(String string)
	{
		append(string, ChatColor.RED);
		return this;
	}

	public ColoredStringBuilder lightPurple(String string)
	{
		append(string, ChatColor.BLACK);
		return this;
	}

	public ColoredStringBuilder yellow(String string)
	{
		append(string, ChatColor.YELLOW);
		return this;
	}

	public ColoredStringBuilder white(String string)
	{
		append(string, ChatColor.WHITE);
		return this;
	}

	public ColoredStringBuilder color(String string, ChatColor color)
	{
		Validate.isTrue(color.isColor(), "Must be a color, not a format");
		append(string, color);
		return this;
	}

	/**
	 * Formatting
	 */
	public ColoredStringBuilder obfuscated()
	{
		currentFormatting.add(ChatColor.MAGIC);
		return this;
	}

	public ColoredStringBuilder bold()
	{
		currentFormatting.add(ChatColor.BOLD);
		return this;
	}

	public ColoredStringBuilder strikethrough()
	{
		currentFormatting.add(ChatColor.STRIKETHROUGH);
		return this;
	}

	public ColoredStringBuilder underline()
	{
		currentFormatting.add(ChatColor.UNDERLINE);
		return this;
	}

	public ColoredStringBuilder italic()
	{
		currentFormatting.add(ChatColor.ITALIC);
		return this;
	}

	public ColoredStringBuilder format(ChatColor format)
	{
		Validate.isTrue(format.isFormat(), "Must be a format, not a color");
		currentFormatting.add(ChatColor.ITALIC);
		return this;
	}

	public ColoredStringBuilder removeOfuscated()
	{
		currentFormatting.remove(ChatColor.MAGIC);
		return this;
	}

	public ColoredStringBuilder removeBold()
	{
		currentFormatting.remove(ChatColor.BOLD);
		return this;
	}

	public ColoredStringBuilder removeSrikethrough()
	{
		currentFormatting.remove(ChatColor.STRIKETHROUGH);
		return this;
	}

	public ColoredStringBuilder removeUnderline()
	{
		currentFormatting.remove(ChatColor.UNDERLINE);
		return this;
	}

	public ColoredStringBuilder removeItalic()
	{
		currentFormatting.remove(ChatColor.ITALIC);
		return this;
	}

	public ColoredStringBuilder removeFormat(ChatColor format)
	{
		Validate.isTrue(format.isFormat(), "Must be a format, not a color");
		currentFormatting.remove(ChatColor.ITALIC);
		return this;
	}

	public ColoredStringBuilder resetFormatting()
	{
		currentFormatting = Sets.newHashSet();
		return this;
	}

	/**
	 * Methods
	 */
	private void append(String string, ChatColor color)
	{
		boolean endsWithFormatting = endsWithFormatting(builder.toString());

		if(!endsWithFormatting) builder.append(ChatColor.RESET);
		if(!endsWithColor(builder.toString(), color)) builder.append(color);
		if(!endsWithFormatting) for(ChatColor formatting : currentFormatting)
			builder.append(formatting);

		builder.append(string);
	}

	private boolean endsWithColor(String string, ChatColor color)
	{
		List<String> hasList = getCharSet(getLastColors(string), false);
		if(hasList.isEmpty()) return false;

		hasList.removeAll(FORMATTING_CHARS);

		List<String> leftOvers = hasList;
		if(leftOvers.contains(color.getChar())) leftOvers.remove(color.getChar());

		if(hasList.contains(color.getChar())) return leftOvers.size() == 0;
		return false;
	}

	private boolean endsWithFormatting(String string)
	{
		List<String> hasList = getCharSet(getLastColors(string), true);
		if(hasList.isEmpty()) return false;

		List<String> leftOvers = hasList;
		leftOvers.removeAll(currentFormatting);

		if(hasList.containsAll(currentFormatting)) return leftOvers.size() == 0;
		return false;
	}

	public List<String> getCharSet(final String string, final boolean include)
	{
		return new ArrayList<String>()
		{
			{
				for(char c : string.toCharArray())
				{
					if(!include && c == '§') continue;
					add("" + c);
				}
			}
		};
	}

	public static String getLastColors(String string)
	{
		String result = "";
		int colors = 0;

		for(int i = string.length() - 1; i > -1; i--)
		{
			if(string.charAt(i) == '§' && i < string.length() - 1)
			{
				ChatColor color = ChatColor.getByChar(string.charAt(i + 1));

				if(color != null)
				{
					boolean isColor = color.isColor();
					if(isColor && colors == 1) continue;
					if(isColor) colors++;
					if(color.equals(ChatColor.RESET)) break;
					result = color.toString() + result;
				}
			}
		}

		return result;
	}

	public String build()
	{
		return builder.toString();
	}

	@Override
	public String toString()
	{
		return build();
	}
}
