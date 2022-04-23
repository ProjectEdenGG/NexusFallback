package gg.projecteden.nexus.fallback;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.objenesis.ObjenesisStd;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class NexusFallback extends JavaPlugin implements Listener {

	@Getter
	private static NexusFallback instance;
	@Getter
	private final static Map<Class<? extends Feature>, Feature> registered = new HashMap<>();

	public static Map<Class<?>, Object> singletons = new HashMap<>();

	public NexusFallback() {
		instance = this;
	}

	@SuppressWarnings("unchecked")
	public static <T> T singletonOf(Class<T> clazz) {
		return (T) singletons.computeIfAbsent(clazz, $ -> {
			try {
				return clazz.getConstructor().newInstance();
			} catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException ex) {
				return new ObjenesisStd().newInstance(clazz);
			}
		});
	}

	@Override
	public void onLoad() {
		for (Class<? extends Feature> clazz : new Reflections(getClass().getPackageName()).getSubTypesOf(Feature.class))
			registered.put(clazz, singletonOf(clazz));

		registered.values().forEach(Feature::onLoad);
	}

	@Override
	public void onEnable() {
		for (Feature feature : registered.values()) {
			feature.onEnable();
			registerListener(feature);
		}
	}

	@Override
	public void onDisable() {
		registered.values().forEach(Feature::onDisable);
	}

	public static void registerListener(Listener listener) {
		instance.getServer().getPluginManager().registerEvents(listener, NexusFallback.getInstance());
	}

	public static boolean isNexusEnabled(){
		return Bukkit.getServer().getPluginManager().isPluginEnabled("Nexus");
	}

}
