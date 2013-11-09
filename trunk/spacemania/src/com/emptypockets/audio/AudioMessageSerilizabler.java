package com.emptypockets.audio;

import java.util.ArrayList;

import com.emptyPockets.utils.ObjectPool;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class AudioMessageSerilizabler extends Serializer<AudioMessage> {

	ObjectPool<AudioMessage> messagePool;
	
	public AudioMessageSerilizabler(int pool){
		messagePool = new ObjectPool<AudioMessage>();
		
		addToPool(pool);
	}
	
	public void addToPool(int count){
		for(int i = 0; i < count; i++){
			messagePool.release(new AudioMessage());
		}
	}
	
	@Override
	public void write(Kryo kryo, Output output, AudioMessage object) {
		output.writeInt(object.sequence);
		output.writeInt(object.sampleFreq);
		output.writeInt(object.data.length);
		output.writeShorts(object.data);
	}

	@Override
	public AudioMessage read(Kryo kryo, Input input, Class<AudioMessage> type) {
		AudioMessage message = messagePool.getNext();
		message.sequence = input.readInt();
		message.sampleFreq= input.readInt();
		int dataSize = input.readInt();
		
		if(message.data == null || message.data.length != dataSize){
			for(int i= 0; i < dataSize; i++){
				message.data[i] = input.readShort();
			}
		}
		
		return message;
	}

	
}
