package de.lksbhm.mona.ui.screens;

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
			Transition transition = TransitionBuilder.newTransition().slideInLeft()
					.fadeClearColors().duration(.6f).get();
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
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(getStage());
		mux.addProcessor(backButtonHandler);
		setInputProcessor(mux);
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
		StringBuilder sb = new StringBuilder();
		String lineSeparator = LksBhmGame.getGame(Mona.class)
				.getPlatformManager().getPlatform().getLineSeparator();
		for (int i = 0; i < state.levelPackages.size(); i++) {
			final LevelPackage pack = state.levelPackages.getPackage(i);
			sb.setLength(0);
			sb.append(pack.getDisplayName());
			sb.append(lineSeparator);
			for (int diffI = 0; diffI <= pack.getDifficulty().ordinal(); diffI++) {
				sb.append('*');
			}
			String buttonText = sb.toString();
			if (pack.isSolved()) {
				packageButton = new TextButton(buttonText, solvedButtonStyle);
			} else {
				packageButton = new TextButton(buttonText, unsolvedButtonStyle);
			}
			packageButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Transition transition = TransitionBuilder.newTransition()
							.duration(.6f).slideInRight().fadeClearColors()
							.get();
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
		baseTable.add(listScroll).center().width(width * .7f).fill().expand()
				.padBottom(height * .1f);
	}

	private void applyState() {
		setupPackageWidgets();
		setupStandardWidgets();
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return false;
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
		layoutWidgets();
		getStage().setScrollFocus(listScroll);
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
