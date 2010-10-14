package common.world.behavior;

import common.world.Entity;

public abstract class Behavior {
	Entity entity;
	
	public abstract void update();
	
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}
