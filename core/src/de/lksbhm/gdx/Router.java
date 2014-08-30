package de.lksbhm.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;

import de.lksbhm.gdx.ui.screens.TransitionableResettableConsumerScreen;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.util.CircularBuffer;
import de.lksbhm.gdx.util.Pair;

public class Router {

	private final LksBhmGame game;
	private final CircularBuffer<Pair<TransitionableResettableConsumerScreen, Object>> history;
	@SuppressWarnings("rawtypes")
	private final Pool<Pair> pairPool;

	public Router(LksBhmGame game) {
		this(game, 0);
	}

	@SuppressWarnings("rawtypes")
	public Router(LksBhmGame game, int historySize) {
		this.game = game;
		history = new CircularBuffer<Pair<TransitionableResettableConsumerScreen, Object>>(
				historySize);
		pairPool = new Pool<Pair>() {
			@Override
			protected Pair newObject() {
				return new Pair<TransitionableResettableConsumerScreen, Object>();
			}
		};
	}

	/**
	 * Changes the current controller and sets the specified view
	 * 
	 * @param screen
	 * @param state
	 */
	public void changeScreen(
			Class<? extends TransitionableResettableConsumerScreen> screen, Object state) {
		TransitionableResettableConsumerScreen s = obtainScreen(screen);
		if (state != null) {
			s.setState(state);
		}
		changeScreen(s);
	}

	public void changeScreen(
			Class<? extends TransitionableResettableConsumerScreen> screen,
			Object state, Transition t) {
		TransitionableResettableConsumerScreen s = obtainScreen(screen);
		if (state != null) {
			s.setState(state);
		}
		changeScreen(s, t);
	}

	public void changeScreen(TransitionableResettableConsumerScreen screen) {
		saveCurrentScreenInHistory();
		game.setScreen(screen);
	}

	public void changeScreen(TransitionableResettableConsumerScreen screen, Transition t) {
		saveCurrentScreenInHistory();
		t.apply(game, game.getScreen(), screen);
	}

	public <T extends TransitionableResettableConsumerScreen> T obtainScreen(
			Class<T> screen) {
		return game.getResourceConsumerManager().obtainConsumerInstance(screen,
				true);
	}

	/**
	 * Moves back in the history one step and restores the respective location.
	 */
	public void resetPreviousScreenFromHistory() {
		if (!history.isEmpty()) {
			Pair<TransitionableResettableConsumerScreen, Object> previous = history
					.pop();
			TransitionableResettableConsumerScreen screen = previous.getFirst();
			Object state = previous.getSecond();
			if (state != null) {
				screen.setState(state);
			}
			game.setScreen(previous.getFirst());
		} else {
			Gdx.app.log("Router",
					"Unable to reset previous location: history empty");
		}
	}

	public int getHistorySize() {
		return history.size();
	}

	public boolean isHistoryEmpty() {
		return history.isEmpty();
	}

	public void clearHistory() {
		history.clear();
	}

	public void saveCurrentScreenInHistory() {
		TransitionableResettableConsumerScreen screen = game.getScreen();
		if (screen != null) {
			@SuppressWarnings("unchecked")
			Pair<TransitionableResettableConsumerScreen, Object> pair = pairPool
					.obtain();
			pair.setFirst(screen);
			pair.setSecond(screen.getState());
			history.push(pair);
		}
	}
}
