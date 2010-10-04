public class Tank extends Entity {
	
	public Tank() {
	}

	void update(long ms) {
		position.add(velocity);

		velocity = velocity.scale(.99);
	}
	
	public void rotateLeft(double amount) {
		angle += amount;
	}
	
	public void rotateRight(double amount) {
		angle -= amount;
	}
}
