package com.emptypockets.engine;

import java.util.ArrayList;

public class Engine {
	int tick = 0;
	ArrayList<MovingEntity> entities = new ArrayList<MovingEntity>();
	
	public void update(float time) {
		synchronized (entities) {
			for (MovingEntity e : entities) {
				e.update(time);
			}
		}
	}
	
	public void tick(){
		tick++;
	}

	public void updateEntityVel(String name, float velX, float velY) {
		synchronized (entities) {
			MovingEntity ent = getEntity(name);
			if (ent != null) {
				ent.setAcl(velX, velY);
			}
		}
	}

	public MovingEntity getEntity(String name) {
		synchronized (entities) {
			MovingEntity ent = null;
			for (MovingEntity e : entities) {
				if (e.getName().equals(name)) {
					ent = e;
				}
			}
			return ent;
		}
	}
	
	public MovingEntity getEntity(int id) {
		synchronized (entities) {
			MovingEntity ent = null;
			for (MovingEntity e : entities) {
				if (e.getId()==id) {
					ent = e;
				}
			}
			return ent;
		}
	}

	public void addEntity(int id, String name) {
		MovingEntity ent = new MovingEntity();
		ent.setName(name);
		addEntity(ent);
	}

	public void removeEntity(String name) {
		synchronized (entities) {
			MovingEntity ent = getEntity(name);
			if (ent != null) {
				entities.remove(ent);
			}
		}
	}

	public void removeEntity(int id) {
		synchronized (entities) {
			MovingEntity ent = getEntity(id);
			if (ent != null) {
				entities.remove(ent);
			}
		}
	}
	public void addEntity(MovingEntity ent) {
		synchronized (entities) {
			entities.add(ent);
		}
	}

	public void removeEntity(MovingEntity ent) {
		synchronized (entities) {
			entities.remove(ent);
		}
	}

	public int getTick() {
		return tick;
	}

	public ArrayList<MovingEntity> getEntities() {
		return entities;
	}
}
