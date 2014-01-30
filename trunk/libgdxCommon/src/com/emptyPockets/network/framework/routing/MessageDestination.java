package com.emptyPockets.network.framework.routing;

public interface MessageDestination {
	/**
	 * 
	 * @param message
	 * @param additionData
	 * @return true if this message should be captured 
	 */
	public boolean recieveMessage(Object message, Object... additionData);
	public boolean accepts(Object message);
}
