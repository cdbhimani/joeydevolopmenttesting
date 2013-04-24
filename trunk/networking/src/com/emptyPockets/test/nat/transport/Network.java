package com.emptyPockets.test.nat.transport;

import com.emptyPockets.test.nat.transport.stun.STUNRequest;
import com.emptyPockets.test.nat.transport.stun.STUNResponse;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
	public static void registerClasses(EndPoint con){
		Kryo kryo = con.getKryo();
		kryo.register(STUNRequest.class);
		kryo.register(STUNResponse.class);
	}
}
