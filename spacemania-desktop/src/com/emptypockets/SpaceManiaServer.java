package com.emptypockets;

import java.util.Scanner;

import com.emptypockets.networking.server.ServerManager;

public class SpaceManiaServer {
	public static void main(String input[]){
		ServerManager server = new ServerManager(10);
		
		Scanner in = new Scanner(System.in);
		String line;
		while ((line = in.nextLine()) != null) {
			try {
				if (line.length() > 0) {
						server.getCommand().processCommand(line);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
}
