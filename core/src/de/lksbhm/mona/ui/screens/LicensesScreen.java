package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;

public class LicensesScreen extends AbstractScreen {

	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Transition transition = TransitionBuilder.newTransition()
					.fadeClearColors().slideInLeft().duration(.6f).get();
			InfoScreen.setAsCurrentScreen(transition);
		}
	};

	public LicensesScreen() {
		InputMultiplexer mux = new InputMultiplexer(getStage(),
				backButtonHandler);
		setInputProcessor(mux);
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setState(Object state) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getState() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void setAsCurrentScreen(final Transition transition) {
		final Router router = LksBhmGame.getGame().getRouter();
		router.obtainScreen(LicensesScreen.class,
				new ResourceConsumerObtainedCallback<LicensesScreen>() {
					@Override
					public void onObtained(LicensesScreen screen) {
						router.changeScreen(screen, transition);
					}
				});
	}
}
