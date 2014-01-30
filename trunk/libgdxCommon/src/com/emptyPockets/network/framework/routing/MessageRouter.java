package com.emptyPockets.network.framework.routing;

import java.util.ArrayList;

public class MessageRouter implements MessageDestination {
	ArrayList<MessageDestination> destinations;
	
	public MessageRouter(){
		destinations = new ArrayList<MessageDestination>();
	}
	
	public void register(MessageDestination dest){
		destinations.add(dest);
	}

	@Override
	public boolean recieveMessage(Object message, Object... additionData) {
		for(MessageDestination dest : destinations){
			if(dest.accepts(message)){
				if(dest.recieveMessage(message, additionData)){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean accepts(Object message) {
		return true;
	}
}
