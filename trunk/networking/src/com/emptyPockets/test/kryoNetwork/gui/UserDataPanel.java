package com.emptyPockets.test.kryoNetwork.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.emptyPockets.test.kryoNetwork.transport.messages.UserData;

class UserDataPanel extends Table {
	UserData data;

	TextField ping;
	TextField name;
	TextField tcpAddress;
	TextField udpAddress;
	TextButton connect;

	public UserDataPanel(Skin skin) {
		super(skin);
		create(skin);
		updateLayout();
	}

	public void create(Skin skin) {
		ping = new TextField("", skin);
		name = new TextField("", skin);
		udpAddress = new TextField("", skin);
		tcpAddress = new TextField("", skin);
		connect = new TextButton("Connect", skin);
	}

	public void updateLayout() {
		row();
		add("Name");
		add(name);
		row();
		add("Ping");
		add(ping);
		row();
		add("TCP");
		add(tcpAddress);
		row();
		add("UDP");
		add(udpAddress);
		row();
		add(connect).fillX().expand().colspan(2);
		pack();
	}

	public String getText(Object obj) {
		return obj == null ? "null" : obj.toString();
	}

	public void update() {
		System.out.println("Updating");
		if (data != null) {
			name.setText(getText(data.name));
			ping.setText(getText(data.serverPing) + " ms");
			tcpAddress.setText(getText(data.serverTCPAddress));
			udpAddress.setText(getText(data.serverUDPAddress));
		}
	}

	public void setUserData(UserData data) {
		this.data = data;
		update();
	}
}