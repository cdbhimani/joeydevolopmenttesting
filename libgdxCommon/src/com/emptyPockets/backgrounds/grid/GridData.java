package com.emptyPockets.backgrounds.grid;

import com.badlogic.gdx.math.Vector2;

public class GridData {
	Node[][] nodes;
	DualNodeLink[] links;
	
	public void createGrid(GridSettings set){

		float posX = 0;
		float posY = 0;
		float inverseMass = 0;
		
		//Create Nodes
		nodes = new Node[set.numX][set.numY];
		Vector2[][] posData = new Vector2[set.numX][set.numY];
		
		for(int x = 0; x < set.numX; x++){
			for(int y = 0; y < set.numY; y++){
				float xP = set.bounds.x+set.bounds.width*(x/(set.numX-1f));
				float yP = set.bounds.y+set.bounds.height*(y/(set.numY-1f));
				posData[x][y] = new Vector2(xP,yP);
			}
		}
	}
	public void solve() {
		for(DualNodeLink link : links){
			link.solve();
		}
	}

	public void update(float delta) {
		for(Node[] nodeData : nodes){
			for(Node node : nodeData){
				node.update(delta);
			}
		}
	}
}
