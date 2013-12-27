package com.censoredsoftware.demigods.panel

import com.censoredsoftware.demigods.engine.mythos.{Mythos, MythosPlugin}
import java.util
import com.censoredsoftware.censoredlib.trigger.Trigger
import org.bukkit.permissions.Permission
import org.bukkit.event.Listener
import com.censoredsoftware.demigods.engine.structure.Structure
import com.censoredsoftware.demigods.engine.deity.{Alliance, Deity}
import com.censoredsoftware.demigods.engine.item.DivineItem
import com.google.common.collect.Sets
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority

class DemigodsPanelPlugin extends MythosPlugin
{
  override def onEnable(): Unit =
  {
    Bukkit.getServer.getServicesManager.register(classOf[Mythos], this, this, ServicePriority.Highest)
  }

  override def onDisable(): Unit =
  {}

  override def getTitle: java.lang.String = "Panel"

  override def getTagline: java.lang.String = "The Official Demigods Web Panel!"

  override def getAuthor: java.lang.String = "_Alex & HmmmQuestionMark"

  override def isPrimary: java.lang.Boolean = Boolean.box(x = false)

  override def allowSecondary(): java.lang.Boolean = Boolean.box(x = true)

  override def getIncompatible: Array[java.lang.String] = new Array[java.lang.String](0)

  override def useBaseGame(): java.lang.Boolean = Boolean.box(x = true)

  override def getDivineItems: util.Collection[DivineItem] = Sets.newHashSet()

  override def getAlliances: util.Collection[Alliance] = Sets.newHashSet()

  override def getDeities: util.Collection[Deity] = Sets.newHashSet()

  override def getStructures: util.Collection[Structure] = Sets.newHashSet()

  override def levelSeperateSkills(): java.lang.Boolean = Boolean.box(x = true)

  override def getListeners: util.Collection[Listener] = Sets.newHashSet()

  override def getPermissions: util.Collection[Permission] = Sets.newHashSet()

  override def getTriggers: util.Collection[Trigger] = Sets.newHashSet()

  override def setSecondary(): Unit =
  {}

  override def lock(): Unit =
  {}
}