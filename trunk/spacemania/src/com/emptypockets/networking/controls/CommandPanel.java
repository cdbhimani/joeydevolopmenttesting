package com.emptypockets.networking.controls;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptyPockets.graphics.GraphicsToolkit;
import com.emptyPockets.gui.Scene2DToolkit;
import com.emptyPockets.gui.ScreenSizeHelper;
import com.emptypockets.client.ClientScreen;
import com.emptypockets.networking.log.ServerLogger;

public class CommandPanel extends Table {
	Skin skin;
	TextField command;
	TextButton remoteConnect;
	TextButton sendButton;
	
	CommandHub commandHub;


	int currentCommand = 0;
	int commandHistory = 10;
	ArrayList<String> commands = new ArrayList<String>(commandHistory);
	
	
	public CommandPanel(CommandHub commandHub) {
		super(Scene2DToolkit.getToolkit().getSkin());
		this.commandHub = commandHub;
		createPanel();
	}

	public void createPanel() {
		command = new TextField("\\start 8080,", getSkin());
		sendButton = new TextButton("Send", getSkin());
		remoteConnect = new TextButton("Con", getSkin());

		float size = ScreenSizeHelper.getcmtoPxlX(1);
		row();
		add(remoteConnect).height(size).width(size);
		add(command).expandX().fillX().height(size);
		add(sendButton).height(size).width(size);
		
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
		
		commandHub.processCommand(command.getText());
		command.setText("\\");
		command.setCursorPosition(1);
	}
	public Skin getSkin() {
		return Scene2DToolkit.getToolkit().getSkin();
	}
}
