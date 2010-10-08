package client;

import static org.lwjgl.opengl.GL11.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import common.util.Log;
import common.util.Vector2D;

public class Model {
	Texture texture;
	private float halfWidth;
	private float halfHeight;
	
	public Model(String path) {
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream(path));
			halfWidth = texture.getTextureWidth()/2;
			halfHeight = texture.getTextureHeight()/2;
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
			glScaled(.5, .5, .5);
			
//			Vector2D heading = new Vector2D(angle);
//			glBegin(GL_LINES);
//			{
//				glVertex2i(0,0);
//				glVertex2d(50*heading.getX(),50*heading.getY());
//			}
//			glEnd();
//			
			glRotatef((float)Math.toDegrees(angle), 0, 0, 1);
//			
//			glColor3f(1.0f, 1.0f, 1.0f);
//			glBegin(GL_TRIANGLES);
//			{
//				glVertex2i(0,-10);
//				glVertex2i(0,10);
//				glVertex2i(50,0);
//			}
//			glEnd();
//			
			render(w);
		}
		glPopMatrix();
	}
	
	public void render(Window w) {
		
		glColor3f(1.0f, 1.0f, 1.0f);
//		
//		halfWidth += 3;
//		halfHeight += 3;
//		
//		glRotatef(90, 0, 0, 1);
//		glBegin(GL_QUADS);
//		{
//			glTexCoord2f(0.0f, 0.0f);
//			glVertex2f(-halfWidth, -halfHeight);
//			
//			glTexCoord2f(1.0f, 0.0f);
//			glVertex2f(halfWidth, -halfHeight);
//			
//			glTexCoord2f(1.0f, 1.0f);
//			glVertex2f(halfWidth, halfHeight);
//			
//			glTexCoord2f(0.0f, 1.0f);
//			glVertex2f(-halfWidth, halfHeight);
//		}
//		glEnd();
//		
//		halfWidth -= 3;
//		halfHeight -= 3;
		
		
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
