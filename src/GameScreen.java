import static org.lwjgl.opengl.GL11.*;

import java.util.LinkedList;
import java.util.List;

public class GameScreen implements Screen {
	public static final int FRAME_STEP = 10;
	
	List<Entity> entities;
	
	double x;
	double y;
	double xv;
	double yv;
	
	public GameScreen() {
		entities = new LinkedList<Entity>();
		entities.add(new Entity());
		this.x = 100;
		this.y = 100;
		
		this.xv = 50;
		this.yv = 50;
	}

	public void enter() {
		
	}

	public void update(long ms) {
		long left = ms;
		while (left > 0) {
			long render = Math.min(left,FRAME_STEP);
			left -= render;
			for (Entity entity : entities) {
				entity.update(render);
			}
		}
		
		x += Util.timeScale(xv,ms);
		y += Util.timeScale(yv,ms);
	}

	public void render(Window w) {
		glClear(GL_COLOR_BUFFER_BIT);
		
		for (Entity entity : entities) {
			entity.render(w);
		}
		
		glPushMatrix();
		{
			glTranslated(x, y, 0);
//			glRotatef(angle, 0.0f, 0.0f, 1.0f);
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

	public void leave() {
		
	}
}
