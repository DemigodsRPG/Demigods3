package com.legit2.Demigods.Event.DivineBlock;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.legit2.Demigods.Libraries.DivineBlock;

/*
 * Represents an event that is called when a character is created.
 */
public class AltarCreateEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
    protected DivineBlock block;
    protected AltarCreateCause cause;

    public AltarCreateEvent(final DivineBlock block, final AltarCreateCause cause)
    {
        this.block = block;
        this.cause = cause;
    }
    
    /*
     * getDivineBlock() : Gets the Altar's ID.
     */
    public DivineBlock getDivineBlock()
    {
        return this.block;
    }
    
    /*
     * getCause() : Gets the Altar's creation cause;
     */
    public AltarCreateCause getCause()
    {
        return this.cause;
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
    
    public enum AltarCreateCause
    {
    	ADMIN_WAND,
    	GENERATED;
    }
}