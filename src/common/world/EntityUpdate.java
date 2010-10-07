package common.world;

import java.util.List;

import common.network.NetworkObject;

public class EntityUpdate extends NetworkObject {
	private static final long serialVersionUID = -6281198949891263824L;
	
	public Entity entity;
	
	public EntityUpdate(Entity entity) {
		this.entity = entity;
	}
	
	public void update(List<Entity> entities) {
		for (Entity e : entities) {
			if (canUpdate(e)) update(e);
		}
	}
	
	public void update(Entity e) {
		e.setVelocity(entity.getVelocity());
		e.setPosition(entity.getPosition());
		e.setAngle(entity.getAngle());
	}
	
	public boolean canUpdate(Entity e) {
		return e.getId() == entity.getId();
	}
	
	public String toString() {
		return entity.toString();
	}
}
