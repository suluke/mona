package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;

public class TransitionBuilder {
	private static final TransitionBuilder instance = new TransitionBuilder();
	private AbstractTransition transition = null;
	private final Pool<ExtraDistanceSlideInLeft> extraDistanceSlideInLeftPool = new Pool<ExtraDistanceSlideInLeft>() {
		@Override
		public ExtraDistanceSlideInLeft newObject() {
			ExtraDistanceSlideInLeft transition = new ExtraDistanceSlideInLeft();
			transition.setPool(this);
			transition.setDisposeOnFinish(true);
			return transition;
		};
	};
	private final Pool<SlideInRight> slideInRightPool = new Pool<SlideInRight>() {
		@Override
		public SlideInRight newObject() {
			SlideInRight transition = new SlideInRight();
			transition.setPool(this);
			transition.setDisposeOnFinish(true);
			return transition;
		};
	};
	private final Pool<InterpolateClearColor> interpolateClearColorPool = new Pool<InterpolateClearColor>() {
		@Override
		public InterpolateClearColor newObject() {
			InterpolateClearColor transition = new InterpolateClearColor();
			transition.setPool(this);
			transition.setDisposeOnFinish(true);
			return transition;
		};
	};

	private final Pool<SlideInLeft> slideInLeftPool = new Pool<SlideInLeft>() {
		@Override
		public SlideInLeft newObject() {
			SlideInLeft transition = new SlideInLeft();
			transition.setPool(this);
			transition.setDisposeOnFinish(true);
			return transition;
		};
	};

	private TransitionBuilder() {

	}

	public static TransitionBuilder buildNew() {
		if (instance.transition != null) {
			Gdx.app.error("TransitionBuilder",
					"Starting a new transition before old one is retrieved");
		}
		instance.transition = null;
		return instance;
	}

	public TransitionBuilder extraDistanceSlideInRight() {
		ExtraDistanceSlideInLeft transition = extraDistanceSlideInLeftPool
				.obtain();
		set(transition);
		return this;
	}

	public TransitionBuilder slideInRight() {
		SlideInRight transition = slideInRightPool.obtain();
		set(transition);
		return this;
	}

	public TransitionBuilder interpolateClearColor() {
		InterpolateClearColor transition = interpolateClearColorPool.obtain();
		set(transition);
		return this;
	}

	public TransitionBuilder slideInLeft() {
		SlideInLeft transition = slideInLeftPool.obtain();
		set(transition);
		return this;
	}

	private void set(AbstractTransition t) {
		if (transition != null) {
			t.runParallel(transition);
		}
		transition = t;
	}

	public Transition get() {
		Transition result = transition;
		transition = null;
		return result;
	}
}
