package client;

import static org.lwjgl.opengl.GL11.*;

import common.key.KeyEvent;
import common.key.KeyListener;
import common.key.KeyboardHandler;
import common.key.TankController;
import common.network.NetworkHandler;
import common.util.Log;
import common.world.Tank;
import common.world.GameWorld;

public class GameScreen implements Screen, KeyListener {
	private NetworkHandler nh;
	private GameWorld world;
	
	TankController tc;
	private KeyboardHandler kb;
	
	public GameScreen(GameWorld world, NetworkHandler nh, KeyboardHandler kb) {
		this.nh = nh;
		this.world = world;
		this.kb = kb;
		
		kb.addKeyListener(this);
	}

	public void enter() {
		//TODO request world
	}
	
	public void controlTank(int id) {
		Log.p.out("Trying to control: "+id);
		Tank t = (Tank)world.findEntity(id);
		if (t == null) {
			Log.p.out("ERORR: TANK NOT FOUND");
			return;
		}
		this.tc = new TankController(t,kb);
		Log.p.out("Controlling tank: "+id);
	}

	public void update() {
		if (tc != null) tc.update();
		world.update();
	}
	
	public void render(Window w) {
		glClear(GL_COLOR_BUFFER_BIT);
		world.render(w);
	}

	public void leave() {
		//TODO send leave message
	}

	public void keyPressed(KeyEvent e) {
		nh.send(e);
	}

	public void keyReleased(KeyEvent e) {
		nh.send(e);
	}
}
