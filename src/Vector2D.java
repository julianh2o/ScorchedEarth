import java.io.Serializable;

public class Vector2D extends NetworkObject implements Serializable {
	private static final long serialVersionUID = -2786458774826777729L;
	
	protected double x;
	protected double y;
	
	public Vector2D() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(double angle) {
		this.x = Math.cos(angle);
		this.y = Math.sin(angle);
	}
	
	public double getMagnitude() {
		return getLength();
	}

	public Vector2D getUnitVector() {
		double mag = getMagnitude();
		return new Vector2D(x/mag, y/mag);
	}

	public double getAngle() {
		return Math.atan2(x,y);
	}

	public double angleTo(Vector2D v) {
		return Math.abs(getAngle() - v.getAngle());
	}

	public double dot(Vector2D v) {
		return getMagnitude() * v.getMagnitude() * Math.cos(angleTo(v));
	}

	public Vector2D add(Vector2D v) {
		return new Vector2D(x + v.x, y + v.y);
	}

	public Vector2D rotate(double angle) {
		return new Vector2D(x * Math.cos(angle) + y * Math.sin(angle), y * Math.cos(angle) - x * Math.sin(angle));
	}

	public Vector2D scale(double s) {
		return new Vector2D(x*s, y*s);
	}
	
	public double getLength() {
		return Math.sqrt(x*x + y*y);
	}
	
	public String toString() {
		return "<"+getX()+", "+getY()+">";
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
