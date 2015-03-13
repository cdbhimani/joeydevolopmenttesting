package com.emptyPockets.test.kryoNetwork.gui;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.emptyPockets.gui.Scene2DToolkit;
import com.emptyPockets.test.kryoNetwork.client.ServerStateListener;
import com.emptyPockets.test.kryoNetwork.server.ServerState;
import com.emptyPockets.test.kryoNetwork.transport.messages.ServerStateResponse;
import com.emptyPockets.test.kryoNetwork.transport.messages.UserData;

public class ServerPanel extends Window implements ServerStateListener {
	ScrollPane scroll;
	WidgetGroup panelHolder;

	HashMap<String, UserDataPanel> panels = new HashMap<String, UserDataPanel>();
	ArrayList<UserDataPanel> allPanels = new ArrayList<UserDataPanel>();

	public ServerPanel(String title, Skin skin) {
		super(title, skin);
		create(skin);
	}

	public void create(Skin skin) {
		panelHolder = new VerticalGroup();
		scroll = new ScrollPane(panelHolder, skin);
		
		add(scroll).expand().fill();
	}

	private UserDataPanel getUserDataPanel(UserData data) {
		return panels.get(getKey(data));
	}

	private void addPanel(UserData data) {
		UserDataPanel panel = new UserDataPanel(Scene2DToolkit.getToolkit().getSkin());
		panel.setUserData(data);
		panels.put(getKey(data), panel);
		panelHolder.addActor(panel);
	}

	private String getKey(UserData data) {
		return data.serverTCPAddress;
	}

	@Override
	public void notifyStateUpdate(ServerState state) {
		synchronized (state.updateLock) {
			ServerStateResponse lastResponse = state.lastResponse;
			if (lastResponse != null) {
				ArrayList<UserData> serverData = state.lastResponse.connections;
				// Remove old panels
				for (UserData data : serverData) {
					if (getKey(data)!=null) {
						UserDataPanel panel = getUserDataPanel(data);
						if (panel == null) {
							addPanel(data);
						} else {
							panel.setUserData(data);
						}
					}
				}
			}
		}
		// panels.addActor(new TextField("Update",
		// Scene2DToolkit.getToolkit().getSkin()));
	}
}
