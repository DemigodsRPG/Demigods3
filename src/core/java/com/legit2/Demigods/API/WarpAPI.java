package com.legit2.Demigods.API;

import com.legit2.Demigods.Demigods;
import com.legit2.Demigods.Libraries.Objects.PlayerCharacter;
import com.legit2.Demigods.Libraries.Objects.SerialLocation;

import java.util.ArrayList;
import java.util.Map;

public class WarpAPI
{
    private static final Demigods API = Demigods.INSTANCE;

    public ArrayList<SerialLocation> getWarps(PlayerCharacter character)
    {
        ArrayList<SerialLocation> warps = new ArrayList<SerialLocation>();
        if(character == null || API.data.getAllWarps(character) == null) return null;
        for(Map.Entry<Integer, SerialLocation> warp : API.data.getAllWarps(character).entrySet())
        {
            if(!warps.contains(warp.getValue())) warps.add(warp.getValue());
        }
        return warps;
    }
}
