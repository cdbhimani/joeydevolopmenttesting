package chapter2.entities;

import java.util.HashMap;

public class EntityManager {
    public static final int MINER_BOB_ID = 1;
    public static final int MINER_WIFE_ID = 2;
    HashMap<Integer, BaseGameEntity> entities;
    static EntityManager manager = new EntityManager();
    
    private EntityManager(){
	entities = new HashMap<Integer, BaseGameEntity>();
    }
    
    public static EntityManager getInstance(){
	return manager;
    }
    public BaseGameEntity getEntityById(int id){
	return entities.get(id);
    }
    public void addEntity(BaseGameEntity e){
	entities.put(e.getId(), e);
    }
    
    public void removeEntity(BaseGameEntity e){
	entities.remove(e.getId());
    }
    
}
