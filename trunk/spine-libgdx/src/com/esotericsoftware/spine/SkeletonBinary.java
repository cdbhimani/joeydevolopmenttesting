
package com.esotericsoftware.spine;

import com.esotericsoftware.spine.Animation.AttachmentFrames;
import com.esotericsoftware.spine.Animation.ColorFrames;
import com.esotericsoftware.spine.Animation.CurveFrames;
import com.esotericsoftware.spine.Animation.RotateFrames;
import com.esotericsoftware.spine.Animation.ScaleFrames;
import com.esotericsoftware.spine.Animation.TranslateFrames;
import com.esotericsoftware.spine.attachments.AnimatedRegionAttachment;
import com.esotericsoftware.spine.attachments.AnimatedRegionAttachment.PlayMode;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.esotericsoftware.spine.attachments.TextureAtlasAttachmentResolver;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.SerializationException;

import java.io.IOException;

public class SkeletonBinary {
	static public final int FRAMES_SCALE = 0;
	static public final int FRAMES_ROTATE = 1;
	static public final int FRAMES_TRANSLATE = 2;
	static public final int FRAMES_ATTACHMENT = 3;
	static public final int FRAMES_COLOR = 4;

	static public final int ATTACHMENT_REGION = 0;
	static public final int ATTACHMENT_ANIMATED_REGION = 1;

	static public final int CURVE_LINEAR = 0;
	static public final int CURVE_STEPPED = 1;
	static public final int CURVE_BEZIER = 2;

	private final AttachmentResolver attachmentResolver;

	public SkeletonBinary (TextureAtlas atlas) {
		attachmentResolver = new TextureAtlasAttachmentResolver(atlas);
	}

	public SkeletonBinary (AttachmentResolver attachmentResolver) {
		this.attachmentResolver = attachmentResolver;
	}

	public SkeletonData readSkeletonData (FileHandle file) {
		if (file == null) throw new IllegalArgumentException("file cannot be null.");

		SkeletonData skeletonData = new SkeletonData(attachmentResolver);
		DataInput input = new DataInput(file.read(512));
		try {
			// Bones.
			for (int i = 0, n = input.readInt(true); i < n; i++) {
				String name = input.readString();
				BoneData parent = null;
				String parentName = input.readString();
				if (parentName != null) {
					parent = skeletonData.findBone(parentName);
					if (parent == null) throw new SerializationException("Bone not found: " + parentName);
				}
				BoneData boneData = new BoneData(name, parent);
				boneData.x = input.readFloat();
				boneData.y = input.readFloat();
				boneData.scaleX = input.readFloat();
				boneData.scaleY = input.readFloat();
				boneData.rotation = input.readFloat();
				boneData.length = input.readFloat();
				skeletonData.addBone(boneData);
			}

			// Slots.
			for (int i = 0, n = input.readInt(true); i < n; i++) {
				String slotName = input.readString();
				String boneName = input.readString();
				BoneData boneData = skeletonData.findBone(boneName);
				if (boneData == null) throw new SerializationException("Bone not found: " + boneName);
				SlotData slotData = new SlotData(slotName, boneData);
				Color.rgba8888ToColor(slotData.getColor(), input.readInt());
				slotData.setAttachmentName(input.readString());
				skeletonData.addSlot(slotData);
			}

			// Default skin.
			skeletonData.setDefaultSkin(readSkin(input, "default"));

			// Skins.
			for (int i = 0, n = input.readInt(true); i < n; i++)
				skeletonData.addSkin(readSkin(input, input.readString()));

			input.close();
		} catch (IOException ex) {
			throw new SerializationException("Error reading skeleton file.", ex);
		}

		skeletonData.bones.shrink();
		skeletonData.slots.shrink();
		skeletonData.skins.shrink();
		return skeletonData;
	}

	private Skin readSkin (DataInput input, String skinName) throws IOException {
		int slotCount = input.readInt(true);
		if (slotCount == 0) return null;
		Skin skin = new Skin(skinName);
		for (int i = 0; i < slotCount; i++) {
			int slotIndex = input.readInt(true);
			int attachmentCount = input.readInt(true);
			for (int ii = 0; ii < attachmentCount; ii++) {
				String name = input.readString();
				skin.addAttachment(slotIndex, name, readAttachment(input, name));
			}
		}
		return skin;
	}

	private Attachment readAttachment (DataInput input, String attachmentName) throws IOException {
		String name = input.readString();
		if (name == null) name = attachmentName;

		Attachment attachment;
		int type = input.readByte();
		switch (type) {
		case ATTACHMENT_REGION:
			attachment = new RegionAttachment(name);
			break;
		case ATTACHMENT_ANIMATED_REGION:
			float fps = input.readFloat();
			PlayMode playMode = PlayMode.values()[input.readInt(true)];
			attachment = new AnimatedRegionAttachment(name, 1 / fps, playMode);
			break;
		default:
			throw new SerializationException("Unknown attachment type: " + type + " (" + name + ")");
		}

		attachment.setOffset(input.readFloat(), input.readFloat(), input.readFloat(), input.readFloat(), input.readFloat());
		return attachment;
	}

	public Animation readAnimation (FileHandle file, SkeletonData skeleton) {
		if (file == null) throw new IllegalArgumentException("file cannot be null.");
		if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null.");

		Array allFrames = new Array();
		float duration = 0;

		DataInput input = new DataInput(file.read(512));
		try {
			int boneCount = input.readInt(true);
			for (int i = 0; i < boneCount; i++) {
				String boneName = input.readString();
				int boneIndex = skeleton.findBoneIndex(boneName);
				if (boneIndex == -1) throw new SerializationException("Bone not found: " + boneName);
				int itemCount = input.readInt(true);
				for (int ii = 0; ii < itemCount; ii++) {
					int framesType = input.readByte();
					int keyCount = input.readInt(true);
					switch (framesType) {
					case FRAMES_ROTATE: {
						RotateFrames frames = new RotateFrames(keyCount);
						frames.setBoneIndex(boneIndex);
						for (int keyframeIndex = 0; keyframeIndex < keyCount; keyframeIndex++) {
							frames.setKeyframe(keyframeIndex, input.readFloat(), input.readFloat());
							if (keyframeIndex < keyCount - 1) readCurve(input, keyframeIndex, frames);
						}
						allFrames.add(frames);
						duration = Math.max(duration, frames.getDuration());
						break;
					}
					case FRAMES_TRANSLATE:
					case FRAMES_SCALE:
						TranslateFrames frames;
						if (framesType == FRAMES_SCALE)
							frames = new ScaleFrames(keyCount);
						else
							frames = new TranslateFrames(keyCount);
						frames.setBoneIndex(boneIndex);
						for (int keyframeIndex = 0; keyframeIndex < keyCount; keyframeIndex++) {
							frames.setKeyframe(keyframeIndex, input.readFloat(), input.readFloat(), input.readFloat());
							if (keyframeIndex < keyCount - 1) readCurve(input, keyframeIndex, frames);
						}
						allFrames.add(frames);
						duration = Math.max(duration, frames.getDuration());
						break;
					default:
						throw new RuntimeException("Invalid frame type for a bone: " + framesType + " (" + boneName + ")");
					}
				}
			}

			int slotCount = input.readInt(true);
			for (int i = 0; i < slotCount; i++) {
				String slotName = input.readString();
				int slotIndex = skeleton.findSlotIndex(slotName);
				int itemCount = input.readInt(true);
				for (int ii = 0; ii < itemCount; ii++) {
					int framesType = input.readByte();
					int keyCount = input.readInt(true);
					switch (framesType) {
					case FRAMES_COLOR: {
						ColorFrames frames = new ColorFrames(keyCount);
						frames.setSlotIndex(slotIndex);
						for (int keyframeIndex = 0; keyframeIndex < keyCount; keyframeIndex++) {
							float time = input.readFloat();
							Color.rgba8888ToColor(Color.tmp, input.readInt());
							frames.setKeyframe(keyframeIndex, time, Color.tmp.r, Color.tmp.g, Color.tmp.b, Color.tmp.a);
							if (keyframeIndex < keyCount - 1) readCurve(input, keyframeIndex, frames);
						}
						allFrames.add(frames);
						duration = Math.max(duration, frames.getDuration());
						break;
					}
					case FRAMES_ATTACHMENT:
						AttachmentFrames frames = new AttachmentFrames(keyCount);
						frames.setSlotIndex(slotIndex);
						for (int keyframeIndex = 0; keyframeIndex < keyCount; keyframeIndex++)
							frames.setKeyframe(keyframeIndex, input.readFloat(), input.readString());
						allFrames.add(frames);
						duration = Math.max(duration, frames.getDuration());
						break;
					}
				}
			}
		} catch (IOException ex) {
			throw new SerializationException("Error reading skeleton file.", ex);
		}

		allFrames.shrink();
		return new Animation(allFrames);
	}

	private void readCurve (DataInput input, int keyframeIndex, CurveFrames frames) throws IOException {
		switch (input.readByte()) {
		case CURVE_STEPPED:
			frames.setStepped(keyframeIndex);
			break;
		case CURVE_BEZIER:
			frames.setCurve(keyframeIndex, input.readFloat(), input.readFloat(), input.readFloat(), input.readFloat());
			break;
		}
	}
}
