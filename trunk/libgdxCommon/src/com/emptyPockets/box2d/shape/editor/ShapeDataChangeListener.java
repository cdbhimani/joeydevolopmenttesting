package com.emptyPockets.box2d.shape.editor;

import com.emptyPockets.box2d.shape.data.ShapeData;

public interface ShapeDataChangeListener {
	public abstract void shapeDataChanged(ShapeData data, boolean finished);
}
