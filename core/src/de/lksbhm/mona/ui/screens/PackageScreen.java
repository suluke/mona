package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.levels.LevelPackage;
import de.lksbhm.mona.levels.LevelPackageCollection;

public class PackageScreen extends AbstractScreen {

	private final PackageScreenState state = new PackageScreenState();
	private final InputAdapter backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Transition transition = TransitionBuilder.buildNew().slideInLeft()
					.interpolateClearColor().duration(.6f).get();
			LevelPackageCollection collection = state.pack
					.getLevelPackageCollection();
			PackagesListScreen.showAsCurrentScreen(collection, transition);
		}
	};
	private final Table levelButtonGrid = new Table();
	private TextButton[] levelButtons;
	private static final int levelButtonsPerRow = 5;
	private TextButtonStyle buttonStyle;

	public PackageScreen() {
		setClearColor(0.75f, 0.5f, 0.5f, 1);
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		buttonStyle = LksBhmGame.getGame().getDefaultSkin()
				.get("play", TextButtonStyle.class);
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
		levelButtons = new TextButton[state.pack.getSize()];
		int i = 0;
		for (Level l : state.pack) {
			final Level level = l;
			levelButtons[i] = new TextButton(l.getLevelId(), buttonStyle);
			levelButtons[i].addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					final Router router = LksBhmGame.getGame().getRouter();
					router.obtainScreen(
							LevelScreenOne.class,
							new ResourceConsumerObtainedCallback<LevelScreenOne>() {
								@Override
								public void onObtained(
										LevelScreenOne levelScreen) {
									levelScreen.setLevel(level);
									Transition transition = TransitionBuilder
											.buildNew().slideInRight()
											.interpolateClearColor()
											.duration(.6f).get();
									router.changeScreen(levelScreen, transition);
								}
							});
				}
			});
			i++;
		}
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

	@Override
	protected void onShow() {
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(Gdx.input.getInputProcessor());
		mux.addProcessor(backButtonHandler);
		Gdx.input.setInputProcessor(mux);
		layoutWidgets();
	}

	private void layoutWidgets() {
		Table base = getBaseTable();
		base.clear();
		levelButtonGrid.clear();
		for (int i = 0; i < levelButtons.length; i++) {
			levelButtonGrid.add(levelButtons[i]);
			if (i % levelButtonsPerRow == levelButtonsPerRow - 1) {
				levelButtonGrid.row();
			}
		}
		base.add(levelButtonGrid);
	}

	public static void showAsCurrentScreen(final LevelPackage pack,
			final Transition transition) {
		final Router router = LksBhmGame.getGame().getRouter();
		router.obtainScreen(PackageScreen.class,
				new ResourceConsumerObtainedCallback<PackageScreen>() {
					@Override
					public void onObtained(PackageScreen packageScreen) {
						packageScreen.setLevelPackage(pack);
						router.changeScreen(packageScreen, transition);
					}
				});
	}
}
