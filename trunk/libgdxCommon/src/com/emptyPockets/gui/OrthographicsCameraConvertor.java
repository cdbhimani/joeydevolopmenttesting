package com.emptyPockets.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class OrthographicsCameraConvertor implements ViewportConvertor {

	//Temp for camera projections 
	Vector3 _tmpCam2MouseVec = new Vector3();
	Vector2 _panelToCamsizeA = new Vector2();
	Vector2 _panelToCamsizeB = new Vector2();
	OrthographicCamera camera;
	
	public OrthographicsCameraConvertor(OrthographicCamera camera){
		this.camera = camera;
	}
	
	public void camToPanel(float x, float y, Vector2 vec){
		synchronized (_tmpCam2MouseVec) {
			_tmpCam2MouseVec.set(x,y,0);
			camera.unproject(_tmpCam2MouseVec);
			vec.x = _tmpCam2MouseVec.x;
			vec.y = _tmpCam2MouseVec.y;
		}
	}
	
	public void panelToCam(float x, float y, Vector2 vec){
		synchronized (_tmpCam2MouseVec) {
			_tmpCam2MouseVec.set(x,y,0);
			camera.project(_tmpCam2MouseVec);
			vec.x = _tmpCam2MouseVec.x;
			vec.y = _tmpCam2MouseVec.y;
		}
	}
	
	public float panelToCam(float val){
		camToPanel(0, 0, _panelToCamsizeA);
		camToPanel(0, val, _panelToCamsizeB);
		return _panelToCamsizeA.dst(_panelToCamsizeB);
	}
}
