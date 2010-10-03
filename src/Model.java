import static org.lwjgl.opengl.GL11.*;

public class Model {
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
		glBegin(GL_QUADS);
		{
			glVertex2i(-50, -50);
			glVertex2i(50, -50);
			glVertex2i(50, 50);
			glVertex2i(-50, 50);
		}
		glEnd();
	}
}
