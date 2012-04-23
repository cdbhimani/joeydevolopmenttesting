package com.joey.testing.game.cellSpace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.joey.testing.game.entities.BaseGameEntity;
import com.joey.testing.game.entities.Vehicle;
import com.joey.testing.game.shapes.Circle2D;
import com.joey.testing.game.shapes.Rectangle2D;
import com.joey.testing.game.shapes.Shape2D;
import com.joey.testing.game.shapes.Vector2D;

public class CellSpacePartition<T extends BaseGameEntity> {
	public int maxDepth = 1;
	public Cell<T> tree;

	HashMap<Cell<T>, HashSet<T>> cellToEntities = new HashMap<Cell<T>, HashSet<T>>();
	HashMap<T,HashSet<Cell<T>>> entitiesToCells= new HashMap<T,HashSet<Cell<T>>>();
	ArrayList<T> entites = new ArrayList<T>();
	
	public CellSpacePartition(){
		setSize(new Rectangle2D(0,0,1,1),0);
	}
	
	public void setSize(Rectangle2D r, int maxDepth){	
		this.maxDepth = maxDepth;
		tree =  new Cell<T>(r, 0,maxDepth);
		createCellToEnties();
		updateAllEntities();
	}
	
	public void getNearEntities(T entity, float rad, HashSet<T> result){
		Circle2D c = new Circle2D(entity.pos.x, entity.pos.y, rad);
		getAllEntitiesForLeafCells(c, tree, result);
	}
	private void createCellToEnties(){
		//clear old
		cellToEntities.clear();
		
		//Remove From Entities
		for(HashSet<Cell<T>>c : entitiesToCells.values()){
			c.clear();
		}
		//Get all leaf cells
		ArrayList<Cell<T>> result = new ArrayList<Cell<T>>();
		getAllLeafCells(tree, result);
		
		//Create hashmap
		for(Cell c : result){
			cellToEntities.put(c, new HashSet<T>());
		}
	}

	public void removeEntity(T entity){
		entites.remove(entity);
		System.out.println("Cell Space Remove entity not done");
	}
	
	public void addEntity(T entity){
		entites.add(entity);
		entitiesToCells.put(entity, new HashSet<Cell<T>>());
		
		updateEntity(entity);
	}
	
	public void updateAllEntities(){
		for(T entity : entites){
			entity.updateBoundingShape();
			updateEntity(entity);
		}
	}
	
	public void updateEntity(T entity) {
		HashSet<Cell<T>> cells = entitiesToCells.get(entity);
		boolean valid = false;
		for(Cell<T> c : cells){
			if(c.region.contains(entity.boundingShape)){
				return;
			}
		}
		
		//Remove from all cells
		for(Cell<T> c : cells){
			cellToEntities.get(c).remove(entity);
		}
		entitiesToCells.get(entity).clear();
		//Add to new
		addAllEntitiesThatIntersectLeafCells(entity, tree);
	}
	
	
	/**
	 * For getting the entities
	 * @param shape
	 * @param c
	 * @param result
	 */
	public void addAllEntitiesThatIntersectLeafCells(T entity, Cell<T> c){
		if(c.isLeaf){
			if(c.region.intersects(entity.boundingShape)){
				cellToEntities.get(c).add(entity);
				entitiesToCells.get(entity).add(c);
			}
		}else{
			//If shape fully contain cell, add all leafs
			if(entity.boundingShape.contains(c.region)){
				addAllEntitiesForGivenCell(entity,c);
			}
			else{
				addAllEntitiesThatIntersectLeafCells(entity,c.topLeft);
				addAllEntitiesThatIntersectLeafCells(entity,c.topRight);
				addAllEntitiesThatIntersectLeafCells(entity,c.bottomLeft);
				addAllEntitiesThatIntersectLeafCells(entity,c.bottomRight);
			}
		}
	}
	
	public void addAllEntitiesForGivenCell(T entity,Cell<T> c){
		if(c.isLeaf){
			cellToEntities.get(c).add(entity);
			entitiesToCells.get(entity).add(c);
		}
		else{
			addAllEntitiesForGivenCell(entity,c.topLeft);
			addAllEntitiesForGivenCell(entity,c.topRight);
			addAllEntitiesForGivenCell(entity,c.bottomLeft);
			addAllEntitiesForGivenCell(entity,c.bottomRight);
		}
	}
	
	/**
	 * For getting the entities
	 * @param shape
	 * @param c
	 * @param result
	 */
	public void getAllEntitiesForLeafCells(Shape2D shape, Cell<T> c, HashSet<T> result){
		if(c.isLeaf){
			if(c.region.intersects(shape)){
				result.addAll(cellToEntities.get(c));
			}
		}else{
			//If shape fully contain cell, add all leafs
			if(shape.contains(c.region)){
				getAllEntitiesForGivenCell(c, result);
			}
			else{
				getAllEntitiesForLeafCells(shape,c.topLeft,result);
				getAllEntitiesForLeafCells(shape,c.topRight,result);
				getAllEntitiesForLeafCells(shape,c.bottomLeft,result);
				getAllEntitiesForLeafCells(shape,c.bottomRight,result);
			}
		}
	}
	
	public void getAllEntitiesForGivenCell(Cell<T> c, HashSet<T> result){
		if(c.isLeaf){
			result.addAll(cellToEntities.get(c));
		}
		else{
			getAllEntitiesForGivenCell(c.topLeft,result);
			getAllEntitiesForGivenCell(c.topRight,result);
			getAllEntitiesForGivenCell(c.bottomLeft,result);
			getAllEntitiesForGivenCell(c.bottomRight,result);
		}
	}
	
	/**
	 * For getting the leafs
	 * @param shape
	 * @param c
	 * @param result
	 */
	
	public void getAllLeafCellsThatIntersectShape(Shape2D shape, Cell<T> c, ArrayList<Cell<T>> result){
		if(c.isLeaf){
			if(c.region.intersects(shape)){
				result.add(c);
			}
		}else{
			//If shape fully contain cell, add all leafs
			if(shape.contains(c.region)){
				getAllLeafCells(c, result);
			}
			else{
				getAllLeafCellsThatIntersectShape(shape,c.topLeft,result);
				getAllLeafCellsThatIntersectShape(shape,c.topRight,result);
				getAllLeafCellsThatIntersectShape(shape,c.bottomLeft,result);
				getAllLeafCellsThatIntersectShape(shape,c.bottomRight,result);
			}
		}
	}
	
	public void getAllLeafCells(Cell<T> c, ArrayList<Cell<T>> result){
		if(c.isLeaf){
			result.add(c);
		}
		else{
			getAllLeafCells(c.topLeft,result);
			getAllLeafCells(c.topRight,result);
			getAllLeafCells(c.bottomLeft,result);
			getAllLeafCells(c.bottomRight,result);
		}
	}
	
	
}
