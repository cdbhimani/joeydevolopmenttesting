package com.emptyPockets.logging;

import java.util.ArrayList;


public class Console {
	static Console console;
	
	ArrayList<ConsoleScreen> screens = new ArrayList<ConsoleScreen>();
	
	private Console(){
		
	}
	
	private static Console getConsole(){
		if(console == null){
			console = new Console();
		}
		return console;
	}
	
	public static void register(ConsoleScreen screen){
		Console console = getConsole();
		synchronized (console.screens) {
			console.screens.add(screen);
		}
	}
	
	public static void unregister(ConsoleScreen screen){
		Console console = getConsole();
		synchronized (console.screens) {
			console.screens.remove(screen);
		}
	}
	
	public static void print(String msg){
		Console console = getConsole();
		synchronized (console.screens) {
			for(ConsoleScreen view : console.screens){
				view.print(msg);
			}
		}
	}
	
	public static void println(String msg){
		Console console = getConsole();
		synchronized (console.screens) {
			for(ConsoleScreen view : console.screens){
				view.println(msg);
			}
		}
	}

	public static void printf(String msg, Object... obj){
		Console console = getConsole();
		synchronized (console.screens) {
			for(ConsoleScreen view : console.screens){
				view.printf(msg, obj);
			}
		}
	}
}
