package common.world;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import client.Model;
import client.Window;

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
	
	public Entity(GameWorld world, int id, Type type) {
		this.world = world;
		this.id = id;
		this.type = type;
		configureType();
	}
	
	private void configureType() {
		switch (type) {
		case TANK:
			setBehavior(new TankBehavior());
			break;
		case BLOCK:
			break;
		}
	}

	public Entity(GameWorld world, int id, Type type, int model, Behavior behavior) {
		this.world = world;
		this.id = id;
		this.type = type;
		this.model = model;
		this.behavior = behavior;
	}
	
	public void update() {
		if (behavior != null) behavior.update();
	}
	
	@Deprecated
	public void render(Window w) {
		Model modelObject = w.getModel(getModel());
		if (modelObject != null) {
			//modelObject.renderAt(w,getPosition().getX(),getPosition().getY(),getRotation());
		}
	}
	
	public byte[] getBytes() {
		return NetworkEntity.newBuilder()
		.setId(id)
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
        Body b = getBody(); 
        b.setPosition(ne.getX(), ne.getY()); 
        BodyUtil.setVelocity(b,new Vector2f(ne.getXvel(),ne.getYvel())); 
        b.setRotation(ne.getR()); 
        BodyUtil.setAngularVelocity(b,ne.getRvel()); 
	}
}
