package com.emptyPockets.test.kryoNetwork.server;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.emptyPockets.test.kryoNetwork.client.ServerStateListener;
import com.emptyPockets.test.kryoNetwork.transport.messages.ServerStateResponse;
import com.emptyPockets.test.kryoNetwork.transport.messages.UserData;

public class ServerState {
	public Object updateLock = new Object();
	public ServerStateResponse lastResponse;
	
	ArrayList<ServerStateListener> serverStateListeners = new ArrayList<ServerStateListener>();
	
	public void addListener(ServerStateListener listener){
		serverStateListeners.add(listener);
	}
	
	public void removeListener(ServerStateListener listener){
		serverStateListeners.remove(listener);
	}
	public void setServerResponse(ServerStateResponse object) {
		for(ServerStateListener serverStateListener : serverStateListeners){
			serverStateListener.notifyStateUpdate(this);	
		}
		synchronized (updateLock) {
			lastResponse = object;
		}
	}
	
	
	public void drawServer(ShapeRenderer shape){
		synchronized (updateLock) {
			if(lastResponse != null){
				shape.begin(ShapeType.Circle);
				shape.setColor(Color.RED);
				for(UserData data : lastResponse.connections){
					if(data.pos != null){
						shape.circle(data.pos.x, data.pos.y, 10);
					}
				}
				shape.end();
			}
		}
	}

}
