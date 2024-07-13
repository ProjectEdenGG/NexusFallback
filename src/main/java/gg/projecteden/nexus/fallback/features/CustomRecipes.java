package gg.projecteden.nexus.fallback.features;

import gg.projecteden.nexus.fallback.Feature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import static gg.projecteden.nexus.fallback.NexusFallback.handledByNexus;

public class CustomRecipes implements Feature {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (handledByNexus(event)) return;

        CraftingInventory inventory = event.getInventory();
        ItemStack result = inventory.getResult();
        if (result == null) return;
        if (result.isEmpty()) return;
        if (!result.getItemMeta().hasCustomModelData()) return;
        if (result.getItemMeta().getCustomModelData() != 0)
            inventory.setResult(null);
    }

}
