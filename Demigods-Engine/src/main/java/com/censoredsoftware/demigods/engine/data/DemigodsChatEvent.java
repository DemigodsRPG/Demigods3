package com.censoredsoftware.demigods.engine.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

public class DemigodsChatEvent extends Event implements Cancellable
{
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean cancel = false;
	private String message;
	private Set<Player> recipients;

	public DemigodsChatEvent(String message)
	{
		this.message = message;
		this.recipients = Collections.synchronizedSet(Sets.newHashSet(Bukkit.getServer().getOnlinePlayers()));
	}

	public DemigodsChatEvent(String message, Collection<DCharacter> recipients)
	{
		this.message = message;
		this.recipients = Sets.newHashSet(Collections2.filter(Collections2.transform(recipients, new Function<DCharacter, Player>()
		{
			@Override
			public Player apply(DCharacter character)
			{
				return character.getOfflinePlayer().isOnline() ? character.getOfflinePlayer().getPlayer() : null;
			}
		}), new Predicate<Player>()
		{
			@Override
			public boolean apply(@Nullable Player player)
			{
				return player != null;
			}
		}));
	}

	public boolean isCancelled()
	{
		return cancel;
	}

	public void setCancelled(boolean cancel)
	{
		this.cancel = cancel;
	}

	public String getMessage()
	{
		return message;
	}

	public Set<Player> getRecipients()
	{
		return recipients;
	}

	public void setRecipients(Set<Player> recipients)
	{
		this.recipients = recipients;
	}

	@Override
	public HandlerList getHandlers()
	{
		return HANDLERS;
	}

	public static HandlerList getHandlerList()
	{
		return HANDLERS;
	}
}
