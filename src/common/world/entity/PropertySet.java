package common.world.entity;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import org.newdawn.slick.opengl.Texture;

import common.ResourceManager;
import common.util.Log;

public class PropertySet {
	private String path;
	HashMap<String,String> attributes;
	
	public PropertySet(String path, File f) {
		this.setPath(path);
		try {
			attributes = ResourceManager.loadProperties(f);
		} catch (Exception e) {
			Log.p.error("Error Loading PropertySet",e);
		}
	}
	
	// #######################################
	
	public int getInt(String key) {
		try {
			return Integer.parseInt(attributes.get(key));
		} catch (Exception e) {
			return 0;
		}
	}

	public float getFloat(String key) {
		try {
			return Float.parseFloat(attributes.get(key));
		} catch (Exception e) {
			return 0;
		}
	}
	
	public boolean getBoolean(String key, boolean def) {
		try {
			return Boolean.parseBoolean(attributes.get(key));
		} catch (Exception e) {
			return def;
		}
	}
	
	public boolean getBoolean(String key) {
		return getBoolean(key,false);
	}
	
	public String getString(String key) {
		return attributes.get(key);
	}
	
	public boolean hasKey(String key) {
		return attributes.containsKey(key);
	}
	
	public Texture getTexture(String key) {
		String textureName = getString(key);
		if (textureName == null) return null;
		return ResourceManager.getInstance().getTexture(textureName);
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	
	public String toString() {
		String s = "\n{";
		Set<String> keys = attributes.keySet();
		for (String key : keys) {
			s += key + " = " + attributes.get(key) + "\n";
		}
		s += "}";
		return s;
	}
}
