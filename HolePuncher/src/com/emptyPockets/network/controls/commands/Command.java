package com.emptyPockets.network.controls.commands;


public abstract class Command {
	String name;
	String description;
	public Command(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public Command(String name) {
		this(name,"None");
	}

	public abstract void proceeArg(String data);

	public String getName() {
		return name;
	}
	
	protected void setDescription(String description) {
		this.description = description;
	}

	public String getDescription(){
		return description;
	}
}
