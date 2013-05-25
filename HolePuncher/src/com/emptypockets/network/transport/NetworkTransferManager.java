package com.emptypockets.network.transport;

import com.esotericsoftware.kryo.Kryo;

public class NetworkTransferManager {
	public static void register(Kryo kryo){
		FrameworkMessages.getFrameWork().register(kryo);
		kryo.register(TransportObject.class);
		kryo.register(RemoteMessage.class);
	}
}
