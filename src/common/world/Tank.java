package common.world;

import common.util.VectorUtil;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;

public class Tank extends Entity {
	private int model;
	
	public Tank(GameWorld world, int id) {
		super(world, id);
		
		setModel(0);
	}
	
	public Body getBody() {
		return getWorld().getBody(this);
	}
	
	public void update() {
		Body b = getBody();
		Vector2f vel = new Vector2f(b.getVelocity());
		Vector2f lateral = VectorUtil.create((float)(b.getRotation() + Math.PI/2));
		
		Vector2f result = new Vector2f();
		vel.projectOntoUnit(lateral, result);
		
		result = result.negate();
		result.scale(.2F);
		
		b.adjustVelocity(result);
	}
	
	public void accelerate(float amount) {
		Body b = getBody();
		
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
		getBody().adjustRotation(radians);
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
//		float vel = getBody().getVelocity().length();
//		if (vel == 0) return getRotationSpeed()/2F;
//		return Math.max(Math.min(getRotationSpeed()/2,getRotationSpeed() - getRotationSpeed()*(1/vel)),getRotationSpeed());
		return getRotationSpeed();
	}
	
	public float getRotationSpeed() {
		return 0.1F;
	}
	
	public float getAcceleration() {
		return 0.5F;
	}
	
	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}
}
