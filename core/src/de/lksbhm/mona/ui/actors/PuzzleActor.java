package de.lksbhm.mona.ui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import de.lksbhm.mona.puzzle.Piece;
import de.lksbhm.mona.puzzle.Puzzle;

public class PuzzleActor extends Widget {

	private Puzzle puzzle;
	private PuzzleActorStyle style;
	private int cellWidth;
	private int cellHeight;

	public PuzzleActor(PuzzleActorStyle style) {
		this.style = style;
	}

	public PuzzleActor(Skin skin) {
		this.style = skin.get(PuzzleActorStyle.class);
	}

	public void setStyle(PuzzleActorStyle style) {
		this.style = style;
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

	}

	@Override
	public void layout() {
	}

	public static class PuzzleActorStyle {
		public Drawable edge;
		public Drawable straight;
		public Drawable innerTile;
		public int innerTileMidOffsetX = 0;
		public int innerTileMidOffsetY = 0;
		public Drawable rightInnerTile;
		public int rightInnerTileMidOffsetX = 0;
		public int rightInnerTileMidOffsetY = 0;
		public Drawable bottomTile;
		public int bottomTileMidOffsetX = 0;
		public int bottomTileMidOffsetY = 0;
		public Drawable bottomRightTile;
		public int bottomRightTileMidOffsetX = 0;
		public int bottomRightTileMidOffsetY = 0;
		public int tilePaddingX = 0;
		public int tilePaddingY = 0;
		public int outerMarginLeft = 10;
		public int outerMarginRight = 10;
		public int outerMarginTop = 10;
		public int outerMarginBottom = 10;

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
	}
}
