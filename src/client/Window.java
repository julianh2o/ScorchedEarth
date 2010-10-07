package client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Window {
	private DisplayMode	mode;
	boolean fullscreen;
	List<Model> models;
	
	public Window() {
		windowed();
		try {
			Display.create();
			glInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		models = new ArrayList<Model>();
		models.add(new Model());
	}
	
	public boolean shouldExit() {
		return Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested();
	}
	
	public void doRender(Screen screen) {
		if (Display.isVisible()) {
			processKeyboard();
			screen.render(this);
		}
		Display.update();
	}
	
	public void fullscreen() {
		fullscreen = true;
		try {
			mode = findDisplayMode(800, 600, Display.getDisplayMode().getBitsPerPixel());
			Display.setDisplayModeAndFullscreen(mode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void windowed() {
		fullscreen = false;
		try {
			mode = new DisplayMode(640, 480);
			Display.setDisplayModeAndFullscreen(mode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void processKeyboard() {
		if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
			if (fullscreen) 
				windowed();
			else
				fullscreen();
		}
		
		while ( Mouse.next() );
	}
	
	private void glInit() {
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluOrtho2D(0, mode.getWidth(), 0, mode.getHeight());
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glViewport(0, 0, mode.getWidth(), mode.getHeight());
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Display.setVSyncEnabled(true);
	}
	
	private DisplayMode findDisplayMode(int width, int height, int bpp) throws LWJGLException {
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		for ( DisplayMode mode : modes ) {
			//Log.p.out("Display Mode: "+mode.toString());
			if ( mode.getWidth() == width && mode.getHeight() == height && mode.getBitsPerPixel() >= bpp && mode.getFrequency() <= 60 ) {
				return mode;
			}
		}
		return Display.getDesktopDisplayMode();
	}
	
	public void cleanup() {
		Display.destroy();
	}
	
	public Model getModel(int id) {
		return models.get(id);
	}
}
