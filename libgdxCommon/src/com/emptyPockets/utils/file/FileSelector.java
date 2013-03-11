package com.emptyPockets.utils.file;

import com.badlogic.gdx.files.FileHandle;

public interface FileSelector {
	public FileHandle getSelectedFile();
	public void displayGUI();
}
