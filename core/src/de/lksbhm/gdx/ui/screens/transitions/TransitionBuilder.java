package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;

import de.lksbhm.gdx.ui.screens.transitions.CallbackBasedTransition.Callback;

public class TransitionBuilder {
	private float duration = -1;
	private static final TransitionBuilder instance = new TransitionBuilder();
	private AbstractTransition transition = null;
	private final Pool<SlideInLeftExtraDistance> slideInLeftExtraDistancePool = new Pool<SlideInLeftExtraDistance>() {
		@Override
		public SlideInLeftExtraDistance newObject() {
			SlideInLeftExtraDistance transition = new SlideInLeftExtraDistance();
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
	private final Pool<FadeClearColor> fadeClearColorPool = new Pool<FadeClearColor>() {
		@Override
		public FadeClearColor newObject() {
			FadeClearColor transition = new FadeClearColor();
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
	private final Pool<CallbackBasedTransition> callbackBasedTransitionPool = new Pool<CallbackBasedTransition>() {
		@Override
		public CallbackBasedTransition newObject() {
			CallbackBasedTransition transition = new CallbackBasedTransition();
			transition.setPool(this);
			transition.setDisposeOnFinish(true);
			return transition;
		};
	};
	private final Pool<CancelOnTap> cancelOnTapPool = new Pool<CancelOnTap>() {
		@Override
		public CancelOnTap newObject() {
			CancelOnTap transition = new CancelOnTap();
			transition.setPool(this);
			transition.setDisposeOnFinish(true);
			return transition;
		};
	};
	private final Pool<FadeOutFadeIn> fadeOutFadeInPool = new Pool<FadeOutFadeIn>() {
		@Override
		public FadeOutFadeIn newObject() {
			FadeOutFadeIn transition = new FadeOutFadeIn();
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

	public TransitionBuilder slideInLeftExtraDistance() {
		SlideInLeftExtraDistance transition = slideInLeftExtraDistancePool
				.obtain();
		set(transition);
		return this;
	}

	public TransitionBuilder slideInRight() {
		SlideInRight transition = slideInRightPool.obtain();
		set(transition);
		return this;
	}

	public TransitionBuilder fadeClearColors() {
		FadeClearColor transition = fadeClearColorPool.obtain();
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
		if (duration >= 0) {
			result.setDuration(duration);
		}
		duration = -1;
		transition = null;
		return result;
	}

	public TransitionBuilder duration(float duration) {
		this.duration = duration;
		return this;
	}

	public TransitionBuilder callbackBasedTransition(Callback callback) {
		CallbackBasedTransition transition = callbackBasedTransitionPool
				.obtain();
		transition.setCallback(callback);
		set(transition);
		return this;
	}

	public TransitionBuilder cancelOnTap() {
		CancelOnTap transition = cancelOnTapPool.obtain();
		set(transition);
		return this;
	}

	public TransitionBuilder fadeOutFadeIn() {
		FadeOutFadeIn transition = fadeOutFadeInPool.obtain();
		set(transition);
		return this;
	}
}
