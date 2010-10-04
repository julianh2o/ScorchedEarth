import static org.lwjgl.opengl.GL11.*;

import java.util.LinkedList;
import java.util.List;

public class GameScreen implements Screen {
	public static final int FRAME_STEP = 10;
	
	List<Entity> entities;
	
	public GameScreen() {
		entities = new LinkedList<Entity>();
		entities.add(new Entity());
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
	}

	public void render(Window w) {
		glClear(GL_COLOR_BUFFER_BIT);
		
		for (Entity entity : entities) {
			entity.render(w);
		}
	}

	public void leave() {
		
	}
}
