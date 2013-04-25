package com.emptyPockets.gui.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class ExceptionHandeling extends Window{
	Button max;
	Button min;
	Button close;
	Table contentPane;
	
	public ExceptionHandeling(String title, Skin skin) {
		super(title, skin);
		createCompoents(skin);
		updateLayout();
	}

	private void createCompoents(Skin skin){
		min = new TextButton("_", skin);
		max = new TextButton("O", skin);
		close = new TextButton("X", skin);
		contentPane = new Table(skin);
	}
	
	private void updateLayout(){
		row();
		add().expandX().fillX();
		add(min);
		add(max);
		add(close);
		row();
		add(contentPane).expand().fill().colspan(4);
	}

	public Table getContentPane() {
		return contentPane;
	}

	public void setContentPane(Table contentPane) {
		this.contentPane = contentPane;
	}
}
