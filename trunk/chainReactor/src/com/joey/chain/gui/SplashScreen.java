package com.joey.chain.gui;

import java.text.Normalizer.Form;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.joey.chain.ReactorApp;

public class SplashScreen extends GameScreen {

	SpriteBatch batch;
	BitmapFont font;

	FrameBuffer frmBuff;
	TextureRegion frm;
	OrthographicCamera frmCam;
	
	int frmSizeX = 256;
	int frmSizeY = 256;
	
	public SplashScreen(ReactorApp game) {
		super(game);
		setClearColor(new Color(1,1,1,1));
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		frmBuff = new FrameBuffer(Format.RGBA8888, frmSizeX, frmSizeY, false);
		frm = new TextureRegion(frmBuff.getColorBufferTexture());
		frmCam= new OrthographicCamera(frmSizeX, frmSizeY);
		frmCam.translate(frmSizeX/2, frmSizeY/2);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}
	

	public void drawToTexture(){
		frmCam.update();
		ShapeRenderer shape = new ShapeRenderer();
		shape.setProjectionMatrix(frmCam.combined);
		frmBuff.begin();
	
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		shape.begin(ShapeType.FilledRectangle);
		shape.setColor(Color.BLACK);
		shape.filledRect(0,0,frmSizeX/2, frmSizeY/2);
		shape.end();
	
		shape.begin(ShapeType.FilledCircle);
		shape.setColor(Color.GREEN);
		shape.filledCircle(MathUtils.random(frmSizeX), MathUtils.random(frmSizeY), 20);
		shape.end();
		
		frmBuff.end();
	}
	
	int pos = 0;
	
	@Override
	public void render(float delta) {
		super.render(delta);
		drawToTexture();

		batch.setProjectionMatrix(cam.combined);
		
		batch.begin();
		batch.draw(frm, pos++, 30,frmSizeX,frmSizeY);
		if(pos>Gdx.graphics.getWidth()-frmSizeX)pos=0;
		font.setColor(Color.RED);
		StringBuilder message = new StringBuilder();
		message.append("Time :"+(int)(delta*1000)+"\n");
		font.drawMultiLine(batch, message.toString(), 10, Gdx.graphics.getHeight());
		batch.end();
	}

}
