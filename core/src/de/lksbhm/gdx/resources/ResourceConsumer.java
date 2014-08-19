package de.lksbhm.gdx.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;

public interface ResourceConsumer extends Disposable {
	void requestResources(AssetManager manager);

	void onResourcesLoaded(AssetManager manager);

	boolean isRequestingLoadingAnimation();

	long getEstimatedMemoryUsage();
}
