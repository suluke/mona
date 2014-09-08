package de.lksbhm.mona.ui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class InvalidMarker {
	private InvalidMarkerStyle style;
	private float midX;
	private float midY;
	private float timePassed = 0;
	private float cellSize;

	public void reset() {
		timePassed = 0;
	}

	public void setStyle(InvalidMarkerStyle style) {
		this.style = style;
	}

	public void setMid(float x, float y) {
		this.midX = x;
		this.midY = y;
	}

	public void setCellSize(float cellSize) {
		this.cellSize = cellSize;
	}

	public void render(Batch batch) {
		timePassed += Gdx.graphics.getDeltaTime();
		if (style.repeat) {
			if (timePassed > style.expandTime) {
				if (timePassed < style.expandTime + style.repeatTimeout) {
					return;
				} else {
					timePassed = 0;
				}
			}
		}
		if (timePassed > style.expandTime) {
			timePassed = style.expandTime;
		}
		float interpolation = style.interpolation.apply(timePassed
				/ style.expandTime);
		float currentWidth = style.maxWidth * cellSize * interpolation;
		float currentHeight = currentWidth * style.aspectRatio;
		float currentAlpha = style.alpha;
		if (style.alphaFade != null) {
			if (style.fadeIn) {
				currentAlpha *= style.alphaFade.apply(timePassed
						/ style.expandTime);
			} else {
				currentAlpha *= 1 - style.alphaFade.apply(timePassed
						/ style.expandTime);
			}
		}
		Color c = batch.getColor();
		batch.setColor(c.r, c.g, c.b, c.a * currentAlpha);
		style.texture.draw(batch, midX - currentWidth / 2, midY - currentHeight
				/ 2, currentWidth, currentHeight);
		batch.setColor(c);
	}

	public static class InvalidMarkerStyle {
		public Drawable texture;
		public float expandTime = 0.7f;
		public float aspectRatio = 1;
		public float maxWidth = 1.3f;
		public boolean repeat = true;
		public Interpolation interpolation = Interpolation.sineOut;
		public float repeatTimeout = 1;
		public Interpolation alphaFade = Interpolation.fade;
		public boolean fadeIn = false;
		public float alpha = 1f;

		public void set(InvalidMarkerStyle other) {
			texture = other.texture;
			expandTime = other.expandTime;
			aspectRatio = other.aspectRatio;
			maxWidth = other.maxWidth;
			repeat = other.repeat;
			repeatTimeout = other.repeatTimeout;
			interpolation = other.interpolation;
			alphaFade = other.alphaFade;
			alpha = other.alpha;
			fadeIn = other.fadeIn;
		}
	}
}
