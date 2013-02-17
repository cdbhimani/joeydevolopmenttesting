package com.joey.chain.gui.testing;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.joey.chain.ReactorApp;
import com.joey.chain.gui.GameScreen;

public class FrameBufferScreen extends GameScreen {

	SpriteBatch batch;
	BitmapFont font;

	FrameBuffer frmBuff;
	TextureRegion frm;
	OrthographicCamera frmCam;
	Window win = new Window("",getSkin());
	Stage stage;
	int frmSizeX = 2048;
	int frmSizeY = 2048;
	FrameBufferActor actor;
	public FrameBufferScreen(ReactorApp game) {
		super(game);
		setClearColor(new Color(1,1,1,1));
	}
	
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		super.touchDown(x, y, pointer, button);
		win.setWidth(win.getWidth()+1);
		win.setHeight(win.getHeight()+1);
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
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), false);
		
		frmBuff = new FrameBuffer(Format.RGBA8888, frmSizeX, frmSizeY, false);
		frmCam= new OrthographicCamera(frmSizeX, frmSizeY);
		frmCam.translate(frmSizeX/2, frmSizeY/2);

		actor = new FrameBufferActor(frmBuff);
		
		actor.setFillParent(true);
		
		Image i = new Image(frmBuff.getColorBufferTexture());


		ScrollPane scroll = new ScrollPane(i,getSkin());
		

		
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

	@Override
	public void drawScreen(float delta) {
		drawToTexture();
	}

	@Override
	public void drawOverlay(float delta) {
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void updateLogic(float delta) {
	}

}

class FrameBufferActor extends Widget{

	FrameBuffer buffer;
	
	Texture bufferTexture;
	ShapeRenderer shape = new ShapeRenderer();
	
	public FrameBufferActor(FrameBuffer buf){
		this.buffer = buf;
		this.bufferTexture = buf.getColorBufferTexture();
		this.setWidth(bufferTexture.getWidth());
		this.setHeight(bufferTexture.getHeight());
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(bufferTexture,0,0,bufferTexture.getWidth(),bufferTexture.getHeight());
		System.out.printf("\n\n%d %d %d %d\n", 0,0,bufferTexture.getWidth(),bufferTexture.getHeight());
	}

}
