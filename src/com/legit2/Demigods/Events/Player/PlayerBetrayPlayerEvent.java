package com.legit2.Demigods.Events.Player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/*
 * Represents an event that is called when a player is killed by another player.
 */
public class PlayerBetrayPlayerEvent extends PlayerKillPlayerEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    protected Player killedPlayer;
    protected String alliance;
    boolean cancelled = false;

    public PlayerBetrayPlayerEvent(final Player attacker, final Player killedPlayer, final String alliance)
    {
        super(attacker, killedPlayer);
        this.alliance = alliance;
    }

    public boolean isCancelled()
    {
        return cancelled;
    }

    public void setCancelled(boolean cancel)
    {
        this.cancelled = cancel;
    }
    
    /*
     * getAlliance() : Gets the alliance involved.
     */
    public String getAlliance()
    {
        return this.alliance;
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