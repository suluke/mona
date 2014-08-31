package de.lksbhm.gdx.contexts;

public interface ContextListener {
	void onEnter(Context contextInstance);

	void onLeave(Context contextInstance);

	String getContextName();
}
