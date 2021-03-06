package com.emptypockets.engine;

import com.badlogic.gdx.math.Vector2;

public class MovingEntity {
	int id;
	String name;
	Vector2 pos;
	Vector2 vel;
	Vector2 acl;

	public MovingEntity() {
		pos = new Vector2();
		vel = new Vector2();
		acl = new Vector2();
	}

	public void update(float time) {
		vel.x = vel.x + acl.x * time;
		vel.y = vel.y + acl.y * time;

		pos.x = pos.x + vel.x * time;
		pos.y = pos.y + vel.y * time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPos(float x, float y) {
		this.pos.set(x, y);
	}

	public void setPos(Vector2 pos) {
		this.pos.set(pos);
	}

	public void setVel(float x, float y) {
		this.vel.set(x, y);
	}

	public void setVel(Vector2 vel) {
		this.vel.set(vel);
	}

	public float posY() {
		return pos.y;
	}

	public float posX() {
		return pos.x;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAcl(float x, float y) {
		this.acl.set(x, y);
	}

	public Vector2 getAcl() {
		return acl;
	}

	public void setAcl(Vector2 acl) {
		this.acl = acl;
	}
}
