package de.lksbhm.gdx.contexts;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

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
			contextClass = ClassReflection.forName(name);
		} catch (ReflectionException e) {
			throw new RuntimeException("No context found for name " + name);
		}
		if (!ClassReflection.isAssignableFrom(Context.class, contextClass)) {
			throw new RuntimeException("No context found for name " + name);
		}
		return contextClass;
	}

	public void enterContext(Context context) {
		Class<?> clazz = context.getClass();
		do {
			ArrayList<ContextListener> list = listeners.get(clazz);
			if (list != null) {
				for (ContextListener listener : list) {
					listener.onEnter(context);
				}
			}
			clazz = clazz.getSuperclass();
		} while (ClassReflection.isAssignableFrom(Context.class, clazz));
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
