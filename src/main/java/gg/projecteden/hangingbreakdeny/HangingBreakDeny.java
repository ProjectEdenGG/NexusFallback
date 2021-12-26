package gg.projecteden.hangingbreakdeny;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.Association;
import com.sk89q.worldguard.protection.association.Associables;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.SimpleFlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.plugin.java.JavaPlugin;

public class HangingBreakDeny extends JavaPlugin implements Listener {

	public static WorldGuardPlugin plugin;
	public static SimpleFlagRegistry registry;
	public static StateFlag flag;

	@Override
	public void onLoad() {
		plugin = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		registry = (SimpleFlagRegistry) WorldGuard.getInstance().getFlagRegistry();

		if (plugin == null || registry == null) {
			getLogger().warning("Could not find WorldGuard, aborting registry");
			return;
		}

		flag = new StateFlag("hanging-break", false);

		try {
			try {
				registry.register(flag);
			} catch (FlagConflictException duplicate) {
				flag = (StateFlag) registry.get(flag.getName());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onItemFrameBreak(HangingBreakEvent event) {
		if (RemoveCause.ENTITY.equals(event.getCause()))
			return;

		if (query(event.getEntity().getLocation(), flag) == State.DENY)
			event.setCancelled(true);
	}

	@EventHandler
	public void onItemFrameBreak(HangingBreakByEntityEvent event) {
		Entity remover = event.getRemover();
		if (remover instanceof Player)
			return;

		if (query(event.getEntity().getLocation(), flag) == State.DENY)
			event.setCancelled(true);
	}

	public static State query(org.bukkit.Location location, StateFlag... flags) {
		Validate.notNull(location, "Location cannot be null");
		Validate.notNull(flags, "Flags cannot be null");

		Location loc = BukkitAdapter.adapt(location);
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		return container.createQuery().queryState(loc, Associables.constant(Association.OWNER), flags);
	}

}
