package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;

public class InfoScreen extends AbstractScreen {
	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Router router = LksBhmGame.getGame().getRouter();
			Transition transition = TransitionBuilder.buildNew()
					.fadeClearColors().fadeOutFadeIn().duration(.6f).get();
			router.changeScreen(MainMenuScreen.class, null, transition);
		}
	};

	public InfoScreen() {
		setClearColor(.816f, .588f, .29f, 1);
		InputMultiplexer mux = new InputMultiplexer(getStage(),
				backButtonHandler);
		setInputProcessor(mux);
	}

	@Override
	protected void onShow() {
		System.out.println(ShaderProgram.getManagedStatus());
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

}