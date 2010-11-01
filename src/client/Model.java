package client;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;

import org.newdawn.slick.opengl.Texture;

import common.world.entity.PropertySet;

public class Model extends PropertySet {
	public Model(String path, File f) {
		super(path,f);
	}
	
	public void renderAt(Window w, double x, double y, double angle, double aimAngle) {
		glPushMatrix(); {
			glTranslated(x, y, 0);
			
			glPushMatrix(); {
				glRotatef((float)Math.toDegrees(angle), 0, 0, 1);
				render(w,getMain());
			} glPopMatrix();
			
			if (getAim() != null) {
				glPushMatrix(); {
					glRotatef((float)Math.toDegrees(aimAngle), 0, 0, 1);
					render(w,getAim());
				} glPopMatrix();
			}
			
			
		} glPopMatrix();
	}
	
	public void render(Window w,Texture texture) {
		glColor3f(1.0f, 1.0f, 1.0f);
		float halfWidth = getWidth()/2;
		float halfHeight = getHeight()/2;
		
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
	
	public Texture getMain() {
		return getTexture("main");
	}
	
	public Texture getAim() {
		return getTexture("aim");
	}
	
	public float getWidth() {
		return getFloat("width");
	}
	
	public float getHeight() {
		return getFloat("height");
	}
}