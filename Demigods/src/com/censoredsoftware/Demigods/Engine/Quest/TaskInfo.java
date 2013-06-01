package com.censoredsoftware.Demigods.Engine.Quest;

import java.util.List;

public class TaskInfo
{
	private String name, quest, permission;
	private int order;
	private double award, penalty;
	private List<String> about, accepted, complete, failed;
	private Quest.Type type;
	private Subtype subtype;

	public TaskInfo(String name, String quest, String permission, int order, double award, double penalty, List<String> about, List<String> accepted, List<String> complete, List<String> failed, Quest.Type type, Subtype subtype)
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

	public List<String> getAbout()
	{
		return about;
	}

	public List<String> getAccepted()
	{
		return accepted;
	}

	public List<String> getComplete()
	{
		return complete;
	}

	public List<String> getFailed()
	{
		return failed;
	}

	public Quest.Type getType()
	{
		return type;
	}

	public Subtype getSubtype()
	{
		return subtype;
	}
}
