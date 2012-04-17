package com.joey.testing.spatialPartitioning;

import java.util.ArrayList;

public class QuadTreeNode {
	Rectangle2D region;
	boolean isLeaf = false;
	int divisionLimit;

	ArrayList<Point2D> points;

	QuadTreeNode NW;
	QuadTreeNode SW;
	QuadTreeNode NE;
	QuadTreeNode SE;

	public QuadTreeNode(Rectangle2D Location, int divisionLimit) {
		this.region = Location;
		this.divisionLimit = divisionLimit;
		this.isLeaf = true;
		points = new ArrayList<Point2D>(divisionLimit);
	}

	public void addPoint(Point2D p) {
		// If leaf add to current points
		if (isLeaf) {
			points.add(p);

			// If too many added, subdivide
			if (points.size() > divisionLimit && !reachedMaxSubdivision()) {
				subDivide();
			}
		} else {
			if (NW.contains(p)) {
				NW.addPoint(p);
			} else if (NE.contains(p)) {
				NE.addPoint(p);
			} else if (SW.contains(p)) {
				SW.addPoint(p);
			} else if (SE.contains(p)) {
				SE.addPoint(p);
			}
		}

	}

	public void subDivide() {
		isLeaf = false;
		int midX = region.x1+(region.x2-region.x1)/2;
		int midY = region.y1+(region.y2-region.y1)/2;

		//Create Regions
		NW = new QuadTreeNode(new Rectangle2D(region.x1,region.y1, midX, midY), divisionLimit);
		NE = new QuadTreeNode(new Rectangle2D(midX,region.y1, region.x2, midY), divisionLimit);
		SW = new QuadTreeNode(new Rectangle2D(region.x1, midY,midX,region.y2), divisionLimit);
		SE = new QuadTreeNode(new Rectangle2D(midX, midY,region.x2,region.y2), divisionLimit);

		//Add Points to region
		for(Point2D p : points){
			addPoint(p);
		}
		//Clear points from here
		points.clear();
	}

	public boolean reachedMaxSubdivision(){
		return !(Math.abs(region.x1-region.x2) > 1 || Math.abs(region.y1-region.y2) >1);
	}
	public boolean contains(Point2D p) {
		return region.contains(p);
	}

}
