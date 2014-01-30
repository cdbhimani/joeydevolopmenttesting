package com.emptyPockets.network.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.emptyPockets.network.framework.User;

public class UserPanel extends Table {
	User user;

	Label nameLabel;
	Label pingLabel;
	
	public UserPanel(User user, Skin skin) {
		setUser(user);
		nameLabel = new Label("", skin);
		pingLabel = new Label("", skin);
		createPanel();
	}
	
	public void createPanel(){
		
	}

	/**
	 * This method will update the displayed text
	 * for the users. 
	 */
	public void update() {
		nameLabel.setText(user.getUserName());
		pingLabel.setText(String.valueOf(user.getPing()));
	}

	public void setUser(User user) {
		this.user = user;
	}
}
