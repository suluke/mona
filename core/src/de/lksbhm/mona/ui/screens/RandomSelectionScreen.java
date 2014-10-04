package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.assets.AssetManager;

public class RandomSelectionScreen extends AbstractScreen {

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupWidgets();
	}

	private void setupWidgets() {

	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return false;
	}

	@Override
	public void setState(Object state) {

	}

	@Override
	public Object getState() {
		return null;
	}
}
