package archangel.aosora.util;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
	
	private static KeyListener instance;
	private boolean keyPressed[] = new boolean[GLFW_KEY_LAST + 2];

	private KeyListener() {
		// TODO Auto-generated constructor stub
	}
	
	private static KeyListener getInstance() {
		if(instance == null) {
			KeyListener.instance = new KeyListener();
		}
		
		return instance;
	}
	
	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS) {
			getInstance().keyPressed[key] = true;
		} else if (action == GLFW_RELEASE) {
			getInstance().keyPressed[key] = false;
		}
	}

	public static boolean isKeyPressed(int keyCode) {
		return getInstance().keyPressed[keyCode];
	}
	
	
}
