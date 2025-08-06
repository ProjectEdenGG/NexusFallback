package gg.projecteden.nexus.fallback.features;

import gg.projecteden.nexus.fallback.Feature;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.persistence.PersistentDataType;

import static gg.projecteden.nexus.fallback.NexusFallback.handledByNexus;

public class EntityDamage implements Feature {

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EntityDamageEvent event) {
		if (!handledByNexus(event))
			event.setCancelled(true);
	}

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

	private static final NamespacedKey KEY = new NamespacedKey("survivalinvisiframes", "invisible");

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInvis(HangingBreakByEntityEvent event) {
		if (event.getEntity().getType() != EntityType.ITEM_FRAME || !event.getEntity().getPersistentDataContainer().has(KEY, PersistentDataType.BYTE))
			return;
		if (!handledByNexus(event, "InvisibleItemFrame"))
			event.setCancelled(true);
	}

}
