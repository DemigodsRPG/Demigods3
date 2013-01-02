package com.legit2.Demigods.Deities.Gods;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.legit2.Demigods.DUtil;
import com.legit2.Demigods.ReflectCommand;
import com.legit2.Demigods.Deities.Deity;

public class Zeus implements Deity, Listener
{
	private static final long serialVersionUID = 2242753324910371936L;

	private static final int SHOVECOST = 170;
	private static final int SHOVEDELAY = 1500; //milliseconds
	private static final int LIGHTNINGCOST = 140;
	private static final int LIGHTNINGDELAY = 1000; //milliseconds
	private static final int ZEUSULTIMATECOST = 3700;
	private static final int ZEUSULTIMATECOOLDOWNMAX = 600; //seconds
	private static final int ZEUSULTIMATECOOLDOWNMIN = 60;

	private static long ZEUSULTIMATETIME;
	private static long ZEUSSHOVETIME;
	private static long ZEUSLIGHTNINGTIME;
	private static Material SHOVEBIND = null;
	private static Material LIGHTNINGBIND = null;
	private static boolean LIGHTNING = false;
	private static boolean SHOVE = false;

	public Zeus()
	{
		ZEUSULTIMATETIME = System.currentTimeMillis();
		ZEUSSHOVETIME = System.currentTimeMillis();
		ZEUSLIGHTNINGTIME = System.currentTimeMillis();
	}
	@Override
	public String getAlliance()
	{
		return "God";
	}
	
	@Override
	public ArrayList<Material> getClaimItems()
	{
		ArrayList<Material> claimItems = new ArrayList<Material>();
		claimItems.add(Material.IRON_INGOT);
		
		return claimItems;
	}
	
	@Override
	public void printInfo(Player player)
	{
		if(DUtil.hasDeity(player.getName(), getName()) && DUtil.isImmortal(player))
		{
			int devotion = DUtil.getDevotion(player.getName(), getName());
			/*
			 *  Calculate special values first
			 */
			
			//shove
			int targets = (int)Math.ceil(1.561*Math.pow(devotion, 0.128424));
			double multiply = 0.1753*Math.pow(devotion, 0.322917);
			
			//ultimate
			int t = (int)(ZEUSULTIMATECOOLDOWNMAX - ((ZEUSULTIMATECOOLDOWNMAX - ZEUSULTIMATECOOLDOWNMIN)*((double)DUtil.getAscensions(player.getName())/100)));
			
			/*
			 *  The printed text
			 */
			
			player.sendMessage("--"+ChatColor.GOLD+"Zeus"+ChatColor.GRAY+" ["+devotion+"]");
			player.sendMessage(":Immune to fall damage.");
			player.sendMessage(":Strike lightning at a target location. " + ChatColor.GREEN + "/lightning");
			player.sendMessage(ChatColor.YELLOW+"Costs "+LIGHTNINGCOST+" Favor.");
			if(LIGHTNINGBIND != null) player.sendMessage(ChatColor.AQUA+"    Bound to "+ LIGHTNINGBIND.name());
			else player.sendMessage(ChatColor.AQUA+"    Use /bind to bind this skill to an item.");
			player.sendMessage(":Use the force of wind to knock back enemies. "+ChatColor.GREEN+"/shove");
			player.sendMessage(ChatColor.YELLOW+"Costs "+SHOVECOST+" Favor.");
			player.sendMessage("Affects up to "+targets+" targets with power " + (int)(Math.round(multiply*10))+".");
			if(SHOVEBIND != null) player.sendMessage(ChatColor.AQUA+"    Bound to "+ SHOVEBIND.name());
			else player.sendMessage(ChatColor.AQUA+"    Use /bind to bind this skill to an item.");
			player.sendMessage(":Zeus strikes lightning on nearby enemies as they are");
			player.sendMessage("raised in the air and dropped. "+ChatColor.GREEN+"/storm");
			player.sendMessage(ChatColor.YELLOW+"Costs "+ZEUSULTIMATECOST+" Favor. Cooldown time: "+t+" seconds.");
			return;
		}
		player.sendMessage("--"+ChatColor.GOLD+"Zeus");
		player.sendMessage("Passive: Immune to fall damage.");
		player.sendMessage("Active: Strike lightning at a target location. "+ChatColor.GREEN+"/lightning");
		player.sendMessage(ChatColor.YELLOW+"Costs "+LIGHTNINGCOST+" Favor. Can bind.");
		player.sendMessage("Active: Use the force of wind to knock back enemies. "+ChatColor.GREEN+"/shove");
		player.sendMessage(ChatColor.YELLOW+"Costs "+SHOVECOST+" Favor. Can bind.");
		player.sendMessage("Ultimate: Zeus strikes lightning on nearby enemies as they are");
		player.sendMessage("raised in the air and dropped. "+ChatColor.GREEN+"/storm");
		player.sendMessage(ChatColor.YELLOW+"Costs "+ZEUSULTIMATECOST+" Favor. Has cooldown.");
		player.sendMessage(ChatColor.YELLOW+"Select item: iron ingot");
	}
	@Override
	public String getName()
	{
		return "Zeus";
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onEntityDamange(EntityDamageEvent damageEvent)
	{
		if(damageEvent.getEntity() instanceof Player)
		{
			Player player = (Player)damageEvent.getEntity();
			if(!DUtil.hasDeity(player.getName(), "Zeus") || !DUtil.isImmortal(player)) return;
			
			if(damageEvent.getCause()==DamageCause.FALL)
			{
				damageEvent.setDamage(0);
				return;
			}
			else if(damageEvent.getCause()==DamageCause.LIGHTNING)
			{
				damageEvent.setDamage(0);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onPlayerInteract(PlayerInteractEvent interactEvent)
	{
		Player player = interactEvent.getPlayer();
		
		if(!DUtil.hasDeity(player.getName(), "Zeus") || !DUtil.isImmortal(player)) return;
		
		if(SHOVE || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == SHOVEBIND)))
		{
			if(ZEUSSHOVETIME > System.currentTimeMillis()) return;
			
			ZEUSSHOVETIME = System.currentTimeMillis()+SHOVEDELAY;
			
			if(DUtil.getFavor(player.getName()) >= SHOVECOST)
			{
				shove(player);
				DUtil.setFavor(player.getName(), DUtil.getFavor(player.getName())-SHOVECOST);
				return;
			}
			else
			{
				player.sendMessage(ChatColor.YELLOW+"You do not have enough Favor.");
				DUtil.setData(player.getName(), "shove", false);
			}
		}
		if(LIGHTNING || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == LIGHTNINGBIND)))
		{
			if(ZEUSLIGHTNINGTIME > System.currentTimeMillis()) return;
			
			ZEUSLIGHTNINGTIME = System.currentTimeMillis()+LIGHTNINGDELAY;
			
			if(DUtil.getFavor(player.getName()) >= LIGHTNINGCOST)
			{
				lightning(player);
				DUtil.setFavor(player.getName(), DUtil.getFavor(player.getName())-LIGHTNINGCOST);
				return;
			}
			else
			{
				player.sendMessage(ChatColor.YELLOW+"You do not have enough Favor.");
				DUtil.setData(player.getName(), "lightning", false);
			}
		}
	}
	
	@Override
	public void onEvent(Event event)
	{
		if(event instanceof EntityDamageEvent)
		{
			EntityDamageEvent damageEvent = (EntityDamageEvent)event;
			if(damageEvent.getEntity() instanceof Player)
			{
				Player player = (Player)damageEvent.getEntity();
				if(!DUtil.hasDeity(player.getName(), getName()) || !DUtil.isImmortal(player)) return;
				
				if(damageEvent.getCause()==DamageCause.FALL)
				{
					damageEvent.setDamage(0);
					return;
				}
				else if(damageEvent.getCause()==DamageCause.LIGHTNING)
				{
					damageEvent.setDamage(0);
					return;
				}
			}
		}
		else if(event instanceof PlayerInteractEvent)
		{
			PlayerInteractEvent interactEvent = (PlayerInteractEvent)event;
			Player player = interactEvent.getPlayer();
			
			if(!DUtil.hasDeity(player.getName(), "Zeus") || !DUtil.isImmortal(player)) return;
			
			if(SHOVE || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == SHOVEBIND)))
			{
				if(ZEUSSHOVETIME > System.currentTimeMillis()) return;
				
				ZEUSSHOVETIME = System.currentTimeMillis()+SHOVEDELAY;
				
				if(DUtil.getFavor(player.getName()) >= SHOVECOST)
				{
					shove(player);
					DUtil.setFavor(player.getName(), DUtil.getFavor(player.getName())-SHOVECOST);
					return;
				}
				else
				{
					player.sendMessage(ChatColor.YELLOW+"You do not have enough Favor.");
					DUtil.setData(player.getName(), "shove", false);
				}
			}
			if(LIGHTNING || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == LIGHTNINGBIND)))
			{
				if(ZEUSLIGHTNINGTIME > System.currentTimeMillis()) return;
				
				ZEUSLIGHTNINGTIME = System.currentTimeMillis()+LIGHTNINGDELAY;
				
				if(DUtil.getFavor(player.getName()) >= LIGHTNINGCOST)
				{
					lightning(player);
					DUtil.setFavor(player.getName(), DUtil.getFavor(player.getName())-LIGHTNINGCOST);
					return;
				}
				else
				{
					player.sendMessage(ChatColor.YELLOW+"You do not have enough Favor.");
					DUtil.setData(player.getName(), "lightning", false);
				}
			}
		}
	}
	
	/*
	 *  Commands
	 */
	@ReflectCommand.Command(name = "lightning", sender = ReflectCommand.Sender.PLAYER, permission = "demigods.god.zeus")
	public static void lightningCommand(Player player, String arg1)
	{
		if(!DUtil.hasDeity(player.getName(), "Zeus")) return;
		
		if(arg1.equalsIgnoreCase("bind"))
		{
			if(LIGHTNINGBIND == null)
			{
				if(DUtil.isBound(player, player.getItemInHand().getType())) player.sendMessage(ChatColor.YELLOW+"That item is already bound to a skill.");
				if(player.getItemInHand().getType() == Material.AIR) player.sendMessage(ChatColor.YELLOW+"You cannot bind a skill to air.");
				else
				{
					DUtil.setBind(player, player.getItemInHand().getType());
					LIGHTNINGBIND = player.getItemInHand().getType();
					player.sendMessage(ChatColor.YELLOW+"Lightning is now bound to "+player.getItemInHand().getType().name()+".");
				}
			}
			else
			{
				DUtil.removeBind(player, LIGHTNINGBIND);
				player.sendMessage(ChatColor.YELLOW+"Lightning is no longer bound to "+LIGHTNINGBIND.name()+".");
				LIGHTNINGBIND = null;
			}
		}
		
		if(DUtil.getData(player.getName(), "lightning") != null && (Boolean) DUtil.getData(player.getName(), "lightning")) 
		{
			DUtil.setData(player.getName(), "lightning", false);
			player.sendMessage(ChatColor.YELLOW+"Lightning is no longer active.");
		}
		else
		{
			DUtil.setData(player.getName(), "lightning", true);
			player.sendMessage(ChatColor.YELLOW+"Lightning is now active.");
		}
	}
	
	@ReflectCommand.Command(name = "shove", sender = ReflectCommand.Sender.PLAYER, permission = "demigods.god.zeus")
	public static void shoveCommand(Player player, String[] args)
	{
		if(!DUtil.hasDeity(player.getName(), "Zeus")) return;
		
		if(args.length == 1 && args[0].equalsIgnoreCase("bind"))
		{
			if(SHOVEBIND == null)
			{
				if(DUtil.isBound(player, player.getItemInHand().getType())) player.sendMessage(ChatColor.YELLOW+"That item is already bound to a skill.");
				if(player.getItemInHand().getType() == Material.AIR) player.sendMessage(ChatColor.YELLOW+"You cannot bind a skill to air.");
				else
				{
					DUtil.setBind(player, player.getItemInHand().getType());
					SHOVEBIND = player.getItemInHand().getType();
					player.sendMessage(ChatColor.YELLOW+"Shove is now bound to "+player.getItemInHand().getType().name()+".");
				}
			}
			else
			{
				DUtil.removeBind(player, SHOVEBIND);
				player.sendMessage(ChatColor.YELLOW+"Shove is no longer bound to "+SHOVEBIND.name()+".");
				SHOVEBIND = null;
			}
		}
		
		if(DUtil.getData(player.getName(), "shove") != null && (Boolean) DUtil.getData(player.getName(), "shove"))
		{
			DUtil.setData(player.getName(), "shove", false);
			player.sendMessage(ChatColor.YELLOW+"Shove is no longer active.");
		}
		else
		{
			DUtil.setData(player.getName(), "shove", true);
			player.sendMessage(ChatColor.YELLOW+"Shove is now active.");
		}
	}
	
	@ReflectCommand.Command(name = "storm", sender = ReflectCommand.Sender.PLAYER, permission = "demigods.god.zeus.ultimate")
	public static void stormCommand(Player player)
	{
		if(!DUtil.hasDeity(player.getName(), "Zeus")) return;
		long TIME = ZEUSULTIMATETIME;
		if(System.currentTimeMillis() < TIME)
		{
			player.sendMessage(ChatColor.YELLOW+"You cannot use the lightning storm again for "+((((TIME)/1000)-(System.currentTimeMillis()/1000)))/60+" minutes");
			player.sendMessage(ChatColor.YELLOW+"and "+((((TIME)/1000)-(System.currentTimeMillis()/1000))%60)+" seconds.");
			return;
		}
		if(DUtil.getFavor(player.getName())>=ZEUSULTIMATECOST)
		{
			if(!DUtil.canPVP(player.getLocation()))
			{
				player.sendMessage(ChatColor.YELLOW+"You can't do that from a no-PVP zone.");
				return;
			}
			int t = (int)(ZEUSULTIMATECOOLDOWNMAX - ((ZEUSULTIMATECOOLDOWNMAX - ZEUSULTIMATECOOLDOWNMIN)*((double)DUtil.getAscensions(player.getName())/100)));
			int num = storm(player);
			if(num > 0)
			{
				player.sendMessage("In exchange for "+ChatColor.AQUA+ZEUSULTIMATECOST+ChatColor.WHITE+" Favor, ");
				player.sendMessage(ChatColor.GOLD+"Zeus"+ChatColor.WHITE+" has unloaded his wrath on "+num+" targets.");
				DUtil.setFavor(player.getName(), DUtil.getFavor(player.getName())-ZEUSULTIMATECOST);
				player.setNoDamageTicks(1000);
				ZEUSULTIMATETIME = System.currentTimeMillis()+t*1000;
			} else player.sendMessage(ChatColor.YELLOW+"There are no targets nearby.");
		} else player.sendMessage(ChatColor.YELLOW+"Lightning storm requires "+ZEUSULTIMATECOST+" Favor.");
		return;
	}
	
	/*
	 * ---------------
	 * Helper methods
	 * ---------------
	 */
	private static void shove(Player player)
	{
		if(!DUtil.canPVP(player.getLocation()))
		{
			player.sendMessage(ChatColor.YELLOW+"You can't do that from a no-PVP zone.");
			return;
		}
		
		ArrayList<LivingEntity> hit = new ArrayList<LivingEntity>();
		
		int devotion = DUtil.getDevotion(player.getName(), "Zeus");
		int targets = (int)Math.ceil(1.561*Math.pow(devotion, 0.128424));
		double multiply = 0.1753*Math.pow(devotion, 0.322917);
		
		for (Block b : player.getLineOfSight(null, 10))
		{
			for (LivingEntity le : player.getWorld().getLivingEntities())
			{
				if(targets == hit.size()) break;
				
				if(le instanceof Player)
				{
					if(DUtil.areAllied(player.getName(), ((Player) le).getName())) continue;
				}
				if((le.getLocation().distance(b.getLocation()) <= 5) && !hit.contains(le))
				{
					if(DUtil.canPVP(le.getLocation())) hit.add(le);
				}
			}
		}
		
		if(hit.size() > 0)
		{
			for (LivingEntity le : hit)
			{
				Vector v = player.getLocation().toVector();
				Vector victor = le.getLocation().toVector().subtract(v); //HAHAHAHA
				victor.multiply(multiply);
				le.setVelocity(victor);
			}
		}
	}
	
	private static void lightning(Player player)
	{
		if(!DUtil.canPVP(player.getLocation()))
		{
			player.sendMessage(ChatColor.YELLOW+"You can't do that from a no-PVP zone.");
			return;
		}
		
		Location target = null;
		Block b = player.getTargetBlock(null, 200);
		target = b.getLocation();
		
		if(player.getLocation().distance(target) > 2)
		{
			try
			{
				strikeLightning(player, target);
			}
			catch (Exception e){} //Ignore errors
		}
		else player.sendMessage(ChatColor.YELLOW+"Your target is too far away, or too close to you.");
	}
	
	private static int storm(Player player)
	{
		ArrayList<Entity> entitylist = new ArrayList<Entity>();
		Vector ploc = player.getLocation().toVector();
		
		for (Entity anEntity : player.getWorld().getEntities())
		{
			if(anEntity.getLocation().toVector().isInSphere(ploc, 50.0)) entitylist.add(anEntity);
		}
		
		int count = 0;
		for (Entity eee : entitylist)
		{
			try
			{
				LivingEntity e1 = (LivingEntity)eee;
				if(e1 instanceof Player)
				{
					Player ptemp = (Player)e1;
					if(!DUtil.areAllied(player.getName(), ptemp.getName())&&!ptemp.equals(player))
					{
						strikeLightning(player, ptemp.getLocation());
						strikeLightning(player, ptemp.getLocation());
						strikeLightning(player, ptemp.getLocation());
						count++;
					}
				}
				else
				{
					count++;
					strikeLightning(player, e1.getLocation());
					strikeLightning(player, e1.getLocation());
					strikeLightning(player, e1.getLocation());
				}
			}
			catch (Exception e){} //Ignore non-living entities.
		}
		return count;
	}
	
	private static void strikeLightning(Player player, Location target)
	{
		if(!player.getWorld().equals(target.getWorld())) return;
		if(!DUtil.canPVP(target)) return;

		player.getWorld().strikeLightning(target);
		
		for (Entity e : target.getBlock().getChunk().getEntities())
		{
			if(e instanceof LivingEntity)
			{
				LivingEntity le = (LivingEntity)e;
				if(le.getLocation().distance(target) < 1.5)	DUtil.customDamage(player, le, DUtil.getAscensions(player.getName())*2);
			}
		}
	}
	@Override
	public void onTick(long timeSent) {}
}