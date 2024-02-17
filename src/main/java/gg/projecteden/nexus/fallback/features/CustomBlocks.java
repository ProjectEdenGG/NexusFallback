package gg.projecteden.nexus.fallback.features;

import gg.projecteden.api.common.annotations.Environments;
import gg.projecteden.api.common.utils.Env;
import gg.projecteden.nexus.fallback.Feature;
import gg.projecteden.parchment.event.block.CustomBlockUpdateEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Set;

import static gg.projecteden.nexus.fallback.NexusFallback.handledByNexus;

@Environments(Env.TEST)
public class CustomBlocks implements Feature {

	private static final Set<Material> handledMaterials = Set.of(Material.NOTE_BLOCK, Material.TRIPWIRE, Material.STRING);

	private void error(Player player) {
		player.sendMessage("Interactions with certain blocks are being prevented while Nexus is disabled");
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(CustomBlockUpdateEvent event) {
		if (handledByNexus(event))
			return;

		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockPhysicsEvent event) {
		if (handledByNexus(event))
			return;

		Block eventBlock = event.getBlock();
		Material material = eventBlock.getType();
		if (handledMaterials.contains(material)) {
			event.setCancelled(true);
			eventBlock.getState().update(true, false);
		}

		Block aboveBlock = eventBlock.getRelative(BlockFace.UP);
		if (handledMaterials.contains(aboveBlock.getType())) {
			event.setCancelled(true);

			while (handledMaterials.contains(aboveBlock.getType())) {
				// Leave this as (true, true) -> (true, false) will crash the server
				aboveBlock.getState().update(true, true);

				aboveBlock = aboveBlock.getRelative(BlockFace.UP);
			}
		}
	}

	@EventHandler
	public void on(PlayerInteractEvent event) {
		if (handledByNexus(event))
			return;

		if (event.useInteractedBlock() == Result.DENY || event.useItemInHand() == Result.DENY)
			return;

		if (!EquipmentSlot.HAND.equals(event.getHand()))
			return;

		Block clickedBlock = event.getClickedBlock();
		if (clickedBlock == null)
			return;

		Material material = clickedBlock.getType();
		if (handledMaterials.contains(material)) {
			event.setCancelled(true);
			error(event.getPlayer());
		}
	}

	@EventHandler
	public void on(BlockBreakEvent event) {
		if (handledByNexus(event))
			return;

		if (handledMaterials.contains(event.getBlock().getType())) {
			event.setCancelled(true);
			error(event.getPlayer());
		}
	}

	@EventHandler
	public void on(BlockPlaceEvent event) {
		if (handledByNexus(event))
			return;

		if (handledMaterials.contains(event.getBlock().getType())) {
			event.setCancelled(true);
			error(event.getPlayer());
		}
	}

	@EventHandler
	public void on(BlockPistonExtendEvent event) {
		if (handledByNexus(event))
			return;

		for (Block block : event.getBlocks()) {
			if (handledMaterials.contains(block.getType())) {
				event.setCancelled(true);
				break;
			}
		}
	}

	@EventHandler
	public void on(BlockPistonRetractEvent event) {
		if (handledByNexus(event))
			return;

		for (Block block : event.getBlocks()) {
			if (handledMaterials.contains(block.getType())) {
				event.setCancelled(true);
				break;
			}
		}
	}

	@EventHandler
	public void on(NotePlayEvent event) {
		if (handledByNexus(event))
			return;

		event.setCancelled(true);
	}

}
