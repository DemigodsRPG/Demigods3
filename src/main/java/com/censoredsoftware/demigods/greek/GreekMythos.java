package com.censoredsoftware.demigods.greek;

import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.demigods.engine.Mythos;
import com.censoredsoftware.demigods.engine.deity.Alliance;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.engine.listener.*;
import com.censoredsoftware.demigods.engine.structure.Structure;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.censoredsoftware.demigods.greek.structure.GreekStructure;
import com.censoredsoftware.demigods.greek.trigger.DivinityUnbalanced;
import com.censoredsoftware.demigods.greek.trigger.NewPlayerNeedsHelp;
import com.censoredsoftware.demigods.greek.trigger.ProcessAltars;
import com.google.common.collect.Sets;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashMap;
import java.util.Set;

public class GreekMythos extends Mythos {
    @Override
    public String getName() {
        return "Greek";
    }

    @Override
    public String getDescription() {
        return "Greek mythology, as described by Hesiod, Homer, and other Greek bards.";
    }

    @Override
    public String getAuthor() {
        return "_Alex and HmmmQuestionMark";
    }

    @Override
    public Set<Alliance> getAlliances() {
        return Sets.newHashSet((Alliance[]) GreekAlliance.values());
    }

    @Override
    public Set<Deity> getDeities() {
        return Sets.newHashSet((Deity[]) GreekDeity.values());
    }

    @Override
    public Set<Structure> getStructures() {
        return Sets.newHashSet((Structure[]) GreekStructure.values());
    }

    // Listeners
    public enum GreekListener {
        BATTLE(new BattleListener()), CHAT(new ChatListener()), ENTITY(new EntityListener()), FLAG(new FlagListener()), GRIEF(new GriefListener()), PLAYER(new PlayerListener()), TRIBUTE(new TributeListener());

        private Listener listener;

        private GreekListener(Listener listener) {
            this.listener = listener;
        }

        public Listener getListener() {
            return listener;
        }
    }

    // Permissions
    public enum GreekPermission {
        BASIC(new Permission("demigods.basic", "The very basic permissions for Demigods.", PermissionDefault.TRUE, new HashMap<String, Boolean>() {
            {
                put("demigods.basic.create", true);
                put("demigods.basic.forsake", true);
            }
        })), ADMIN(new Permission("demigods.admin", "The admin permissions for Demigods.", PermissionDefault.OP)), PVP_AREA_COOLDOWN(new Permission("demigods.bypass.pvpareacooldown", "Bypass the wait for leaving/entering PVP zones.", PermissionDefault.FALSE));

        private Permission permission;

        private GreekPermission(Permission permission) {
            this.permission = permission;
        }

        public Permission getPermission() {
            return permission;
        }
    }

    // Triggers
    public enum GreekTrigger {
        /**
         * Balance related.
         */
        DIVINITY_UNBALANCED(new DivinityUnbalanced()), NEW_PLAYER_NEEDS_HELP(new NewPlayerNeedsHelp()), PROCESS_ALTARS(new ProcessAltars());

        private Trigger trigger;

        private GreekTrigger(Trigger trigger) {
            this.trigger = trigger;
        }

        public Trigger getTrigger() {
            return trigger;
        }
    }
}
