package common.world;

public class Tank extends Entity {
	private int model;
	
	public Tank(int id) {
		super(id);
		setModel(0);
	}

	
	public void accelerate(float amount) {
		//Vector2D dvel = new Vector2D(getRotation());
//		dvel.scale(amount);
//		adjustVelocity(dvel);
	}
	
	public void forward() {
		accelerate(getAcceleration());
	}
	
	public void backward() {
		accelerate(-getAcceleration());
	}
	
	public void rotate(float amount) {
//		setRotation(getRotation() + amount);
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
		return 1F;
	}
	
	public float getRotationSpeed() {
		return 2.0F;
	}
	
	public float getAcceleration() {
		return 2.0F;
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
