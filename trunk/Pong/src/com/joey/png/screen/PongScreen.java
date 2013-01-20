package com.joey.png.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.joey.png.PongGame;

public class PongScreen extends BaseScreen {

	SpriteBatch batch = new SpriteBatch();
	
	Pixmap pixmap;
	Texture texture;

	float[][] rawData;
	float max = 0;
	int sizeX = 512;
	int sizeY = 256;
	
	public PongScreen(PongGame game) {
		super(game);
		
		pixmap = new Pixmap(sizeX,sizeY,Format.RGB888);
		rawData = new float[sizeX][sizeY];
		setupFire();
		updateFire();
	}
		
	public void setupFire(){
		float base = 320;
		 max = 1024;
		for(int x = 0; x < sizeX; x++){
				rawData[x][0] = (float)(base+(Math.random()*(max-base)));
		}
	}
	
	public void updateFire(){
		setupFire();
		for(int x = 1; x < sizeX-1; x++){
			for(int y = 1; y < sizeY; y++){
				float a = rawData[x-1][y-1];
				float b = rawData[x][y-1];
				float c = rawData[x+1][y-1];
				rawData[x][y] = (a+b+c)/3-1;
			}
		}
	}
	
	
	public void getColor(float value, Color c){
		c.a = value;
		c.g = value;
		c.b = value;
		c.r = value;
		if(value > 1){
			System.out.println("WHOOP"+value);
		}
	}
	public void updateTexture(){
		Color c = new Color();
		for(int x = 0; x < sizeX; x++){
			for(int y = 0; y < sizeY; y++){
				getColor(rawData[x][y]/max, c);
				pixmap.setColor(c);
				pixmap.drawPixel(x, y);
			}
		}
		texture = new Texture(pixmap);
	}
	
	@Override
	public void render(float delta, GL10 gl) {
		gl.glClearColor( 0, 1, 0, 1 );
		gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		updateFire();
		updateTexture();
		batch.begin();
		batch.draw(texture, 50, 50);
		batch.end();
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		game.setScreen(game.menuScreen);
		return true;
	}
}
