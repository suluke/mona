package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.mona.Mona;

public class RewardScreen extends AbstractScreen {

	private final RewardScreenState state = new RewardScreenState();
	private boolean movedToNextScreen = false;
	private final Action changeScreenAction = new Action() {
		@Override
		public boolean act(float delta) {
			if (!movedToNextScreen) {
				movedToNextScreen = true;
				moveToNextScreen();
			}
			return true;
		}
	};

	private Label plusXLabel;

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupWidgets();
	}

	private void setupWidgets() {
		Skin skin = LksBhmGame.getGame().getDefaultSkin();
		plusXLabel = new Label("", skin);
	}

	@Override
	protected void onShow() {
		layoutWidgets();
		Table base = getBaseTable();
		base.setColor(1, 1, 1, 0);
		movedToNextScreen = false;
		Action sequence = Actions.sequence(Actions.delay(.6f),
				Actions.alpha(1, 1), Actions.delay(1), Actions.alpha(0, 1),
				changeScreenAction);
		base.addAction(sequence);
	}

	private void layoutWidgets() {
		Table base = getBaseTable();
		base.clear();
		base.add(plusXLabel);
	}

	private void moveToNextScreen() {
		Transition transition = TransitionBuilder.buildNew()
				.slideInLeftExtraDistance().fadeClearColors().duration(.6f)
				.get();
		LksBhmGame.getGame(Mona.class).getRouter()
				.changeScreen(state.nextScreen, transition);
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return false;
	}

	public void setup(int reward, AbstractScreen nextScreen) {
		state.reward = reward;
		state.nextScreen = nextScreen;
		applyState();
	}

	@Override
	public void setState(Object state) {
		if (state.getClass() != RewardScreenState.class) {
			throw new RuntimeException();
		}
		this.state.set((RewardScreenState) state);
		applyState();
	}

	@Override
	public Object getState() {
		return new RewardScreenState().set(state);
	}

	private void applyState() {
		plusXLabel.setText("+" + state.reward);
	}

	private static class RewardScreenState {
		public AbstractScreen nextScreen;
		public int reward;

		public RewardScreenState set(RewardScreenState state) {
			reward = state.reward;
			return this;
		}
	}
}
