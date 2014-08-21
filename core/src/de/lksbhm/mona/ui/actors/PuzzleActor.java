package de.lksbhm.mona.ui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import de.lksbhm.mona.puzzle.Piece;
import de.lksbhm.mona.puzzle.Puzzle;

public class PuzzleActor extends Widget {

	private Puzzle puzzle;
	private final PuzzleActorStyle style = new PuzzleActorStyle();
	private float marginLeft;
	private float marginTop;
	private float cellWidth;
	private float cellHeight;
	private float paddingWidth;
	private float paddingHeight;
	private final boolean invertY = false; // false means, origin is down left
	private final Vector2 tileCanvasOrigin = new Vector2();
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
		Piece[][] tiles = puzzle.getTiles();
		for (Piece[] array : tiles) {
			for (Piece tile : array) {
				drawTile(batch, tile);
			}
		}
	}

	private void drawTile(Batch batch, Piece tile) {
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
		switch (tile.getType()) {
		case EDGE: {
			style.edge.draw(batch, tileCanvasOrigin.x, tileCanvasOrigin.y,
					cellWidth, cellHeight);
			break;
		}
		case EMPTY: {
			break;
		}
		case STRAIGHT: {
			style.straight.draw(batch, tileCanvasOrigin.x, tileCanvasOrigin.y,
					cellWidth, cellHeight);
			break;
		}
		default: {
			throw new RuntimeException();
		}
		}
	}

	private void drawInner(Batch batch, Piece tile) {
		int x = tile.getX();
		int y = tile.getY();
		float drawX = marginLeft + x * (cellWidth + paddingWidth);
		float drawY = marginTop + y * (cellHeight + paddingHeight);
		style.innerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
		tileCanvasOrigin.set(drawX, drawY);
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
		tileCanvasOrigin.set(drawX, drawY);
	}

	private void drawTop(Batch batch, Piece tile) {
		int x = tile.getX();
		float drawX = marginLeft + x * (cellWidth + paddingWidth);
		float drawY = marginTop;
		if (!invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
		tileCanvasOrigin.set(drawX, drawY);
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
		tileCanvasOrigin.set(drawX, drawY);
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
		tileCanvasOrigin.set(drawX, drawY);
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
		tileCanvasOrigin.set(drawX, drawY);
	}

	private void drawBottomLeft(Batch batch, Piece tile) {
		float drawX = marginLeft;
		float drawY = marginTop + (puzzle.getHeight() - 1)
				* (cellHeight + paddingHeight);
		if (!invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
		tileCanvasOrigin.set(drawX, drawY);
	}

	private void drawTopLeft(Batch batch, Piece tile) {
		float drawX = marginLeft;
		float drawY = marginTop;
		if (!invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		style.innerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
		tileCanvasOrigin.set(drawX, drawY);
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
	}

	public static class PuzzleActorStyle {
		public Drawable edge;
		public Drawable straight;
		public Drawable innerTile;
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

		public PuzzleActorStyle(Drawable edgeDrawable,
				Drawable straightDrawable, Drawable tile) {
			this.edge = edgeDrawable;
			this.straight = straightDrawable;
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
