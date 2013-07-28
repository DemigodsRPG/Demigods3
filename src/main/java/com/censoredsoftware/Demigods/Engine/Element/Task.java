package com.censoredsoftware.Demigods.Engine.Element;

import java.util.ArrayList;

import org.bukkit.event.Listener;

public class Task
{
	private Info info;
	private Listener listener;

	public Task(Info info, Listener listener)
	{
		this.info = info;
		this.listener = listener;
	}

	public Info getInfo()
	{
		return info;
	}

	public Listener getListener()
	{
		return listener;
	}

	public static class Info
	{
		private String name, quest, permission;
		private int order;
		private double award, penalty;
		private java.util.List about, accepted, complete, failed;
		private List.Type type;
		private Subtype subtype;

		public Info(String name, String quest, String permission, int order, double award, double penalty, java.util.List about, java.util.List accepted, java.util.List complete, java.util.List failed, List.Type type, Subtype subtype)
		{
			this.name = name;
			this.quest = quest;
			this.permission = permission;
			this.order = order;
			this.award = award;
			this.penalty = penalty;
			this.about = about;
			this.accepted = accepted;
			this.complete = complete;
			this.failed = failed;
			this.type = type;
			this.subtype = subtype;
		}

		public enum Subtype
		{
			TECHNICAL, QUEST
		}

		public String getName()
		{
			return name;
		}

		public String getQuest()
		{
			return quest;
		}

		public String getPermission()
		{
			return permission;
		}

		public int getOrder()
		{
			return order;
		}

		public double getAward()
		{
			return award;
		}

		public double getPenalty()
		{
			return penalty;
		}

		public java.util.List getAbout()
		{
			return about;
		}

		public java.util.List getAccepted()
		{
			return accepted;
		}

		public java.util.List getComplete()
		{
			return complete;
		}

		public java.util.List getFailed()
		{
			return failed;
		}

		public List.Type getType()
		{
			return type;
		}

		public Subtype getSubtype()
		{
			return subtype;
		}
	}

	public static class List extends ArrayList<Task>
	{
		private String name, permission;
		private java.util.List about, accepted, complete, failed;
		private Type type;

		public List(String name, String permission, java.util.List about, java.util.List accepted, java.util.List complete, java.util.List failed, Type type, java.util.List tasks)
		{
			this.name = name;
			this.permission = permission;
			this.about = about;
			this.accepted = accepted;
			this.complete = complete;
			this.failed = failed;
			this.type = type;
			addAll(tasks);
		}

		public enum Type
		{
			PASSIVE, DEMO, TUTORIAL
		}

		public String getName()
		{
			return name;
		}

		public String getPermission()
		{
			return permission;
		}

		public java.util.List getAbout()
		{
			return about;
		}

		public java.util.List getAccepted()
		{
			return accepted;
		}

		public java.util.List getComplete()
		{
			return complete;
		}

		public java.util.List getFailed()
		{
			return failed;
		}

		public java.util.List getTasks()
		{
			return this;
		}

		public List.Type getType()
		{
			return type;
		}
	}
}
