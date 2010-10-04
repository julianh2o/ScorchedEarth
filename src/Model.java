import static org.lwjgl.opengl.GL11.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Model {
	Texture texture;
	public Model() {
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("resources/tank.png"));
		} catch (FileNotFoundException e) {
			Log.p.out("File Not Found when loading texture");
			e.printStackTrace();
		} catch (IOException e) {
			Log.p.out("Error Loading Texture");
			e.printStackTrace();
		} 
	}
	
	public void renderAt(Window w, double x, double y) {
		glPushMatrix();
		{
			glTranslated(x, y, 0);
			render(w);
		}
		glPopMatrix();
	}
	
	public void render(Window w) {
		glColor3f(1.0f, 1.0f, 1.0f);
		texture.bind();
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0.0f, 0.0f);
			glVertex2i(-50, -50);
			
			glTexCoord2f(1.0f, 0.0f);
			glVertex2i(50, -50);
			
			glTexCoord2f(1.0f, 1.0f);
			glVertex2i(50, 50);
			
			glTexCoord2f(0.0f, 1.0f);
			glVertex2i(-50, 50);
		}
		glEnd();
	}
}
