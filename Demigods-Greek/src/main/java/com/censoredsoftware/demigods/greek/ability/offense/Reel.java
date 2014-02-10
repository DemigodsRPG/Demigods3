package com.censoredsoftware.demigods.greek.ability.offense;

import com.censoredsoftware.demigods.engine.entity.player.DemigodsCharacter;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsPlayer;
import com.censoredsoftware.demigods.engine.entity.player.attribute.Skill;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.material.MaterialData;

import java.util.List;

public class Reel extends GreekAbility
{
	private static final String NAME = "Reel", COMMAND = "reel";
	private static final int COST = 120, DELAY = 4, REPEAT = 0;
	private static final List<String> DETAILS = Lists.newArrayList("Use a fishing rod for a stronger attack.");
	private static final Skill.Type TYPE = Skill.Type.OFFENSE;
	private static final MaterialData WEAPON = new MaterialData(Material.FISHING_ROD);

	public Reel(String deity)
	{
		super(NAME, COMMAND, deity, COST, DELAY, REPEAT, DETAILS, TYPE, WEAPON, new Predicate<Player>()
		{

			@Override
			public boolean apply(Player player)
			{
				// Set variables
				DemigodsCharacter character = DemigodsPlayer.Util.getPlayer(player).getCurrent();
				int damage = (int) Math.ceil(0.37286 * Math.pow(character.getMeta().getAscensions() * 100, 0.371238)); // TODO
				LivingEntity target = Ability.Util.autoTarget(player);

				if(!Ability.Util.target(player, target.getLocation(), true)) return false;

				Ability.Util.dealDamage(player, target, damage, EntityDamageEvent.DamageCause.CUSTOM);

				if(target.getLocation().getBlock().getType() == Material.AIR)
				{
					target.getLocation().getBlock().setType(Material.WATER);
					target.getLocation().getBlock().setData((byte) 0x8);
				}

				return true;
			}
		}, null, null);
	}
}
