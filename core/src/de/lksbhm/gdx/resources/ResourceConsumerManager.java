package de.lksbhm.gdx.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.util.LRUCache;

/**
 * Manager to obtain {@link ResourceConsumer ResourceConsumers} from. Handles
 * caching of the consumers and resource requirements for them
 * 
 * @author suluke
 *
 */
public class ResourceConsumerManager {
	private final LRUCache<Class<? extends ResourceConsumer>, ResourceConsumer> lruCache;
	private MemoryUsageStrategy memoryStrategy;
	private final LksBhmGame<?, ?> game;

	public ResourceConsumerManager(LksBhmGame<?, ?> game) {
		this(game, new NeverReleaseStrategy());
	}

	public ResourceConsumerManager(LksBhmGame<?, ?> game,
			MemoryUsageStrategy strategy) {
		lruCache = new LRUCache<Class<? extends ResourceConsumer>, ResourceConsumer>();
		memoryStrategy = strategy;
		this.game = game;
	}

	/**
	 * Sets the strategy to determine when removals of buffered
	 * {@link ResourceConsumer}s are necessary.
	 * 
	 * @param strategy
	 */
	public void setMemoryUsageStrategy(MemoryUsageStrategy strategy) {
		memoryStrategy = strategy;
	}

	/**
	 * Returns a {@link ResourceConsumer} with the given class. If the manager
	 * has not managed an instance of the required type yet, a new instance will
	 * be created using the consumer's mandatory default constructor.
	 * 
	 * @param consumer
	 * @param actuallyLoadResources
	 * @param state
	 * @return
	 */
	public <T extends ResourceConsumer> void obtainConsumerInstanceAndLoadResources(
			Class<T> consumer,
			final ResourceConsumerObtainedCallback<T> callback) {
		@SuppressWarnings("unchecked")
		final T instance = (T) lruCache.access(consumer);
		if (instance == null) {
			releaseMemoryIfNecessary();
			final T newInstance = instantiateConsumer(consumer);
			final AssetManager manager = game.getAssetManager();
			newInstance.requestResources(manager);
			if (newInstance.isRequestingLoadingAnimation()) {
				game.animateAssetManagerLoad(game.getAssetManager(),
						newInstance.getClass(), new Runnable() {
							@Override
							public void run() {
								newInstance.onResourcesLoaded(manager);
								callback.onObtained(newInstance);
							}
						});
			} else {
				manager.finishLoading();
				newInstance.onResourcesLoaded(manager);
				callback.onObtained(newInstance);
			}
		} else {
			callback.onObtained(instance);
		}
	}

	/**
	 * Will instantiate the resource consumer and have it register the resources
	 * it depends on with the {@link LksBhmGame#getGame() game's} assetManager,
	 * but without loading them.
	 * 
	 * @param consumer
	 * @return
	 */
	public <T extends ResourceConsumer> T obtainConsumerInstanceWithoutLoadingResources(
			Class<T> consumer) {
		@SuppressWarnings("unchecked")
		T instance = (T) lruCache.access(consumer);
		if (instance == null) {
			releaseMemoryIfNecessary();
			instance = instantiateConsumer(consumer);
			AssetManager manager = game.getAssetManager();
			instance.requestResources(manager);
		}
		return instance;
	}

	private <T extends ResourceConsumer> T instantiateConsumer(Class<T> type) {
		T consumer = null;
		try {
			Constructor c = ClassReflection.getConstructor(type);
			@SuppressWarnings("unchecked")
			T newInstance = (T) c.newInstance();
			consumer = newInstance;
		} catch (ReflectionException e) {
			throw new RuntimeException(e);
		}
		lruCache.put(type, consumer);
		return consumer;
	}

	private void releaseMemoryIfNecessary() {
		while (memoryStrategy.isReleaseRequired()) {
			ResourceConsumer consumer = lruCache.evict();
			consumer.dispose();
			memoryStrategy.notifyRelease(consumer);
		}
	}

	public void dispose(Class<? extends ResourceConsumer> class1) {
		lruCache.remove(class1).dispose();
	}
}
