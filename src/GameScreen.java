import static org.lwjgl.opengl.GL11.*;

import java.util.LinkedList;
import java.util.List;

public class GameScreen implements Screen {
	public static final int FRAME_STEP = 10;
	
	List<Entity> entities;
	List<Tank> tanks;
	List<Controller> controllers;
	
	public GameScreen() {
		entities = new LinkedList<Entity>();
		tanks = new LinkedList<Tank>();
		controllers = new LinkedList<Controller>();
		
		
		Tank tank = new Tank();
		tanks.add(tank);
		entities.add(tank);
		
		TankController tc = new TankController(tank);
		controllers.add(tc);
	}

	public void enter() {
		
	}

	public void update(long ms) {
		long left = ms;
		while (left > 0) {
			long render = Math.min(left,FRAME_STEP);
			left -= render;
			smallUpdate(render);
		}
	}
	
	public void smallUpdate(long ms) {
		for (Entity entity : entities) {
			entity.update(ms);
		}
		
		for (Controller controller : controllers) {
			controller.update(ms);
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
