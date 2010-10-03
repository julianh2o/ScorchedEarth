import static org.lwjgl.opengl.GL11.*;

public class GameScreen implements Screen {
	double x;
	double y;
	double xv;
	double yv;
	
	public GameScreen() {
		this.x = 100;
		this.y = 100;
		
		this.xv = 1;
		this.yv = 1;
	}

	public void enter() {
		
	}

	public void update(long ms) {
		x += Util.timeScale(xv,ms);
		y += Util.timeScale(yv,ms);
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT);
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
