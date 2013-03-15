package com.emptyPockets.test.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.emptyPockets.graphics.GraphicsToolkit;
import com.emptyPockets.gui.GameScreen;
import com.emptyPockets.utils.OrthoCamController;

public class ShaderLesson1 extends GameScreen{
	ShaderProgram shader;
	Mesh mesh;
	Texture texture;
	String vertFile;
	String fragFile;
	OrthoCamController control;
	ShapeRenderer background;
	
	public ShaderLesson1(InputMultiplexer inputProcessor) {
		super(inputProcessor);
		vertFile = "shader/shadertut/lesson1.vert";
		fragFile = "shader/shadertut/lesson1.frag";
		control = new OrthoCamController(getScreenCamera());
		setClearColor(Color.BLACK);
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
		shader = new ShaderProgram(Gdx.files.internal(vertFile), Gdx.files.internal(fragFile));
		float sizeX = 100;
		float sizeY = 100;
		mesh = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.  ColorUnpacked(), VertexAttribute.TexCoords(0));
		mesh.setVertices(new float[] 
		{	-sizeX, -sizeY, 0, 1, 1, 1, 1, 0, 1,
			sizeX, -sizeY, 0, 1, 1, 1, 1, 1, 1,
			sizeX, sizeY, 0, 1, 1, 1, 1, 1, 0,
			-sizeX, sizeY, 0, 1, 1, 1, 1, 0, 0});
		mesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		background =  new ShapeRenderer();
	}
	
	@Override
	public void hide() {
		super.hide();
		mesh.dispose();
		shader.dispose();
		texture.dispose();
		background.dispose();
		
		background = null;
		mesh = null;
		shader = null;
		texture = null;
	}
	
	@Override
	public void drawBackground(float delta) {
		background.setProjectionMatrix(getScreenCamera().combined);
		GraphicsToolkit.draw2DAxis(background, getScreenCamera(), 1, Color.WHITE);
	}

	@Override
	public void drawScreen(float delta) {
		texture.bind();
		shader.begin();
		shader.setUniformMatrix("u_worldView", getScreenCamera().combined);
		shader.setUniformi("u_texture", 0);
		mesh.render(shader, GL10.GL_TRIANGLES);
		shader.end();
	}

	@Override
	public void drawOverlay(float delta) {
		// TODO Auto-generated method stub
		
	}

}
