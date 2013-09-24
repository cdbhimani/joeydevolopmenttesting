package com.emptyPockets.backgrounds.grid2D;

import com.badlogic.gdx.math.Vector2;

public class FixedNodeLink extends NodeLink {
	Node node;
	Vector2 pos;
	NodeLinkSettings cfg;
	float initialDist;

	Vector2 temp = new Vector2();
	float currentDist;

	public FixedNodeLink(Node node, Vector2 fixPoint, NodeLinkSettings settings) {
		this.node = node;
		this.pos = fixPoint;
		this.initialDist = node.pos.dst(this.pos);
		this.cfg = settings;
	}

	public void solve() {
		temp.x = node.pos.x - pos.x;
		temp.y = node.pos.y - pos.y;
		currentDist = temp.len();
		if (currentDist * currentDist < 0.001f) {
			return;
		}
		temp.mul((initialDist - currentDist) / currentDist);

		temp.x = temp.x * cfg.stiffness - (node.vel.x) * cfg.damping;
		temp.y = temp.y * cfg.stiffness - (node.vel.y) * cfg.damping;

		node.applyForce(temp);
	}

	public void reset() {
		node.pos.set(pos);
		node.vel.set(0,0);
		node.acl.set(0,0);
	}
}
