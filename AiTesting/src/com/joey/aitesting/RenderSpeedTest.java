package com.joey.aitesting;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderSpeedTest implements ApplicationListener{

	private Mesh mesh;
	 // private Texture texture;

	  private SpriteBatch batch;

	  @Override
	  public void create() {
	    if (mesh == null) {
	      mesh = new Mesh(true, 3, 3, new VertexAttribute(Usage.Position, 3,"a_position"));

	      mesh.setVertices(new float[] { -0.5f, -0.5f, 0, 
	          0.5f, -0.5f, 0, 
	          0, 0.5f, 0 });

	      mesh.setIndices(new short[] { 0, 1, 2 });

	     // texture = new Texture(Gdx.files.internal("fish1.png"));

	      batch = new SpriteBatch();
	    }

	  }

	  @Override
	  public void render() {
	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

	    batch.begin();

	    mesh.render(GL10.GL_TRIANGLES, 0, 3);
	   // batch.draw(texture, 10, 10);

	    batch.end();

	  }

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}