package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

public interface TransitionableScreen extends Screen {
	Stage getStage();

	InputProcessor getInputProcessor();

	void render(float delta, boolean clear);

	Color getClearColor();

	/**
	 * When a screen is replaced using a transition, TransitionScreens are used.
	 * By replacing the previous screen with a TransitionScreen, though, hide()
	 * is being called on the previous screen, which in turn would make an
	 * additional call to show() necessary.
	 */
	void disableHide();

	void enableHide();

	void disableShow();

	void enableShow();
}
