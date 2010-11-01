package common.world.entity;

import java.io.File;

public class EntityType extends PropertySet {
	
	public EntityType(String path, File f) {
		super(path,f);
	}
	
	public int getMaxLife() {
		return getInt("maxLife");
	}
	
	public float getWidth() {
		return getFloat("width");
	}

	public float getHeight() {
		return getFloat("height");
	}
	
	public String getModel() {
		return getString("model");
	}
	
	public String getBehavior() {
		return getString("behavior");
	}

	public float getMass() {
		return getFloat("mass");
	}

	public boolean getFixed() {
		return getBoolean("fixed");
	}

	public float getRotationalDamping() {
		return getFloat("rdamping");
	}

	public float getDamping() {
		return getFloat("damping");
	}
	
	public float getRestitution() {
		return getFloat("restitution");
	}

	public boolean getRotatable() {
		return getBoolean("canRotate",false);
	}
}
