package com.legit2.Demigods.Event.DivineBlock;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.legit2.Demigods.Libraries.DivineBlock;

/*
 * Represents an event that is called when a character is created.
 */
public class ShrineCreateEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
    protected OfflinePlayer owner;
    protected DivineBlock block;
    protected String alliance;
    protected String deity;

    public ShrineCreateEvent(final OfflinePlayer owner, final DivineBlock block, final String alliance, final String deity)
    {
        this.owner = owner;
        this.block = block;
        this.alliance = alliance;
        this.deity = deity;
    }
    
    /*
     * getOwner() : Gets the player.
     */
    public OfflinePlayer getOwner()
    {
        return this.owner;
    }
    
    /*
     * getDivineBlock() : Gets the Shrine's DivineBlock.
     */
    public DivineBlock getDivineBlock()
    {
        return this.block;
    }
    
    /*
     * getAlliance() : Gets the alliance involved.
     */
    public String getAlliance()
    {
        return this.alliance;
    }
    
    /*
     * getDeity() : Gets the deity involved.
     */
    public String getDeity()
    {
        return this.deity;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}