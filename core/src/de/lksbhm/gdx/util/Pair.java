package de.lksbhm.gdx.util;

import java.util.Map;

/**
 * A class to encapsulate two objects as a pair. The class also implements
 * Map.Entry which makes it compatible with custom implementation of the
 * {@link Map} interface.
 * 
 * @param <K>
 *            the type of the first element of the pair
 * @param <V>
 *            the type of the second element of the pair
 */
public class Pair<K, V> implements java.util.Map.Entry<K, V> {

	private K k;
	private V v;

	/**
	 * Constructs a new, empty pair.
	 */
	public Pair() {

	}

	/**
	 * Creates a new {@link Pair} with the given objects as elements.
	 * 
	 * @param first
	 *            the first element of the Pair
	 * @param second
	 *            the second element in the Pair
	 */
	public Pair(final K first, final V second) {
		k = first;
		v = second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((k == null) ? 0 : k.hashCode());
		result = prime * result + ((v == null) ? 0 : v.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		Pair other = (Pair) obj;
		if (k == null) {
			if (other.k != null) {
				return false;
			}
		} else if (!k.equals(other.k)) {
			return false;
		}
		if (v == null) {
			if (other.v != null) {
				return false;
			}
		} else if (!v.equals(other.v)) {
			return false;
		}
		return true;
	}

	public K getFirst() {
		return k;
	}

	public K setFirst(K first) {
		K oldK = k;
		k = first;
		return oldK;
	}

	public V getSecond() {
		return v;
	}

	public V setSecond(V second) {
		V oldV = v;
		v = second;
		return oldV;
	}

	/*
	 * Map.Entry implementation
	 */
	@Override
	public K getKey() {
		return getFirst();
	}

	public K setKey(K key) {
		return setFirst(key);
	}

	@Override
	public V getValue() {
		return getSecond();
	}

	@Override
	public V setValue(V value) {
		return setSecond(value);
	}
}
