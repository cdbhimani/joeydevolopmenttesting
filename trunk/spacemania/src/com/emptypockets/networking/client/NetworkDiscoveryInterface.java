package com.emptypockets.networking.client;

import java.net.InetAddress;
import java.util.List;

public interface NetworkDiscoveryInterface {

	void notifyDiscoveredHosts(List<InetAddress> hosts);

}
