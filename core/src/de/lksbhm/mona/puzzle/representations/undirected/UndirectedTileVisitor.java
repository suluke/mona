package de.lksbhm.mona.puzzle.representations.undirected;

public interface UndirectedTileVisitor {
	void visitTopLeft(TopLeft topLeft);

	void visitTopBottom(TopBottom topBottom);

	void visitLeftRight(LeftRight leftRight);

	void visitBottomRight(BottomRight bottomRight);

	void visitBottomLeft(BottomLeft bottomLeft);

	void visitTopRight(TopRight topRight);

	void visitEmpty(UndirectedEmpty empty);
}
