package com.legit2.Demigods.API;

import com.legit2.Demigods.Demigods;
import com.legit2.Demigods.Libraries.Objects.Altar;
import com.legit2.Demigods.Libraries.Objects.PlayerCharacter;
import com.legit2.Demigods.Libraries.Objects.SerialLocation;

import java.util.ArrayList;

public class WarpAPI
{
    private static final Demigods API = Demigods.INSTANCE;

    public ArrayList<SerialLocation> getWarps(PlayerCharacter character)
    {
        if(character == null || API.data.getCharData(character.getID(), "warps") == null) return null;
        return (ArrayList<SerialLocation>) API.data.getCharData(character.getID(), "warps");
    }

    public boolean hasWarp(Altar altar, PlayerCharacter character)
    {
        for(SerialLocation warp : getWarps(character))
        {
            if(API.zone.zoneAltar(warp.unserialize()) == altar) return true;
        }
        return false;
    }
}