
package com.esotericsoftware.spine.attachments;

import com.esotericsoftware.spine.Attachment;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Slot;

import static com.badlogic.gdx.graphics.g2d.SpriteBatch.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.NumberUtils;

/** Attachment that displays a texture region. */
public class RegionAttachment extends Attachment {
	private TextureRegion region;
	private final float[] vertices = new float[20];
	private final float[] offset = new float[8];

	public RegionAttachment (String name) {
		super(name);
	}

	public void setOffset (float x, float y, float scaleX, float scaleY, float rotation) {
		if (!isResolved()) {
			// Stuff into offset until the region is available.
			float[] offset = this.offset;
			offset[0] = x;
			offset[1] = y;
			offset[2] = scaleX;
			offset[3] = scaleY;
			offset[4] = rotation;
		} else {
			float localX = -getWidth() * scaleX / 2;
			float localY = -getHeight() * scaleY / 2;
			float localX2 = -localX;
			float localY2 = -localY;
			float cos = MathUtils.cosDeg(rotation);
			float sin = MathUtils.sinDeg(rotation);
			float localXCos = localX * cos;
			float localXSin = localX * sin;
			float localYCos = localY * cos;
			float localYSin = localY * sin;
			float localX2Cos = localX2 * cos;
			float localX2Sin = localX2 * sin;
			float localY2Cos = localY2 * cos;
			float localY2Sin = localY2 * sin;
			offset[0] = x + localXCos - localYSin;
			offset[1] = y + localYCos + localXSin;
			offset[2] = x + localXCos - localY2Sin;
			offset[3] = y + localY2Cos + localXSin;
			offset[4] = x + localX2Cos - localY2Sin;
			offset[5] = y + localY2Cos + localX2Sin;
			offset[6] = x + localX2Cos - localYSin;
			offset[7] = y + localYCos + localX2Sin;
		}
	}

	void resolve (AtlasRegion region) {
		setRegion(region);
		setResolved(true);
		setOffset(offset[0], offset[1], offset[2], offset[3], offset[4]);
	}

	public void setRegion (TextureRegion region) {
		if (region == null) throw new IllegalArgumentException("region cannot be null.");
		this.region = region;
		float[] vertices = this.vertices;
		vertices[U1] = region.getU();
		vertices[V1] = region.getV2();
		vertices[U2] = region.getU();
		vertices[V2] = region.getV();
		vertices[U3] = region.getU2();
		vertices[V3] = region.getV();
		vertices[U4] = region.getU2();
		vertices[V4] = region.getV2();
	}

	public TextureRegion getRegion () {
		if (region == null) throw new IllegalStateException("RegionAttachment is not resolved: " + this);
		return region;
	}

	public void draw (SpriteBatch batch, Slot slot) {
		if (region == null) throw new IllegalStateException("RegionAttachment is not resolved: " + this);

		Color skeletonColor = slot.getSkeleton().getColor();
		Color slotColor = slot.getColor();
		float color = NumberUtils.intToFloatColor( //
			((int)(255 * skeletonColor.a * slotColor.a) << 24) //
				| ((int)(255 * skeletonColor.b * slotColor.b) << 16) //
				| ((int)(255 * skeletonColor.g * slotColor.g) << 8) //
				| ((int)(255 * skeletonColor.r * slotColor.r)));
		float[] vertices = this.vertices;
		vertices[C1] = color;
		vertices[C2] = color;
		vertices[C3] = color;
		vertices[C4] = color;

		updateWorldVertices(slot.getBone());

		batch.draw(region.getTexture(), vertices, 0, vertices.length);
	}

	public void updateWorldVertices (Bone bone) {
		float[] vertices = this.vertices;
		float[] offset = this.offset;
		float x = bone.getWorldX();
		float y = bone.getWorldY();
		float m00 = bone.getM00();
		float m01 = bone.getM01();
		float m10 = bone.getM10();
		float m11 = bone.getM11();
		vertices[X1] = offset[0] * m00 + offset[1] * m01 + x;
		vertices[Y1] = offset[0] * m10 + offset[1] * m11 + y;
		vertices[X2] = offset[2] * m00 + offset[3] * m01 + x;
		vertices[Y2] = offset[2] * m10 + offset[3] * m11 + y;
		vertices[X3] = offset[4] * m00 + offset[5] * m01 + x;
		vertices[Y3] = offset[4] * m10 + offset[5] * m11 + y;
		vertices[X4] = offset[6] * m00 + offset[7] * m01 + x;
		vertices[Y4] = offset[6] * m10 + offset[7] * m11 + y;
	}

	public float[] getWorldVertices () {
		return vertices;
	}

	public float getWidth () {
		return region.getRegionWidth();
	}

	public float getHeight () {
		return region.getRegionHeight();
	}
}
