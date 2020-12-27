/**
 * 
 */
package archangel.aosora.util;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Arrays;

/**
 * @author Michael Clayden
 *
 */
public class MouseListener {
	
	private static final boolean base[] = new boolean[GLFW_MOUSE_BUTTON_LAST + 1];
	
	private static MouseListener instance;
	private double scrollX, scrollY;
	private double xPos, yPos, lastY, lastX;
	private boolean mouseButtonPressed[] = new boolean[GLFW_MOUSE_BUTTON_LAST + 1];
	private boolean isDragging;
	
	private MouseListener() {
		this.scrollX = 0.0;
		this.scrollY = 0.0;
		this.xPos = 0.0;
		this.yPos = 0.0;
		this.lastX = 0.0;
		this.lastY = 0.0;
	}
	
	public static MouseListener getInstance() {
		if (instance == null) {
			instance = new MouseListener();
		}
		
		return instance;
	}
	
	public static void mousePosCallback(long window, double xpos, double ypos) {
		getInstance().lastX = getInstance().xPos;
		getInstance().lastY = getInstance().yPos;
		getInstance().xPos = xpos;
		getInstance().yPos = ypos;
		getInstance().isDragging = Arrays.mismatch(base, getInstance().mouseButtonPressed) != -1;
	}
	
	public static void mouseButtonCallback(long window, int button, int action, int mods) {
		if (action == GLFW_PRESS) {
			getInstance().mouseButtonPressed[button] = true;
		} else if (action == GLFW_RELEASE) {
			getInstance().mouseButtonPressed[button] = false;
			getInstance().isDragging = false;
		}
	}
	
	public static void mouseScrollCallback(long window, double xoffset, double yoffset) {
		getInstance().scrollX = xoffset;
		getInstance().scrollY = yoffset;
	}
	
	public static void endFrame() {
		getInstance().scrollX = 0.0;
		getInstance().scrollY = 0.0;
		getInstance().lastX = getInstance().xPos;
		getInstance().lastY = getInstance().yPos;
	}

	public static float getX() {
		return (float)getInstance().xPos;
	}

	public static float getY() {
		return (float)getInstance().yPos;
	}
	
	public static float getDx() {
		return (float)(getInstance().lastX - getInstance().xPos);
	}
	
	public static float getDy() {
		return (float)(getInstance().lastY - getInstance().yPos);
	}
	
	public static float getScrollX() {
		return (float) getInstance().scrollX;
	}
	
	public static float getScrollY() {
		return (float) getInstance().scrollY;
	}
	
	public static boolean isDragging() {
		return getInstance().isDragging;
	}
	
	public static boolean mouseButtonDown(int button) {
		if (button < getInstance().mouseButtonPressed.length) {
			return getInstance().mouseButtonPressed[button];
		} else {
			return false;
		}
	}
	
	
}
