package gg.projecteden.nexus.fallback.features;

import gg.projecteden.nexus.fallback.Feature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import static gg.projecteden.nexus.fallback.NexusFallback.handledByNexus;

public class CustomRecipes implements Feature {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (handledByNexus(event)) return;

        if (event.getInventory().getResult() == null) return;
        if (event.getInventory().getResult().isEmpty()) return;
        if (event.getInventory().getResult().getItemMeta().getCustomModelData() != 0)
            event.getInventory().setResult(null);
    }

}
