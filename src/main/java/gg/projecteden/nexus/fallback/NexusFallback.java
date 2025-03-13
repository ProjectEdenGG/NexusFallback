package gg.projecteden.nexus.fallback;

import gg.projecteden.api.common.EdenAPI;
import gg.projecteden.api.common.utils.Env;
import gg.projecteden.api.common.utils.ReflectionUtils;
import gg.projecteden.api.common.utils.Utils;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NexusFallback extends JavaPlugin implements Listener {

	@Getter
	private static NexusFallback instance;
	@Getter
	private static API api;
	@Getter
	private final static Map<Class<? extends Feature>, Feature> registered = new HashMap<>();

	public static Map<Class<?>, Object> singletons = new HashMap<>();

	public NexusFallback() {
		instance = this;
		api = new API();
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

	public static class API extends EdenAPI {
		Env env;

		public API() {
			instance = this;
			env = _getEnv();
		}

		@Override
		public Env getEnv() {
			return env;
		}

		@Override
		public void shutdown() {}

		public static Env _getEnv() {
			String env = getInstance().getConfig().getString("env", Env.PROD.name()).toUpperCase();
			try {
				return Env.valueOf(env);
			} catch (IllegalArgumentException ex) {
				return Env.PROD;
			}
		}

	}

	@Override
	public void onLoad() {
		for (Class<? extends Feature> clazz : ReflectionUtils.subTypesOf(Feature.class, getClass().getPackageName())) {
			if(Utils.canEnable(clazz))
				registered.put(clazz, singletonOf(clazz));
		}

		registered.values().forEach(Feature::onLoad);
	}

	@Override
	public void onEnable() {
		for (Feature feature : registered.values()) {
			feature.onEnable();
			registerListener(feature);
			getLogger().info("Registered feature " + feature.getClass().getSimpleName());
		}
	}

	@Override
	public void onDisable() {
		registered.values().forEach(Feature::onDisable);
		api.shutdown();
	}

	public static void registerListener(Listener listener) {
		instance.getServer().getPluginManager().registerEvents(listener, NexusFallback.getInstance());
	}

	public static boolean handledByNexus(Event event) {
		return Arrays.stream(event.getHandlers().getRegisteredListeners()).anyMatch(listener -> "Nexus".equals(listener.getPlugin().getName()));
	}

	public static boolean handledByNexus(Event event, String className) {
		return Arrays.stream(event.getHandlers().getRegisteredListeners()).anyMatch(listener -> listener.getListener().getClass().getSimpleName().endsWith(className));
	}

}
