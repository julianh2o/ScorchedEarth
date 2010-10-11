package common.world;

import client.Model;
import client.Window;

public class Entity {
	GameWorld world;
	int id;
	
	public Entity(GameWorld world, int id) {
		this.world = world;
		this.id = id;
	}
	
	public void render(Window w) {
		Model modelObject = w.getModel(getModel());
		if (modelObject != null) {
			//modelObject.renderAt(w,getPosition().getX(),getPosition().getY(),getRotation());
		}
	}
	
	// Getters and Setters
	
	public int getModel() {
		return 0;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GameWorld getWorld() {
		return world;
	}

	public void setWorld(GameWorld world) {
		this.world = world;
	}
}
