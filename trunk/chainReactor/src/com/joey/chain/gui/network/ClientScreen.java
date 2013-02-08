package com.joey.chain.gui.network;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.joey.chain.ReactorApp;
import com.joey.chain.common.ConsoleLogger;
import com.joey.chain.common.ConsoleViewer;
import com.joey.chain.gui.StageScreen;

public class ClientScreen extends StageScreen {
	Client client;
	public static int tcpPort = 8080;
	public static int udpPort = 8080;
	ConsoleLogger loger;
	ConsoleViewer view;
	ClientListener clientListen; 
	TextField serverAddress;
	
	public ClientScreen(ReactorApp game){
		super(game);
		client = new Client();
		loger = new ConsoleLogger();
		view = new ConsoleViewer(loger);
		clientListen = new ClientListener(loger);
	}

	public void startClient(){
		loger.println("CLIENT : Starting Client - Begin");
		client.start();
		try {
			client.connect(5000, serverAddress.getText(), tcpPort, udpPort);
		} catch (IOException e) {
			e.printStackTrace();
			loger.println("CLIENT : Starting Client - Error "+e.getLocalizedMessage());
		}
		loger.println("CLIENT : tarting Client - Complete");
	}
	
	public void stopClient(){
		loger.println("CLIENT : Stopping Client - Begin");
		client.stop();
		try{
			client.removeListener(clientListen);
		}catch(Exception e){
			loger.println("CLIENT : Stopping Client - Error :"+e.getLocalizedMessage());	
		}
		
		loger.println("CLIENT : Stopping Client - Complete");
	}
	
	@Override
	public void drawScreen(float delta) {
		view.draw(cam);
	}

	@Override
	public void drawOverlay(float delta) {
	}

	@Override
	public void updateLogic(float delta) {
	}

	@Override
	public void createStage(Stage stage) {
		serverAddress = new TextField(getSkin());
		
		TextButton startClient = new TextButton("Connect", getSkin());
		startClient.setClickListener(new ClickListener() {
			@Override
			public void click(Actor actor, float x, float y) {
				startClient();
			}
		});
		TextButton stopClient = new TextButton("Disconnect", getSkin());
		stopClient.setClickListener(new ClickListener() {
			@Override
			public void click(Actor actor, float x, float y) {
				stopClient();
			}
		});
		
		Table clientContTable = new Table(getSkin());
		clientContTable.pad(2).defaults().spaceBottom(1);
		clientContTable.row();
		clientContTable.add(serverAddress).fillX();
		clientContTable.row();
		clientContTable.add(startClient).fillX();
		clientContTable.row();
		clientContTable.add(stopClient).fillX();
		clientContTable.row();

		
		
		Window client = new Window("Client", getSkin());
		
		client.add(clientContTable).fill();
		client.pack();
		
		stage.addActor(client);
	}
}

class ClientListener extends Listener{
	ConsoleLogger log;
	
	public ClientListener(ConsoleLogger loger) {
		this.log = loger;
	}
	
	@Override
	public void disconnected(Connection connection) {
		// TODO Auto-generated method stub
		super.disconnected(connection);
		log.println("CLIENT : Disconnect from : "+connection.getRemoteAddressTCP());
	}
	
	@Override
	public void connected(Connection connection) {
		// TODO Auto-generated method stub
		super.connected(connection);
		log.println("CLIENT :Connection to : "+connection.getRemoteAddressTCP());
	}
	
	@Override
	public void received(Connection connection, Object object) {
		// TODO Auto-generated method stub
		super.received(connection, object);
		log.println("CLIENT :Recieved from : "+connection.getRemoteAddressTCP()+" : "+object);
	}
	
}
