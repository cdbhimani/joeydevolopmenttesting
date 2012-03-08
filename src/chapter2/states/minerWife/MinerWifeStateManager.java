package chapter2.states.minerWife;

public class MinerWifeStateManager {
    private static MinerWifeStateManager manager = new MinerWifeStateManager();

    public MinerWifeGlobalState MinerWifeGlobalState;
    public DoHouseWork DoHouseWork;
    public VisitBathroom VisitBathroom;
    public CookStew CookStew;
    private MinerWifeStateManager() {
	MinerWifeGlobalState = new MinerWifeGlobalState();
	DoHouseWork = new DoHouseWork();
	VisitBathroom = new VisitBathroom();
	CookStew = new CookStew();
    }

    public static MinerWifeStateManager getInstance() {
	return manager;
    }

}
