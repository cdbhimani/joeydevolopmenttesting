package com.emptyPockets.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class VertexTool implements Disposable{
	int vertexLength = 5;
	int middleSegmentCount = 20;
	
	float[] vertexData;
	
	TextureAtlas atlas;
	AtlasRegion startRegion;
	AtlasRegion  middleRegion;
	AtlasRegion  endRegion;
	Matrix3 transform;
	
	public void dispose(){
		vertexData = null;
		atlas.dispose();
		startRegion = null;
		endRegion=null;
		middleRegion = null;
		transform = null;
	}
	
	public void create(){
		atlas = new TextureAtlas("pack/SpaceTest.pack");
		transform = new Matrix3();
		createRegions();
		createVertexData();
	}
	
	private void createRegions(){
		startRegion = atlas.findRegion("lasers/default/start");
		startRegion.flip(false, true);
		endRegion = atlas.findRegion("lasers/default/end");
		endRegion.flip(false, true);
		middleRegion = atlas.findRegion("lasers/default/laser");
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
		resetVertexTexturePosition();
		resetVertexColor(Color.WHITE.toFloatBits());
	}
	
	private void resetVertexPosition(){
		//Set laser start
		setRegionVertexPosition(vertexData,0,0,0, startRegion);
		//Set Middle Position
		for(int i=0; i < middleSegmentCount; i++){
				setRegionVertexPosition(vertexData,i+1,0,startRegion.getRegionHeight()+i*middleRegion.getRegionHeight(), middleRegion);
		}
		//Set laser end
		setRegionVertexPosition(vertexData,middleSegmentCount+1,0,startRegion.getRegionHeight()+middleSegmentCount*middleRegion.getRegionHeight(), endRegion);
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
	
	public void resetVertexTexturePosition(){
		//Set laser start
		setRegionVertexTexturePosition(vertexData,0,startRegion);
		//Set Middle Position
		for(int i=0; i < middleSegmentCount; i++){
			setRegionVertexTexturePosition(vertexData,i+1, middleRegion);
		}
		//Set laser end
		setRegionVertexTexturePosition(vertexData,middleSegmentCount+1,endRegion);		
	}
	
	private void setRegionVertexPosition(float[] vertexData, int region, float x, float y, AtlasRegion textureRegion){
		int i;
		i = 0;
		vertexData[region*vertexLength*4+i * vertexLength + 0] = x;
		vertexData[region*vertexLength*4+i * vertexLength + 1] = y;
		i++;
		vertexData[region*vertexLength*4+i * vertexLength + 0] = x;
		vertexData[region*vertexLength*4+i * vertexLength + 1] = y+textureRegion.getRegionHeight();
		i++;
		vertexData[region*vertexLength*4+i * vertexLength + 0] = x+textureRegion.getRegionWidth();
		vertexData[region*vertexLength*4+i * vertexLength + 1] = y+textureRegion.getRegionHeight();
		i++;
		vertexData[region*vertexLength*4+i * vertexLength + 0] = x+textureRegion.getRegionWidth();
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
	
	public void draw(SpriteBatch sbatch, Vector2 p1, float angle, float color){
		resetVertexPosition();
		transform.idt();
		transform.translate(p1);
		transform.rotate(angle);
		transform.translate(-startRegion.getRegionWidth()/2, -startRegion.getRegionHeight()/2);
		applyTransform();
		resetVertexColor(color);
		
		sbatch.draw(middleRegion.getTexture(), vertexData, 0, vertexData.length);
	}
}
