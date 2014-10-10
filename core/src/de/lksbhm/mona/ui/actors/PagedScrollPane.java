/**
 * Taken from https://gist.github.com/MobiDevelop/5540815
 * Copyright MobiDevelop
 * With minor changes: removed unused fields, add <?> to cell where necessary, add relativePageWidth option, applyPageBounds(), remove default space(50)
 */

package de.lksbhm.mona.ui.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class PagedScrollPane extends ScrollPane {

	private final Table content;
	private boolean wasPanDragFling = false;

	private float relativePageWidth = 1.f;

	public PagedScrollPane() {
		super(null);
		content = new Table();
		setWidget(content);
	}

	public PagedScrollPane(Skin skin) {
		super(null, skin);
		content = new Table();
		setWidget(content);
	}

	public PagedScrollPane(Skin skin, String styleName) {
		super(null, skin, styleName);
		content = new Table();
		setWidget(content);
	}

	public PagedScrollPane(Actor widget, ScrollPaneStyle style) {
		super(null, style);
		content = new Table();
		setWidget(content);
	}

	public void addPages(Actor... pages) {
		for (Actor page : pages) {
			addPage(page);
		}
	}

	public void addPage(Actor page) {
		applyPageBounds(content.add(page));
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (wasPanDragFling && !isPanning() && !isDragging() && !isFlinging()) {
			wasPanDragFling = false;
			scrollToPage();
		} else {
			if (isPanning() || isDragging() || isFlinging()) {
				wasPanDragFling = true;
			}
		}
	}

	@Override
	public void setWidth(float width) {
		super.setWidth(width);
		applyPageBounds();
	}

	private void applyPageBounds() {
		if (content != null) {
			for (Cell<?> cell : content.getCells()) {
				applyPageBounds(cell);
			}
			content.invalidate();
		}
	}

	private void applyPageBounds(Cell<?> pageCell) {
		pageCell.width(getWidth() * relativePageWidth).expandY().fillY();
	}

	public void setPageSpacing(float pageSpacing) {
		if (content != null) {
			content.defaults().space(pageSpacing);
			for (Cell<?> cell : content.getCells()) {
				cell.space(pageSpacing);
			}
			content.invalidate();
		}
	}

	/**
	 * Sets the width of pages relative to this actor
	 * 
	 * @param relativePageWidth
	 */
	public void setRelativePageWidth(float relativePageWidth) {
		this.relativePageWidth = relativePageWidth;
		applyPageBounds();
	}

	private void scrollToPage() {
		final float width = getWidth();
		final float scrollX = getScrollX();
		final float maxX = getMaxX();

		if (scrollX >= maxX || scrollX <= 0)
			return;

		Array<Actor> pages = content.getChildren();
		float pageX = 0;
		float pageWidth = 0;
		if (pages.size > 0) {
			for (Actor a : pages) {
				pageX = a.getX();
				pageWidth = a.getWidth();
				if (scrollX < (pageX + pageWidth * 0.5)) {
					break;
				}
			}
			setScrollX(MathUtils
					.clamp(pageX - (width - pageWidth) / 2, 0, maxX));
		}
	}

}