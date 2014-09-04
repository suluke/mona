package de.lksbhm.gdx.contexts;

/**
 * Hides a bit of the unsafe type mess.
 * 
 *
 * @param <ContextType>
 */
public abstract class AbstractContextListenerHelper<ContextType extends Context>
		implements ContextListener {
	private final Class<ContextType> contextClass;

	public AbstractContextListenerHelper(Class<ContextType> contextClass) {
		this.contextClass = contextClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEnter(Context contextInstance) {
		onEnterContext((ContextType) contextInstance);
	}

	protected abstract void onEnterContext(ContextType context);

	@SuppressWarnings("unchecked")
	@Override
	public void onLeave(Context contextInstance) {
		onLeaveContext((ContextType) contextInstance);
	}

	protected abstract void onLeaveContext(ContextType context);

	@Override
	public String getContextName() {
		return contextClass.getName();
	}
}
