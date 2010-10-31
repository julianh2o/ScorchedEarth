package client;

import static org.lwjgl.opengl.GL11.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import common.util.Log;

public class Model {
	Texture main;
	Texture aim;
	private float width, height;
	
	public Model(String path, float width, float height) {
		this.setWidth(width);
		this.setHeight(height);
		main = loadTexture(path);
		aim = null;
	}
	
	public Model(String path, String aimPath, float width, float height) {
		this(path,width,height);
		aim = loadTexture(aimPath);
	}
	
	private Texture loadTexture(String path) {
		try {
			return TextureLoader.getTexture("PNG", new FileInputStream(path));
		} catch (FileNotFoundException e) {
			Log.p.out("File Not Found when loading texture");
			e.printStackTrace();
		} catch (IOException e) {
			Log.p.out("Error Loading Texture");
			e.printStackTrace();
		}
		return null;
	}
	
	public void renderAt(Window w, double x, double y, double angle, double aimAngle) {
		glPushMatrix(); {
			glTranslated(x, y, 0);
			
			glPushMatrix(); {
				glRotatef((float)Math.toDegrees(angle), 0, 0, 1);
				render(w,main);
			} glPopMatrix();
			
			if (aim != null) {
				glPushMatrix(); {
					glRotatef((float)Math.toDegrees(aimAngle), 0, 0, 1);
					render(w,aim);
				} glPopMatrix();
			}
			
			
		} glPopMatrix();
	}
	
	public void render(Window w,Texture texture) {
		glColor3f(1.0f, 1.0f, 1.0f);
		float halfWidth = width/2;
		float halfHeight = height/2;
		
		glEnable(GL_TEXTURE_2D);
		texture.bind();
		glBegin(GL_QUADS); {
			glTexCoord2f(0.0f, 0.0f);
			glVertex2f(-halfWidth, -halfHeight);
			
			glTexCoord2f(1.0f, 0.0f);
			glVertex2f(halfWidth, -halfHeight);
			
			glTexCoord2f(1.0f, 1.0f);
			glVertex2f(halfWidth, halfHeight);
			
			glTexCoord2f(0.0f, 1.0f);
			glVertex2f(-halfWidth, halfHeight);
		} glEnd();
		glDisable(GL_TEXTURE_2D);
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
}


//			Vector2D heading = new Vector2D(angle);
//			glBegin(GL_LINES);
//			{
//				glVertex2i(0,0);
//				glVertex2d(50*heading.getX(),50*heading.getY());
//			}
//			glEnd();
//			


//		
//		halfWidth += 3;
//		halfHeight += 3;
//		
//		glRotatef(90, 0, 0, 1);
//		glBegin(GL_QUADS);
//		{
//			glVertex2f(-halfWidth, -halfHeight);
//			glTexCoord2f(0.0f, 0.0f);
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
		
