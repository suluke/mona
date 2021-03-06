package de.lksbhm.mona.ui.actors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Pool;

import de.lksbhm.mona.puzzle.Piece;
import de.lksbhm.mona.puzzle.Piece.Type;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.PuzzleChangedListener;
import de.lksbhm.mona.puzzle.representations.Direction;
import de.lksbhm.mona.ui.actors.InvalidMarker.InvalidMarkerStyle;

public class PuzzleActor extends Widget {
	private Puzzle puzzle;
	private final Color tmpColor = new Color();
	private final PuzzleActorStyle style = new PuzzleActorStyle();
	private float marginLeft;
	private float marginTop;
	private float cellWidth;
	private float cellHeight;
	private float lineWidth;
	private float paddingWidth;
	private float paddingHeight;
	private final boolean invertY = false; // false means, origin is down left
	private final ArrayList<PuzzleModificationListener> modificationListeners = new ArrayList<PuzzleModificationListener>();
	/*
	 * alpha will be ignored anyways
	 */
	private float brightnessIncrease = 0;
	private final PuzzleActorInput inputListener = new PuzzleActorInput(this);
	private final InvalidMarkerStyle markerStyle = new InvalidMarkerStyle();
	private final Pool<InvalidMarker> markerPool = new Pool<InvalidMarker>() {
		@Override
		protected InvalidMarker newObject() {
			InvalidMarker marker = new InvalidMarker();
			marker.setStyle(markerStyle);
			return marker;
		}

		@Override
		public void free(InvalidMarker object) {
			super.free(object);
			object.reset();
		};
	};
	private boolean puzzleHasChanged = false;
	private final PuzzleChangedListener changeListener = new PuzzleChangedListener() {
		@Override
		public void onChange() {
			markers.clear();
			puzzleHasChanged = true;
		}
	};
	private final LinkedList<InvalidMarker> markers = new LinkedList<InvalidMarker>();
	private final Action updateMarkersDaemon = new Action() {
		private final float duration = (markerStyle.expandTime + markerStyle.repeatTimeout);
		private float time;

		@Override
		public boolean act(float delta) {
			if (time < duration) {
				time += delta;
				if (time < duration) {
					return false;
				}
			}
			time = 0;

			if (puzzleHasChanged) {
				Iterator<Piece> invalidTilesIterator = puzzle
						.invalidTilesIterator();
				for (InvalidMarker marker : markers) {
					markerPool.free(marker);
				}
				markers.clear();
				while (invalidTilesIterator.hasNext()) {
					Piece tile = invalidTilesIterator.next();
					InvalidMarker marker = markerPool.obtain();
					marker.setStyle(markerStyle);
					marker.setCellSize(Math.min(cellWidth, cellHeight));
					marker.setMid(
							PuzzleActorCoordinateHelper.getTileOriginX(
									PuzzleActor.this, tile, false)
									+ cellWidth
									/ 2,
							PuzzleActorCoordinateHelper.getTileOriginY(
									PuzzleActor.this, tile, false)
									+ cellHeight
									/ 2);
					markers.add(marker);
				}
			}
			return false;
		}
	};

	public PuzzleActor(PuzzleActorStyle style, InvalidMarkerStyle markerStyle) {
		this.style.set(style);
		this.markerStyle.set(markerStyle);
		addListener(inputListener);
		addAction(updateMarkersDaemon);
	}

	public PuzzleActor(Skin skin) {
		this(skin.get(PuzzleActorStyle.class), skin
				.get(InvalidMarkerStyle.class));
	}

	public void setStyle(PuzzleActorStyle style) {
		this.style.set(style);
		invalidate();
	}

	public void setPuzzle(Puzzle puzzle) {
		if (this.puzzle != null) {
			this.puzzle.removeChangeListener(changeListener);
		}
		if (puzzle != null) {
			puzzle.addChangeListener(changeListener);
		}
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
		tmpColor.set(getColor());
		tmpColor.a *= parentAlpha;
		batch.setColor(tmpColor);
		boolean brightnessShaderActive = false;
		if (brightnessIncrease != 0) {
			ShaderProgram brightnessShader = PuzzleActorStyle
					.getBrightnessShader();
			if (brightnessShader != null) {
				if (brightnessShader.isCompiled()) {
					batch.setShader(brightnessShader);
					brightnessShader.setUniformf(
							PuzzleActorStyle.brightnessUniformName,
							brightnessIncrease);
					brightnessShaderActive = true;
				} else {
					Gdx.app.error("PuzzleActor", brightnessShader.getLog());
				}
			}
		}
		drawTileBackgrounds(batch);
		drawTileTypes(batch);
		drawTileConnectors(batch);
		drawInvalidMarkers(batch);
		if (brightnessShaderActive) {
			// reset shader
			batch.setShader(null);
		}
	}

	private void drawInvalidMarkers(Batch batch) {
		for (InvalidMarker marker : markers) {
			marker.render(batch);
		}
	}

	private void drawTileConnectors(Batch batch) {
		Piece inAdj;
		Piece outAdj;
		for (Piece tile : puzzle) {
			if (tile.getType() == Type.INVISIBLE) {
				continue;
			}
			inAdj = tile.getInAdjacent();
			outAdj = tile.getOutAdjacent();
			if (inAdj != null) {
				if (inAdj.getInAdjacent() == tile
						|| inAdj.getOutAdjacent() == tile) {
					// We're iterating from top-left to bottom-right, so
					// this would be an overdraw
					if (tile.getInDirection() != Direction.UP
							&& tile.getInDirection() != Direction.LEFT) {
						// draw continuous line
						drawConnector(batch, tile, tile.getInDirection(), true);
					}
				} else {
					// only draw up to some point in between
					drawConnector(batch, tile, tile.getInDirection(), false);
				}
			}
			if (outAdj != null) {
				if (outAdj.getInAdjacent() == tile
						|| outAdj.getOutAdjacent() == tile) {
					// We're iterating from top-left to bottom-right, so
					// this would be an overdraw
					if (tile.getOutDirection() != Direction.UP
							&& tile.getOutDirection() != Direction.LEFT) {
						// draw continuous line
						drawConnector(batch, tile, tile.getOutDirection(), true);
					}
				} else {
					// only draw up to some point in between
					drawConnector(batch, tile, tile.getOutDirection(), false);
				}
			}
		}
	}

	private void drawTileTypes(Batch batch) {
		for (Piece tile : puzzle) {
			if (tile.getType() == Type.INVISIBLE) {
				continue;
			}
			drawType(batch, tile);
		}
	}

	private void drawTileBackgrounds(Batch batch) {
		for (Piece tile : puzzle) {
			if (tile.getType() == Type.INVISIBLE) {
				continue;
			}
			drawBackground(batch, tile);
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
		// initialize here because compiler is too stupid to see it later
		float endX = 0;
		float endY = 0;
		boolean horizontal = direction == Direction.LEFT
				|| direction == Direction.RIGHT;
		// set end coordinate that will be equal to start coordinate
		if (horizontal) {
			endY = startY;
			startY -= lineWidth / 2;
			endY += lineWidth / 2;
		} else {
			endX = startX;
			startX -= lineWidth / 2;
			endX += lineWidth / 2;
		}

		// determine the more complicated end coordinate
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
					endX = startX - cellWidth / 2;
				} else {
					endX = startX + cellWidth / 2;
				}
			} else {
				if (direction == Direction.UP) {
					endY = startY - cellHeight / 2;
				} else {
					endY = startY + cellHeight / 2;
				}
			}
		}
		if (!invertY) {
			startY = getHeight() - startY;
			endY = getHeight() - endY;
		}

		// sort coordinates to draw from lower to higher
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

		// add a little offset so line ends will overlap
		if (horizontal) {
			startX -= lineWidth / 2;
			endX += lineWidth / 2;
		} else {
			startY -= lineWidth / 2;
			endY += lineWidth / 2;
		}

		startX += getX();
		endX += getX();
		startY += getY();
		endY += getY();

		// draw, finally
		if (horizontal) {
			style.connectorHorizontal.draw(batch, startX, startY,
					endX - startX, endY - startY);
		} else {
			style.connectorVertical.draw(batch, startX, startY, endX - startX,
					endY - startY);
		}
	}

	private void drawType(Batch batch, Piece tile) {
		final float tileX = PuzzleActorCoordinateHelper.getTileOriginX(this,
				tile, true);
		final float tileY = PuzzleActorCoordinateHelper.getTileOriginY(this,
				tile, true);
		switch (tile.getType()) {
		case EDGE: {
			style.edge.draw(batch, tileX, tileY, cellWidth, cellHeight);
			break;
		}
		case STRAIGHT: {
			style.straight.draw(batch, tileX, tileY, cellWidth, cellHeight);
			break;
		}
		default: {
			// don't care for the rest
			break;
		}
		}
	}

	private void drawBackground(Batch batch, Piece tile) {
		final int x = tile.getX();
		final int y = tile.getY();
		final int width = puzzle.getWidth();
		final int height = puzzle.getHeight();
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
		if (invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		drawX += getX();
		drawY += getY();
		style.innerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawBottom(Batch batch, Piece tile) {
		int x = tile.getX();
		float drawX = marginLeft + x * (cellWidth + paddingWidth);
		float drawY = marginTop + (puzzle.getHeight() - 1)
				* (cellHeight + paddingHeight);
		if (invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		drawX += getX();
		drawY += getY();
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawTop(Batch batch, Piece tile) {
		int x = tile.getX();
		float drawX = marginLeft + x * (cellWidth + paddingWidth);
		float drawY = marginTop;
		if (invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		drawX += getX();
		drawY += getY();
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawRight(Batch batch, Piece tile) {
		int y = tile.getY();
		float drawX = marginLeft + (puzzle.getWidth() - 1)
				* (cellWidth + paddingWidth);
		float drawY = marginTop + y * (cellHeight + paddingHeight);
		if (invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		drawX += getX();
		drawY += getY();
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawBottomRight(Batch batch, Piece tile) {
		float drawX = marginLeft + (puzzle.getWidth() - 1)
				* (cellWidth + paddingWidth);
		float drawY = marginTop + (puzzle.getHeight() - 1)
				* (cellHeight + paddingHeight);
		if (invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		drawX += getX();
		drawY += getY();
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawTopRight(Batch batch, Piece tile) {
		float drawX = marginLeft + (puzzle.getWidth() - 1)
				* (cellWidth + paddingWidth);
		float drawY = marginTop;
		if (invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		drawX += getX();
		drawY += getY();
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawLeft(Batch batch, Piece tile) {
		int y = tile.getY();
		float drawX = marginLeft;
		float drawY = marginTop + y * (paddingHeight + cellHeight);
		if (invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		drawX += getX();
		drawY += getY();
		style.innerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawBottomLeft(Batch batch, Piece tile) {
		float drawX = marginLeft;
		float drawY = marginTop + (puzzle.getHeight() - 1)
				* (cellHeight + paddingHeight);
		if (invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		drawX += getX();
		drawY += getY();
		style.rightInnerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	private void drawTopLeft(Batch batch, Piece tile) {
		float drawX = marginLeft;
		float drawY = marginTop;
		if (invertY) {
			drawY = getHeight() - drawY - cellHeight;
		}
		drawX += getX();
		drawY += getY();
		style.innerTile.draw(batch, drawX, drawY, cellWidth, cellHeight);
	}

	@Override
	public void layout() {
		markers.clear();

		style.validate();
		float width = getWidth();
		float height = getHeight();
		float puzzleWidth = puzzle.getWidth();
		float puzzleHeight = puzzle.getHeight();

		float usableWidth = (1 - style.outerMarginLeft - style.outerMarginRight)
				* width;
		float usableHeight = (1 - style.outerMarginTop - style.outerMarginBottom)
				* height;
		marginLeft = width * style.outerMarginLeft;
		marginTop = height * style.outerMarginTop;
		cellWidth = usableWidth / puzzleWidth;
		cellHeight = usableHeight / puzzleHeight;

		paddingWidth = style.tilePaddingX * cellWidth * (puzzleWidth - 1)
				/ puzzleWidth;
		paddingHeight = style.tilePaddingY * cellHeight * (puzzleHeight - 1)
				/ puzzleHeight;

		cellWidth -= paddingWidth;
		cellHeight -= paddingHeight;

		if (style.forceSquareTiles) {
			if (cellWidth > cellHeight) {
				float diff = cellWidth - cellHeight;
				cellWidth -= diff;
				diff *= puzzleWidth;
				diff /= puzzleWidth - 1;
				paddingWidth += diff;
			} else {
				float diff = cellHeight - cellWidth;
				cellHeight -= diff;
				diff *= puzzleHeight;
				diff /= puzzleHeight - 1;
				paddingHeight += diff;
			}
		}
		if (style.forceEqualPadding) {
			paddingWidth = paddingHeight = Math
					.min(paddingWidth, paddingHeight);
		}
		if (style.forceCenter) {
			marginTop = (height - paddingHeight * (puzzleHeight - 1) - cellHeight
					* puzzleHeight) / 2;
			marginLeft = (width - paddingWidth * (puzzleWidth - 1) - cellWidth
					* puzzleWidth) / 2;
		}
		lineWidth = Math.min(cellWidth, cellHeight) * style.connectorWidth;
	}

	public void cancelInput() {
		inputListener.cancel();
	}

	public void setBrightnessIncrease(float increase) {
		brightnessIncrease = increase;
	}

	public void addModificationListener(PuzzleModificationListener listener) {
		modificationListeners.add(listener);
	}

	public void removeModificationListener(PuzzleModificationListener listener) {
		modificationListeners.remove(listener);
	}

	public void clearModificationListeners() {
		modificationListeners.clear();
	}

	/**
	 * @return the puzzle
	 */
	Puzzle getPuzzle() {
		return puzzle;
	}

	/**
	 * @return the style
	 */
	PuzzleActorStyle getStyle() {
		return style;
	}

	/**
	 * @return the marginLeft
	 */
	float getMarginLeft() {
		return marginLeft;
	}

	/**
	 * @return the marginTop
	 */
	float getMarginTop() {
		return marginTop;
	}

	/**
	 * @return the cellWidth
	 */
	float getCellWidth() {
		return cellWidth;
	}

	/**
	 * @return the cellHeight
	 */
	float getCellHeight() {
		return cellHeight;
	}

	/**
	 * @return the lineWidth
	 */
	float getLineWidth() {
		return lineWidth;
	}

	/**
	 * @return the paddingWidth
	 */
	float getPaddingWidth() {
		return paddingWidth;
	}

	/**
	 * @return the paddingHeight
	 */
	float getPaddingHeight() {
		return paddingHeight;
	}

	/**
	 * @return the invertY
	 */
	boolean isInvertY() {
		return invertY;
	}

	void onAddConnection() {
		for (PuzzleModificationListener listener : modificationListeners) {
			listener.onAddConnection();
		}
	}

	void onClearTileConnections() {
		for (PuzzleModificationListener listener : modificationListeners) {
			listener.onClearTileConnections();
		}
	}

	void onClearAllTileConnections() {
		for (PuzzleModificationListener listener : modificationListeners) {
			listener.onClearAllTileConnections();
		}
	}
}
