package common.world;

import java.io.Serializable;

import common.util.Util;
import common.util.Vector2D;

public class Tank extends Entity implements Serializable {
	private static final long serialVersionUID = 4043804903222636075L;
	
	private int model;
	

	public Tank(int id) {
		super(id);
		setModel(0);
	}

	public void update(long ms) {
		position = position.add(velocity);

		double friction = Util.timeScale(getTankFriction(), ms);
		if (velocity.getMagnitude() < friction) {
			velocity = new Vector2D(0,0);
		} else {
			Vector2D drag = velocity.getUnitVector();
			drag = drag.scale(-friction);
			velocity = velocity.add(drag);
		}
		
		if (velocity.getMagnitude() > getMaxSpeed()) {
			velocity = velocity.getUnitVector().scale(getMaxSpeed());
		}
	}
	
	public void rotate(double amount) {
		velocity = velocity.rotate(-amount);
		setAngle(getAngle() + amount);
	}
	
	public void forward(long ms) {
		Vector2D dvel = new Vector2D(getAngle());
		dvel = dvel.scale(getAcceleration());
		velocity = velocity.add(Util.timeScale(dvel,ms));
	}
	
	public void backward(long ms) {
		Vector2D dvel = new Vector2D(getAngle());
		dvel = dvel.scale(-getAcceleration());
		velocity = velocity.add(Util.timeScale(dvel,ms));
	}
	
	public void turnLeft(long ms) {
		double r = Util.timeScale(getActualRotationSpeed(), ms);
		rotate(r);
	}
	
	public void turnRight(long ms) {
		double r = Util.timeScale(getActualRotationSpeed(), ms);
		rotate(-r);
	}
	
	public double getActualRotationSpeed() {
		return Math.max(getRotationSpeed()*(velocity.getMagnitude()/getMaxSpeed()),getRotationSpeed()/2);
	}
	
	public double getRotationSpeed() {
		return 2.0;
	}
	
	public double getAcceleration() {
		return 2.0;
	}
	
	public double getMaxSpeed() {
		return 1.5;
	}
	
	public double getTankFriction() {
		return 1.0;
	}
	
	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}
}
