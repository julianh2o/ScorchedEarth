package client;

import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.util.glu.GLU.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {
	DisplayMode	mode;
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
		//0
		models.add(new Model("resources/tank2.png","resources/barrel.png",1F,1F));
		
		//1
		models.add(new Model("resources/grass.png",1F,1F));
		
		//2
		models.add(new Model("resources/dirt.png",1F,1F));
		
		//3
		models.add(new Model("resources/block.png",1F,1F));
		
		//4
		models.add(new Model("resources/pink.png","resources/barrel.png",1F,1F));
		
		//5
		models.add(new Model("resources/bullet.png",1F,1F));
	}
	
	public boolean shouldExit() {
		return Display.isCloseRequested();
	}
	
	public void doRender(Screen screen) {
		if (Display.isVisible()) {
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
	
	private void glInit() {
		
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
