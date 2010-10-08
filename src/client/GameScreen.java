package client;

import static org.lwjgl.opengl.GL11.*;

import common.key.KeyEvent;
import common.key.KeyListener;
import common.key.KeyboardHandler;
import common.network.NetworkHandler;
import common.util.Log;
import common.world.Tank;
import common.world.World;

public class GameScreen implements Screen, KeyListener {
	private NetworkHandler nh;
	private World world;
	
	private TankController tc;
	private KeyboardHandler kb;
	
	public GameScreen(World world, NetworkHandler nh, KeyboardHandler kb) {
		this.nh = nh;
		this.world = world;
		this.kb = kb;
		
		kb.addKeyListener(this);
	}

	public void enter() {
		//TODO request world
	}
	
	public void controlTank(int id) {
		Tank t = (Tank)world.findEntity(id);
		this.tc = new TankController(t,kb);
		Log.p.out("Controlling tank: "+id);
	}

	public void update(long ms) {
		if (tc != null) tc.update(ms);
		world.update(ms);
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