package de.lksbhm.gdx.ui.screens.transitions;

import de.lksbhm.gdx.LksBhmGame;

public interface Transition {
	void apply(LksBhmGame onGame, TransitionableScreen fromScreen,
			TransitionableScreen toScreen);

	void setDuration(float duration);

	float getDuration();

	void abort();
}
