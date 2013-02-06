package com.joey.chain.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.FlickScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.joey.chain.ReactorApp;

public class FrameBufferScreen extends GameScreen {

	SpriteBatch batch;
	BitmapFont font;

	FrameBuffer frmBuff;
	TextureRegion frm;
	OrthographicCamera frmCam;
	Window win = new Window(getSkin());
	
	int frmSizeX = 2048;
	int frmSizeY = 2048;
	FrameBufferActor actor;
	public FrameBufferScreen(ReactorApp game) {
		super(game);
		setClearColor(new Color(1,1,1,1));
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		super.touchDown(x, y, pointer);
		win.width++;
		win.height++;
		win.invalidate();
		win.invalidateHierarchy();
		win.validate();
		
		return true;
	}
	@Override
	public void show() {
		
		// TODO Auto-generated method stub
		super.show();
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		frmBuff = new FrameBuffer(Format.RGBA8888, frmSizeX, frmSizeY, false);
		frmCam= new OrthographicCamera(frmSizeX, frmSizeY);
		frmCam.translate(frmSizeX/2, frmSizeY/2);
		actor = new FrameBufferActor(frmBuff);
		
		actor.setFillParent(true);
		
		Image i = new Image(frmBuff.getColorBufferTexture());


		FlickScrollPane scroll = new FlickScrollPane(i,"");
		

		
		win.row().fill().expandX();
		win.add(scroll).fill().expand().maxSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
		win.row();
		win.pack();

		
		stage.addActor(win);
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

		Gdx.gl.glClearColor(0, 0, 0, 1);
		for(int x = 0; x < 10; x++){
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			shape.begin(ShapeType.FilledRectangle);
			shape.setColor(Color.WHITE);
			shape.filledRect(MathUtils.random(frmSizeX), MathUtils.random(frmSizeY),50, 50);
			shape.end();
		
			shape.begin(ShapeType.FilledCircle);
			shape.setColor(Color.GREEN);
			shape.filledCircle(MathUtils.random(frmSizeX), MathUtils.random(frmSizeY), 20);
			shape.end();
		}
		frmBuff.end();
	}
	
	int pos = 0;
	
	@Override
	public void render(float delta) {
		super.render(delta);		
		drawToTexture();
		drawStage(delta);
	}

	@Override
	public void drawScreen(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawOverlay(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateLogic(float delta) {
		// TODO Auto-generated method stub
		
	}

}

class FrameBufferActor extends Widget{

	FrameBuffer buffer;
	
	Texture bufferTexture;
	ShapeRenderer shape = new ShapeRenderer();
	
	public FrameBufferActor(FrameBuffer buf){
		this.buffer = buf;
		this.bufferTexture = buf.getColorBufferTexture();
		this.width = bufferTexture.getWidth();
		this.height = bufferTexture.getHeight();
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(bufferTexture,0,0,bufferTexture.getWidth(),bufferTexture.getHeight());
		System.out.printf("\n\n%d %d %d %d\n", 0,0,bufferTexture.getWidth(),bufferTexture.getHeight());
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
