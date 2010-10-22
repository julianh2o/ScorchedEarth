package common.world;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;

import common.network.NetworkProto.NetworkEntity;
import common.util.BodyUtil;
import common.world.behavior.Behavior;
import common.world.behavior.TankBehavior;

public class Entity {
	public enum Type {
		TANK,
		BLOCK
	}
	
	private GameWorld world;
	private Behavior behavior;
	private int id;
	private Type type;
	private int model;
	
	private boolean dirty;
	
	public Entity(GameWorld world, int id, Type type) {
		this.world = world;
		this.id = id;
		this.type = type;
		this.dirty = true;
		configureType();
	}
	
	private void configureType() {
		switch (type) {
		case TANK:
			setBehavior(new TankBehavior());
			model = 0;
			break;
		case BLOCK:
			model = 3;
			break;
		}
	}

	public Entity(GameWorld world, int id, Type type, int model, Behavior behavior) {
		this.world = world;
		this.id = id;
		this.type = type;
		this.model = model;
		this.behavior = behavior;
		this.dirty = true;
	}
	
	public void update() {
		if (behavior != null) behavior.update();
	}
	
	public byte[] getBytes() {
		return NetworkEntity.newBuilder()
		.setId(id)
		.setType(getType().ordinal())
		.setModel(model)
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
	
	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void updateWith(NetworkEntity ne) {
		this.type = Type.values()[ne.getType()];
		this.model = ne.getModel();
		
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
}
