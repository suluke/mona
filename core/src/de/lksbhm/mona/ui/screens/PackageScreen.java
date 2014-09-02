package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.assets.AssetManager;

import de.lksbhm.mona.levels.LevelPackage;

public class PackageScreen extends AbstractScreen {

	private final PackageScreenState state = new PackageScreenState();

	public PackageScreen() {
		setClearColor(0.75f, 0.5f, 0.5f, 1);
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupStandardWidgets();
	}

	private void setupStandardWidgets() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return false;
	}

	public void setLevelPackage(LevelPackage pack) {
		state.pack = pack;
		applyState();
	}

	@Override
	public void setState(Object state) {
		if (state.getClass() != PackageScreenState.class) {
			throw new RuntimeException();
		}
		this.state.set((PackageScreenState) state);
		applyState();
	}

	private void applyState() {
		setupPackageWidgets();
	}

	private void setupPackageWidgets() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getState() {
		return state;
	}

	private static class PackageScreenState {
		public LevelPackage pack;

		public void set(PackageScreenState state) {
			pack = state.pack;
		}
	}
}
