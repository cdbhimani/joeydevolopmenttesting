package com.emptyPockets.test.holepuncher;

import java.util.ArrayList;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.emptyPockets.gui.ScreenSizeHelper;
import com.emptyPockets.gui.StageScreen;
import com.emptyPockets.logging.ConsoleScreen;
import com.emptyPockets.network.server.NetworkConnection;
import com.emptyPockets.network.server.NetworkNode;
import com.emptyPockets.network.server.NetworkNodeListenerAdapter;
import com.emptyPockets.network.log.LogListener;
import com.emptyPockets.network.log.ServerLogger;
import com.emptyPockets.network.log.ServerLogger.Logger;

public class HolePuncher extends StageScreen {

	NetworkNode node;

	TextField command;
	TextButton remoteConnect;
	TextButton sendButton;
	ConsoleScreen console;

	int currentCommand = 0;
	int commandHistory = 10;
	ArrayList<String> commands = new ArrayList<String>(commandHistory);
	
	LogListener log = new LogListener() {
		@Override
		public void print(String message) {
			console.println(message);
		}
	};

	public void pause() {
		node.stop();
	};

	public void hide() {
		node.stop();
	};

	public HolePuncher(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);

		node = new NetworkNode(8080, 10 * 1024);
		console = new ConsoleScreen("", getSkin());
		updateLogger();
		
		NetworkNodeListenerAdapter echo = new NetworkNodeListenerAdapter() {
			@Override
			public void dataRecieved(NetworkNode node, NetworkConnection connection, Object data) {
				super.dataRecieved(node, connection, data);
				ServerLogger.info(node.getNodeName(), "Recieved from [" + connection.getNodeName() + "] - "+data);
			}
		};
		node.addListener(echo);
	}

	public void updateLogger() {
		ServerLogger.remove(log);
		ServerLogger.add(log);
	}

	@Override
	public void createStage(Stage stage) {
		command = new TextField("\\start 8080,", getSkin());
		sendButton = new TextButton("Send", getSkin());
		remoteConnect = new TextButton("Con", getSkin());

		float size = ScreenSizeHelper.getcmtoPxlX(1);
		Table table = new Table(getSkin());
		table.row();
		table.add(remoteConnect).height(size).width(size);
		table.add(command).expandX().fillX().height(size);
		table.add(sendButton).height(size).width(size);
		table.row();
		table.add(console.getContent()).colspan(3).expand().fill();
		table.setFillParent(true);

		stage.addActor(table);

		remoteConnect.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				command.setText("\\connect 54.217.240.178:8080");
			}
		});
		command.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event instanceof InputEvent){
					InputEvent e = (InputEvent)event;
					if(e.getType() == Type.keyUp){
						int c= e.getKeyCode();
						if(c == 66){
							sendCommand();
						}
					}else if(e.getType() == Type.keyDown){
						int c= e.getKeyCode();
						if(c == 19){
							previousCommand();
						}else if(c == 20){
							nextCommand();
						}
					}
				}
				return false;
			}
		});
		sendButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				ServerLogger.info("");
				ServerLogger.info(command.getText());
				ServerLogger.info("");
				sendCommand();
			}
		});
	}
	
	public void previousCommand(){
		currentCommand++;
		if(currentCommand > commands.size()-1){
			currentCommand = commands.size()-1;
		}
		command.setText(commands.get(currentCommand));
		command.setCursorPosition(command.getText().length());
	}

	public void nextCommand(){
		currentCommand--;
		if(currentCommand < 0){
			currentCommand = 0;
		}
		command.setText(commands.get(currentCommand));
		command.setCursorPosition(command.getText().length());
	}
	public void sendCommand(){
		currentCommand = -1;
		String cmd = command.getText();
		commands.add(0, cmd);
		
		if(commands.size() >= commandHistory){
			commands.remove(commands.size()-1);
		}
		
		node.getCommandHub().processCommand(command.getText());
		command.setText("\\");
		command.setCursorPosition(1);
	}

	@Override
	public void drawBackground(float delta) {

	}

	@Override
	public void drawScreen(float delta) {

	}

	@Override
	public void drawOverlay(float delta) {

	}

}
