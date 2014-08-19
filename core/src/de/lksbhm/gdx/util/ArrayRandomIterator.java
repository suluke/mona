package de.lksbhm.gdx.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

/**
 * Complexity: n log n. Memory: 2n
 * 
 * @author suluke
 *
 * @param <T>
 */
public class ArrayRandomIterator<T> implements Iterator<T> {

	private final int length;
	private int position = 0;
	T[] array;
	private final Pair<Integer, Integer>[] mixer;

	@SuppressWarnings("unchecked")
	public ArrayRandomIterator(T[] array, Random r) {
		length = array.length;
		this.array = array;
		mixer = new Pair[length];
		for (int i = 0; i < length; i++) {
			// 2 times length as sorting keys for less collisions
			mixer[i] = new Pair<Integer, Integer>(i, r.nextInt(2 * length));
		}
		Arrays.sort(mixer, new Comparator<Pair<Integer, Integer>>() {
			@Override
			public int compare(Pair<Integer, Integer> o1,
					Pair<Integer, Integer> o2) {
				return o1.getSecond() - o2.getSecond();
			}
		});
	}

	@Override
	public boolean hasNext() {
		return position != length;
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new RuntimeException();
		}
		T next = array[mixer[position].getFirst()];
		position++;
		return next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
