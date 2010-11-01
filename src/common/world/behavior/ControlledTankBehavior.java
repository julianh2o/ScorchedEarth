package common.world.behavior;

import net.phys2d.raw.Body;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import client.GameScreen;

import common.util.VectorUtil;
import common.world.Entity;

public class ControlledTankBehavior extends TankBehavior {
	GameScreen screen;
	long lastShot;
	
	public ControlledTankBehavior(GameScreen screen) {
		this.screen = screen;
		lastShot = System.currentTimeMillis();
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
		
		if (screen.kb().isDown(Keyboard.KEY_SPACE)) {
			long since = (System.currentTimeMillis() - lastShot);
			if (since > 250) {
				fire();
			}
		}
		
		super.update();
	}
	
	private void fire() {
		lastShot = System.currentTimeMillis();
		
		Entity bullet = new Entity(-1,"projectile.entity");
		screen.getWorld().addEntity(bullet, entity.getX(), entity.getY());
		Body body = bullet.getBody();
		float mult = 10;
		net.phys2d.math.Vector2f bulletVelocity = VectorUtil.create(entity.getAim());
		bulletVelocity.scale(mult);
		body.adjustVelocity(bulletVelocity);
		body.addExcludedBody(entity.getBody());
	}
}
