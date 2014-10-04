package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.lksbhm.gdx.LksBhmGame;

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

	private Label unlockedDailiesLabel;
	private Label unlockedRandomLabel;

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupWidgets();
	}

	private void setupWidgets() {
		Skin skin = LksBhmGame.getGame().getDefaultSkin();
		plusXLabel = new Label("", skin);
		unlockedDailiesLabel = new Label("unlocked daily levels", skin,
				"rewardScreen.unlockedDailies");
		unlockedRandomLabel = new Label("unlocked random levels", skin,
				"rewardScreen.unlockedRandom");
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
		state.leaveScreenRunnable.run();
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return false;
	}

	public void setup(int reward, Runnable leaveScreenRunnable) {
		state.reward = reward;
		state.leaveScreenRunnable = leaveScreenRunnable;
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
		public Runnable leaveScreenRunnable;
		public int reward;

		public RewardScreenState set(RewardScreenState state) {
			reward = state.reward;
			leaveScreenRunnable = state.leaveScreenRunnable;
			return this;
		}
	}
}
