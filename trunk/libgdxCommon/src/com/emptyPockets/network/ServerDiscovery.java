package com.emptyPockets.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.emptyPockets.gui.Scene2DToolkit;
import com.emptyPockets.logging.Console;
import com.esotericsoftware.kryonet.Client;

public class ServerDiscovery {

	public static void findServer(final Stage stage, final Client client, final int tcpPort, final int udpPort, final int discoveryTimeout, final int connectionTimeout){
		Console.println("CLIENT : Searching Server connections");
		List<InetAddress> servers = client.discoverHosts(udpPort, discoveryTimeout);
		Console.println("CLIENT : Found : "+servers.size());
		
		Skin skin = Scene2DToolkit.getToolkit().getSkin();
		final Window win = new Window("Server List", skin);
		win.debug();
		
		Table connectTable = new Table(skin);
		connectTable.row();
		connectTable.debug();
		
		for(final InetAddress address : servers){
			Label label = new Label(address.toString(), skin);
			TextButton connectButton = new TextButton("Connect", skin);
	
			connectTable.add(label);
			connectTable.add(connectButton);
			connectTable.row();
			
			connectButton.addListener(new ChangeListener() {
				
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					try {
						client.start();
						Console.println("CLIENT : Attempting Connection");
						client.connect(connectionTimeout, address, tcpPort, udpPort);
						win.setVisible(false);
						Console.println("CLIENT : Connection Successfull");
					} catch (IOException e) {
						Console.println("CLIENT : Error "+e.getLocalizedMessage());
						e.printStackTrace();
					}
				}
			});
		}
		
		ScrollPane connectScroll = new ScrollPane(connectTable, skin);
		connectScroll.setScrollbarsOnTop(false);
		TextButton cancel = new TextButton("Cancel", skin);
		
		Table main = new Table(skin);
		main.row();
		main.add(connectScroll);
		main.row();
		main.add(cancel);
	
		win.debug();
		win.add(main);
		win.pack();
		
		stage.addActor(win);
		
		cancel.addCaptureListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				win.setVisible(false);
				win.clear();
				win.clearActions();
				stage.getRoot().removeActor(win);
			}});
	}
}
