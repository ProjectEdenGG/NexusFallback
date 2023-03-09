package gg.projecteden.nexus.fallback.features;

import gg.projecteden.nexus.fallback.Feature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;

import static gg.projecteden.nexus.fallback.NexusFallback.isNexusEnabled;

public class HangingBreakDeny implements Feature {

	@EventHandler
	public void on(HangingBreakEvent event) {
		if (isNexusEnabled())
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void on(HangingBreakByEntityEvent event) {
		if (isNexusEnabled())
			return;

		event.setCancelled(true);
	}

}
