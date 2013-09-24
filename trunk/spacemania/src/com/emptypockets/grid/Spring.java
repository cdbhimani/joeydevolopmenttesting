package com.emptypockets.grid;

import com.badlogic.gdx.math.Vector2;

public class Spring {
	public PointMass End1;
	public PointMass End2;
	public float TargetLength;
	public float Stiffness;
	public float Damping;

	public Spring(PointMass end1, PointMass end2, float stiffness, float damping) {
		End1 = end1;
		End2 = end2;
		Stiffness = stiffness;
		Damping = damping;
		TargetLength = end1.pos.dst(end2.pos) * 0.95f;
	}

	public void update() {
		Vector2 posDiff = new Vector2(End1.pos);
		posDiff.sub(End2.pos);

		float length = posDiff.len();
		// these springs can only pull, not push
		if (length <= TargetLength)
			return;

		posDiff.mul((length - TargetLength)*Stiffness/length);
		
		Vector2 velDiff = new Vector2(End2.vel);
		velDiff.sub(End1.vel);
		velDiff.mul(Damping);
		
		Vector2 force = new Vector2(posDiff);
		force.sub(velDiff);		

		End2.applyForce(force);
		force.mul(-1);
		End1.applyForce(force);
	}
}