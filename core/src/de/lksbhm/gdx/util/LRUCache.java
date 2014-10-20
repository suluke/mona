package de.lksbhm.gdx.util;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Pool;

/**
 * Least Recently Used Cache implementation.
 * 
 * @author suluke
 *
 */
public class LRUCache<Key, Value> {
	private final Pool<DoubleLinkedNode<Pair<Key, Value>>> nodePool = new Pool<DoubleLinkedNode<Pair<Key, Value>>>() {
		@Override
		protected DoubleLinkedNode<Pair<Key, Value>> newObject() {
			return new DoubleLinkedNode<Pair<Key, Value>>();
		}
	};

	private final Pool<Pair<Key, Value>> pairPool = new Pool<Pair<Key, Value>>() {
		@Override
		protected Pair<Key, Value> newObject() {
			return new Pair<Key, Value>();
		}
	};
	/**
	 * Used to access linked list nodes corresponding to the key and splice the
	 * nodes to the front of the queue.
	 */
	private final Map<Key, DoubleLinkedNode<Pair<Key, Value>>> map = new HashMap<Key, DoubleLinkedNode<Pair<Key, Value>>>();
	/**
	 * The head of a double linked list used as a queue here
	 */
	private final Head head = new Head();

	public void put(Key k, Value v) {
		Pair<Key, Value> data = pairPool.obtain();
		data.setKey(k);
		data.setValue(v);
		DoubleLinkedNode<Pair<Key, Value>> node = head.pushBack(data);
		map.put(k, node);
	}

	public Value access(Key k) {
		DoubleLinkedNode<Pair<Key, Value>> node = map.get(k);
		if (node == null) {
			return null;
		}
		node.detach();
		head.pushBack(node);

		return node.getData().getSecond();
	}

	/**
	 * Removes the least recently used datum from the cache and returns it.
	 * 
	 * @return the least recently used object in this cache.
	 */
	public Value evict() {
		DoubleLinkedNode<Pair<Key, Value>> node = head.popFront();
		if (node == null) {
			// TODO rather throw exception when nothing to evict?
			return null;
		}
		Value value = node.getData().getValue();
		remove(node);
		return value;
	}

	public Value remove(Key key) {
		DoubleLinkedNode<Pair<Key, Value>> node = map.remove(key);
		if (node == null) {
			// TODO rather throw exception when nothing to remove?
			return null;
		}
		Value value = node.getData().getValue();
		remove(node);
		return value;
	}

	/**
	 * Correctly unsets and frees the nodes and pairs used to store a key-value
	 * datum
	 * 
	 * @param node
	 */
	private void remove(DoubleLinkedNode<Pair<Key, Value>> node) {
		Pair<Key, Value> data = node.getData();
		map.remove(data.getKey());
		data.setKey(null);
		data.setValue(null);
		pairPool.free(data);
		node.detach();
		node.setData(null);
		nodePool.free(node);
	}

	public boolean isEmpty() {
		return head.getNext() == head;
	}

	/**
	 * An extension to the double linked node class to represent a head element
	 * of a double linked list
	 *
	 */
	private class Head extends DoubleLinkedNode<Pair<Key, Value>> {
		public Head() {
			this.setPrevious(this);
			this.setNext(this);
		}

		public DoubleLinkedNode<Pair<Key, Value>> pushBack(Pair<Key, Value> data) {
			DoubleLinkedNode<Pair<Key, Value>> node = nodePool.obtain();
			node.setData(data);
			pushBack(node);
			return node;
		}

		public void pushBack(DoubleLinkedNode<Pair<Key, Value>> node) {
			node.setNext(this);
			node.setPrevious(this.getPrevious());
			this.getPrevious().setNext(node);
			this.setPrevious(node);
		}

		public DoubleLinkedNode<Pair<Key, Value>> popFront() {
			DoubleLinkedNode<Pair<Key, Value>> node = getNext();
			if (node == this) {
				throw new IllegalStateException(
						"Cannot remove items from an empty queue.");
			}
			node.getNext().setPrevious(this);
			this.setNext(node.getNext());
			node.setNext(null);
			node.setPrevious(null);
			return node;
		}
	}
}
