package chapter2.states;

import chapter2.entities.BaseGameEntity;
import chapter2.messageSystem.Telegram;

public class StateMachine<T extends BaseGameEntity> {
    T owner;
    
    State<T> currentState;
    State<T> previousState;
    State<T> globalState;
    
    public StateMachine(T entity){
	this.owner = entity;
    }
    
    public void update(){
	if(globalState != null){
	    globalState.Execute(owner);
	}
	
	if(currentState != null){
	    currentState.Execute(owner);
	}
    }
    
    public void changeState(State<T> newState){
	//Keep record of previous state
	previousState = currentState;
	if(currentState != null)
	    currentState.Exit(owner);
	
	//Enter new state
	currentState = newState;
	if(currentState != null)
	    currentState.Enter(owner);
    }
    
    public void revertToPreviousState(){
	changeState(previousState);
    }
    
    public boolean isInState(State<T> s){
	return s == currentState;
    }

    public State<T> getCurrentState() {
        return currentState;
    }

    public State<T> getPreviousState() {
        return previousState;
    }

    public State<T> getGlobalState() {
        return globalState;
    }

    public void setGlobalState(State<T> globalState) {
        this.globalState = globalState;
    }

    public T getOwner() {
        return owner;
    }

    public boolean handleMessage(Telegram t) {
	boolean handled = false;
	if(currentState != null){
	    handled =  currentState.handleMessage(owner, t);
	}
	
	if(!handled || globalState != null){
	    handled = globalState.handleMessage(owner, t);
	}
	return handled;
    }
}
