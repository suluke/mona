package de.lksbhm.gdx.util;

import com.badlogic.gdx.assets.AssetManager;

public class LoadableAssetManager implements Loadable<Object> {
	private final AssetManager assetManager;

	public LoadableAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	@Override
	public boolean update() {
		return assetManager.update();
	}

	@Override
	public float getProgress() {
		return assetManager.getProgress();
	}

	@Override
	public void finish() {
		assetManager.finishLoading();
	}

	@Override
	public Object get() {
		return null;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}
