package de.lksbhm.gdx.util;

public interface Instantiator<Type> {
	Type instantiate();

	Type[] allocateArray(int size);
}
