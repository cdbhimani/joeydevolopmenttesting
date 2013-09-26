package com.emptypockets.audio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptyPockets.gui.ScreenSizeHelper;
import com.emptyPockets.gui.StageScreen;

public class AudioMessageScreen extends StageScreen {

	ExecutorService executor = Executors.newFixedThreadPool(1);
	AudioRecorder recorder;
	AudioDevice player;

	TextButton recButton;
	TextButton playButton;

	int sec = 5;
	int samples = 44100;
	boolean isMono = true;

	short[] data = new short[sec * samples];

	public AudioMessageScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);

		recorder = Gdx.audio.newAudioRecorder(samples, isMono);
		player = Gdx.audio.newAudioDevice(samples, isMono);
	}

	@Override
	public void createStage(Stage stage) {
		recButton = new TextButton("Start", getSkin());
		playButton = new TextButton("Stop", getSkin());

		Table layout = new Table(getSkin());
		layout.row();
		layout.add(recButton).size(ScreenSizeHelper.getcmtoPxlX(1), ScreenSizeHelper.getcmtoPxlY(1));
		layout.add().fillX().expandX();
		layout.add(playButton).size(ScreenSizeHelper.getcmtoPxlX(1), ScreenSizeHelper.getcmtoPxlY(1));

		layout.row();
		layout.add();
		layout.add().expand().fill();
		layout.add();

		layout.setFillParent(true);

		stage.addActor(layout);

		recButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				executor.submit(new Runnable() {
					@Override
					public void run() {
						recButton.setText("WAIT");
						System.out.println("Record: Start");
						recorder.read(data, 0, data.length);
						System.out.println("Record: End");
						recButton.setText("Start");
					}
				});
			}
		});

		playButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				executor.submit(new Runnable() {
					@Override
					public void run() {
						System.out.println("Play : Start");
						player.writeSamples(data, samples, data.length);
						System.out.println("Play : End");
					}
				});
			}
		});
	}

	@Override
	public void drawBackground(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawScreen(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawOverlay(float delta) {
		// TODO Auto-generated method stub

	}

}