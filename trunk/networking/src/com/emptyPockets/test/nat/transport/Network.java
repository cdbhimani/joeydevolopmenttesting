package com.emptyPockets.test.nat.transport;

import java.util.ArrayList;

import com.emptyPockets.test.nat.transport.messages.LoginData;
import com.emptyPockets.test.nat.transport.messages.LoginRequest;
import com.emptyPockets.test.nat.transport.messages.ServerStateRequest;
import com.emptyPockets.test.nat.transport.messages.ServerStateResponse;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
	public static void registerClasses(EndPoint con){
		Kryo kryo = con.getKryo();
		kryo.register(LoginRequest.class);
		kryo.register(LoginData.class);
		kryo.register(ServerStateRequest.class);
		kryo.register(ServerStateResponse.class);
		kryo.register(ArrayList.class);
//		kryo.register(.class);
	}
}
