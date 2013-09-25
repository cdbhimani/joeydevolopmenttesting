package com.emptyPockets.utils;

public class Array2DShifter<T> {

	public void shiftLeft(T[][] data) {
		T tmp[] = data[0];
		for (int x = 0; x < data.length - 1; x++) {
			data[x] = data[x + 1];
		}
		data[data.length - 1] = tmp;
	}

	public void shiftRight(T[][] data) {
		T tmp[] = data[data.length - 1];
		for (int x = data.length - 1; x > 0; x--) {
			data[x] = data[x - 1];
		}
		data[0] = tmp;
	}

	public void shiftUp(T[][] data) {
		T tmp;
		for (int x = 0; x < data.length; x++) {
			tmp = data[x][0];
			for (int y = 0; y < data[x].length - 1; y++) {
				data[x][y] = data[x][y + 1];
			}
			data[x][data[x].length - 1] = tmp;
		}
	}

	public void shiftDown(T[][] data) {
		T tmp;
		for (int x = 0; x < data.length; x++) {
			tmp = data[x][data[0].length - 1];
			for (int y = data[x].length - 1; y > 0; y--) {
				data[x][y] = data[x][y - 1];
			}
			data[x][0] = tmp;
		}
	}

	public static void print(Object[][] data) {
		for (int y = 0; y < data[0].length; y++) {
			for (int x = 0; x < data.length; x++) {
				if (x != 0) {
					System.out.print(" - ");
				}
				System.out.print(data[x][y]);
			}
			System.out.println();
		}
	}

	public static void main(String input[]) {
		Array2DShifter<String> shifter = new Array2DShifter<String>();

		String[][] data = new String[3][3];

		for (int x = 0; x < data.length; x++) {
			for (int y = 0; y < data[x].length; y++) {

				data[x][y] = "[" + (x + 1) + "-" + (y + 1) + "]";
			}
		}

		System.out.println("\nBEFORE\n=========================\n");
		shifter.print(data);
		System.out.println("\n\nAFTER\n=========================\n");
		shifter.shiftDown(data);
		shifter.shiftRight(data);
		shifter.print(data);
	}

}
