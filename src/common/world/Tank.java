package common.world;

import java.io.Serializable;

import common.util.Util;
import common.util.Vector2D;

public class Tank extends Entity implements Serializable {
	private static final long serialVersionUID = 4043804903222636075L;
	

	public Tank(int id) {
		super(id);
	}

	public void update(long ms) {
		position = position.add(velocity);

		double friction = Util.timeScale(.006, ms);
		if (velocity.getMagnitude() < friction) {
			velocity = new Vector2D(0,0);
		} else {
			Vector2D drag = velocity.getUnitVector();
			drag = drag.scale(-friction);
			velocity = velocity.add(drag);
		}
	}
	
	public void rotateLeft(double amount) {
		setAngle(getAngle()+amount);
	}
	
	public void rotateRight(double amount) {
		setAngle(getAngle()-amount);
	}
	
	public void forward(long ms) {
		Vector2D dvel = new Vector2D(getAngle());
		dvel = dvel.scale(3);
		velocity = velocity.add(Util.timeScale(dvel,ms));
	}
	
	public void turnLeft(long ms) {
		rotateLeft(Util.timeScale(3, ms));
	}
	
	public void turnRight(long ms) {
		rotateRight(Util.timeScale(3, ms));
	}
}
