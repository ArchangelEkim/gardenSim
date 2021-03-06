package archangel.aosora.util;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;



public class Window {
	
	private int width, height;
	private String title;
	private long glfwWindow;
	
	public static Window window;

	private Window() {
		this.width = 1920;
		this.height = 1080;
		this.title = "Garden";
	}
	
	public static Window getInstance() {
		if (Window.window == null) {
			Window.window = new Window();
		}
		
		return window;
	}
	
	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		
		init();
		loop();
		
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialise GLFW. Most GLFW functions will not work before this.
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialise GLFW");
		}
		
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE); // the window will be maximised
		
		// Create the window
		glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
		if (glfwWindow == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		
		// Setup a cursor callback. It will be called every time the mouse is moved.
		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		// Setup a mouse button callback. It will be called every time the mouse is clicked.
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		// Setup a mouse scroll callback. It will be called every time the mousewheel is moved.
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		
		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*
			
			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(glfwWindow, pWidth, pHeight);
			
			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			// Center the window
			glfwSetWindowPos(
					glfwWindow, 
					(vidmode.width() - pWidth.get(0)) / 2, 
					(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(glfwWindow);
		// Enable v-sync
		glfwSwapInterval(1);
		
		// Make the window visible
		glfwShowWindow(glfwWindow);
		
	}
	
	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGl context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGl
		// binding available for use.
		GL.createCapabilities();
		
		// Set the clear color
		glClearColor(1.0f, 0.5f, 0.0f, 0.0f);
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (!glfwWindowShouldClose(glfwWindow)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the frame buffer
			
			glfwSwapBuffers(glfwWindow); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

}
