package common.world;

import client.Model;
import client.Window;

public class Entity {
	
	int id;
	
	public Entity(int id) {
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
}
