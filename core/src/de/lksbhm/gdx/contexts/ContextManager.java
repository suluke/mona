package de.lksbhm.gdx.contexts;

import java.util.ArrayList;
import java.util.HashMap;

public class ContextManager {
	private final HashMap<Class<?>, ArrayList<ContextListener>> listeners = new HashMap<Class<?>, ArrayList<ContextListener>>();

	public void addListener(ContextListener listener) {
		Class<?> contextClass = getContextClass(listener.getContextName());
		ArrayList<ContextListener> list = listeners.get(contextClass);
		if (list == null) {
			list = new ArrayList<ContextListener>();
			listeners.put(contextClass, list);
		}
		list.add(listener);
	}

	public boolean removeListener(ContextListener listener) {
		Class<?> contextClass = getContextClass(listener.getContextName());
		ArrayList<ContextListener> list = listeners.get(contextClass);
		return list.remove(listener);
	}

	private Class<?> getContextClass(String name) {
		Class<?> contextClass;
		try {
			contextClass = Class.forName(name);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("No context found for name " + name);
		}
		if (!Context.class.isAssignableFrom(contextClass)) {
			throw new RuntimeException("No context found for name " + name);
		}
		return contextClass;
	}

	public void enterContext(Context context) {
		ArrayList<ContextListener> list = listeners.get(context.getClass());
		if (list != null) {
			for (ContextListener listener : list) {
				listener.onEnter(context);
			}
		}
	}

	public void leaveContext(Context context) {
		ArrayList<ContextListener> list = listeners.get(context.getClass());
		if (list != null) {
			for (ContextListener listener : list) {
				listener.onLeave(context);
			}
		}
	}
}
