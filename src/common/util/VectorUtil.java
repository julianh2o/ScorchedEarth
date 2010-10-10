package common.util;

import org.jbox2d.common.Vec2;

public class VectorUtil {
	
	public Vec2 create(double angle) {
		float x = (float)Math.cos(angle);
		float y = (float)Math.sin(angle);
		return new Vec2(x,y);
	}
	
//	public float getMagnitude() {
//		return (float)Math.sqrt(x*x + y*y);
//	}
//
//	public Vector2D getUnitVector() {
//		double mag = getMagnitude();
//		return new Vector2D(x/mag, y/mag);
//	}
//
//	public double getAngle() {
//		return Math.atan2(x,y);
//	}
//
//	public double angleTo(Vector2D v) {
//		return Math.abs(getAngle() - v.getAngle());
//	}
//
//	public Vector2D rotate(double angle) {
//		return new Vector2D(x * Math.cos(angle) + y * Math.sin(angle), y * Math.cos(angle) - x * Math.sin(angle));
//	}
//
//	public Vector2D scale(double s) {
//		return new Vector2D(x*s, y*s);
//	}
//	
//	public double getLength() {
//		return Math.sqrt(x*x + y*y);
//	}
}
