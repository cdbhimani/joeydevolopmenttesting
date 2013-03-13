package com.emptyPockets.test;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.emptyPockets.box2d.gui.Box2DScreen;
import com.emptyPockets.utils.OrthoCamController;

public class ErrorTesting extends Box2DScreen{
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRender;
	OrthoCamController control;
	VertexTool vertex;
	
	public ErrorTesting(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		setClearColor(Color.BLACK);
		setShowDebug(true);
		control = new OrthoCamController(getBox2DWorldCamera());
		vertex = new VertexTool();
	}

	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
		input.addProcessor(control);
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		input.removeProcessor(control);
	}
	@Override
	public void show() {
		super.show();
		spriteBatch = new SpriteBatch();
		shapeRender = new ShapeRenderer();
		vertex.create();
	}

	@Override
	public void hide() {
		super.hide();
		spriteBatch.dispose();
		spriteBatch = null;
		shapeRender.dispose();
		shapeRender = null;
		vertex.dispose();
		vertex = null;
	}
	
	@Override
	public void createWorld(World world) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createStage(Stage stage) {
		// TODO Auto-generated method stub
		
	}

	public void transform(float[] vertix, int vertixCount, int vertixLength, Matrix3 mat){
		float x;
		float y;
		
		for(int i = 0; i < vertixCount; i++){
			x = vertix[i*vertixLength];
			y = vertix[i*vertixLength+1];
			vertix[i*vertixLength] = x * mat.val[0] + y * mat.val[3] + mat.val[6];
			vertix[i*vertixLength+1]= x * mat.val[1] + y * mat.val[4] + mat.val[7];
		}
	}
	
	int angle = 0;
	@Override
	public void drawScreen(float delta) {
		spriteBatch.setProjectionMatrix(getBox2DWorldCamera().combined);
		vertex.draw(spriteBatch, null, null);
		
	}


	/**
	    * Creates a mesh which will draw a repeated texture. To be used whenever we are dealing with a region of a TextureAtlas
	    * @param vertices For re-use, the vertices to use for the mesh. If insufficient are provided, a new array will be constructed
	    * @param region The AtlasRegion to use
	    * @param scale The factor by which we want to repeat our texture
	    * @param x
	    * @param y
	    * @param originX
	    * @param originY
	    * @param width
	    * @param height
	    * @param scaleX Scale by which we want to expand the mesh on X
	    * @param scaleY Scale by which we want to expand the mesh on Y
	    * @param rotation Degrees of rotation for mesh
	    * @param colorBase The initial base color
	    * @param alpha The alpha by which to mult the colorBase by; resulting in the end interpolation target.
	    * @return
	    */
	    private void constructMesh(
	    		float[] vertex, 
	    		AtlasRegion region, 
	    		int scale, float x, float y, 
	    		float originX, float originY, 
	    		float width, float height,
	    		float scaleX, float scaleY, 
	    		float rotation, 
	    		Color colorT, 
	    		float alpha) {
	 
	        if(scale*20 > vertex.length){
	        	vertex = new float[20*scale];
	        }
	 
	        float color = colorT.toFloatBits();
	        float colorE;
	 
	        int idx = 0;
	 
	        // bottom left and top right corner points relative to origin
	        final float worldOriginX = x + originX;
	        final float worldOriginY = y + originY;
	        float fx = -originX;
	        float fy = -originY;
	        float fx2 = width - originX;
	        float fy2 = height - originY;
	 
	        // scale
	        if (scaleX != 1 || scaleY != 1) {
	            fx *= scaleX;
	            fy *= scaleY;
	            fx2 *= scaleX;
	            fy2 *= scaleY;
	        }
	 
	        // construct corner points, start from top left and go counter clockwise
	        final float p1x = fx;
	        final float p1y = fy;
	        final float p2x = fx;
	        final float p2y = fy2;
	        final float p3x = fx2;
	        final float p3y = fy2;
	        final float p4x = fx2;
	        final float p4y = fy;
	 
	        float Fx1;
	        float Fy1;
	        float Fx2;
	        float Fy2;
	        float Fx3;
	        float Fy3;
	        float Fx4;
	        float Fy4;
	 
	        // rotate
	        if (rotation != 0) {
	            final float cos = MathUtils.cosDeg(rotation);
	            final float sin = MathUtils.sinDeg(rotation);
	 
	            Fx1 = cos * p1x - sin * p1y;
	            Fy1 = sin * p1x + cos * p1y;
	 
	            Fx2 = cos * p2x - sin * p2y;
	            Fy2 = sin * p2x + cos * p2y;
	 
	            Fx3 = cos * p3x - sin * p3y;
	            Fy3 = sin * p3x + cos * p3y;
	 
	            Fx4 = Fx1 + (Fx3 - Fx2);
	            Fy4 = Fy3 - (Fy2 - Fy1);
	        } else {
	            Fx1 = p1x;
	            Fy1 = p1y;
	 
	            Fx2 = p2x;
	            Fy2 = p2y;
	 
	            Fx3 = p3x;
	            Fy3 = p3y;
	 
	            Fx4 = p4x;
	            Fy4 = p4y;
	        }
	 
	        float x1 = Fx1 + worldOriginX;
	        float y1 = Fy1 + worldOriginY;
	        float x2 = Fx2 + worldOriginX;
	        float y2 = Fy2 + worldOriginY;
	 
	        float scaleX2 = ((Fx2-Fx1) / scale);
	        float scaleY2 = ((Fy2-Fy1) / scale);
	        float scaleX3 = ((Fx3-Fx4) / scale);
	        float scaleY3 = ((Fy3-Fy4) / scale);
	        float scaleAlpha = (colorT.a - (colorT.a*alpha)) / scale;                       
	 
	        float x3 = x1;
	        float y3 = y1;
	        float x4 = x2;
	        float y4 = y2;
	 
	        final float u1 = region.getU();
	        final float v1 = region.getV();
	        final float u2 = region.getU2();
	        final float v2 = region.getV2();
	 
	        int i;
	        int vertexLength = 5;
	        for(int j = 0; j < scale; j++){
		        i=0;
				vertex[(i+j*vertexLength)*vertexLength+0]=x1;
				vertex[(i+j*vertexLength)*vertexLength+1]=y1;
				vertex[(i+j*vertexLength)*vertexLength+2]=color;
				vertex[(i+j*vertexLength)*vertexLength+3]=u1;
				vertex[(i+j*vertexLength)*vertexLength+4]=v1;
	
				i++;
				vertex[(i+j*vertexLength)*vertexLength+0]=x2;
				vertex[(i+j*vertexLength)*vertexLength+1]=y2;
				vertex[(i+j*vertexLength)*vertexLength+2]=color;
				vertex[(i+j*vertexLength)*vertexLength+3]=u1;
				vertex[(i+j*vertexLength)*vertexLength+4]=v2;
				
				i++;
				vertex[(i+j*vertexLength)*vertexLength+0]=x3;
				vertex[(i+j*vertexLength)*vertexLength+1]=y3;
				vertex[(i+j*vertexLength)*vertexLength+2]=color;
				vertex[(i+j*vertexLength)*vertexLength+3]=u2;
				vertex[(i+j*vertexLength)*vertexLength+4]=v2;
	
				i++;
				vertex[(i+j*vertexLength)*vertexLength+0]=x4;
				vertex[(i+j*vertexLength)*vertexLength+1]=y4;
				vertex[(i+j*vertexLength)*vertexLength+2]=color;
				vertex[(i+j*vertexLength)*vertexLength+3]=u2;
				vertex[(i+j*vertexLength)*vertexLength+4]=v1;
	        }
	    }
}
