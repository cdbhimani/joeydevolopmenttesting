
package com.esotericsoftware.spine.attachments;

import com.esotericsoftware.spine.Slot;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

// BOZO - AnimatedRegionAttachment doesn't support regions with a different size.

/** Attachment that displays various texture regions over time. */
public class AnimatedRegionAttachment extends RegionAttachment {
	private final PlayMode playMode;
	private float frameTime;
	private TextureRegion[] keyFrames;

	/** @param frameTime Time in seconds each frame is shown. */
	public AnimatedRegionAttachment (String name, float frameTime, PlayMode playMode) {
		super(name);
		if (playMode == null) throw new IllegalArgumentException("playMode cannot be null.");

		this.frameTime = frameTime;
		this.playMode = playMode;
	}

	public void draw (SpriteBatch batch, Slot slot) {
		if (keyFrames == null) throw new IllegalStateException("AnimatedRegionAttachment is not resolved: " + this);

		int frameIndex = (int)(slot.getAttachmentTime() / frameTime);
		switch (playMode) {
		case forward:
			frameIndex = Math.min(keyFrames.length - 1, frameIndex);
			break;
		case forwardLoop:
			frameIndex = frameIndex % keyFrames.length;
			break;
		case pingPong:
			frameIndex = frameIndex % (keyFrames.length * 2);
			if (frameIndex >= keyFrames.length) frameIndex = keyFrames.length - 1 - (frameIndex - keyFrames.length);
			break;
		case random:
			frameIndex = MathUtils.random(keyFrames.length - 1);
			break;
		case backward:
			frameIndex = Math.max(keyFrames.length - frameIndex - 1, 0);
			break;
		case backwardLoop:
			frameIndex = frameIndex % keyFrames.length;
			frameIndex = keyFrames.length - frameIndex - 1;
			break;
		}
		setRegion(keyFrames[frameIndex]);
		super.draw(batch, slot);
	}

	/** May be null if the attachment is not resolved. */
	public TextureRegion[] getKeyFrames () {
		if (keyFrames == null) throw new IllegalStateException("AnimatedRegionAttachment is not resolved: " + this);
		return keyFrames;
	}

	public void setKeyFrames (TextureRegion[] keyFrames) {
		this.keyFrames = keyFrames;
	}

	static public enum PlayMode {
		forward, backward, forwardLoop, backwardLoop, pingPong, random
	}
}
