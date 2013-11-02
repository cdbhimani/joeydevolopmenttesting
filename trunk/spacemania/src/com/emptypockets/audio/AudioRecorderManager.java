package com.emptypockets.audio;

import java.util.ArrayList;

import com.badlogic.gdx.audio.AudioRecorder;

public class AudioRecorderManager implements Runnable {
	AudioRecorder recorder;

	int sec = 5;
	int samples = 44100;
	boolean isMono = true;

	ArrayList<AudioMessage> waitingMessages = new ArrayList<AudioMessage>();

	Object recordingLock = new Object();
	int recordMessageSequence = 0;

	boolean alive = true;
	boolean recording = false;

	public AudioRecorderManager() {
		addMessages(5);
	}

	private void addMessages(int count) {
		synchronized (waitingMessages) {
			for (int i = 0; i < count; i++) {
				AudioMessage message = new AudioMessage();
				message.sequence = 0;
				message.data = new short[1];
				waitingMessages.add(message);
			}
		}
	}

	public void startRecording() {
		synchronized (recordingLock) {
			if (!recording) {
				recording = true;
				recordingLock.notifyAll();
				recordMessageSequence = 0;
			}
		}
	}

	public void stopRecording() {
		synchronized (recordingLock) {
			recording = false;
			recordingLock.notifyAll();
		}
	}

	private AudioMessage getNextMessage() {
		int attempts = 50;
		synchronized (waitingMessages) {
			while (attempts-- > 0) {
				if (waitingMessages.size() > 0) {
					return waitingMessages.get(0);
				} else {
					try {
						waitingMessages.wait();
					} catch (InterruptedException e) {
					}
				}
			}
		}
		throw new RuntimeException("getNextMessage never recieved a message");
	}

	private void sendMessage(AudioMessage message) {
		synchronized (waitingMessages) {
			waitingMessages.add(message);
			waitingMessages.notifyAll();
		}
	}

	private void record(AudioMessage message) {
		if (message.data.length != sec * samples) {
			message.data = new short[sec * samples];
		}
		message.sequence = recordMessageSequence;
		recordMessageSequence++;
		recorder.read(message.data, 0, samples * sec);
	}

	public void run() {
		while (alive) {
			synchronized (recordingLock) {
				if (recording) {
					AudioMessage message = getNextMessage();
					record(message);
					sendMessage(message);
				} else {
					try {
						recordingLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
