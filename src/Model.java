import static org.lwjgl.opengl.GL11.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Model {
	Texture texture;
	private float halfWidth;
	private float halfHeight;
	
	public Model() {
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("resources/tank.png"));
			halfWidth = texture.getTextureWidth()/2;
			halfHeight = texture.getTextureHeight()/2;
			Log.p.out("halfWidth: "+halfWidth);
			Log.p.out("halfHeight: "+halfHeight);
		} catch (FileNotFoundException e) {
			Log.p.out("File Not Found when loading texture");
			e.printStackTrace();
		} catch (IOException e) {
			Log.p.out("Error Loading Texture");
			e.printStackTrace();
		} 
	}
	
	public void renderAt(Window w, double x, double y, double angle) {
		glPushMatrix();
		{
		
			
			glTranslated(x, y, 0);
			glRotatef((float)angle, 0, 0, 1);
			
			glColor3f(1.0f, 1.0f, 1.0f);
			glEnd();
			glBegin(GL_TRIANGLES);
			{
				glVertex2i(-10,0);
				glVertex2i(10,0);
				glVertex2i(0,50);
			}
			glEnd();
			
			render(w);
		}
		glPopMatrix();
	}
	
	public void render(Window w) {
		
		glColor3f(1.0f, 1.0f, 1.0f);
		
		halfWidth += 3;
		halfHeight += 3;
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0.0f, 0.0f);
			glVertex2f(-halfWidth, -halfHeight);
			
			glTexCoord2f(1.0f, 0.0f);
			glVertex2f(halfWidth, -halfHeight);
			
			glTexCoord2f(1.0f, 1.0f);
			glVertex2f(halfWidth, halfHeight);
			
			glTexCoord2f(0.0f, 1.0f);
			glVertex2f(-halfWidth, halfHeight);
		}
		glEnd();
		
		halfWidth -= 3;
		halfHeight -= 3;
		
		
		glEnable(GL_TEXTURE_2D);
		texture.bind();
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0.0f, 0.0f);
			glVertex2f(-halfWidth, -halfHeight);
			
			glTexCoord2f(1.0f, 0.0f);
			glVertex2f(halfWidth, -halfHeight);
			
			glTexCoord2f(1.0f, 1.0f);
			glVertex2f(halfWidth, halfHeight);
			
			glTexCoord2f(0.0f, 1.0f);
			glVertex2f(-halfWidth, halfHeight);
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}
}
