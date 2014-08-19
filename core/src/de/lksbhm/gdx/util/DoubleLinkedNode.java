package de.lksbhm.gdx.util;

public class DoubleLinkedNode<T> {
	private DoubleLinkedNode<T> previous;
	private DoubleLinkedNode<T> next;
	private T data;

	public DoubleLinkedNode() {

	}

	public DoubleLinkedNode(T data) {
		this.data = data;
	}

	/**
	 * @return the previous
	 */
	public DoubleLinkedNode<T> getPrevious() {
		return previous;
	}

	/**
	 * @param previous
	 *            the previous to set
	 */
	public void setPrevious(DoubleLinkedNode<T> previous) {
		this.previous = previous;
	}

	/**
	 * @return the next
	 */
	public DoubleLinkedNode<T> getNext() {
		return next;
	}

	/**
	 * @param next
	 *            the next to set
	 */
	public void setNext(DoubleLinkedNode<T> next) {
		this.next = next;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

}
