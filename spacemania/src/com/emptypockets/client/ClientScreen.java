package com.emptypockets.client;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.emptyPockets.graphics.GraphicsToolkit;
import com.emptyPockets.gui.ScreenSizeHelper;
import com.emptyPockets.gui.StageScreen;
import com.emptyPockets.logging.ConsoleScreen;
import com.emptyPockets.utils.OrthoCamController;
import com.emptypockets.networking.client.ClientManager;
import com.emptypockets.networking.transfer.ClientStateTransferObject;

public class ClientScreen extends StageScreen implements Runnable {
	ClientStateTransferObject state;
	ConsoleScreen console;
	Touchpad touchPad;
	ClientManager client;
	ShapeRenderer shape;
	OrthoCamController control;

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
		state.velX = touchPad.getKnobPercentX();
		state.velY = touchPad.getKnobPercentY();
	}

	@Override
	public void createStage(Stage stage) {
		int insetSize = ScreenSizeHelper.getcmtoPxlX(0.5f);
		int touchPadSize = ScreenSizeHelper.getcmtoPxlX(2);

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
		inset.add(client.getCommand().getPanel()).fillX().expandX().colspan(3);

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
	}

	@Override
	public void initializeRender() {
		super.initializeRender();
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
					state.velX = touchPad.getKnobPercentX()*vel;
					state.velY = touchPad.getKnobPercentY()*vel;
					client.send(state);
				}
				Thread.sleep(100);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
}
