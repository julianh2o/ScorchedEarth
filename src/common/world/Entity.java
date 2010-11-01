package common.world;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;

import common.ResourceManager;
import common.network.NetworkProto.NetworkEntity;
import common.util.BodyUtil;
import common.world.behavior.Behavior;
import common.world.entity.EntityType;

public class Entity {
	private EntityType type;
	
	private GameWorld world;
	private Behavior behavior;
	private int id;
	private int life;
	
	private float aim;
	
	private boolean dirty;
	
	public Entity(int id, String type) {
		this.type = getTypeForString(type);
		this.id = id;
		this.aim = 0;
		this.dirty = true;
		configureType();
	}
	
	private void configureType() {
		if (type == null) return;
		this.life = type.getMaxLife();
	}

	public void update() {
		if (behavior != null) behavior.update();
	}
	
	public byte[] getBytes() {
		return NetworkEntity.newBuilder()
		.setId(id)
		.setType(getType().getPath())
		.setAim(aim)
		.setX(getX())
		.setY(getY())
		.setXvel(getXVel())
		.setYvel(getYVel())
		.setR(getRotation())
		.setRvel(getAngularVelocity()).build().toByteArray();
	}
	

	// Getters and Setters
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

	public Body getBody() {
		return getWorld().getBody(this);
	}
	
	public float getX() {
		return getBody().getPosition().getX();
	}
	
	public float getY() {
		return getBody().getPosition().getY();
	}
	
	public float getXVel() {
		return getBody().getVelocity().getX();
	}
	
	public float getYVel() {
		return getBody().getVelocity().getY();
	}
	
	public float getRotation() {
		return getBody().getRotation();
	}
	
	public float getAngularVelocity() {
		return getBody().getAngularVelocity();
	}
	
	public Behavior getBehavior() {
		return behavior;
	}

	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
		behavior.setEntity(this);
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}
	
	public static EntityType getTypeForString(String type) {
		return ResourceManager.getInstance().getEntityType(type);
	}

	public void updateWith(NetworkEntity ne) {
		this.type = getTypeForString(ne.getType());
		
		this.life = ne.getLife();
		this.aim = ne.getAim();
		
        Body b = getBody(); 
        b.setPosition(ne.getX(), ne.getY()); 
        BodyUtil.setVelocity(b,new Vector2f(ne.getXvel(),ne.getYvel())); 
        b.setRotation(ne.getR()); 
        BodyUtil.setAngularVelocity(b,ne.getRvel()); 
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setAim(float aim) {
		this.aim = aim;
	}

	public float getAim() {
		return aim;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getLife() {
		return life;
	}
	
	public void damage(int damage) {
		setLife(getLife() - damage);
	}
}
