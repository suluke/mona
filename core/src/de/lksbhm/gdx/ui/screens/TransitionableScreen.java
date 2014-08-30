package de.lksbhm.gdx.ui.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

public interface TransitionableScreen extends Screen {
	Stage getStage();

	void render(float delta, boolean clear);

	Color getClearColor();
}
