package com.joey.testing.speedTests;

public class SpeedCalculator {
	public static double calculationAddFloat(long times, float numA, float numB, float result){
		long start;
		long diffCalc;
		long i = 0;
		
		//Get Calculation Time
		start= System.nanoTime();
		for(i = 0; i < times; i++){
			result = numA/numB;
		}
		diffCalc = System.nanoTime()-start;		
		return (diffCalc)/400./times;
	}
	
	
	public static void main(String input[]){
		for(long i = 1000; i < 10000000; i+=10000){
			float numA = (float)((Math.random())*Float.MAX_VALUE);
			float numB = (float)((Math.random())*Float.MAX_VALUE);
			float result =0;
			
			System.out.printf("%d : %g\n",i,SpeedCalculator.calculationAddFloat(i, numA, numB, result));
		}
		
	}
}
