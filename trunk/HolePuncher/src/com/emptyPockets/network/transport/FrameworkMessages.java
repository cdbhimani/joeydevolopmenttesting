package com.emptyPockets.network.transport;

import java.io.IOException;

import com.emptyPockets.network.connection.UDPConnection;
import com.emptyPockets.network.connection.UDPConnectionListener;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.minlog.Log;

public class FrameworkMessages implements UDPConnectionListener{
	//Variables
	public static FrameworkMessages framework = null;
	
	//Framework messages
	public static class KeepAlive extends FrameworkMessages{
	}
	public static class Ping extends FrameworkMessages{
		long sendTime;
		long returnTime;
		boolean isResponse = false;
		
		public void start(){
			sendTime = System.currentTimeMillis();
		}
		
		public void finish(){
			returnTime = System.currentTimeMillis();
		}
		
		public long getTime(){
			return returnTime-sendTime;
		}
	}

	private FrameworkMessages(){
	}
	
	public static FrameworkMessages getFrameWork(){
		if(framework == null){
			 framework= new FrameworkMessages();
		}
		return framework;
	}
	
	@Override
	public void notifyObjectRecieved(UDPConnection con, TransportObject object) {
		if(object.data instanceof FrameworkMessages){
			if(object.data instanceof Ping){
				Ping p = (Ping) object.data;
				if(p.isResponse){
					p.finish();
					Log.info("PING : "+p.getTime());
				}else{
					p.isResponse = true;
					try {
						con.sendTransportObject(object);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}

	public static void register(Kryo kryo) {
		kryo.register(Ping.class);
		kryo.register(KeepAlive.class);
	}
}
