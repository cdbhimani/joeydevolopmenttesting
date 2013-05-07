package com.emptypockets.networking.controls.commands;


public abstract class Command {
	String name;
	
	public Command(String name) {
		this.name = name;
	}

	public abstract void proceeArg(String data);

	public String getName() {
		return name;
	}
}
