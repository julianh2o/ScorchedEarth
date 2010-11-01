package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

import client.Model;

import common.util.Log;
import common.world.Entity;
import common.world.entity.EntityType;

public class ResourceManager {
	public static ResourceManager instance;
	
	HashMap<String,Model> models;
	HashMap<String,EntityType> entityTypes;
	TextureManager textureManager;
	
	public ResourceManager() {
		models = new HashMap<String,Model>();
		entityTypes = new HashMap<String,EntityType>();
		textureManager = new TextureManager("resources");
		
		File data = new File("data");
		loadResources(data);
	}
	
	public void loadResources(File f) {
		File[] files = f.listFiles();
		for (int i=0; i<files.length; i++) {
			try {
				if (files[i].isDirectory()) {
					loadResources(files[i]);
				} else {
					loadFile(files[i]);
				}
			} catch (Exception e) {
				Log.p.error("Error loading file: "+files[i].toString(),e);
			}
		}
	}
	
	public void loadFile(File f) throws Exception {
		Log.p.out("Loading File: "+f.toString());
		String ext = getExtension(f.getName());
		
		if ("entity".equalsIgnoreCase(ext)) {
			entityTypes.put(f.getName(),new EntityType(f.getName(),f));
		}
		
		if ("model".equalsIgnoreCase(ext)) {
			models.put(f.getName(),new Model(f.getName(),f));
		}
	}

	private String getExtension(String filename) {
		int index = filename.lastIndexOf(".");
		return filename.substring(index+1);
	}
	
	public Texture getTexture(String name) {
		return textureManager.getTexture(name);
	}

	public static ResourceManager getInstance() {
		if (instance == null) {
			instance = new ResourceManager();
		}
		return instance;
	}
	
	public static HashMap<String,String> loadProperties(File f) throws IOException {
		HashMap<String,String> attributes = new HashMap<String,String>();
		
		appendProperties(f,attributes);
		
		return attributes;
	}
	
	public static void appendProperties(File f, HashMap<String,String> attributes) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		String line;
		while ((line = br.readLine()) != null) {
			int index = line.indexOf("=");
			String key = line.substring(0,index);
			String value = line.substring(index+1);
			attributes.put(key, value);
		}
	}

	public Model getModelFor(Entity e) {
		return models.get(e.getType().getModel());
	}

	public Model getModel(String string) {
		return models.get(string);
	}

	public EntityType getEntityType(String type) {
		return entityTypes.get(type);
	}
} 
