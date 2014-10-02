package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.contexts.Context;
import de.lksbhm.gdx.contexts.ContextImplementation;
import de.lksbhm.gdx.ui.screens.TransitionableResettableConsumerScreen;
import de.lksbhm.gdx.ui.screens.transitions.CallbackBasedTransition.Callback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;

/**
 * Shows a fancy image or something to flash up on game start. This should
 * always be, as an invariant, the first screen (after the loading screen) that
 * the user sees.
 * 
 */
public class SplashScreen extends AbstractScreen implements Context, Callback {
	private final ContextImplementation contextImplementation = new ContextImplementation(
			this);

	private TransitionableResettableConsumerScreen nextScreen = null;
	private Label moLabel;
	private Label naLabel;
	private final VerticalGroup leftColumn = new VerticalGroup();
	private final VerticalGroup rightColumn = new VerticalGroup();
	private final Container<VerticalGroup> leftContainer = new Container<VerticalGroup>();
	private final Container<VerticalGroup> rightContainer = new Container<VerticalGroup>();
	private final HorizontalGroup curtainGroup = new HorizontalGroup();
	private boolean movedToNextScreen = false;
	private final ShapeRenderer shapeRenderer;
	private final Action moveToNextScreenAction = new Action() {
		@Override
		public boolean act(float delta) {
			if (!movedToNextScreen) {
				moveTotNextScreen();
				movedToNextScreen = true;
			}
			return true;
		}
	};

	private float progress;

	public SplashScreen() {
		// ShapeRenderer assumes 8 vertices for a quad, and even if it didn't it
		// still needed 6 vertices for the 2 triangles that make up the quad
		shapeRenderer = new ShapeRenderer(16);
		leftColumn.align(Align.right);
		rightColumn.align(Align.left);
		leftContainer.setActor(leftColumn);
		rightContainer.setActor(rightColumn);
		curtainGroup.addActor(leftContainer);
		curtainGroup.addActor(rightContainer);
		getBaseTable().add(curtainGroup);
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		moLabel = new Label("M  O ", LksBhmGame.getGame().getDefaultSkin());
		naLabel = new Label(" N  A", LksBhmGame.getGame().getDefaultSkin());
		float width = Math.max(moLabel.getWidth(), naLabel.getWidth());
		moLabel.setAlignment(Align.right, Align.center);
		naLabel.setAlignment(Align.left, Align.center);
		leftColumn.addActor(moLabel);
		rightColumn.addActor(naLabel);
		leftContainer.width(width);
		rightContainer.width(width);
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return false;
	}

	@Override
	public void setState(Object state) {
	}

	@Override
	public Object getState() {
		return null;
	}

	@Override
	protected void onShow() {
		progress = 0;
		enterContext();
		Color color = curtainGroup.getColor();
		color.a = 0;
		curtainGroup.setColor(color);
		movedToNextScreen = false;
		Action foregroundAlpha = Actions.alpha(1, 1);
		Action sequence = Actions.sequence(foregroundAlpha, Actions.delay(.6f),
				moveToNextScreenAction);
		curtainGroup.addAction(sequence);
	}

	@Override
	protected void onHide() {
		leaveContext();
		LksBhmGame.getGame().getResourceConsumerManager()
				.dispose(SplashScreen.class);
	}

	@Override
	public void update(float progress) {
		curtainGroup.space(getStage().getWidth() * progress);
		curtainGroup.invalidate();
		getBaseTable().invalidate();
		this.progress = progress;
	}

	@Override
	public void enterContext() {
		contextImplementation.enterContext();
	}

	@Override
	public void leaveContext() {
		contextImplementation.leaveContext();
	}

	public void moveTotNextScreen() {
		Transition transition = TransitionBuilder.buildNew()
				.callbackBasedTransition(SplashScreen.this).cancelOnTap()
				.duration(2f).get();
		Router router = LksBhmGame.getGame().getRouter();
		if (nextScreen != null) {
			router.changeScreen(nextScreen, transition);
		} else {
			router.changeScreen(MainMenuScreen.class, null, transition);
		}
	}

	public void setNextScreen(TransitionableResettableConsumerScreen screen) {
		nextScreen = screen;
	}

	@Override
	public void render(float delta, boolean clear) {
		float halfWidth = Gdx.graphics.getWidth() / 2;
		float height = Gdx.graphics.getHeight();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(getClearColor());
		shapeRenderer.rect(0, 0, halfWidth * (1 - progress), height);
		shapeRenderer.rect(halfWidth + halfWidth * progress, 0, halfWidth
				* (1 - progress), height);
		shapeRenderer.end();
		super.render(delta, clear);
	}

	@Override
	protected void onDispose() {
		super.onDispose();
		shapeRenderer.dispose();
	}
}
