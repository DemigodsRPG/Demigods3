package com.censoredsoftware.demigods.panel

import com.censoredsoftware.demigods.engine.mythos.{Mythos, MythosPlugin}
import com.censoredsoftware.censoredlib.trigger.Trigger
import org.bukkit.permissions.Permission
import org.bukkit.event.Listener
import com.censoredsoftware.demigods.engine.structure.Structure
import com.censoredsoftware.demigods.engine.deity.{Alliance, Deity}
import com.censoredsoftware.demigods.engine.item.DivineItem
import com.google.common.collect.{ImmutableSet, ImmutableCollection}
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.inventory.ItemStack
import com.censoredsoftware.demigods.engine.item.DivineItem.Flag

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

  override def getDivineItems: ImmutableCollection[DivineItem] = ImmutableSet.of()

  override def getDivineItem(itemName: java.lang.String): DivineItem = Mythos.Util.getDivineItem(this, itemName)

  override def getDivineItem(itemStack: ItemStack): DivineItem = Mythos.Util.getDivineItem(this, itemStack)

  override def itemHasFlag(itemStack: ItemStack, flag: Flag): Boolean = Mythos.Util.itemHasFlag(this, itemStack, flag)

  override def getAlliances: ImmutableCollection[Alliance] = ImmutableSet.of()

  override def getAlliance(allianceName: java.lang.String): Alliance = Mythos.Util.getAlliance(this, allianceName)

  override def getDeities: ImmutableCollection[Deity] = ImmutableSet.of()

  override def getDeity(deityName: java.lang.String): Deity = Mythos.Util.getDeity(this, deityName)

  override def getStructures: ImmutableCollection[Structure] = ImmutableSet.of()

  override def getStructure(structureName: java.lang.String): Structure = Mythos.Util.getStructure(this, structureName)

  override def levelSeperateSkills(): java.lang.Boolean = Boolean.box(x = true)

  override def getListeners: ImmutableCollection[Listener] = ImmutableSet.of()

  override def getPermissions: ImmutableCollection[Permission] = ImmutableSet.of()

  override def getTriggers: ImmutableCollection[Trigger] = ImmutableSet.of()

  override def setSecondary(): Unit =
  {}

  override def lock(): Unit =
  {}
}