package com.emptyPockets.test.beam;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class VertexTool implements Disposable{
	int vertexLength = 5;
	int middleSegmentCount = 1;
	
	float[] vertexData;
	
	TextureAtlas atlas;
	AtlasRegion startBackgroundRegion;
	AtlasRegion  middleBackgroundRegion;
	AtlasRegion  endBackgroundRegion;
	
	AtlasRegion startOverlayRegion;
	AtlasRegion  middleOverlayRegion;
	AtlasRegion  endOverlayRegion;
	
	float middleScaleX = 1;
	float middleScaleY = 3;
	float startScaleX = 1;
	float startScaleY = 1;
	float endScaleX = 1;
	float endScaleY = 1;
	
	Matrix3 transform;
	public void dispose(){
		vertexData = null;
		atlas.dispose();
		startBackgroundRegion = null;
		endBackgroundRegion=null;
		middleBackgroundRegion = null;
		transform = null;
	}
	
	public void create(){
		atlas = new TextureAtlas("pack/SpaceTest.pack");
		transform = new Matrix3();
		createRegions();
		createVertexData();
	}
	
	private void createRegions(){
		startBackgroundRegion = atlas.findRegion("lasers/default/start");
		startBackgroundRegion.flip(false, true);
		endBackgroundRegion = atlas.findRegion("lasers/default/end");
		endBackgroundRegion.flip(false, true);
		middleBackgroundRegion = atlas.findRegion("lasers/default/laser");
		
		startOverlayRegion = atlas.findRegion("lasers/default/start_overlay");
		startOverlayRegion.flip(false, true);
		endOverlayRegion = atlas.findRegion("lasers/default/end_overlay");
		endOverlayRegion.flip(false, true);
		middleOverlayRegion = atlas.findRegion("lasers/default/laserOverlayStatic");
	}
	
	private void createVertexData(){
		vertexData = createVertex(middleSegmentCount+2);
		resetVertexFull();
	}
	
	private float[] createVertex(int regionCount){
		return new float[vertexLength*4*(regionCount)];
	}
	
	public void applyTransform(){
		transform(vertexData, transform);
	}
	public void resetVertexFull(){
		resetVertexPosition();
		resetVertexColor(Color.WHITE.toFloatBits());
	}
	
	public float getSizeX(){
		return middleBackgroundRegion.getRegionWidth()*middleScaleX;
	}
	
	public float getSizeY(){
		return startBackgroundRegion.getRegionHeight()*startScaleY+
		middleBackgroundRegion.getRegionHeight()*middleScaleY+
		endBackgroundRegion.getRegionHeight()*endScaleY;
	}
	private void resetVertexPosition(){
		//Set laser start
		setRegionVertexPosition(vertexData,0,0,0,startScaleX, startScaleY, startBackgroundRegion);
		//Set Middle Position
		for(int i=0; i < middleSegmentCount; i++){
				setRegionVertexPosition(vertexData,i+1,0,startBackgroundRegion.getRegionHeight()*startScaleY+i*middleBackgroundRegion.getRegionHeight()*middleScaleY,middleScaleX,middleScaleY,middleBackgroundRegion);
		}
		//Set laser end
		setRegionVertexPosition(vertexData,middleSegmentCount+1,0,startBackgroundRegion.getRegionHeight()*startScaleY+middleSegmentCount*middleBackgroundRegion.getRegionHeight()*middleScaleY, endScaleX, endScaleY,endBackgroundRegion);
	}

	public void resetVertexColor(float color){
		//Set laser start
		setRegionVertexColor(vertexData,0,color);
		//Set Middle Position
		for(int i=0; i < middleSegmentCount; i++){
			setRegionVertexColor(vertexData,i+1,color);
		}
		//Set laser end
		setRegionVertexColor(vertexData,middleSegmentCount+1,color);
	}
	
	public void resetVertexTexturePosition(AtlasRegion start, AtlasRegion middle, AtlasRegion end){
		//Set laser start
		setRegionVertexTexturePosition(vertexData,0,start);
		//Set Middle Position
		for(int i=0; i < middleSegmentCount; i++){
			setRegionVertexTexturePosition(vertexData,i+1, middle);
		}
		//Set laser end
		setRegionVertexTexturePosition(vertexData,middleSegmentCount+1,end);		
	}
	
	private void setRegionVertexPosition(float[] vertexData, int region, float x, float y, float scaleX, float scaleY, AtlasRegion textureRegion){
		int i;
		i = 0;
		vertexData[region*vertexLength*4+i * vertexLength + 0] = x;
		vertexData[region*vertexLength*4+i * vertexLength + 1] = y;
		i++;
		vertexData[region*vertexLength*4+i * vertexLength + 0] = x;
		vertexData[region*vertexLength*4+i * vertexLength + 1] = y+textureRegion.getRegionHeight()*scaleY;
		i++;
		vertexData[region*vertexLength*4+i * vertexLength + 0] = x+textureRegion.getRegionWidth()*scaleX;
		vertexData[region*vertexLength*4+i * vertexLength + 1] = y+textureRegion.getRegionHeight()*scaleY;
		i++;
		vertexData[region*vertexLength*4+i * vertexLength + 0] = x+textureRegion.getRegionWidth()*scaleX;
		vertexData[region*vertexLength*4+i * vertexLength + 1] = y;
	}
	
	private void setRegionVertexTexturePosition(float[] vertexData, int region, AtlasRegion textureRegion){
		int i = 0;
		i = 0;
		vertexData[region*vertexLength*4+i * vertexLength + 3] = textureRegion.getU();
		vertexData[region*vertexLength*4+i * vertexLength + 4] = textureRegion.getV();
		i++;
		vertexData[region*vertexLength*4+i * vertexLength + 3] = textureRegion.getU();
		vertexData[region*vertexLength*4+i * vertexLength + 4] = textureRegion.getV2();
		i++;
		vertexData[region*vertexLength*4+i * vertexLength + 3] = textureRegion.getU2();
		vertexData[region*vertexLength*4+i * vertexLength + 4] = textureRegion.getV2();
		i++;
		vertexData[region*vertexLength*4+i * vertexLength + 3] = textureRegion.getU2();
		vertexData[region*vertexLength*4+i * vertexLength + 4] = textureRegion.getV();
	}
	
	private void setRegionVertexColor(float[] vertexData, int region,float color) {
		int i = 0;
		i = 0;
		vertexData[region*vertexLength*4+i * vertexLength + 2] = color;
		i++;
		vertexData[region*vertexLength*4+i * vertexLength + 2] = color;
		i++;
		vertexData[region*vertexLength*4+i * vertexLength + 2] = color;
		i++;
		vertexData[region*vertexLength*4+i * vertexLength + 2] = color;
	}
	
	public void transform(float[] vertexData,Matrix3 mat){
		float x;
		float y;
		
		int pos = 0;
		while(pos<vertexData.length){
			x = vertexData[pos];
			y = vertexData[pos+1];
			vertexData[pos] = x * mat.val[0] + y * mat.val[3] + mat.val[6];
			vertexData[pos+1]= x * mat.val[1] + y * mat.val[4] + mat.val[7];
			pos+=vertexLength;
		}
	}

	public void printVertex(float[] vertex, int vert){
		int pos = vert*vertexLength;
		System.out.printf("P[%f,%f]", vertex[pos],vertex[pos+1]);
	}
	
	public void draw(SpriteBatch sbatch, Vector2 p1, float angle, float backColor,float overlayColor){
		resetVertexPosition();
		transform.idt();
		transform.translate(p1);
		transform.rotate(angle);
		transform.translate(-startBackgroundRegion.getRegionWidth()/2, -startBackgroundRegion.getRegionHeight()/2);
		applyTransform();
		
		middleBackgroundRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		resetVertexColor(backColor);
		resetVertexTexturePosition(startBackgroundRegion, middleBackgroundRegion, endBackgroundRegion);
		sbatch.draw(middleBackgroundRegion.getTexture(), vertexData, 0, vertexData.length);
		
		resetVertexColor(overlayColor);
		resetVertexTexturePosition(startOverlayRegion, middleOverlayRegion, endOverlayRegion);
		sbatch.draw(middleBackgroundRegion.getTexture(), vertexData, 0, vertexData.length);
	}
}
