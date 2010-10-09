package client;

import org.lwjgl.input.Keyboard;

import common.key.KeyEvent;
import common.key.KeyListener;
import common.key.KeyboardHandler;
import common.world.Tank;

public class TankController implements KeyListener {
	private Tank tank;
	private KeyboardHandler kb;

	public TankController(Tank tank, KeyboardHandler kb) {
		this.tank = tank;
		this.kb = kb;
	}
	
	public void update(long ms) {
		if (kb.isDown(Keyboard.KEY_W)) {
			tank.forward(ms);
		}
		if (kb.isDown(Keyboard.KEY_S)) {
			tank.backward(ms);
		}
		
		if (kb.isDown(Keyboard.KEY_A)) {
			tank.turnLeft(ms);
		}
		if (kb.isDown(Keyboard.KEY_D)) {
			tank.turnRight(ms);
		}
	}
	
	public void keyPressed(KeyEvent k) {
		
	}

	public void keyReleased(KeyEvent k) {
		
	}

	public Tank getTank() {
		return tank;
	}

	public void setTank(Tank tank) {
		this.tank = tank;
	}
	
}
