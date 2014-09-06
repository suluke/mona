package de.lksbhm.gdx.ui.screens.transitions;

public class TransitionBuilder {
	private static final TransitionBuilder instance = new TransitionBuilder();
	private AbstractTransition transition = null;

	private TransitionBuilder() {

	}

	public static TransitionBuilder buildNew() {
		instance.transition = null;
		return instance;
	}

	public TransitionBuilder slideInRight() {
		// TODE implement pooling
		SlideInRight transition = new SlideInRight();
		set(transition);
		return instance;
	}

	public TransitionBuilder interpolateClearColor() {
		// TODE implement pooling
		InterpolateClearColor transition = new InterpolateClearColor();
		set(transition);
		return instance;
	}

	private void set(AbstractTransition t) {
		if (transition != null) {
			t.runParallel(transition);
		}
		transition = t;
	}

	public Transition get() {
		return transition;
	}
}
