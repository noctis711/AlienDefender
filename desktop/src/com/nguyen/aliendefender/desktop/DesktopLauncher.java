package com.nguyen.aliendefender.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nguyen.aliendefender.AlienDefender;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new AlienDefender(), config);
		config.height = 720;
		config.width = 1280;
		config.title = "Alien Defender";
		config.fullscreen = true;
	}
}
