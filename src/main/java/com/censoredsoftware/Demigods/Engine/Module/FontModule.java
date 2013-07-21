package com.censoredsoftware.Demigods.Engine.Module;

import java.util.List;

import com.google.common.collect.Lists;

public class FontModule
{
	private List<Integer> charWidths;
	private List<Character> charMapIndex;

	public FontModule()
	{
		charWidths = Lists.newArrayList();
		charMapIndex = Lists.newArrayList();

		charWidths.add(0);
		charMapIndex.add(null);

		for(int i = 1; i < 256; i++)
		{
			charWidths.add(5);
			charMapIndex.add((char) i);
		}

		int charWidths8[] = { 1, 2, 8, 10, 11, 13, 14, 15, 20, 21, 23, 29, 30, 31, 176, 177, 178, 193, 194, 196, 197, 202, 203, 205, 206, 207, 208, 209, 210, 215, 216, 219, 220, 223, 236, 237, 251 };
		int charWidths7[] = { 3, 4, 5, 6, 16, 17, 18, 19, 26, 27, 28, 182, 183, 185, 187, 188, 189, 204, 224, 227, 229, 230, 231, 234, 247 };
		int charWidths6[] = { 9, 12, 22, 24, 25, 64, 126, 169, 199, 200, 201, 211, 214, 225, 226, 228, 232, 235, 239, 240, 241, 242, 243, 246 };
		int charWidths4[] = { 7, 34, 40, 41, 42, 60, 62, 102, 107, 123, 125, 221, 222, 253, 254 };
		int charWidths3[] = { 32, 73, 91, 93, 116, 139, 158, 255 };
		int charWidths2[] = { 39, 96, 108, 141, 161, 179, 249, 250 };
		int charWidths1[] = { 33, 44, 46, 58, 59, 105, 124, 173 };

		overrideCharWidths(charWidths8, 8);
		overrideCharWidths(charWidths7, 7);
		overrideCharWidths(charWidths6, 6);
		overrideCharWidths(charWidths4, 4);
		overrideCharWidths(charWidths3, 3);
		overrideCharWidths(charWidths2, 2);
		overrideCharWidths(charWidths1, 1);
	}

	private void overrideCharWidths(int[] widthsArray, int toVal)
	{
		if((widthsArray != null) && (widthsArray.length > 0))
		{
			for(int index : widthsArray)
			{
				charWidths.set(index, toVal);
			}
		}
	}

	public int getStringWidth(String string)
	{
		int stringWidth = 0;
		String inputString = string.replaceAll("\u00A7.", "");
		char[] inputStringMap = inputString.toCharArray();

		if(string != null)
		{
			for(char c : inputStringMap)
			{
				stringWidth += (getCharWidth(c) + 1);
			}
		}
		return stringWidth;
	}

	public int getCharWidth(char c)
	{
		return getCharWidth(c, 5);
	}

	public int getCharWidth(char c, int defaultVal)
	{
		int k = charMapIndex.indexOf(c);
		if(c != '\247' && k >= 0) return charWidths.get(k);
		return defaultVal;
	}

	public int getChatBoxWidth()
	{
		return 318;
	}

	public int getRemainingChatWidth(String string)
	{
		return(getChatBoxWidth() - getStringWidth(string));
	}
}
