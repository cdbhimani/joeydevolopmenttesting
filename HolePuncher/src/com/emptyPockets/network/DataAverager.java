package com.emptyPockets.network;

import java.util.ArrayList;

public class DataAverager<T extends Number> {
	ArrayList<T> history;
	float avg;
	int maxCapacity = 0;
	public DataAverager(int num){
		history = new ArrayList<T>(num);
		avg = 0;
		maxCapacity = num;
	}
	
	public void addRecord(T num){
		if(maxCapacity == history.size()){
			history.remove(0);
		}
		history.add(num);
		updateAvg();
	}
	
	private void updateAvg(){
		avg = 0;
		for(T val : history){
			avg += val.floatValue();
		}
		avg/=history.size();
	}
	public String toString(){
		StringBuilder rst = new StringBuilder();
		rst.append("{DataAverager: vals[");
		for(T val : history){
			rst.append(val.toString());
			rst.append(",");
		}
		rst.append("] avg[");
		rst.append(avg);
		rst.append("]}");
		return rst.toString();
	}
	public float getAvg(){
		return avg;
	}
	
	public static void main(String input[]){
		DataAverager<Float> avg = new DataAverager<Float>(3);
		while(true){
			avg.addRecord((float) (10+5*Math.random()));
			System.out.println(avg);
		}
		
	}
}
