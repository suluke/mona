package de.lksbhm.mona.puzzle.representations.grouped;

import de.lksbhm.mona.puzzle.representations.directional.DirectionalTile;

public enum TileGroupType {
	STRAIGHTS, STRAIGHT_STRAIGHT_EDGE, EDGE_STRAIGHT_EDGE, STRAIGHT_EDGE_STRAIGHT, STRAIGHT_EDGE_EDGE, EDGES, NONE;
	public static TileGroupType fromUndirectedNode(DirectionalTile node) {
		if (node.isEmpty()) {
			return NONE;
		}
		DirectionalTile adj1 = node.getFirstAdjacent();
		DirectionalTile adj2 = node.getSecondAdjacent();
		if (node.isStraight()) {
			if (adj1.isStraight()) {
				if (adj2.isStraight()) {
					return STRAIGHTS;
				} else {
					return STRAIGHT_STRAIGHT_EDGE;
				}
			} else {
				if (adj2.isStraight()) {
					return STRAIGHT_STRAIGHT_EDGE;
				} else {
					return EDGE_STRAIGHT_EDGE;
				}
			}
		} else {
			if (adj1.isStraight()) {
				if (adj2.isStraight()) {
					return STRAIGHT_EDGE_STRAIGHT;
				} else {
					return STRAIGHT_EDGE_EDGE;
				}
			} else {
				if (adj2.isStraight()) {
					return TileGroupType.STRAIGHT_EDGE_EDGE;
				} else {
					return EDGES;
				}
			}
		}
	}
}
