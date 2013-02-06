package com.legit2.Demigods.Events.Player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/*
 * Represents an event that is called when a player is killed by another player.
 */
public class PlayerKillPlayerEvent extends PlayerEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    protected Player killedPlayer;
    boolean cancelled = false;

    public PlayerKillPlayerEvent(final Player attacker, final Player killedPlayer)
    {
        super(attacker);
        this.killedPlayer = killedPlayer;
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
     * getKilled() : Gets the player that was killed by the player.
     */
    public Player getKilled()
    {
        return this.killedPlayer;
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