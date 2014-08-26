package de.lksbhm.mona.ui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class InvalidMarker {
	private InvalidMarkerStyle style;
	private float midX;
	private float midY;
	private float currentWidth = 0;
	private float timePassed = 0;

	public void setStyle(InvalidMarkerStyle style) {
		this.style = style;
	}

	public void setMid(float x, float y) {
		this.midX = x;
		this.midY = y;
	}

	public void render(Batch batch, float offsetX, float offsetY) {
		timePassed += Gdx.graphics.getDeltaTime();
		if (timePassed > style.expandTime) {
			timePassed = style.expandTime;
		}
		currentWidth = style.maxWidth
				* style.interpolation.apply(timePassed / style.expandTime);
		if (style.repeat && timePassed == style.expandTime) {
			timePassed = 0;
		}
		float currentHeight = currentWidth * style.aspectRatio;
		style.texture.draw(batch, midX - currentWidth / 2 + offsetX, midY
				- currentHeight / 2 + offsetY, currentWidth, currentHeight);
	}

	public static class InvalidMarkerStyle {
		public Drawable texture;
		public float expandTime = 1;
		public float aspectRatio = 1;
		public float maxWidth = 100;
		public boolean repeat = true;
		public Interpolation interpolation = Interpolation.pow2Out;

		public void set(InvalidMarkerStyle other) {
			texture = other.texture;
			expandTime = other.expandTime;
			aspectRatio = other.aspectRatio;
			maxWidth = other.maxWidth;
			repeat = other.repeat;
			interpolation = other.interpolation;
		}
	}
}
