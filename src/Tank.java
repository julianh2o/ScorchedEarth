public class Tank extends Entity {
	
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
		angle += amount;
	}
	
	public void rotateRight(double amount) {
		angle -= amount;
	}
}
