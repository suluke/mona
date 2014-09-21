package de.lksbhm.mona.ui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

class PuzzleActorStyle {
	static String brightnessUniformName = "u_brightness";

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
	public boolean forceEqualPadding = true;
	public float outerMarginLeft = 0.05f; // 0.1 = 10 percent relative to
											// actor
											// size
	public float outerMarginRight = 0.05f; // Percent
	public float outerMarginTop = 0.05f; // Percent
	public float outerMarginBottom = 0.05f; // Percent
	public boolean forceSquareTiles = true;
	public boolean forceCenter = true; // if set, marginRight and
										// marginBottom will be ignored
	private static ShaderProgram brightnessShader;

	static ShaderProgram getBrightnessShader() {
		if (brightnessShader == null) {
			brightnessShader = new ShaderProgram(
					Gdx.files.internal("shaders/brightness/shader.vert"),
					Gdx.files.internal("shaders/brightness/shader.frag"));
			System.out.println(ShaderProgram.getManagedStatus());
		}
		return brightnessShader;
	}

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
	public PuzzleActorStyle(Drawable edgeDrawable, Drawable straightDrawable,
			Drawable tile, Drawable connectorHorizontal,
			Drawable connectorVertical) {
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
		forceEqualPadding = style.forceEqualPadding;
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