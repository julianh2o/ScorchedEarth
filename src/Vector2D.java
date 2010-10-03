public class Vector2D {
	double x;
	double y;
	
	public Vector2D() {
		this.x = 0;
		this.y = 0;
	}
	
	public double getLength() {
		return Math.sqrt(x*x + y*y);
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
}
