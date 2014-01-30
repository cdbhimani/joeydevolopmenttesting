package com.emptyPockets.network.gui;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.emptyPockets.network.framework.User;
import com.emptyPockets.network.framework.UserHub;
import com.emptyPockets.network.framework.UserHubListener;

public class UserListPanel extends Table implements UserHubListener {
	UserHub users;
	HashMap<User, UserPanel> panels;
	Skin skin;
	public UserListPanel(Skin skin) {
		panels = new HashMap<User, UserPanel>();
		this.skin = skin;
	}

	@Override
	public void userConnected(User user) {
		synchronized (panels) {
			UserPanel panel = new UserPanel(user, skin);
			panels.put(user, panel);
			updateLayout();
		}
	}

	/**
	 * This method will update all the underlying panels. 
	 */
	public void update(){
		synchronized (panels) {
			Iterator<UserPanel> data = panels.values().iterator();

			while (data.hasNext()) {
				data.next().update();
			}
		}
	}
	public void updateLayout() {
		synchronized (panels) {
			clear();
			Iterator<UserPanel> data = panels.values().iterator();

			while (data.hasNext()) {
				row();
				add(data.next()).fillX().expandX();
			}
		}

	}

	@Override
	public void userDisconnected(User user) {
		synchronized (panels) {
			panels.remove(user);
			updateLayout();
		}
	}
}
