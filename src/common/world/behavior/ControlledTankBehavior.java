package common.world.behavior;

import org.lwjgl.input.Keyboard;

import common.key.KeyboardHandler;
public class ControlledTankBehavior extends TankBehavior {
	KeyboardHandler kb;
	
	public ControlledTankBehavior(KeyboardHandler kb) {
		this.kb = kb;
	}

	@Override
	public void update() {
		if (kb.isDown(Keyboard.KEY_W)) {
			forward();
		}
		if (kb.isDown(Keyboard.KEY_S)) {
			backward();
		}
		
		if (kb.isDown(Keyboard.KEY_A)) {
			turnLeft();
		}
		if (kb.isDown(Keyboard.KEY_D)) {
			turnRight();
		}
		
		super.update();
	}
}
