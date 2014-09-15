package de.lksbhm.gdx.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Least Recently Used Cache implementation.
 * 
 * @author suluke
 *
 */
public class LRUCache<Key, Value> {
	private class Head extends DoubleLinkedNode<Pair<Key, Value>> {
		public Head() {
			this.setPrevious(this);
			this.setNext(this);
		}

		public DoubleLinkedNode<Pair<Key, Value>> add(Pair<Key, Value> data) {
			// TODO pooling?
			DoubleLinkedNode<Pair<Key, Value>> node = new DoubleLinkedNode<Pair<Key, Value>>(
					data);
			add(node);
			return node;
		}

		public void add(DoubleLinkedNode<Pair<Key, Value>> node) {
			node.setNext(this);
			node.setPrevious(this.getPrevious());
			this.getPrevious().setNext(node);
			this.setPrevious(node);
		}

		public DoubleLinkedNode<Pair<Key, Value>> popFront() {
			DoubleLinkedNode<Pair<Key, Value>> node = getNext();
			node.getNext().setPrevious(this);
			this.setNext(node.getNext());
			node.setNext(null);
			node.setPrevious(null);
			return node;
		}
	}

	private final Map<Key, DoubleLinkedNode<Pair<Key, Value>>> map = new HashMap<Key, DoubleLinkedNode<Pair<Key, Value>>>();
	private final Head head = new Head();

	public void put(Key k, Value v) {
		// TODO pooling?
		DoubleLinkedNode<Pair<Key, Value>> node = head
				.add(new Pair<Key, Value>(k, v));
		map.put(k, node);
	}

	public Value access(Key k) {
		DoubleLinkedNode<Pair<Key, Value>> node = map.get(k);
		if (node == null) {
			return null;
		}
		DoubleLinkedNode<Pair<Key, Value>> previous = node.getPrevious();
		DoubleLinkedNode<Pair<Key, Value>> next = node.getNext();
		next.setPrevious(previous);
		previous.setNext(next);
		head.add(node);

		return node.getData().getSecond();
	}

	/**
	 * Removes the least recently used datum from the cache and returns it.
	 * 
	 * @return the least recently used object in this cache.
	 */
	public Value evict() {
		DoubleLinkedNode<Pair<Key, Value>> node = head.popFront();
		map.remove(node.getData().getKey());
		return node.getData().getSecond();
	}

	public Value remove(Key key) {
		DoubleLinkedNode<Pair<Key, Value>> node = map.remove(key);
		DoubleLinkedNode<Pair<Key, Value>> previous = node.getPrevious();
		DoubleLinkedNode<Pair<Key, Value>> next = node.getNext();
		previous.setNext(next);
		next.setPrevious(previous);
		return node.getData().getValue();
	}
}
