package com.emptyPockets.test.nat.server;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.emptyPockets.test.nat.client.ServerStateListener;
import com.emptyPockets.test.nat.transport.messages.ServerStateResponse;
import com.emptyPockets.test.nat.transport.messages.UserData;

public class ServerState {
	Object updateLock = new Object();
	ServerStateResponse lastResponse;
	
	ServerStateListener serverStateListener;
	public void setServerResponse(ServerStateResponse object) {
		if(serverStateListener != null){
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
