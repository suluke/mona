package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.contexts.Context;
import de.lksbhm.gdx.contexts.ContextImplementation;
import de.lksbhm.gdx.ui.screens.TransitionableResettableConsumerScreen;
import de.lksbhm.gdx.ui.screens.transitions.CallbackBasedTransition.Callback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;

/**
 * Shows a fancy image or something to flash up on game start. This should
 * always be, as an invariant, the first screen (after the loading screen) that
 * the user sees.
 * 
 */
public class SplashScreen extends AbstractScreen implements Context, Callback {
	private final ContextImplementation contextImplementation = new ContextImplementation(
			this);

	private TransitionableResettableConsumerScreen nextScreen = null;
	private Label moLabel;
	private Label naLabel;
	private HorizontalGroup foregroundGroup;
	private Cell<?> foregroundCell;
	private boolean movedToNextScreen = false;
	private final Action moveToNextScreenAction = new Action() {
		@Override
		public boolean act(float delta) {
			if (movedToNextScreen) {
				return true;
			} else {
				System.out.println("Move to next screen");
				moveTotNextScreen();
				movedToNextScreen = true;
				return true;
			}
		}
	};

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		foregroundGroup = new HorizontalGroup();
		moLabel = new Label("MO", LksBhmGame.getGame().getDefaultSkin());
		naLabel = new Label("NA", LksBhmGame.getGame().getDefaultSkin());
		foregroundGroup.addActor(moLabel);
		foregroundGroup.addActor(naLabel);
		foregroundCell = getBaseTable().add(foregroundGroup);
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

	@Override
	protected void onShow() {
		enterContext();
		movedToNextScreen = false;
		Action foregroundAlpha = Actions.alpha(1, 1);
		Action sequence = Actions.sequence(foregroundAlpha,
				moveToNextScreenAction);
		foregroundGroup.addAction(sequence);
	}

	@Override
	protected void onHide() {
		leaveContext();
	}

	@Override
	public void update(float progress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterContext() {
		contextImplementation.enterContext();
	}

	@Override
	public void leaveContext() {
		contextImplementation.leaveContext();
	}

	public void moveTotNextScreen() {
		Transition transition = TransitionBuilder.buildNew()
				.callbackBasedTransition(SplashScreen.this).duration(.6f).get();
		Router router = LksBhmGame.getGame().getRouter();
		if (nextScreen != null) {
			router.changeScreen(nextScreen, transition);
		} else {
			router.changeScreen(MainMenuScreen.class, null, transition);
		}
	}

	public void setNextScreen(TransitionableResettableConsumerScreen screen) {
		nextScreen = screen;
	}
}
