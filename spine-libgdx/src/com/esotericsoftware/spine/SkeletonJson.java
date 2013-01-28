
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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.SerializationException;

public class SkeletonJson {
	static public final String FRAMES_SCALE = "scale";
	static public final String FRAMES_ROTATE = "rotate";
	static public final String FRAMES_TRANSLATE = "translate";
	static public final String FRAMES_ATTACHMENT = "attachment";
	static public final String FRAMES_COLOR = "color";

	static public final String ATTACHMENT_REGION = "region";
	static public final String ATTACHMENT_ANIMATED_REGION = "animatedRegion";

	private final Json json = new Json();
	private final AttachmentResolver attachmentResolver;

	public SkeletonJson (TextureAtlas atlas) {
		attachmentResolver = new TextureAtlasAttachmentResolver(atlas);
	}

	public SkeletonJson (AttachmentResolver attachmentResolver) {
		this.attachmentResolver = attachmentResolver;
	}

	public SkeletonData readSkeletonData (FileHandle file) {
		if (file == null) throw new IllegalArgumentException("file cannot be null.");

		SkeletonData skeletonData = new SkeletonData(attachmentResolver);

		OrderedMap<String, OrderedMap> root = json.fromJson(OrderedMap.class, file);

		// Bones.
		OrderedMap<String, OrderedMap> map = root.get("bones");
		for (Entry<String, OrderedMap> entry : map.entries()) {
			BoneData parent = null;
			String parentName = (String)entry.value.get("parent");
			if (parentName != null) {
				parent = skeletonData.findBone(parentName);
				if (parent == null) throw new SerializationException("Bone not found: " + parentName);
			}
			BoneData boneData = new BoneData((String)entry.key, parent);
			boneData.length = getFloat(entry.value, "length", 0);
			boneData.x = getFloat(entry.value, "x", 0);
			boneData.y = getFloat(entry.value, "y", 0);
			boneData.rotation = getFloat(entry.value, "rotation", 0);
			boneData.scaleX = getFloat(entry.value, "scaleX", 1);
			boneData.scaleY = getFloat(entry.value, "scaleY", 1);
			skeletonData.addBone(boneData);
		}

		// Slots.
		map = root.get("slots");
		if (map != null) {
			for (Entry<String, OrderedMap> entry : map.entries()) {
				String boneName = (String)entry.value.get("bone");
				BoneData boneData = skeletonData.findBone(boneName);
				if (boneData == null) throw new SerializationException("Bone not found: " + boneName);
				SlotData slotData = new SlotData(entry.key, boneData);

				String color = (String)entry.value.get("color");
				if (color != null) slotData.getColor().set(Color.valueOf(color));

				slotData.setAttachmentName((String)entry.value.get("attachment"));

				skeletonData.addSlot(slotData);
			}
		}

		// Skins.
		map = root.get("skins");
		if (map != null) {
			for (Entry<String, OrderedMap> entry : map.entries()) {
				Skin skin = new Skin(entry.key);
				for (Entry<String, OrderedMap> slotEntry : ((OrderedMap<String, OrderedMap>)entry.value).entries()) {
					int slotIndex = skeletonData.findSlotIndex(slotEntry.key);
					for (Entry<String, OrderedMap> attachmentEntry : ((OrderedMap<String, OrderedMap>)slotEntry.value).entries()) {
						Attachment attachment = readAttachment(attachmentEntry.key, attachmentEntry.value);
						skin.addAttachment(slotIndex, attachmentEntry.key, attachment);
					}
				}
				if (skin.name.equals("default"))
					skeletonData.setDefaultSkin(skin);
				else
					skeletonData.addSkin(skin);
			}
		}

		skeletonData.bones.shrink();
		skeletonData.slots.shrink();
		skeletonData.skins.shrink();
		return skeletonData;
	}

	private Attachment readAttachment (String name, OrderedMap map) {
		name = (String)map.get("name", name);
		Attachment attachment;
		String type = (String)map.get("type");
		if (type == null) type = ATTACHMENT_REGION;
		if (type.equals(ATTACHMENT_REGION)) {
			attachment = new RegionAttachment(name);

		} else if (type.equals(ATTACHMENT_ANIMATED_REGION)) {
			Float fps = (Float)map.get("fps");
			if (fps == null) throw new SerializationException("Animation attachment missing fps: " + name);

			String playModeString = (String)map.get("playMode");
			PlayMode playMode = playModeString == null ? PlayMode.forward : PlayMode.valueOf(playModeString);

			attachment = new AnimatedRegionAttachment(name, 1 / fps, playMode);

		} else
			throw new SerializationException("Unknown attachment type: " + type + " (" + name + ")");

		float x = getFloat(map, "x", 0);
		float y = getFloat(map, "y", 0);
		float scaleX = getFloat(map, "scaleX", 1);
		float scaleY = getFloat(map, "scaleY", 1);
		float rotation = getFloat(map, "rotation", 0);
		attachment.setOffset(x, y, scaleX, scaleY, rotation);
		return attachment;
	}

	private float getFloat (OrderedMap map, String name, float defaultValue) {
		Object value = map.get(name);
		if (value == null) return defaultValue;
		return getFloat(value);
	}

	private float getFloat (Object value) {
		if (value instanceof Float) return (Float)value;
		return Float.parseFloat((String)value);
	}

	public Animation readAnimation (FileHandle file, SkeletonData skeleton) {
		if (file == null) throw new IllegalArgumentException("file cannot be null.");
		if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null.");

		OrderedMap<String, ?> map = json.fromJson(OrderedMap.class, file);

		Array allFrames = new Array();
		float duration = 0;

		OrderedMap<String, ?> bonesMap = (OrderedMap)map.get("bones");
		for (Entry<String, ?> entry : bonesMap.entries()) {
			String boneName = entry.key;
			int boneIndex = skeleton.findBoneIndex(boneName);
			if (boneIndex == -1) throw new SerializationException("Bone not found: " + boneName);
			OrderedMap<?, ?> propertyMap = (OrderedMap)entry.value;

			for (Entry propertyEntry : propertyMap.entries()) {
				OrderedMap<?, ?> frameMap = (OrderedMap)propertyEntry.value;
				String framesType = (String)propertyEntry.key;
				if (framesType.equals(FRAMES_ROTATE)) {
					RotateFrames frames = new RotateFrames(frameMap.size);
					frames.setBoneIndex(boneIndex);

					int keyframeIndex = 0;
					for (Entry frameEntry : frameMap.entries()) {
						OrderedMap valueMap = (OrderedMap)frameEntry.value;
						float time = Float.parseFloat((String)frameEntry.key);
						frames.setKeyframe(keyframeIndex, time, getFloat(valueMap.get("angle")));
						readCurve(frames, keyframeIndex, valueMap);
						keyframeIndex++;
					}
					allFrames.add(frames);
					duration = Math.max(duration, frames.getDuration());

				} else if (framesType.equals(FRAMES_TRANSLATE) || framesType.equals(FRAMES_SCALE)) {
					TranslateFrames frames;
					if (framesType.equals(FRAMES_SCALE))
						frames = new ScaleFrames(frameMap.size);
					else
						frames = new TranslateFrames(frameMap.size);
					frames.setBoneIndex(boneIndex);

					int keyframeIndex = 0;
					for (Entry frameEntry : frameMap.entries()) {
						OrderedMap valueMap = (OrderedMap)frameEntry.value;
						Float x = getFloat(valueMap.get("x")), y = getFloat(valueMap.get("y"));
						float time = Float.parseFloat((String)frameEntry.key);
						frames.setKeyframe(keyframeIndex, time, x == null ? 0 : x, y == null ? 0 : y);
						readCurve(frames, keyframeIndex, valueMap);
						keyframeIndex++;
					}
					allFrames.add(frames);
					duration = Math.max(duration, frames.getDuration());
				} else
					throw new RuntimeException("Invalid frame type for a bone: " + framesType + " (" + boneName + ")");
			}
		}

		OrderedMap<String, ?> slotsMap = (OrderedMap)map.get("slots");
		if (slotsMap != null) {
			for (Entry<String, ?> entry : slotsMap.entries()) {
				String slotName = entry.key;
				int slotIndex = skeleton.findSlotIndex(slotName);
				OrderedMap<?, ?> propertyMap = (OrderedMap)entry.value;

				for (Entry propertyEntry : propertyMap.entries()) {
					OrderedMap<?, ?> frameMap = (OrderedMap)propertyEntry.value;
					String framesType = (String)propertyEntry.key;
					if (framesType.equals(FRAMES_COLOR)) {
						ColorFrames frames = new ColorFrames(frameMap.size);
						frames.setSlotIndex(slotIndex);

						int keyframeIndex = 0;
						for (Entry frameEntry : frameMap.entries()) {
							float time = Float.parseFloat((String)frameEntry.key);
							OrderedMap valueMap = (OrderedMap)frameEntry.value;
							Color color = Color.valueOf((String)valueMap.get("color"));
							frames.setKeyframe(keyframeIndex, time, color.r, color.g, color.b, color.a);
							readCurve(frames, keyframeIndex, valueMap);
							keyframeIndex++;
						}
						allFrames.add(frames);
						duration = Math.max(duration, frames.getDuration());
						break;
					} else if (framesType.equals(FRAMES_ATTACHMENT)) {
						AttachmentFrames frames = new AttachmentFrames(frameMap.size);
						frames.setSlotIndex(slotIndex);

						int keyframeIndex = 0;
						for (Entry frameEntry : frameMap.entries())
							frames.setKeyframe(keyframeIndex++, Float.parseFloat((String)frameEntry.key), (String)frameEntry.value);
						allFrames.add(frames);
						duration = Math.max(duration, frames.getDuration());
					} else
						throw new RuntimeException("Invalid frame type for a slot: " + framesType + " (" + slotName + ")");
				}
			}
		}

		allFrames.shrink();
		return new Animation(allFrames);
	}

	private void readCurve (CurveFrames frames, int keyframeIndex, OrderedMap valueMap) {
		Object curveObject = valueMap.get("curve");
		if (curveObject == null) return;
		if (curveObject.equals("stepped"))
			frames.setStepped(keyframeIndex);
		else if (curveObject instanceof Array) {
			Array curve = (Array)curveObject;
			frames.setCurve(keyframeIndex, getFloat(curve.get(0)), getFloat(curve.get(1)), getFloat(curve.get(2)),
				getFloat(curve.get(3)));
		}
	}
}
