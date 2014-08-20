package de.lksbhm.gdx.ui.screens;

import com.badlogic.gdx.Screen;

public interface ResettableScreen extends Screen {
	/**
	 * 
	 * @param state
	 *            not supposed to be null
	 */
	void setState(Object state);

	Object getState();
}
