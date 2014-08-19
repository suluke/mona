package de.lksbhm.gdx.ui.screens;

import com.badlogic.gdx.Screen;

public interface ResettableScreen extends Screen {
	void setState(Object state);

	Object getState();
}
