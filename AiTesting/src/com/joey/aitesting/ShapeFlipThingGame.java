package com.joey.aitesting;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.joey.aitesting.game.graphics.ConsoleLogger;
import com.joey.aitesting.game.graphics.ConsoleViewer;
import com.me.mygdxgame.Gestures.OrthoCamController;

public class ShapeFlipThingGame  implements ApplicationListener{
	int sizeX = 20;
	int sizeY = 20;
	int colorNum = 5;
	
	float gridSizeX = 10;
	float gridSizeY = 10;
	
	Color color[];
	byte[][] world;
	OrthoCamController cameraControler;
	OrthographicCamera camera;
	OrthographicCamera logCamera;
	ShapeRenderer shape;
	
	ConsoleLogger log;
	ConsoleViewer logView;
	
	@Override
	public void create() {
		
		log = new ConsoleLogger();
		logView = new ConsoleViewer(log);
		logCamera = new OrthographicCamera();
		
		logView.setTextColor(Color.BLACK);
		
		camera = new OrthographicCamera();
		cameraControler = new OrthoCamController(camera);
		shape = new ShapeRenderer();
		
		Gdx.input.setInputProcessor(cameraControler);
		
		world = new byte[sizeX][sizeY];
		createColors(colorNum);
		randomWorld(world, colorNum);
	}
	
	public void randomWorld(byte[][] world, int colorNum){
		for(int x = 0; x < world.length; x++){
			for(int y = 0; y < world[x].length; y++){
				world[x][y] = (byte)(Math.random()*colorNum);
			}
		}
	}
	public void createColors(int colorNum){
		color = new Color[colorNum];
		
		int count = 0;
		color[count++]=Color.WHITE;if(count>=colorNum)return;
		color[count++]=Color.RED;if(count>=colorNum)return;
		color[count++]=Color.GREEN;if(count>=colorNum)return;
		color[count++]=Color.BLUE;if(count>=colorNum)return;
		color[count++]=Color.ORANGE;if(count>=colorNum)return;
		color[count++]=Color.CYAN;if(count>=colorNum)return;
		color[count++]=Color.MAGENTA;if(count>=colorNum)return;
		color[count++]=Color.PINK;if(count>=colorNum)return;
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		
		logCamera.viewportWidth = width;
		logCamera.viewportHeight = height;
		logCamera.position.x = width/2;
		logCamera.position.y = height/2;
	}

	@Override
	public void render() {
		log.setMaxConsoleLines(1);
		log.println("FPS:"+Gdx.graphics.getFramesPerSecond());
		
		GL10 gl = Gdx.gl10;
		gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		camera.apply(gl);
		shape.setProjectionMatrix(camera.combined);
		for(int x = 0; x < world.length; x++){
			for(int y = 0; y < world[x].length; y++){
		
				shape.setColor(color[world[x][y]]);
				shape.begin(ShapeType.FilledRectangle);
				shape.filledRect(x*gridSizeX, y*gridSizeY, gridSizeX, gridSizeY);
				shape.end();
			}
		}
		

		
//		for(int x = 0; x < world.length; x++){
//			for(int y = 0; y < world[x].length; y++){
//				if(world[x][y] != 0){
//					shape.setColor(Color.BLACK);
//					shape.begin(ShapeType.Rectangle);
//					shape.rect(x*gridSizeX, y*gridSizeY, gridSizeX, gridSizeY);
//					shape.end();
//				}
//			}
//		}
		
		logCamera.update();
		logCamera.apply(gl);
		logView.draw(logCamera);
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
