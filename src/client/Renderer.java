package client;

import common.util.VectorUtil;
import common.world.Entity;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Shape;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
	public static void render(Window w, Body b, Entity e) {
		Shape s = b.getShape();
		if (s instanceof Box) {
			Box box = (Box)s;
			
			float halfWidth = box.getSize().getX()/2.0F;
			float halfHeight = box.getSize().getY()/2.0F;
			
			glPushMatrix();
			glTranslatef(b.getPosition().getX(), b.getPosition().getY(), 0);

			glColor3f(1.0f, 1.0f, 1.0f);




			Vector2f vel = new Vector2f(b.getVelocity());
			Vector2f lateral = VectorUtil.create((float)(b.getRotation() + Math.PI/2));

			Vector2f result = new Vector2f();
			vel.projectOntoUnit(lateral, result);

			result = result.negate();
			result.scale(1F);

			renderVector(result);





			glEnable(GL_TEXTURE_2D);
			w.getModel(e.getModel()).texture.bind();

			glRotatef((float)Math.toDegrees(b.getRotation())+90, 0, 0, 1);
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
			glPopMatrix();
		}
	}
	
	private static void renderVector(Vector2f vector) {
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

	public static void test() {
		float halfWidth = 10F;
		float halfHeight = 10F;

		glPushMatrix();
		{
			glTranslatef(25,25, 0);

			glColor3f(1.0f, 1.0f, 1.0f);
			glDisable(GL_TEXTURE_2D);


			glBegin(GL_LINE_LOOP);
			{
				glVertex2f(-halfWidth, -halfHeight);
				glVertex2f(halfWidth, -halfHeight);
				glVertex2f(halfWidth, halfHeight);
				glVertex2f(-halfWidth, halfHeight);
			}
			glEnd();
		}
		glPopMatrix();
	}
}
