package de.lksbhm.gdx.util;

/**
 * A circular buffer that overwrites the oldest item on {@link #push(Object)} if
 * it already contains the specified maximum number of elements.
 */
public class CircularBuffer<T> {

	private final T[] buffer;
	private int head = 0;
	private int items = 0;

	/**
	 * Creates a circular buffer with a given size.
	 * 
	 * @param size
	 *            the size with which the ringbuffer will be created. 30 in our
	 *            case.
	 */
	@SuppressWarnings("unchecked")
	public CircularBuffer(int size) throws IllegalArgumentException {
		buffer = (T[]) new Object[size];
	}

	/**
	 * Places the given object on top of the ringbuffer.
	 * 
	 * @param obj
	 *            the object which will be placed upon the ringbuffer.
	 */
	public void push(T obj) {
		if (buffer.length == 0) {
			return;
		}
		if (items < buffer.length) {
			items++;
		}
		buffer[head] = obj;
		head = (head + 1) % buffer.length;
	}

	/**
	 * Removes the topmost object of the ringbuffer and returns it.
	 * 
	 * @return the object which used to be on top of the ringbuffer.
	 * @throws Exception
	 */
	public T pop() throws RuntimeException {
		if (items == 0) {
			throw new RuntimeException("No elements left to pop");
		}
		head = (head + buffer.length - 1) % buffer.length;
		items--;
		return buffer[head];
	}

	/**
	 * 
	 * @return the number of elements currently in the buffer
	 */
	public int size() {
		return items;
	}

	/**
	 * 
	 * 
	 * @return the maximum number of elemnts that can be in the buffer at the
	 *         same time.
	 */
	public int capacity() {
		return buffer.length;
	}

	public boolean isEmpty() {
		return items == 0;
	}

	public void clear() {
		head = 0;
		items = 0;
		// also remove references to objects to allow garbage collection
		for (int i = 0; i < buffer.length; ++i) {
			buffer[i] = null;
		}
	}
}
