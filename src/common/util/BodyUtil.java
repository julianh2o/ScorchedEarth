package common.util;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;

public class BodyUtil {
	
	public static void setVelocity(Body b, Vector2f vel) {
		b.adjustVelocity(new Vector2f(b.getVelocity()).negate()); //reset velocity
		b.adjustVelocity(vel);
	}
	
	public static void setAngularVelocity(Body b, float vel) {
		b.adjustAngularVelocity(-b.getAngularVelocity()); //reset velocity
		b.adjustAngularVelocity(vel);
	}
}
