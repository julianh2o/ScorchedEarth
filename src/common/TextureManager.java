package common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import common.util.Log;

public class TextureManager {
	private String root;
	HashMap<String,Texture> textures;
	
	public TextureManager(String root) {
		setRoot(root);
		textures = new HashMap<String,Texture>();
	}
	
	public Texture getTexture(String path) {
		path = root + path;
		
		Texture t = textures.get(path);
		if (t != null) return t;
			
		t = loadTexture(path);
		textures.put(path, t);
		return t;
	}
	
	private Texture loadTexture(String path) {
		try {
			return TextureLoader.getTexture("PNG", new FileInputStream(path));
		} catch (FileNotFoundException e) {
			Log.p.out("File Not Found when loading texture");
			e.printStackTrace();
		} catch (IOException e) {
			Log.p.out("Error Loading Texture");
			e.printStackTrace();
		}
		return null;
	}

	public void setRoot(String root) {
		if (!root.endsWith("/")) {
			root = root += "/";
		}
		
		this.root = root;
	}

	public String getRoot() {
		return root;
	}
}
