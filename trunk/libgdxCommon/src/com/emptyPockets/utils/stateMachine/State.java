package com.emptyPockets.utils.stateMachine;

public interface State<T extends StateMachine<X>, X>{
	public void enterState(StateMachine<X> state);
	public void exitState(StateMachine<X> state);
	public void update(StateMachine<X> state);
}
