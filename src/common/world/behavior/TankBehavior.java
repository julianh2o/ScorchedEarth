package common.world.behavior;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;

import common.util.VectorUtil;

public class TankBehavior extends Behavior {
	public TankBehavior() {
	}

	public void update() {
		Body b = entity.getBody();
		Vector2f vel = new Vector2f(b.getVelocity());
		Vector2f lateral = VectorUtil.create((float)(b.getRotation() + Math.PI/2));

		Vector2f result = new Vector2f();
		vel.projectOntoUnit(lateral, result);

		result = result.negate();
		result.scale(.2F);

		b.adjustVelocity(result);
		
		if (vel.length() > 0) {
			entity.setDirty(true);
		}
	}

	public void accelerate(float amount) {
		Body b = entity.getBody();

		Vector2f dvel = VectorUtil.create(b.getRotation());
		dvel.scale(amount);
		b.adjustVelocity(dvel);
	}

	public void forward() {
		accelerate(getAcceleration());
	}

	public void backward() {
		accelerate(-getAcceleration());
	}

	public void rotate(float radians) {
		entity.getBody().adjustRotation(radians);
	}

	public void turnLeft() {
		float r = getActualRotationSpeed();
		rotate(r);
	}

	public void turnRight() {
		float r = getActualRotationSpeed();
		rotate(-r);
	}

	public float getActualRotationSpeed() {
		return getRotationSpeed();
	}

	public float getRotationSpeed() {
		return 0.1F;
	}

	public float getAcceleration() {
		return 0.5F;
	}
}
