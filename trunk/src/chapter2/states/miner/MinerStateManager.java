package chapter2.states.miner;

import chapter2.states.State;

public class MinerStateManager {
    private static MinerStateManager manager = new MinerStateManager();
    
    public EatDinner EatDinner;
    public EnterMineAndDigforNugget EnterMineAndDigforNugget;
    public QuenchThirst QuenchThirst;
    public GoHomeAndSleepTilRested GoHomeAndSleepTilRested;
    public VisitBankAnddepositGold VisitBankAnddepositGold;
    public MinerGlobalState MinerGlobalState;
    
    private MinerStateManager() {
	EatDinner = new EatDinner();
	EnterMineAndDigforNugget = new EnterMineAndDigforNugget();
	QuenchThirst = new QuenchThirst();
	GoHomeAndSleepTilRested = new GoHomeAndSleepTilRested();
	VisitBankAnddepositGold = new VisitBankAnddepositGold();

	MinerGlobalState = new MinerGlobalState();
    }

    public static MinerStateManager getInstance() {
	return manager;
    }

}
