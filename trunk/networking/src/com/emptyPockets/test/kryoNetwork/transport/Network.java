package com.emptyPockets.test.kryoNetwork.transport;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.test.kryoNetwork.transport.messages.EntityUpdate;
import com.emptyPockets.test.kryoNetwork.transport.messages.LoginRequest;
import com.emptyPockets.test.kryoNetwork.transport.messages.ServerStateRequest;
import com.emptyPockets.test.kryoNetwork.transport.messages.ServerStateResponse;
import com.emptyPockets.test.kryoNetwork.transport.messages.UserData;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
	public static void registerClasses(EndPoint con){
		Kryo kryo = con.getKryo();
		kryo.register(LoginRequest.class);
		kryo.register(UserData.class);
		kryo.register(ServerStateRequest.class);
		kryo.register(ServerStateResponse.class);
		kryo.register(ArrayList.class);
		kryo.register(EntityUpdate.class);
		kryo.register(Vector2.class);
//		kryo.register(.class);
	}
}
