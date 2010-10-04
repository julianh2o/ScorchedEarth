public class Tank extends Entity {
	
	public Tank() {
	}
	
	public void rotateLeft(double amount) {
		angle += amount;
	}
	
	public void rotateRight(double amount) {
		angle -= amount;
	}
}
