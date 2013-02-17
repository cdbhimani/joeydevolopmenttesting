package com.joey.chain.common;


public class ConsoleLogger{
	int maxConsoleSize = 1000;
	int maxConsoleLines = 10;
	StringBuffer console = new StringBuffer();

	public ConsoleLogger() {
		
	}

	public int getMaxConsoleLines() {
		return maxConsoleLines;
	}

	public void setMaxConsoleLines(int maxConsoleLines) {
		this.maxConsoleLines = maxConsoleLines;
	}



	public void println(String s) {
		console.append(s);
		console.append("\n");
		trim();
	}

	public void print(String s) {
		console.append(s);
		trim();
	}

	public void printf(String s, Object... args) {
		console.append(String.format(s, args));
		trim();
	}

	private void trim() {
		int extra = console.length() - maxConsoleSize;
		if (extra > 0)
			console.delete(0, extra);

		int lines = countOccourence('\n');
		if (lines > maxConsoleLines) {
			int idx = indexOf('\n', (lines - maxConsoleLines));
			if (idx != -1) {
				console.delete(0, idx);
			}
		}

	}

	private int indexOf(char c, int times) {
		int count = 0;
		for (int i = 0; i < console.length(); i++) {
			if (console.charAt(i) == c) {
				count++;
				if (count == times) {
					return i;
				}
			}
		}

		return -1;
	}

	private int countOccourence(char c) {
		int count = 0;
		for (int i = 0; i < console.length(); i++) {
			if (console.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}

	public int getMaxConsoleSize() {
		return maxConsoleSize;
	}

	public void setMaxConsoleSize(int maxConsoleSize) {
		this.maxConsoleSize = maxConsoleSize;
	}

	public String toString() {
		return console.toString();
	}
}
