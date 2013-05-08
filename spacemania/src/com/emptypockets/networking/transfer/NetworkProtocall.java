package com.emptypockets.networking.transfer;

import com.emptypockets.engine.Engine;
import com.esotericsoftware.kryo.Kryo;

public class NetworkProtocall {
	public static void register(Kryo kryo){
		kryo.register(Engine.class);
	}
}
