package com.emptyPockets.test.kryoNetwork.server;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class SchedultedMethodCallerTask {
	Timer timer;
	TimerTask task;

	int delay;
	
	Method method;
	Object object;
	Object[] parms;
	public SchedultedMethodCallerTask(String name, Object object,  String methodName, Class... parms) throws SecurityException, NoSuchMethodException{
		timer = new Timer(name);
		delay = 1000;
		setCall(object, methodName, parms);
	}
	
	private void setCall(Object object, String methodName, Class... parms) throws SecurityException, NoSuchMethodException{
		this.object = object;
		this.parms = parms;
		method = object.getClass().getDeclaredMethod(methodName, parms);
	}
	
	public void stop(){
		if(task!= null){
			task.cancel();
		}
	}
	
	public void start(){
		stop();
		task = new TimerTask() {
			@Override
			public void run() {
				runMethod();
			}
		};
		timer.scheduleAtFixedRate(task,0,delay);
	}
	
	private void runMethod(){
		try {
			method.invoke(object, parms);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setDelay(int delay){
		this.delay = delay;
	}
	
	
}
