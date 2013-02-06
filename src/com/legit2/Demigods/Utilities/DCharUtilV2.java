package com.legit2.Demigods.Utilities;

import java.util.HashMap;

import com.legit2.Demigods.Libraries.DCharacter;

public class DCharUtilV2 
{
	public DCharacter getChar(int charID)
	{
		HashMap<Integer, HashMap<String, Object>> characters = DDataUtil.getAllChars();
		
		if(characters.containsKey(charID)) return (DCharacter) characters.get(charID).get("char_object");
		else return null;
	}
}
