package client;

import static org.lwjgl.opengl.GL11.*;

import common.input.KeyEvent;
import common.input.KeyListener;
import common.input.KeyboardHandler;
import common.input.MouseHandler;
import common.network.NetworkHandler;
import common.util.Log;
import common.world.Entity;
import common.world.GameWorld;
import common.world.behavior.ControlledTankBehavior;

public class GameScreen implements Screen, KeyListener {
	private NetworkHandler nh;
	private GameWorld world;
	
	private Entity tank;
	
	private KeyboardHandler kb;
	private MouseHandler mh;
	
	public GameScreen(GameWorld world, NetworkHandler nh, KeyboardHandler kb, MouseHandler mh) {
		this.nh = nh;
		this.world = world;
		this.kb = kb;
		this.mh = mh;
		
		kb.addKeyListener(this);
	}

	public void enter() {
		//TODO request world
	}
	
	public void controlTank(int id) {
		Log.p.out("Trying to control: "+id);
		Entity tank = world.findEntity(id);
		if (tank == null) {
			Log.p.out("ERORR: TANK NOT FOUND");
			return;
		}
		this.setTank(tank);
		tank.setBehavior(new ControlledTankBehavior(kb,mh));
		Log.p.out("Controlling tank: "+id);
	}

	public void update() {
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
		nh.send(NetworkHandler.KEY_EVENT,e.getBytes());
	}

	public void keyReleased(KeyEvent e) {
		nh.send(NetworkHandler.KEY_EVENT,e.getBytes());
	}

	public void setTank(Entity tank) {
		this.tank = tank;
	}

	public Entity getTank() {
		return tank;
	}
}
