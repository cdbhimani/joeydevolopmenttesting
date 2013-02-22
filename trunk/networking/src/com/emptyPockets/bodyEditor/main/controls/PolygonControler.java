package com.emptyPockets.bodyEditor.main.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.bodyEditor.entity.PolygonEntity;
import com.emptyPockets.bodyEditor.main.EntityEditorScreen;

public class PolygonControler extends EntityControler{
	EntityEditorScreen owner;
	
	public PolygonControler(EntityEditorScreen owner){
		this.owner = owner;
	}

	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		Vector2 mouse = new Vector2();
		owner.camToPanel(x, y, mouse);
		float mouseDistance = owner.getMouseDistance();
		float mouseDistance2 = mouseDistance*mouseDistance;
		for(Vector2 p : getEntity().getPolygon()){
			if(p.dst2(mouse) < mouseDistance2){
				return true;
			}
		}
		return false;
	}
	
	private PolygonEntity getEntity(){
		return (PolygonEntity) owner.getEntity();
	}
	
	@Override
	public boolean tap(float x, float y, int count, int button) {
		PolygonEntity entity  = getEntity();
		synchronized (entity) {
			Vector2 v = new Vector2();
			owner.camToPanel(x, y, v);
			entity.getPolygon().add(v);
		}
		return true;
	}
	
	public void drawPolygonEntity(ShapeRenderer shape, PolygonEntity entity){
		//Draw Nodes
		float nodeSize = owner.getMouseDistance();
		shape.begin(ShapeType.Rectangle);
		shape.setColor(Color.RED);
		for(Vector2 p : entity.getPolygon() ){
			shape.rect(p.x-nodeSize, p.y-nodeSize, 2*nodeSize, 2*nodeSize);
		}
		shape.end();
		
		//Draw Lines
		Vector2 p1;
		Vector2 p2;
		shape.begin(ShapeType.Line);
		shape.setColor(Color.WHITE);
		for(int i = 1; i <  entity.getPolygon().size(); i++){
			p1 =  entity.getPolygon().get(i-1);
			p2 =  entity.getPolygon().get(i);
			shape.line(p1.x, p1.y, p2.x, p2.y);
		}
		//Close Shape
		if( entity.getPolygon().size() > 2){
			p1 =  entity.getPolygon().get( entity.getPolygon().size()-1);
			p2 =  entity.getPolygon().get(0);
			shape.line(p1.x, p1.y, p2.x, p2.y);
		}
		shape.end();
	}
}
