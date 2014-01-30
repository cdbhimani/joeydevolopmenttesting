package com.emptyPockets.utils.stateMachine;

public class StateMachine<T> {
	T object;
	State currentState = null;

	public StateMachine(T object) {
		this.object = object;
	}

	public void update() {
		if (currentState != null) {
			currentState.update(this);
		}
	}

	public void setState(State state) {
		if (currentState != null) {
			currentState.exitState(this);
		}
		currentState = state;
		currentState.enterState(this);
	}

	public T getObject() {
		return object;
	}
}
