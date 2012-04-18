package chapter2.entities;

import chapter2.locations.Location;
import chapter2.states.StateMachine;
import chapter2.states.miner.MinerStateManager;

public class Miner extends BaseGameEntity{
    public static final int MAX_FATIGUE = 10;
    public static final int MAX_CARRIED_GOLD = 200;
    public static final int MAX_THIRST = 300;
    
    int location = Location.Home;
    int goldCarried;
    int goldInBank;
    int thirst;
    int fatigue;
   
    public Miner(){
	super();
    }
    
    public Miner(int id){
	super(id);
    }
    
    @Override
    public void init(){
	setStateMachine(new StateMachine<Miner>(this));
	stateMachine.setGlobalState(MinerStateManager.getInstance().MinerGlobalState);
	stateMachine.changeState(MinerStateManager.getInstance().GoHomeAndSleepTilRested);
    }
    
    @Override
    public void update() {
	super.update();
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getGoldCarried() {
        return goldCarried;
    }

    public void setGoldCarried(int goldCarried) {
        this.goldCarried = goldCarried;
    }

    public int getGoldInBank() {
        return goldInBank;
    }

    public void setGoldInBank(int goldInBank) {
        this.goldInBank = goldInBank;
    }

    public int getThirst() {
        return thirst;
    }

    public void setThirst(int thirst) {
        this.thirst = thirst;
    }

    public int getFatigue() {
        return fatigue;
    }

    public void setFatigue(int fatigue) {
        this.fatigue = fatigue;
    }

    public void increaseFatigue() {
	fatigue++;
    }
    
    public void increaseThirst(){
	thirst++;
    }

    public void decreaseFatigue() {
	fatigue--;
    }
    
    public void decreaseThirst(){
	thirst--;
    }
    
    public boolean isTired(){
	return fatigue >MAX_FATIGUE;
    }
    
    public boolean isPocketsFull(){
   	return goldCarried > MAX_CARRIED_GOLD;
    }
    
    public boolean isThirsty(){
	return thirst>MAX_THIRST;
    }
    
    public void addGold(int count){
	goldCarried+=count;
    }

    public void depositGold() {
	goldInBank += goldCarried;
	goldCarried = 0;
    }
    public boolean isFullyRested() {
	// TODO Auto-generated method stub
	return fatigue <= 0;
    }
    
    

}
