package gg.projecteden.nexus.fallback.features;

import gg.projecteden.nexus.fallback.Feature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;

import static gg.projecteden.nexus.fallback.NexusFallback.handledByNexus;

public class HangingBreakDeny implements Feature {

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(HangingBreakEvent event) {
		if (!handledByNexus(event))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(HangingBreakByEntityEvent event) {
		if (!handledByNexus(event))
			event.setCancelled(true);
	}

}
