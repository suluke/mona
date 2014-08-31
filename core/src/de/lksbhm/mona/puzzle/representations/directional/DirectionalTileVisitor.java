package de.lksbhm.mona.puzzle.representations.directional;

public interface DirectionalTileVisitor {
	void visitTopLeft(TopLeftTile topLeft);

	void visitTopBottom(TopBottomTile topBottom);

	void visitLeftRight(LeftRightTile leftRight);

	void visitBottomRight(BottomRightTile bottomRight);

	void visitBottomLeft(BottomLeftTile bottomLeft);

	void visitTopRight(TopRightTile topRight);

	void visitEmpty(NoDirectionTile empty);
}
