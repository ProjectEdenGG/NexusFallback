package gg.projecteden.nexus.fallback.features;

import gg.projecteden.nexus.fallback.Feature;
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

import static gg.projecteden.nexus.fallback.NexusFallback.isNexusEnabled;

public class CustomBlocks implements Feature {

	private void error(Player player){
		player.sendMessage("Note Block interactions are being prevented while Nexus is disabled");
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockPhysicsEvent event) {
		if(isNexusEnabled())
			return;

		Block eventBlock = event.getBlock();
		Material material = eventBlock.getType();
		if (material == Material.NOTE_BLOCK) {
			event.setCancelled(true);
			eventBlock.getState().update(true, false);
		}

		Block aboveBlock = eventBlock.getRelative(BlockFace.UP);
		if (aboveBlock.getType().equals(Material.NOTE_BLOCK)) {
			event.setCancelled(true);

			while (aboveBlock.getType() == Material.NOTE_BLOCK) {
				// Leave this as (true, true) -> (true, false) will crash the server
				aboveBlock.getState().update(true, true);

				aboveBlock = aboveBlock.getRelative(BlockFace.UP);
			}
		}
	}

	@EventHandler
	public void on(PlayerInteractEvent event) {
		if(isNexusEnabled())
			return;

		if (event.useInteractedBlock() == Result.DENY || event.useItemInHand() == Result.DENY)
			return;

		if (!EquipmentSlot.HAND.equals(event.getHand()))
			return;

		Block clickedBlock = event.getClickedBlock();
		if(clickedBlock == null)
			return;

		Material material = clickedBlock.getType();
		if(material.equals(Material.NOTE_BLOCK)) {
			event.setCancelled(true);
			error(event.getPlayer());
		}
	}

	@EventHandler
	public void on(BlockBreakEvent event) {
		if(isNexusEnabled())
			return;

		if(event.getBlock().getType().equals(Material.NOTE_BLOCK)) {
			event.setCancelled(true);
			error(event.getPlayer());
		}
	}

	@EventHandler
	public void on(BlockPlaceEvent event) {
		if(isNexusEnabled())
			return;

		if(event.getBlock().getType().equals(Material.NOTE_BLOCK)) {
			event.setCancelled(true);
			error(event.getPlayer());
		}
	}

	@EventHandler
	public void on(BlockPistonExtendEvent event) {
		if(isNexusEnabled())
			return;

		for (Block block : event.getBlocks()) {
			if(block.getType().equals(Material.NOTE_BLOCK)) {
				event.setCancelled(true);
				break;
			}
		}
	}

	@EventHandler
	public void on(BlockPistonRetractEvent event) {
		if(isNexusEnabled())
			return;

		for (Block block : event.getBlocks()) {
			if(block.getType().equals(Material.NOTE_BLOCK)) {
				event.setCancelled(true);
				break;
			}
		}
	}

	@EventHandler
	public void on(NotePlayEvent event) {
		if(isNexusEnabled())
			return;

		event.setCancelled(true);
	}
}
