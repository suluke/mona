package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.utils.Disposable;

import de.lksbhm.gdx.LksBhmGame;

public interface Transition extends Disposable {
	void apply(LksBhmGame<?, ?, ?> onGame, TransitionableScreen fromScreen,
			TransitionableScreen toScreen);

	void setDuration(float duration);

	float getDuration();

	void abort();

	void setDisposeOnFinish(boolean b);
}
