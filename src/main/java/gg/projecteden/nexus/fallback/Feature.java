package gg.projecteden.nexus.fallback;

import org.bukkit.event.Listener;

public interface Feature extends Listener {

	default void onLoad() {}

	default void onEnable() {}

	default void onDisable() {}

}
