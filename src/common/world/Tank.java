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
		return getWorld().ebmap.get(this);
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
//		return Math.max(getRotationSpeed()*(getVelocity().length()/getMaxSpeed()),getRotationSpeed()/2);
		return .1F;
	}
	
	public float getRotationSpeed() {
		return 2.0F;
	}
	
	public float getAcceleration() {
		return 0.5F;
	}
	
	public float getMaxSpeed() {
		return 1.5F;
	}
	
	public float getTankFriction() {
		return 1.0F;
	}
	
	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}
}
