import java.io.Serializable;

public class Tank extends Entity implements Serializable {
	private static final long serialVersionUID = 4043804903222636075L;

	public Tank() {
	}

	void update(long ms) {
		position = position.add(velocity);

		double friction = .006;
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
}
