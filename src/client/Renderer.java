package client;

import org.lwjgl.util.vector.Vector2f;

import common.ResourceManager;
import common.world.Entity;

import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Shape;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
	public static void render(Window w, Body b, Entity e) {
		Shape s = b.getShape();
		if (s instanceof Box) {
			Box box = (Box)s;
			
			float width = box.getSize().getX();
			float height = box.getSize().getY();
			
			glPushMatrix(); {
				glTranslatef(b.getPosition().getX(), b.getPosition().getY(), 0);
	
				glColor3f(1.0f, 1.0f, 1.0f);
	
				glEnable(GL_TEXTURE_2D);
				
				Model m = ResourceManager.getInstance().getModelFor(e);
				
				glPushMatrix(); {
					m.getMain().bind();
					glRotatef((float)Math.toDegrees(b.getRotation())+90, 0, 0, 1);
					renderSquare(height,width);
				} glPopMatrix();
				
				if (m.getAim() != null) {
					glPushMatrix(); {
						glEnable (GL_BLEND);
						glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
						
						m.getAim().bind();
						glRotatef((float)Math.toDegrees(e.getAim())+90, 0, 0, 1);
						renderSquare(height,width);
					} glPopMatrix();
				}
				
			} glPopMatrix();
		}
	}
	
	public static void renderSquare(float width, float height) {
		float halfWidth = width/2;
		float halfHeight = height/2;
		
		glBegin(GL_QUADS); {
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
	}
	
	public static void renderVector(Vector2f vector) {
		glPushAttrib(GL_TEXTURE_2D);
		glPushMatrix();
		
		glDisable(GL_TEXTURE_2D);
		glColor3f(1.0f, 1.0f, 1.0f);
		
		glBegin(GL_LINES); {
			glVertex2f(0,0);
			glVertex2f(vector.getX(),vector.getY());
		} glEnd();
		
		glPopMatrix();
		glPopAttrib();
	}
	
	public static void renderLine(float x, float y, float xx, float yy) {
		glPushMatrix(); {
			glTranslatef(x, y, 0);
			renderVector(new Vector2f(xx-x,yy-y));
		} glPopMatrix();
	}
}




//			Vector2f vel = new Vector2f(b.getVelocity());
//			Vector2f lateral = VectorUtil.create((float)(b.getRotation() + Math.PI/2));
//
//			Vector2f result = new Vector2f();
//			vel.projectOntoUnit(lateral, result);
//
//			result = result.negate();
//			result.scale(1F);
//
//			renderVector(result);

