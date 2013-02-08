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

public class ServerScreen extends StageScreen {
	Server server;
	Client client;
	public static int tcpPort = 8080;
	public static int udpPort = 8080;
	ConsoleLogger loger;
	ConsoleViewer view;
	ServerListener serverListen; 
	ClientListener clientListen; 
	TextField serverAddress;
	
	public ServerScreen(ReactorApp game){
		super(game);
		server = new Server();
		client = new Client();
		loger = new ConsoleLogger();
		view = new ConsoleViewer(loger);
		serverListen = new ServerListener(loger);
		clientListen = new ClientListener(loger);
	}

	public void startServer(){
		loger.println("SERVER : Starting Server - Begin");
		server.start();
		try {
			server.bind(tcpPort, udpPort);
			server.addListener(serverListen);
		} catch (IOException e) {
			e.printStackTrace();
			loger.println("SERVER : Starting Server - Error "+e.getLocalizedMessage());
		}
		loger.println("SERVER : Starting Server - Complete");
	}
	
	public void stopServer(){
		loger.println("SERVER : Stopping Server - Begin");
		server.stop();
		try{
		server.removeListener(serverListen);
		}catch(Exception e){
			loger.println("SERVER : Stopping Server - Error :"+e.getLocalizedMessage());	
		}
		
		loger.println("SERVER : Stopping Server - Complete");
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

		TextButton startServer = new TextButton("Start", getSkin());
		startServer.setClickListener(new ClickListener() {
			@Override
			public void click(Actor actor, float x, float y) {
				startServer();
			}
		});
		TextButton stopServer = new TextButton("Stop", getSkin());
		stopServer.setClickListener(new ClickListener() {
			@Override
			public void click(Actor actor, float x, float y) {
				stopServer();
			}
		});
		TextButton listConnections = new TextButton("List All Connections", getSkin());
		listConnections.setClickListener(new ClickListener() {
			@Override
			public void click(Actor actor, float x, float y) {
				listConnections();
			}
		});
		
		int buttonHeight = 50;
	
		Table serverContTable = new Table(getSkin());
		serverContTable.pad(2).defaults().spaceBottom(1);
		serverContTable.add(startServer).fillX();
		serverContTable.row();
		serverContTable.add(stopServer).fillX();
		serverContTable.row();
		serverContTable.add(listConnections).fillX();
		serverContTable.row();
		
		
		Window serverWin = new Window("Server", getSkin());
		
		serverWin.add(serverContTable).fill();
		serverWin.pack();
		
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

		
		
		Window clientWin = new Window("Server", getSkin());
		
		clientWin.add(clientContTable).fill();
		clientWin.pack();
		
		stage.addActor(serverWin);
		stage.addActor(clientWin);
	}

	public void listConnections(){
		for(Connection c : server.getConnections()){
			loger.println("SERVER: Connected : "+c.getRemoteAddressTCP());
		}
	}
}

class ServerListener extends Listener{
	ConsoleLogger log;
	
	public ServerListener(ConsoleLogger loger) {
		this.log = loger;
	}
	
	@Override
	public void disconnected(Connection connection) {
		// TODO Auto-generated method stub
		super.disconnected(connection);
		log.println("SERVER: Disconnect from : "+connection.getRemoteAddressTCP());
	}
	
	@Override
	public void connected(Connection connection) {
		// TODO Auto-generated method stub
		super.connected(connection);
		log.println("SERVER: Connection from : "+connection.getRemoteAddressTCP());
	}
	
	@Override
	public void received(Connection connection, Object object) {
		// TODO Auto-generated method stub
		super.received(connection, object);
		log.println("SERVER: Recieved from : "+connection.getRemoteAddressTCP()+" : "+object);
	}
	
}
