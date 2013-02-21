package com.emptyPockets.bodyEditor.main.controls;

import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.bodyEditor.entity.PolygonEntity;
import com.emptyPockets.bodyEditor.main.EntityEditorScreen;

public class PolygonEntityControls extends EntityControler{
	EntityEditorScreen owner;
	
	public PolygonEntityControls(EntityEditorScreen owner){
		this.owner = owner;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		Vector2 mouse = new Vector2();
		owner.camToPanel(x, y, mouse);
		float mouseDistance = owner.getMouseDistance();
		float mouseDistance2 = mouseDistance*mouseDistance;
		for(Vector2 p : owner.getEntity().getPolygon()){
			if(p.dst2(mouse) < mouseDistance2){
				return true;
			}
		}
		return false;
	}
	

	
	@Override
	public boolean tap(float x, float y, int count, int button) {
		PolygonEntity entity  = owner.getEntity();
		synchronized (entity) {
			Vector2 v = new Vector2();
			owner.camToPanel(x, y, v);
			entity.getPolygon().add(v);
		}
		return true;
	}
	

}
