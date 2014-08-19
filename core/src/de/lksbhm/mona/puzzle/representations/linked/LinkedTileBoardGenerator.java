package de.lksbhm.mona.puzzle.representations.linked;

import java.util.Random;

import de.lksbhm.gdx.util.ArrayRandomIterator;

public class LinkedTileBoardGenerator {
	private final LinkedTileBoard result;
	private final Random random;

	private LinkedTileBoardGenerator(int width, int height, Random random) {
		this.random = random;
		int rootX = random.nextInt(width);
		int rootY = random.nextInt(height);
		result = new LinkedTileBoard(width, height);
		LinkedTile root = result.getTile(rootX, rootY);
		root.setParent(root);
		visitNode(root);
		LinkedTile bestNeighbor = selectBestNeighbor(root);
		unsetNotInPath(root, bestNeighbor, width, height);
		setNextLink(root, bestNeighbor);
	}

	private void setNextLink(LinkedTile root, LinkedTile bestNeighbor) {
		LinkedTile next = root;
		LinkedTile current = bestNeighbor;
		while (next != current) {
			current.setChild(next);
			next = current;
			current = current.getParent();
		}
		root.setParent(bestNeighbor);
	}

	private void unsetNotInPath(LinkedTile root, LinkedTile bestNeighbor,
			int width, int height) {
		boolean[][] inPath = new boolean[width][height];
		inPath[root.getX()][root.getY()] = true;
		LinkedTile node = bestNeighbor;
		while (node != root) {
			inPath[node.getX()][node.getY()] = true;
			node = node.getParent();
		}
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (!inPath[x][y]) {
					result.getTile(x, y).setParent(null);
				}
			}
		}
	}

	private void visitNode(LinkedTile node) {
		LinkedTile[] neighbors = node.getNeightbors();
		ArrayRandomIterator<LinkedTile> it = new ArrayRandomIterator<LinkedTile>(
				neighbors, random);
		while (it.hasNext()) {
			LinkedTile neighbor = it.next();
			if (neighbor.getParent() == null) {
				neighbor.setParent(node);
				visitNode(neighbor);
			}
		}
	}

	private LinkedTile selectBestNeighbor(LinkedTile root) {
		LinkedTile[] neighbors = root.getNeightbors();
		int maxLen = 0;
		LinkedTile maxNeighbor = null;
		for (LinkedTile n : neighbors) {
			int len = getPathLength(n);
			if (len > maxLen) {
				maxLen = len;
				maxNeighbor = n;
			}
		}
		return maxNeighbor;
	}

	private int getPathLength(LinkedTile node) {
		int len = 0;
		while (node.getParent() != node) {
			node = node.getParent();
			len++;
		}
		return len;
	}

	public static LinkedTileBoard generateBoard(int width, int height,
			Random random) {
		LinkedTileBoardGenerator generator = new LinkedTileBoardGenerator(
				width, height, random);
		return generator.result;
	}
}
