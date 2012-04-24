package com.joey.aitesting;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.joey.aitesting.game.entities.Vehicle;

public class Test implements ApplicationListener{
	 private Mesh squareMesh;
	    private OrthographicCamera camera;
	    private Texture texture;
	    private SpriteBatch spriteBatch;

	    @Override
	    public void create() {
	        if (squareMesh == null) {
	            squareMesh = new Mesh(true, 4, 4, 
	                    new VertexAttribute(Usage.Position, 3, "a_position"),
	                    new VertexAttribute(Usage.ColorPacked, 4, "a_color"));

	            squareMesh.setVertices(new float[] {
	                    -0.5f, -0.5f, 0, Color.toFloatBits(128, 0, 0, 255),
	                    0.5f, -0.5f, 0, Color.toFloatBits(192, 0, 0, 255),
	                    -0.5f, 0.5f, 0, Color.toFloatBits(192, 0, 0, 255),
	                    0.5f, 0.5f, 0, Color.toFloatBits(255, 0, 0, 255) });   
	            squareMesh.setIndices(new short[] { 0, 1, 2, 3});
	        }

	        texture = new Texture(Gdx.files.internal("fish1.png"));
	        spriteBatch = new SpriteBatch();
	    }

	    @Override
	    public void dispose() { }

	    @Override
	    public void pause() { }

	    @Override
	    public void render() {
	        camera.update();
	        camera.apply(Gdx.gl10);
	        spriteBatch.setProjectionMatrix(camera.combined);

	        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	        squareMesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);

	        spriteBatch.begin();
	        spriteBatch.draw(texture, 0, 0, 1, 1, 0, 0,
	                texture.getWidth(), texture.getHeight(), false, false);
	        spriteBatch.end();
	    }

	    @Override
	    public void resize(int width, int height) {
	        float aspectRatio = (float) width / (float) height;
	        camera = new OrthographicCamera(2f * aspectRatio, 2f);
	    }

	    @Override
	    public void resume() { }
	
}
