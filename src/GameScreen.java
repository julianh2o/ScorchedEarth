import static org.lwjgl.opengl.GL11.*;

import java.util.LinkedList;
import java.util.List;

public class GameScreen implements Screen, KeyListener {
	private static final int NETWORK_INTERVAL = 100;
	
	NetworkHandler nh;
	World world;
	
	List<Controller> controllers;

	private long lastNetworkUpdate;
	
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
		for (Controller controller : controllers) {
			controller.update(ms);
		}
		
		lastNetworkUpdate += ms;
		if (lastNetworkUpdate > NETWORK_INTERVAL) {
			networkUpdate();
			lastNetworkUpdate = 0;
		}
	}
	
	public void networkUpdate() {
		if (nh == null) return;
		for (Entity entity : world.getEntities()) {
			if (entity.isDirty()) {
				Log.p.out("Sending stuff");
				nh.send(entity);
				entity.setDirty(false);
			}
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
