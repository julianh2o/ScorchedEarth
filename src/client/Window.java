package client;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

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
		models.add(new Model("resources/tank2.png","resources/barrel.png",1F,1F));
		models.add(new Model("resources/grass.png",1F,1F));
		models.add(new Model("resources/dirt.png",1F,1F));
		models.add(new Model("resources/block.png",1F,1F));
		models.add(new Model("resources/pink.png","resources/barrel.png",1F,1F));
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
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluOrtho2D(0, mode.getWidth()/30F, 0, mode.getHeight()/30F);
		
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

	public View getView() {
		return new View(0,0,mode.getWidth()/20F,mode.getHeight()/20F,mode.getWidth(),mode.getHeight());
	}
}
