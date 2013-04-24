package com.emptyPockets.engine2D.spatialPartitions.stores.quadTree;

import java.util.ArrayList;
import java.util.HashSet;

import com.emptyPockets.engine2D.entities.types.BaseGameEntity;
import com.emptyPockets.engine2D.shapes.Rectangle2D;

public class QuadTreeNode<T extends BaseGameEntity>{
	public Rectangle2D region;
	public boolean isLeaf;
	public ArrayList<T> points;
	int divisionLimit;
	public QuadTreeNode NW;
	public QuadTreeNode SW;
	public QuadTreeNode NE;
	public QuadTreeNode SE;
	QuadTree owner;
	
	public QuadTreeNode(QuadTree owner,Rectangle2D Location, int divisionLimit) {
		this.owner = owner;
		this.region = Location;
		this.divisionLimit = divisionLimit;
		this.isLeaf = true;
		points = new ArrayList<T>(divisionLimit);
	}

	public void addEntity(T p) {
		// If leaf add to current points
		if (isLeaf) {
				points.add(p);	
			// If too many added, subdivide
			if (points.size() > divisionLimit && !reachedMaxSubdivision()) {
				subDivide();
			}
		} else {
			if (NW.contains(p)) {
				NW.addEntity(p);
			} else if (NE.contains(p)) {
				NE.addEntity(p);
			} else if (SW.contains(p)) {
				SW.addEntity(p);
			} else if (SE.contains(p)) {
				SE.addEntity(p);
			}
		}

	}

	public void subDivide() {
		isLeaf = false;
		float midX = region.x1+(region.x2-region.x1)/2;
		float midY = region.y1+(region.y2-region.y1)/2;

		//Create Regions
		if(NW == null)NW = new QuadTreeNode(owner,new Rectangle2D(region.x1,region.y1, midX, midY), divisionLimit);
		if(NE == null)NE = new QuadTreeNode(owner,new Rectangle2D(midX,region.y1, region.x2, midY), divisionLimit);
		if(SW == null)SW = new QuadTreeNode(owner,new Rectangle2D(region.x1, midY,midX,region.y2), divisionLimit);
		if(SE == null)SE = new QuadTreeNode(owner,new Rectangle2D(midX, midY,region.x2,region.y2), divisionLimit);

		//Add Points to region
		for(T p : points){
			addEntity(p);
		}
		//Clear points from here
		points.clear();
	}

	public boolean reachedMaxSubdivision(){
		return !(Math.abs(region.x1-region.x2) > owner.minDivision || Math.abs(region.y1-region.y2) >owner.minDivision);
	}
	public boolean contains(BaseGameEntity p) {
		return region.contains(p.pos);
	}

	public void reset() {
		if(!isLeaf){
			if(NW != null)NW.reset();
			if(NE != null)NE.reset();
			if(SW != null)SW.reset();
			if(SE != null)SE.reset();	
		}
		isLeaf = true;
		points.clear();
	}

	public void getAllPoints(HashSet<T> rst){
		if(isLeaf){
			rst.addAll(points);
		} else{
			if(NW != null)NW.getAllPoints(rst);
			if(NE != null)NE.getAllPoints(rst);
			if(SW != null)SW.getAllPoints(rst);
			if(SE != null)SE.getAllPoints(rst);
		}
	}
	
	public void getPointsInRegion(Rectangle2D searchArea, HashSet<T> rst) {
		if(searchArea.intersects(region)){
			if(isLeaf){
				for(T p : points){
					if(searchArea.contains(p.pos)){
						rst.add(p);
					}
				}
			}else{
				if(NW != null)NW.getPointsInRegion(searchArea, rst);
				if(NE != null)NE.getPointsInRegion(searchArea, rst);
				if(SW != null)SW.getPointsInRegion(searchArea, rst);
				if(SE != null)SE.getPointsInRegion(searchArea, rst);
			}
		}
	}

	public void dispose() {
		if(NW != null){
			NW.dispose();
			NW = null;
		}
		if(NE != null){
			NE.dispose();
			NE = null;
		}
		if(SW != null){
			SW.dispose();
			SW = null;
		}
		if(SE != null){
			SE.dispose();
			SE = null;
		}
		
		points.clear();
		points = null;
		region = null;
	}

}
