package com.emptypockets.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.emptyPockets.graphics.GraphicsToolkit;
import com.emptyPockets.gui.ScreenSizeHelper;
import com.emptyPockets.gui.StageScreen;
import com.emptyPockets.logging.Console;
import com.emptyPockets.logging.ConsoleScreen;
import com.emptyPockets.utils.OrthoCamController;
import com.emptypockets.engine.MovingEntity;
import com.emptypockets.networking.client.ClientManager;
import com.emptypockets.networking.server.ServerManager;
import com.emptypockets.networking.transfer.ClientStateTransferObject;

public class ClientScreen extends StageScreen implements Runnable {
	final int insetSize = ScreenSizeHelper.getcmtoPxlX(0.7f);
	int touchPadSize = ScreenSizeHelper.getcmtoPxlX(2);
	
	ClientStateTransferObject state;
	ConsoleScreen console;
	Touchpad touchPad;
	ClientManager client;

	ShapeRenderer shape;
	OrthoCamController control;

	TextButton showConsole;
	Thread thread;
	boolean alive;

	public ClientScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		client = new ClientManager();
		console = new ConsoleScreen("Console", getSkin());
		setClearColor(Color.BLACK);
		control = new OrthoCamController(getScreenCamera());
		thread = new Thread(this);
		state = new ClientStateTransferObject();
		alive = true;
		thread.start();
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
		shape = new ShapeRenderer();
	}

	@Override
	public void hide() {
		super.hide();
		shape.dispose();
		shape = null;
	}

	public void updateState(ClientStateTransferObject state) {
		state.valueX = touchPad.getKnobPercentX();
		state.valueY = touchPad.getKnobPercentY();
	}

	@Override
	public void createStage(Stage stage) {
		showConsole = new TextButton("C", getSkin());
		touchPad = new Touchpad(0, getSkin());
		
		Pixmap pix = new Pixmap(4, 4, Format.RGBA8888);
		Color c = new Color(Color.LIGHT_GRAY);
		c.a = 0.2f;
		pix.setColor(c);
		pix.fill();
		pix.setColor(Color.LIGHT_GRAY);
		pix.drawRectangle(0, 0, 4, 4);

		NinePatch ninePatch = new NinePatch(new Texture(pix), 1, 1, 1, 1);
		Drawable draw = new NinePatchDrawable(ninePatch);
		touchPad.getStyle().background = draw;

		Table layout = new Table();
		// top
		layout.row();
		layout.add();
		layout.add().fillX().expandX();
		layout.add();
		// middle
		layout.row();
		layout.add().fillY().expandY();
		layout.add().fill().expand();
		layout.add().fillY().expandY();
		// bottom
		layout.row();
		layout.add(touchPad).width(touchPadSize).size(touchPadSize);
		layout.add().fillX().expandX();
		layout.add();
		layout.setFillParent(true);

		Table inset = new Table();
		inset.row();
		inset.add();
		inset.add().height(insetSize).expandX().fillX();
		inset.add(showConsole).fill().width(insetSize).height(insetSize);
		
		inset.row();
		inset.add().width(insetSize).expandY().fillY();
		inset.add(layout).fill().expand();
		inset.add().width(insetSize).expandY().fillY();

		inset.row();
		inset.add();
		inset.add().height(insetSize).expandX().fillX();
		inset.add();
		
		inset.setFillParent(true);
		stage.addActor(inset);
		stage.addActor(client.getCommand().getPanel());
		client.getCommand().getPanel().setVisible(false);
		
		showConsole.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				client.getCommand().getPanel().setVisible(!client.getCommand().getPanel().isVisible());
				updateCommandScreenSize();
			}
		});
	}

	public void updateCommandScreenSize(){
		client.getCommand().getPanel().setPosition(0, 0);
		client.getCommand().getPanel().setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()-insetSize);
		client.getCommand().getPanel().invalidate();
	}
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		updateCommandScreenSize();
	}
	@Override
	public void initializeRender() {
		super.initializeRender();
		MovingEntity me = client.getEntity(client.getUsername());
		if(me != null){
//			float insetX =getScreenCamera().viewportWidth/10;
//			float insetY = getScreenCamera().viewportHeight/10;
//			
//			
//			float minX = getScreenCamera().position.x-getScreenCamera().viewportWidth/2+insetX;
//			float maxX = getScreenCamera().position.x+getScreenCamera().viewportWidth/2-insetX;
//			float diffX = 0;
//			if(me.posX() < minX){
//				diffX = me.posX()-minX;
//			}else if(me.posX() > maxX){
//				diffX = me.posX()-maxX;
//			}
//			
//			float diffY = 0;
//			float minY = getScreenCamera().position.y-getScreenCamera().viewportHeight/2+insetY;
//			float mayY = getScreenCamera().position.y+getScreenCamera().viewportHeight/2-insetY;
//			if(me.posY() < minY){
//				diffY = me.posY()-minY;
//			}else if(me.posY() > mayY){
//				diffY = me.posY()-mayY;
//			}
//			
//			getScreenCamera().position.x += diffX;
//			getScreenCamera().position.y += diffY;
			
			getScreenCamera().position.x = me.posX();
			getScreenCamera().position.y = me.posY();
			
		}
		shape.setProjectionMatrix(getScreenCamera().combined);
	}

	@Override
	public void drawBackground(float delta) {
		GraphicsToolkit.draw2DAxis(shape, getScreenCamera(), 100, Color.WHITE);
	}

	@Override
	public void drawScreen(float delta) {
		client.render(shape);
	}

	@Override
	public void drawOverlay(float delta) {
	}

	@Override
	public void updateLogic(float delta) {
		super.updateLogic(delta);
		client.update(delta);
	}

	public ConsoleScreen getConsole() {
		return console;
	}

	public void setConsole(ConsoleScreen console) {
		this.console = console;
	}

	@Override
	public void run() {
		while (alive) {
			try {
				if (touchPad != null) {
					float vel = 50;
					state.valueX = touchPad.getKnobPercentX() * vel;
					state.valueY = touchPad.getKnobPercentY() * vel;
					client.send(state);
				}
				Thread.sleep(100);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
}
