package com.emptyPockets.test.datagramChannel.client;

import java.io.IOException;
import java.nio.channels.DatagramChannel;

public class TestChannelClient {
	DatagramChannel channel;
	int maximumPacketSize;
	
	public TestChannelClient() throws IOException{
		channel = DatagramChannel.open();
	}
	
	public void sendObject(Object o){
		
	}
	
}
