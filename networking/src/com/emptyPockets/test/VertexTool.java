package com.emptyPockets.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class VertexTool implements Disposable{
	int vertexLength = 5;
	int middleSegmentCount = 10;
	
	float[] startVert;
	float[] endVert;
	float[] middleVert;
	
	TextureAtlas atlas;
	AtlasRegion startRegion;
	AtlasRegion  middleRegion;
	AtlasRegion  endRegion;
	
	Matrix3 rotation;
	Matrix3 translation;
	Matrix3 scale;
	
	Matrix3 result;
	
	public void dispose(){
		startVert = null;
		endVert = null;
		middleVert = null;
		atlas.dispose();
		startRegion = null;
		endRegion=null;
		middleRegion = null;
	}
	
	public void create(){
		atlas = new TextureAtlas("pack/SpaceTest.pack");
		createRegions();
		createVertexData();
	}
	
	private void createRegions(){
		startRegion = atlas.findRegion("lasers/default/start");
		endRegion = atlas.findRegion("lasers/default/end");
		middleRegion = atlas.findRegion("lasers/default/laser");
	}
	
	private void createVertexData(){
		startVert = createVertex(startRegion, 1, 1, Color.RED);
//		middleVert = createVertex(middleRegion, 1, 1, Color.RED);
//		endVert = createVertex(endRegion, 1, 1, Color.RED);
		middleVert = startVert;
		endVert = startVert;
	}
	
	private float[] createVertex(AtlasRegion region, int repeatCountX,int repeatCountY, Color c){
		float[] vertexData = new float[vertexLength*4*repeatCountX*repeatCountY];
		resetVertex(vertexData, region, repeatCountX, repeatCountY, c);
		return vertexData;
	}
	
	private void resetVertex(float[] vertexData,AtlasRegion region, int repeatCountX,int repeatCountY, Color c){
		float color = c.toFloatBits();
		int vertexPos = 0;
		for(int x=0; x < repeatCountX; x++){
			for(int y=0; y < repeatCountY; y++){
				vertexPos = y+x*repeatCountX;
				setRegionVertexColor(vertexData,vertexPos,color);
				setRegionVertexPosition(vertexData,vertexPos,vertexLength,x*region.getRegionWidth(),y*region.getRegionHeight(), region);
				setRegionVertexTexturePosition(vertexData,vertexPos,vertexLength, region);
			}
		}
	}

	private void setRegionVertexPosition(float[] vertexData, int vertexPos, float x, float y, int vertexLength, AtlasRegion region){
		int i;
		i = 0;
		System.out.println(vertexPos*vertexLength*4+i * vertexLength);
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 0] = x;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 1] = y;
		i++;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 0] = x;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 1] = y+region.getRegionHeight();
		i++;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 0] = x+region.getRegionWidth();
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 1] = y+region.getRegionHeight();
		i++;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 0] = x+region.getRegionWidth();
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 1] = y;
	}
	
	private void setRegionVertexTexturePosition(float[] vertexData, int vertexPos, int vertexLength, AtlasRegion region){
		int i = 0;
		i = 0;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 3] = region.getU();
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 4] = region.getV();
		i++;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 3] =region.getU();
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 4] = region.getV2();
		i++;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 3] = region.getU2();
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 4] = region.getV2();
		i++;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 3] = region.getU2();
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 4] = region.getV();
	}
	
	private void setRegionVertexColor(float[] vertexData, int vertexPos,float color) {
		int i = 0;
		i = 0;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 2] = color;
		i++;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 2] = color;
		i++;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 2] = color;
		i++;
		vertexData[vertexPos*vertexLength*4+i * vertexLength + 2] = color;
	}

	public void printVertex(float[] vertex, int vert){
		int pos = vert*vertexLength;
		System.out.printf("P[%f,%f]", vertex[pos],vertex[pos+1]);
	}
	public void draw(SpriteBatch sbatch, Vector2 p1, Vector2 p2){
		System.out.println("HERE");
		resetVertex(startVert, startRegion, 1,1, Color.RED);
//		setRegionVertexPosition(startVert, 0, 0, 0, vertexLength, startRegion);
		printVertex(startVert, 0);
		printVertex(startVert, 1);
		printVertex(startVert, 2);
		printVertex(startVert, 3);
		
		sbatch.begin();
		sbatch.draw(startRegion.getTexture(), startVert, 0, startVert.length);
		sbatch.draw(middleRegion.getTexture(), middleVert, 0, middleVert.length);
		sbatch.draw(endRegion.getTexture(), endVert, 0, endVert.length);
		sbatch.end();
	}
}
