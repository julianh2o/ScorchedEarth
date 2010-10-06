import static org.lwjgl.opengl.GL11.*;

import java.util.LinkedList;
import java.util.List;

public class GameScreen implements Screen, KeyListener {
	NetworkHandler nh;
	World world;
	
	List<Controller> controllers;

	public GameScreen(World world, NetworkHandler nh) {
		this.nh = nh;
		this.world = world;
		
		controllers = new LinkedList<Controller>();
		
		TankController tc = new TankController(nh);
		controllers.add(tc);
	}

	public void enter() {
		
	}

	public void update(long ms) {
		world.update(ms);
		
		for (Controller controller : controllers) {
			controller.update(ms);
		}
	}
	
	public void render(Window w) {
		glClear(GL_COLOR_BUFFER_BIT);
		world.render(w);
	}

	public void leave() {
		
	}

	public void keyPressed(KeyEvent e) {
		nh.send(e);
	}

	public void keyReleased(KeyEvent e) {
		nh.send(e);
	}
}
