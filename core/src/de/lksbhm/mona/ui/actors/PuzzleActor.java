package de.lksbhm.mona.ui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import de.lksbhm.mona.puzzle.Piece;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.representations.Direction;

public class PuzzleActor extends Widget {

	private Puzzle puzzle;
	private final PuzzleActorStyle style = new PuzzleActorStyle();
	private float marginLeft;
	private float marginTop;
	private float cellWidth;
	private float cellHeight;
	private float lineWidth;
	private float paddingWidth;
	private float paddingHeight;
	private final boolean invertY = false; // false means, origin is down left
	private final InputListener inputListener = new InputListener();

	public PuzzleActor(PuzzleActorStyle style) {
		this.style.set(style);
		addListener(inputListener);
	}

	public PuzzleActor(Skin skin) {
		this(skin.get(PuzzleActorStyle.class));
	}

	public void setStyle(PuzzleActorStyle style) {
		this.style.set(style);
		invalidate();
	}

	public void setPuzzle(Puzzle puzzle) {
		this.puzzle = puzzle;
		invalidate();
	}

	@Override
	public float getPrefWidth() {
		Stage stage = getStage();
		if (stage != null) {
			return stage.getWidth();
		} else {
			return 600;
		}
	}

	@Override
	public float getPrefHeight() {
		Stage stage = getStage();
		if (stage != null) {
			return stage.getHeight();
		} else {
			return 1024;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (puzzle == null) {
			return;
		}
		drawTileBackgrounds(batch);
		drawTileTypes(batch);
		drawTileConnectors(batch);
	}

	private void drawTileConnectors(Batch batch) {
		Piece[][] tiles = puzzle.getTiles();
		Piece inAdj;
		for (Piece[] array : tiles) {
			for (Piece tile : array) {
				inAdj = tile.getInAdjacent();
				if (inAdj != null) {
					if (inAdj.getInAdjacent() == tile
							|| inAdj.getOutAdjacent() == tile) {
						// draw continuous line
						// TODO prevent double draw
						drawConnector(batch, tile, tile.getInDirection(), true);
					} else {
						// only draw up to some point in between
						drawConnector(batch, tile, tile.getInDirection(), false);
					}
				}
			}
		}
	}

	private void drawTileTypes(Batch batch) {
		Piece[][] tiles = puzzle.getTiles();
		for (Piece[] array : tiles) {
			for (Piece tile : array) {
				drawType(batch, tile);
			}
		}
	}

	private void drawTileBackgrounds(Batch batch) {
		Piece[][] tiles = puzzle.getTiles();
		for (Piece[] array : tiles) {
			for (Piece tile : array) {
				drawBackground(batch, tile);
			}
		}
	}

	private void drawConnector(Batch batch, Piece tile, Direction direction,
			boolean continuous) {
		if (direction == Direction.NONE) {
			return;
		}
		int x = tile.getX();
		int y = tile.getY();
		float tileX = marginLeft + x * (cellWidth + paddingWidth);
		float tileY = marginTop + y * (cellHeight + paddingHeight);
		float startX = tileX + cellWidth / 2;
		float startY = tileY + cellHeight / 2;
		float endX = 0;
		float endY = 0;
		boolean horizontal = direction == Direction.LEFT
				|| direction == Direction.RIGHT;
		if (horizontal) {
			endY = startY;
			startY -= lineWidth / 2;
			endY += lineWidth / 2;
		} else {
			endX = startX;
			startX -= lineWidth / 2;
			endX += lineWidth / 2;
		}
		if (continuous) {
			if (horizontal) {
				endX = marginLeft + tile.getNeighbor(direction).getX()
						* (cellWidth + paddingWidth) + cellWidth / 2;
			} else {
				endY = marginTop + tile.getNeighbor(direction).getY()
						* (cellHeight + paddingHeight) + cellHeight / 2;
			}
		} else {
			if (horizontal) {
				if (direction == Direction.LEFT) {
					endX = startX - cellWidth;
				} else {
					endX = startX + cellWidth;
				}
			} else {
				if (direction == Direction.UP) {
					endY = startY - cellHeight;
				} else {
					endY = startY + cellHeight;
				}
			}
		}
		if (invertY) {
			// TODO not sure here
			startY = getHeight() - startY;
			endY = getHeight() - endY;
		}

		if (startX > endX) {
			float swap = startX;
			startX = endX;
			endX = swap;
		}
		if (startY > endY) {
			float swap = startY;
			startY = endY;
			endY = swap;
		}

		if (horizontal) {
			style.connectorHorizontal.draw(batch, startX, startY,
					endX - startX, endY - startY);
		} else {
			style.connectorVertical.draw(batch, startX, startY, endX - startX,
					endY - startY);
		}
	}

	private void drawType(Batch batch, Piece tile) {
		int x = tile.getX();
		int y = tile.getY();
		float tileX = marginLeft + x * (cellWidth + paddingWidth);
		float tileY = marginTop + y * (cellHeight + paddingHeight);
		switch (tile.getType()) {
		case EDGE: {
			style.edge.draw(batch, tileX, tileY, cellWidth, cellHeight);
			break;
		}
		case EMPTY: {
			break;
		}
		case STRAIGHT: {
			style.straight.draw(batch, tileX, tileY, cellWidth, cellHeight);
			break;
		}
		default: {
			throw new RuntimeException();
		}
		}
	}

	private void drawBackground(Batch batch, Piece tile) {
		int x = tile.getX();
		int y = tile.getY();
		int width = puzzle.getWidth();
		int height = puzzle.getHeight();
		if (x == 0) {
			if (y == 0) {
				drawBottomLeft(batch, tile);
			} else if (y == height - 1) {
				drawTopLeft(batch, tile);
			} else {
				drawLeft(batch, tile);
			}
		} else if (x == width - 1) {
			if (y == 0) {
				drawBottomRight(batch, tile);
			} else if (y == height - 1) {
				drawTopRight(batch, tile);
			} else {
				drawRight(batch, tile);
			}
		} else if (y == 0) {
			drawBottom(batch, tile);
		} else if (y == height - 1) {
			drawTop(batch, tile);
		} else {
			drawInner(batch, tile);
		}
	}

	private void drawInner(Batch batch, Piece tile) {
		int x = tile.getX();
		int y = tile.getY();
		float drawX = marginLeft + x * (cellWidth + paddingWidth);
		float drawY = marginTop + y * (cellHeight + paddingHeight);
		if (!invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		style.innerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawBottom(Batch batch, Piece tile) {
		int x = tile.getX();
		float drawX = marginLeft + x * (cellWidth + paddingWidth);
		float drawY = marginTop + (puzzle.getHeight() - 1)
				* (cellHeight + paddingHeight);
		if (!invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawTop(Batch batch, Piece tile) {
		int x = tile.getX();
		float drawX = marginLeft + x * (cellWidth + paddingWidth);
		float drawY = marginTop;
		if (!invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawRight(Batch batch, Piece tile) {
		int y = tile.getY();
		float drawX = marginLeft + (puzzle.getWidth() - 1)
				* (cellWidth + paddingWidth);
		float drawY = marginTop + y * (cellHeight + paddingHeight);
		if (!invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawBottomRight(Batch batch, Piece tile) {
		float drawX = marginLeft + (puzzle.getWidth() - 1)
				* (cellWidth + paddingWidth);
		float drawY = marginTop + (puzzle.getHeight() - 1)
				* (cellHeight + paddingHeight);
		if (!invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawTopRight(Batch batch, Piece tile) {
		float drawX = marginLeft + (puzzle.getWidth() - 1)
				* (cellWidth + paddingWidth);
		float drawY = marginTop;
		if (!invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawLeft(Batch batch, Piece tile) {
		int y = tile.getY();
		float drawX = marginLeft;
		float drawY = marginTop + y * (paddingHeight + cellHeight);
		if (!invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		style.innerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawBottomLeft(Batch batch, Piece tile) {
		float drawX = marginLeft;
		float drawY = marginTop + (puzzle.getHeight() - 1)
				* (cellHeight + paddingHeight);
		if (!invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawTopLeft(Batch batch, Piece tile) {
		float drawX = marginLeft;
		float drawY = marginTop;
		if (!invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		style.innerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	@Override
	public void layout() {

		style.validate();
		float width = getWidth();
		float height = getHeight();

		float usableWidth = (1 - style.outerMarginLeft - style.outerMarginRight)
				* width;
		float usableHeight = (1 - style.outerMarginTop - style.outerMarginBottom)
				* height;
		marginLeft = width * style.outerMarginLeft;
		marginTop = height * style.outerMarginTop;
		cellWidth = usableWidth / puzzle.getWidth();
		cellHeight = usableHeight / puzzle.getHeight();

		paddingWidth = style.tilePaddingX * cellWidth * (puzzle.getWidth() - 1);
		paddingHeight = style.tilePaddingY * cellHeight
				* (puzzle.getHeight() - 1);

		usableWidth -= paddingWidth;
		usableHeight -= paddingHeight;
		cellWidth = usableWidth / puzzle.getWidth();
		cellHeight = usableHeight / puzzle.getHeight();

		if (style.forceSquareTiles) {
			if (cellWidth > cellHeight) {
				float diff = cellWidth - cellHeight;
				cellWidth -= diff;
				diff *= puzzle.getWidth();
				diff /= puzzle.getWidth() - 1;
				paddingWidth += diff;
			} else {
				float diff = cellHeight - cellWidth;
				cellHeight -= diff;
				diff *= puzzle.getHeight();
				diff /= puzzle.getHeight() - 1;
				paddingHeight += diff;
			}
		}
		lineWidth = Math.min(cellWidth, cellHeight) * style.connectorWidth;
	}

	public static class PuzzleActorStyle {
		public Drawable edge;
		public Drawable straight;
		public Drawable innerTile;
		public Drawable connectorHorizontal;
		public Drawable connectorVertical;
		public float connectorWidth = 0.1f; // relative to min(cellwidth,
											// cellheight)
		public float innerTileMidOffsetX = 0;
		public float innerTileMidOffsetY = 0;
		public Drawable rightInnerTile;
		public float rightInnerTileMidOffsetX = 0;
		public float rightInnerTileMidOffsetY = 0;
		public Drawable bottomTile;
		public float bottomTileMidOffsetX = 0;
		public float bottomTileMidOffsetY = 0;
		public Drawable bottomRightTile;
		public float bottomRightTileMidOffsetX = 0; // relative to texture size
		public float bottomRightTileMidOffsetY = 0;
		public float tilePaddingX = 0; // 0.1 = 10 percent relative to tile size
		public float tilePaddingY = 0; // 0.1 = 10 percent relative to tile size
		public float outerMarginLeft = 0.05f; // 0.1 = 10 percent relative to
												// actor
												// size
		public float outerMarginRight = 0.05f; // Percent
		public float outerMarginTop = 0.05f; // Percent
		public float outerMarginBottom = 0.05f; // Percent
		public boolean forceSquareTiles = true;
		public boolean forceCenter = true; // if set, marginRight and
											// marginBottom will be ignored

		/**
		 * Only for instantiation via reflection
		 */
		public PuzzleActorStyle() {

		}

		/**
		 * Constructor that requires all mandatory fields as arguments
		 * 
		 * @param edgeDrawable
		 * @param straightDrawable
		 * @param tile
		 * @param connectorHorizontal
		 * @param connectorVertical
		 */
		public PuzzleActorStyle(Drawable edgeDrawable,
				Drawable straightDrawable, Drawable tile,
				Drawable connectorHorizontal, Drawable connectorVertical) {
			this.edge = edgeDrawable;
			this.straight = straightDrawable;
			this.connectorHorizontal = connectorHorizontal;
			this.connectorVertical = connectorVertical;
			this.innerTile = tile;
			rightInnerTile = tile;
			bottomTile = tile;
			bottomRightTile = tile;
		}

		public PuzzleActorStyle(PuzzleActorStyle style) {
			set(style);
		}

		public void set(PuzzleActorStyle style) {
			edge = style.edge;
			straight = style.straight;
			connectorHorizontal = style.connectorHorizontal;
			connectorVertical = style.connectorVertical;
			connectorWidth = style.connectorWidth;
			innerTile = style.innerTile;
			innerTileMidOffsetX = style.innerTileMidOffsetX;
			innerTileMidOffsetY = style.innerTileMidOffsetY;
			rightInnerTile = style.rightInnerTile;
			rightInnerTileMidOffsetX = style.rightInnerTileMidOffsetX;
			rightInnerTileMidOffsetY = style.rightInnerTileMidOffsetY;
			bottomTile = style.bottomTile;
			bottomTileMidOffsetX = style.bottomTileMidOffsetX;
			bottomTileMidOffsetY = style.bottomTileMidOffsetY;
			bottomRightTile = style.bottomRightTile;
			bottomRightTileMidOffsetX = style.bottomRightTileMidOffsetX;
			bottomRightTileMidOffsetY = style.bottomRightTileMidOffsetY;
			tilePaddingX = style.tilePaddingX;
			tilePaddingY = style.tilePaddingY;
			outerMarginTop = style.outerMarginTop;
			outerMarginBottom = style.outerMarginBottom;
			outerMarginLeft = style.outerMarginLeft;
			outerMarginRight = style.outerMarginRight;
			forceSquareTiles = style.forceSquareTiles;
		}

		public void validate() {
			if (edge == null) {
				throw new RuntimeException(
						"Impossible to obtain valid state without edge set");
			}
			if (straight == null) {
				throw new RuntimeException(
						"Impossible to obtain valid state without straight set");
			}
			if (connectorHorizontal == null) {
				throw new RuntimeException(
						"Impossible to obtain valid state without horizontal connector set");
			}
			if (connectorVertical == null) {
				throw new RuntimeException(
						"Impossible to obtain valid state without vertical connector set");
			}
			if (innerTile == null) {
				throw new RuntimeException(
						"Impossible to obtain valid state without innerTile set");
			}
			if (rightInnerTile == null) {
				rightInnerTile = innerTile;
			}
			if (bottomTile == null) {
				bottomTile = innerTile;
			}
			if (bottomRightTile == null) {
				bottomRightTile = innerTile;
			}
		}
	}

	private class InputListener extends DragListener {
		@Override
		public void dragStart(InputEvent event, float x, float y, int pointer) {
			System.out.println("Start");
		}

		@Override
		public void drag(InputEvent event, float x, float y, int pointer) {
			System.out.println("drag");
		}

		@Override
		public void dragStop(InputEvent event, float x, float y, int pointer) {
			System.out.println("Stop");
		}
	}
}
