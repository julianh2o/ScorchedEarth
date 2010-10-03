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
	
	public Window() {
		fullscreen = true;
		fullscreen();
		
		try {
		Display.create();
		glInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean shouldExit() {
		return Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested();
	}
	
	public void update() {
		if (Display.isVisible()) {
			processKeyboard();
			
			render();
		}
		Display.update();
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		
		glPushMatrix();
		{
			glTranslatef(10,10, 0);
			glRotatef(20, 0.0f, 0.0f, 1.0f);
			glColor3f(1.0f, 1.0f, 1.0f);
			glBegin(GL_QUADS);
			{
				glVertex2i(-50, -50);
				glVertex2i(50, -50);
				glVertex2i(50, 50);
				glVertex2i(-50, 50);
			}
			glEnd();
		}
		glPopMatrix();
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
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
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
}
