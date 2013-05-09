package com.emptypockets.networking.transfer;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.engine.Engine;
import com.emptypockets.engine.MovingEntity;
import com.esotericsoftware.kryo.Kryo;

public class NetworkProtocall {
	public static void register(Kryo kryo){
		kryo.register(Engine.class);
		kryo.register(ArrayList.class);
		kryo.register(MovingEntity.class);
		kryo.register(Vector2.class);
		kryo.register(ClientLoginRequest.class);
		kryo.register(ClientLogoutRequest.class);
		kryo.register(ClientStateTransferObject.class);
	}
}
