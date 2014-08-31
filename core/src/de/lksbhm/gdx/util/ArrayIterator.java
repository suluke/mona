package de.lksbhm.gdx.util;

import java.util.Iterator;

public class ArrayIterator<E> implements Iterator<E> {

	private final E[] array;
	private int i = 0;

	public ArrayIterator(E[] array) {
		this.array = array;
	}

	@Override
	public boolean hasNext() {
		return i != array.length;
	}

	@Override
	public E next() {
		if (!hasNext()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		E result = array[i];
		i++;
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
