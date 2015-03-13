package com.emptyPockets.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;

public class BodyLauncher {
	Body owner;
	Vector2 pos;
	Vector2 dir;
	float speed;
	BodyDef bodyDef;
	long lastShoot = 0;
	long shootInterval;
	float bulletRadius = 0.1f;
	float bulletDensity = 30f;
	float bulletLinearDamping = 0f;
	
	float bulletMaxDistance = 100;
	float bulletMaxDistance2 = bulletMaxDistance*bulletMaxDistance;
	float bulledMinSpeed = 10;
	float bulledMinSpeed2 = bulledMinSpeed*bulledMinSpeed;
	public BodyLauncher(Body owner, Vector2 pos, Vector2 dir, float speed, long shootInterval){
		setOwner(owner);
		setPos(pos);
		setDir(dir);
		setSpeed(speed);
		setShootInterval(shootInterval);
	}
	
	private void updateBodyDef(){
		Vector2 bulPos = owner.getWorldPoint(pos).cpy();
		Vector2 bulVel = owner.getWorldVector(dir).cpy();
		
		Vector2 currentRightNormal = owner.getWorldVector(new Vector2(0,1)).cpy();
		float ownerSpeed = currentRightNormal.dot(owner.getLinearVelocity());
		
		bulVel.nor().mul(speed+ownerSpeed);
		
		if(bodyDef == null){
			bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
		}
		bodyDef.linearDamping = bulletLinearDamping;
		bodyDef.position.set(bulPos);
		bodyDef.linearVelocity.set(bulVel);
	}
	
	private boolean createBullet(){
		updateBodyDef();
		Body body = owner.getWorld().createBody(bodyDef);
		CircleShape shape = new CircleShape();
		shape.setRadius(bulletRadius);
		body.createFixture(shape, bulletDensity);
		body.setUserData(this);
		shape.dispose();
		return true;
	}

	public boolean shoot(){
		if(lastShoot +shootInterval < System.currentTimeMillis()){
			if(createBullet()){
				lastShoot = System.currentTimeMillis();
				return true;
			}
		}
		return false;
	}
	
	public Body getOwner() {
		return owner;
	}
	public void setOwner(Body owner) {
		this.owner = owner;
	}
	public Vector2 getPos() {
		return pos;
	}
	public void setPos(Vector2 pos) {
		this.pos = pos;
	}
	public Vector2 getDir() {
		return dir;
	}
	public void setDir(Vector2 dir) {
		this.dir = dir;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public long getShootInterval() {
		return shootInterval;
	}

	public void setShootInterval(long shootInterval) {
		this.shootInterval = shootInterval;
	}

	public long getLastShoot() {
		return lastShoot;
	}

	public void setLastShoot(long lastShoot) {
		this.lastShoot = lastShoot;
	}

	public float getBulletRadius() {
		return bulletRadius;
	}

	public void setBulletRadius(float bulletRadius) {
		this.bulletRadius = bulletRadius;
	}

	public float getBulletDensity() {
		return bulletDensity;
	}

	public void setBulletDensity(float bulletDensity) {
		this.bulletDensity = bulletDensity;
	}

	public float getBulledMinSpeed() {
		return bulledMinSpeed;
	}

	public void setBulledMinSpeed(float bulledMinSpeed) {
		this.bulledMinSpeed = bulledMinSpeed;
		bulledMinSpeed2 = bulledMinSpeed*bulledMinSpeed ; 
	}

	public float getBulledMinSpeed2() {
		return bulledMinSpeed2;
	}

	public float getBulletLinearDamping() {
		return bulletLinearDamping;
	}

	public void setBulletLinearDamping(float bulletLinearDamping) {
		this.bulletLinearDamping = bulletLinearDamping;
	}

	public float getBulletMaxDistance() {
		return bulletMaxDistance;
	}

	public void setBulletMaxDistance(float bulletMaxDistance) {
		this.bulletMaxDistance = bulletMaxDistance;
		bulletMaxDistance2 = bulletMaxDistance*bulletMaxDistance;
	}

	public float getBulletMaxDistance2() {
		return bulletMaxDistance2;
	}
}
