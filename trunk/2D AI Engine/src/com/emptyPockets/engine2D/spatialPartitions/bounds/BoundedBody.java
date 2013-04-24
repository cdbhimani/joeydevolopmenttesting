package com.emptyPockets.engine2D.spatialPartitions.bounds;

public interface BoundedBody {
	public BoundingShape getBoundingShape();
	public boolean hasBoundedShapeChanged();
}
