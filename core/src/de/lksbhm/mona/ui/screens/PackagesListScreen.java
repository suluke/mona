package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.levels.LevelPackage;
import de.lksbhm.mona.levels.LevelPackageCollection;

public class PackagesListScreen extends AbstractScreen {

	private PackagesListScreenState state;
	private final InputAdapter backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Transition transition = TransitionBuilder.buildNew().slideInLeft()
					.interpolateClearColor().duration(.6f).get();
			LksBhmGame.getGame().getRouter()
					.changeScreen(MainMenuScreen.class, null, transition);
		}
	};
	private final VerticalGroup packagesList = new VerticalGroup();
	private final ScrollPane listScroll = new ScrollPane(packagesList);
	@SuppressWarnings("rawtypes")
	private Container[] packageButtons;
	private TextButtonStyle unsolvedButtonStyle;
	private TextButtonStyle solvedButtonStyle;

	public PackagesListScreen() {
		state = new PackagesListScreenState();
		setClearColor(.6f, .6f, .6f, 1);
		packagesList.setWidth(getDefaultViewportWidth() * .7f);
	}

	public void setLevelPackageCollection(LevelPackageCollection levelPackages) {
		state.levelPackages = levelPackages;
		applyState();
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		unsolvedButtonStyle = LksBhmGame.getGame().getDefaultSkin()
				.get("unsolved", TextButtonStyle.class);
		solvedButtonStyle = LksBhmGame.getGame().getDefaultSkin()
				.get("solved", TextButtonStyle.class);
		setupStandardWidgets();
	}

	private void setupStandardWidgets() {
		// TODO Add other buttons and stuff, not related to package buttons

	}

	private void setupPackageWidgets() {
		TextButton packageButton;
		Container<TextButton> buttonContainer;
		packageButtons = new Container[state.levelPackages.size()];
		for (int i = 0; i < state.levelPackages.size(); i++) {
			final LevelPackage pack = state.levelPackages.getPackage(i);
			if (pack.isSolved()) {
				packageButton = new TextButton(pack.getPackageId(),
						solvedButtonStyle);
			} else {
				packageButton = new TextButton(pack.getPackageId(),
						unsolvedButtonStyle);
			}
			packageButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Transition transition = TransitionBuilder.buildNew()
							.slideInRight().interpolateClearColor().get();
					PackageScreen.showAsCurrentScreen(pack, transition);
				}
			});
			buttonContainer = new Container<TextButton>(packageButton);
			packageButtons[i] = buttonContainer;
		}
	}

	private void layoutWidgets() {
		Table baseTable = getBaseTable();
		baseTable.clear();
		packagesList.clear();
		float width = getDefaultViewportWidth();
		float height = getDefaultViewportHeight();
		if (packageButtons != null) {
			for (@SuppressWarnings("rawtypes")
			Container c : packageButtons) {
				c.padTop(height / 50);
				packagesList.addActor(c);
			}
		}
		baseTable.add(listScroll).center().width(width * .7f).expandY()
				.padBottom(height * .1f);
	}

	private void applyState() {
		setupPackageWidgets();
		setupStandardWidgets();
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return true;
	}

	@Override
	public void setState(Object state) {
		if (state.getClass() != PackagesListScreenState.class) {
			throw new RuntimeException();
		}
		this.state = (PackagesListScreenState) state;
		applyState();
	}

	@Override
	public Object getState() {
		return state;
	}

	private static class PackagesListScreenState {
		public LevelPackageCollection levelPackages;
	}

	@Override
	protected void onShow() {
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(Gdx.input.getInputProcessor());
		mux.addProcessor(backButtonHandler);
		Gdx.input.setInputProcessor(mux);
		layoutWidgets();
	}

	public static void showAsCurrentScreen(
			final LevelPackageCollection collection, final Transition transition) {
		Mona mona = LksBhmGame.getGame(Mona.class);
		final Router router = mona.getRouter();
		router.obtainScreen(PackagesListScreen.class,
				new ResourceConsumerObtainedCallback<PackagesListScreen>() {
					@Override
					public void onObtained(PackagesListScreen listScreen) {
						listScreen.setLevelPackageCollection(collection);
						router.changeScreen(listScreen, transition);
					}
				});
	}
}
