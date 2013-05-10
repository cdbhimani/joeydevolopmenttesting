package com.emptyPockets.logging;

public interface ConsoleListener {
	public void print(String message);
	public void println(String message);
	public void printf(String message, Object... values);
}
