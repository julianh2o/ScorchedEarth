package common.world.behavior;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import client.GameScreen;

import common.util.VectorUtil;

public class ControlledTankBehavior extends TankBehavior {
	GameScreen screen;
	
	public ControlledTankBehavior(GameScreen screen) {
		this.screen = screen;
	}

	@Override
	public void update() {
		if (screen.kb().isDown(Keyboard.KEY_W)) {
			forward();
		}
		if (screen.kb().isDown(Keyboard.KEY_S)) {
			backward();
		}
		
		if (screen.kb().isDown(Keyboard.KEY_A)) {
			turnLeft();
		}
		if (screen.kb().isDown(Keyboard.KEY_D)) {
			turnRight();
		}
		
		
		if (screen.mh() != null) {
			Vector2f aim = new Vector2f(screen.getMouseX(),screen.getMouseY());
			aim = aim.translate(-entity.getX(), -entity.getY());
			entity.setAim(VectorUtil.getAngle(aim));
		}
		
		super.update();
	}
}
